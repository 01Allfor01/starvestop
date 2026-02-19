'use client';

import { useState, useEffect, useRef } from 'react';
import { useRouter } from 'next/navigation';
import Script from 'next/script';
import { Clock, Tag, ChevronRight, Store, Loader2, X } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { cartApi, CartItemDetail } from '@/lib/api/cart';
import { productsApi } from '@/lib/api/products';
import { ordersApi } from '@/lib/api/orders';
import { couponsApi, UserCoupon } from '@/lib/api/coupons';
import { paymentsApi } from '@/lib/api/payments'; // ✅ 결제 API import

export default function OrderPage() {
    const router = useRouter();
    const [loading, setLoading] = useState(true);
    const [orderItems, setOrderItems] = useState<CartItemDetail[]>([]);

    // 쿠폰 상태
    const [availableCoupons, setAvailableCoupons] = useState<UserCoupon[]>([]);
    const [selectedCouponId, setSelectedCouponId] = useState<number | null>(null);

    // 결제 모달 및 토스 위젯 상태
    const [isPaymentModalOpen, setIsPaymentModalOpen] = useState(false);
    const [paymentInfo, setPaymentInfo] = useState<any>(null); // 결제 준비 데이터
    const widgetsRef = useRef<any>(null);

    // 1. 초기 데이터 로드 (장바구니 & 쿠폰)
    useEffect(() => {
        const initOrderPage = async () => {
            try {
                setLoading(true);

                // 1-1. 장바구니 조회
                const simpleCartItems = await cartApi.getCart();
                if (!Array.isArray(simpleCartItems) || simpleCartItems.length === 0) {
                    alert("장바구니가 비어있습니다.");
                    router.push('/cart');
                    return;
                }

                // 1-2. 상품 상세 정보 병합
                const detailedItems = await Promise.all(
                    simpleCartItems.map(async (item) => {
                        try {
                            const productInfo = await productsApi.getProduct(item.productId);
                            return {
                                ...item,
                                price: productInfo.salePrice,
                                originalPrice: productInfo.price,
                                image: productInfo.imageUrl || 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
                                stock: productInfo.stock,
                                storeName: productInfo.storeName,
                                storeId: productInfo.storeId,
                                discount: productInfo.price - productInfo.salePrice,
                            };
                        } catch (e) {
                            return { ...item, price: 0 } as CartItemDetail;
                        }
                    })
                );
                setOrderItems(detailedItems);

                // 1-3. ✅ 실제 쿠폰 목록 조회
                const myCoupons = await couponsApi.getMyCoupons();
                setAvailableCoupons(myCoupons);

            } catch (error) {
                console.error("데이터 로딩 실패:", error);
                alert("주문 정보를 불러오는데 실패했습니다.");
                router.back();
            } finally {
                setLoading(false);
            }
        };

        initOrderPage();
    }, [router]);

    // 금액 계산 로직
    const storeInfo = orderItems.length > 0 ? { id: orderItems[0].storeId, name: orderItems[0].storeName } : null;
    const productTotal = orderItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const selectedCoupon = availableCoupons.find(c => c.id === selectedCouponId);

    // 쿠폰 할인 계산
    const couponDiscount = (selectedCoupon && productTotal >= selectedCoupon.minAmount)
        ? selectedCoupon.discountAmount
        : 0;

    const finalTotal = Math.max(0, productTotal - couponDiscount);

    // ✅ 주문 생성 및 결제 모달 열기 핸들러
    const handleOrder = async () => {
        if (!storeInfo) {
            alert("매장 정보를 찾을 수 없습니다.");
            return;
        }

        if (!confirm(`${finalTotal.toLocaleString()}원을 결제하시겠습니까?`)) return;

        try {
            // 1. 주문 생성
            const orderResponse = await ordersApi.createOrder({
                storeId: storeInfo.id,
                userCouponId: selectedCouponId
            });
            console.log("✅ 주문 성공:", orderResponse);

            // 3. 결제 준비 (백엔드에서 orderKey 등 받아오기)
            const prepareResponse = await paymentsApi.preparePayment(orderResponse.id);

            // 4. 결제 정보 저장 및 모달 오픈
            setPaymentInfo({
                ...prepareResponse,
                orderName: orderItems.length > 1
                    ? `${orderItems[0].productName} 외 ${orderItems.length - 1}건`
                    : orderItems[0].productName
            });
            setIsPaymentModalOpen(true);

        } catch (error: any) {
            console.error("주문 실패:", error);
            const msg = error.response?.data?.message || "주문 처리에 실패했습니다.";
            alert(msg);
        }
    };

    // ✅ 토스 위젯 렌더링 (모달이 열릴 때 실행)
    useEffect(() => {
        if (!isPaymentModalOpen || !paymentInfo || !window.TossPayments) return;

        const renderTossWidgets = async () => {
            try {
                const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm"; // 테스트 키
                const tossPayments = window.TossPayments(clientKey);
                const widgets = tossPayments.widgets({
                    customerKey: paymentInfo.customerKey || "guest"
                });

                await widgets.setAmount({
                    currency: "KRW",
                    value: Number(paymentInfo.amount),
                });

                await Promise.all([
                    widgets.renderPaymentMethods({ selector: "#payment-method", variantKey: "DEFAULT" }),
                    widgets.renderAgreement({ selector: "#agreement", variantKey: "AGREEMENT" }),
                ]);

                widgetsRef.current = widgets;
            } catch (err) {
                console.error("토스 위젯 렌더링 실패:", err);
            }
        };

        renderTossWidgets();
    }, [isPaymentModalOpen, paymentInfo]);

    // ✅ 실제 결제 요청 (모달 내부 버튼 클릭 시)
    const requestTossPayment = async () => {
        if (!widgetsRef.current || !paymentInfo) return;

        try {
            const FRONTEND_URL = window.location.origin;

            await widgetsRef.current.requestPayment({
                orderId: paymentInfo.orderKey,
                orderName: paymentInfo.orderName,

                successUrl: `${FRONTEND_URL}/payment/success`,
                failUrl: `${FRONTEND_URL}/payment/fail`,
            });
        } catch (err) {
            console.error("결제 요청 실패:", err);
        }
    };

    if (loading) return <div className="min-h-screen flex items-center justify-center"><Loader2 className="w-10 h-10 text-primary-500 animate-spin" /></div>;

    return (
        <div className="min-h-screen bg-gray-50 py-8 relative">
            {/* 토스 SDK 로드 */}
            <Script src="https://js.tosspayments.com/v2/standard" />

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <h1 className="text-3xl font-bold text-gray-900 mb-8">주문하기</h1>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* 왼쪽: 주문 정보 */}
                    <div className="lg:col-span-2 space-y-6">
                        {/* 픽업 매장 정보 */}
                        {storeInfo && (
                            <Card>
                                <h2 className="text-xl font-bold text-gray-900 mb-4">픽업 매장</h2>
                                <div className="flex items-center p-4 bg-primary-50 rounded-lg">
                                    <Store className="w-6 h-6 text-primary-600 mr-3" />
                                    <div>
                                        <p className="font-semibold text-gray-900">{storeInfo.name}</p>
                                        <p className="text-sm text-primary-700 mt-1">
                                            <Clock className="w-4 h-4 inline mr-1" />
                                            30분 내에 픽업 부탁드립니다
                                        </p>
                                    </div>
                                </div>
                            </Card>
                        )}

                        {/* 주문 상품 */}
                        <Card>
                            <h2 className="text-xl font-bold text-gray-900 mb-4">
                                주문 상품 ({orderItems?.length || 0}개)
                            </h2>
                            <div className="space-y-4">
                                {orderItems?.map((item) => (
                                    <div key={item.id} className="flex items-center">
                                        <img
                                            src={item.image}
                                            alt={item.productName}
                                            className="w-20 h-20 object-cover rounded-lg mr-4 bg-gray-100"
                                        />
                                        <div className="flex-1">
                                            <p className="font-medium text-gray-900">{item.productName}</p>
                                            <p className="text-sm text-gray-600">수량: {item.quantity}개</p>
                                        </div>
                                        <p className="font-semibold text-gray-900">
                                            {(item.price * item.quantity).toLocaleString()}원
                                        </p>
                                    </div>
                                ))}
                            </div>
                        </Card>

                        {/* ✅ 쿠폰 선택 (실제 데이터 연동) */}
                        <Card>
                            <div className="flex items-center justify-between mb-4">
                                <h2 className="text-xl font-bold text-gray-900">쿠폰</h2>
                                <Badge variant="default">{availableCoupons.length}개 보유</Badge>
                            </div>
                            <div className="space-y-3">
                                <button
                                    onClick={() => setSelectedCouponId(null)}
                                    className={`w-full p-4 border-2 rounded-lg text-left transition ${
                                        selectedCouponId === null
                                            ? 'border-primary-500 bg-primary-50'
                                            : 'border-gray-200 hover:border-gray-300'
                                    }`}
                                >
                                    <p className="font-medium text-gray-900">쿠폰 사용 안 함</p>
                                </button>
                                {availableCoupons.map((coupon) => {
                                    // 최소 주문 금액 체크
                                    const isUsable = productTotal >= coupon.minAmount;
                                    return (
                                        <button
                                            key={coupon.id}
                                            onClick={() => setSelectedCouponId(coupon.id)}
                                            disabled={!isUsable}
                                            className={`w-full p-4 border-2 rounded-lg text-left transition ${
                                                selectedCouponId === coupon.id
                                                    ? 'border-primary-500 bg-primary-50'
                                                    : isUsable ? 'border-gray-200 hover:border-gray-300' : 'border-gray-100 bg-gray-50 opacity-60 cursor-not-allowed'
                                            }`}
                                        >
                                            <div className="flex items-center justify-between">
                                                <div className="flex items-center">
                                                    <Tag className="w-5 h-5 text-primary-500 mr-2" />
                                                    <div>
                                                        <p className="font-medium text-gray-900">{coupon.couponName}</p>
                                                        <p className="text-sm text-gray-600">
                                                            {coupon.minAmount.toLocaleString()}원 이상 구매 시
                                                        </p>
                                                        {!isUsable && <p className="text-xs text-red-500">주문 금액이 부족합니다</p>}
                                                    </div>
                                                </div>
                                                <div className="text-right">
                                                    <span className="text-lg font-bold text-primary-600">
                                                        -{coupon.discountAmount.toLocaleString()}원
                                                    </span>
                                                </div>
                                            </div>
                                        </button>
                                    );
                                })}
                            </div>
                        </Card>

                        {/* 결제 수단 영역 제거됨 */}
                    </div>

                    {/* 오른쪽: 결제 금액 */}
                    <div className="lg:col-span-1">
                        <div className="sticky top-20">
                            <Card>
                                <h2 className="text-xl font-bold text-gray-900 mb-6">결제 금액</h2>
                                <div className="space-y-3 mb-6">
                                    <div className="flex justify-between text-gray-700">
                                        <span>상품 금액</span>
                                        <span>{productTotal.toLocaleString()}원</span>
                                    </div>
                                    {couponDiscount > 0 && (
                                        <div className="flex justify-between text-red-500">
                                            <span>쿠폰 할인</span>
                                            <span>-{couponDiscount.toLocaleString()}원</span>
                                        </div>
                                    )}
                                </div>

                                <div className="pt-6 border-t border-gray-200 mb-6">
                                    <div className="flex justify-between items-center">
                                        <span className="text-lg font-semibold text-gray-900">최종 결제 금액</span>
                                        <span className="text-2xl font-bold text-primary-600">
                                          {finalTotal.toLocaleString()}원
                                        </span>
                                    </div>
                                </div>

                                <Button fullWidth size="lg" onClick={handleOrder}>
                                    {finalTotal.toLocaleString()}원 결제하기
                                    <ChevronRight className="w-5 h-5 ml-2" />
                                </Button>
                            </Card>
                        </div>
                    </div>
                </div>
            </div>

            {/* ✅ 토스 결제 위젯 모달 (추가 창) */}
            {isPaymentModalOpen && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
                    <div className="bg-white rounded-2xl w-full max-w-lg shadow-2xl overflow-hidden max-h-[90vh] overflow-y-auto">
                        <div className="p-4 border-b flex justify-between items-center sticky top-0 bg-white z-10">
                            <h2 className="text-lg font-bold">결제 수단 선택</h2>
                            <button
                                onClick={() => setIsPaymentModalOpen(false)}
                                className="p-2 hover:bg-gray-100 rounded-full"
                            >
                                <X className="w-5 h-5 text-gray-500" />
                            </button>
                        </div>

                        <div className="p-4">
                            {/* 토스 위젯이 렌더링될 영역 */}
                            <div id="payment-method" className="w-full" />
                            <div id="agreement" className="w-full mt-4" />
                        </div>

                        <div className="p-4 border-t bg-gray-50">
                            <Button fullWidth size="lg" onClick={requestTossPayment}>
                                {Number(paymentInfo?.amount || finalTotal).toLocaleString()}원 결제 요청
                            </Button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
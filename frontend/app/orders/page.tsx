'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { MapPin, Clock, CreditCard, Tag, ChevronRight } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import Input from '@/components/ui/Input';

export default function OrderPage() {
    const router = useRouter();
    const [selectedCoupon, setSelectedCoupon] = useState<number | null>(null);
    const [paymentMethod, setPaymentMethod] = useState('card');
    const [orderRequest, setOrderRequest] = useState('');

    // TODO: 실제 API 데이터로 교체
    const orderItems = [
        {
            id: 1,
            productId: 1,
            name: '프리미엄 크루아상 3입',
            storeName: '파리바게뜨 강남점',
            quantity: 2,
            price: 3000,
            image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
        },
        {
            id: 2,
            productId: 2,
            name: '프리미엄 닭가슴살 샐러드',
            storeName: '샐러디 역삼점',
            quantity: 1,
            price: 7200,
            image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
        },
    ];

    const availableCoupons = [
        {
            id: 1,
            name: '5,000원 할인',
            discount: 5000,
            minAmount: 10000,
        },
        {
            id: 2,
            name: '10% 할인',
            discountRate: 10,
            minAmount: 15000,
        },
    ];

    const deliveryInfo = {
        address: '서울특별시 강남구 테헤란로 123',
        phone: '010-1234-5678',
        name: '홍길동',
    };

    const productTotal = orderItems.reduce((sum, item) => sum + item.price * item.quantity, 0);
    const deliveryFee = 0;
    const couponDiscount = selectedCoupon
        ? availableCoupons.find((c) => c.id === selectedCoupon)?.discount || 0
        : 0;
    const finalTotal = productTotal + deliveryFee - couponDiscount;

    const handleOrder = () => {
        // TODO: 실제 주문 API 호출
        console.log('주문하기:', {
            items: orderItems,
            couponId: selectedCoupon,
            paymentMethod,
            orderRequest,
        });
        router.push('/order/complete?orderId=20260209001');
    };

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <h1 className="text-3xl font-bold text-gray-900 mb-8">주문하기</h1>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* 왼쪽: 주문 정보 */}
                    <div className="lg:col-span-2 space-y-6">
                        {/* 배송 정보 */}
                        <Card>
                            <div className="flex items-center justify-between mb-4">
                                <h2 className="text-xl font-bold text-gray-900">배송 정보</h2>
                                <Button variant="outline" size="sm">변경</Button>
                            </div>
                            <div className="space-y-3">
                                <div className="flex items-start">
                                    <MapPin className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                                    <div>
                                        <p className="font-medium text-gray-900">{deliveryInfo.address}</p>
                                        <p className="text-sm text-gray-600 mt-1">
                                            {deliveryInfo.name} · {deliveryInfo.phone}
                                        </p>
                                    </div>
                                </div>
                                <div className="pt-3 border-t border-gray-200">
                                    <Input
                                        placeholder="배송 요청사항 (예: 문 앞에 놓아주세요)"
                                        value={orderRequest}
                                        onChange={(e) => setOrderRequest(e.target.value)}
                                    />
                                </div>
                            </div>
                        </Card>

                        {/* 주문 상품 */}
                        <Card>
                            <h2 className="text-xl font-bold text-gray-900 mb-4">주문 상품</h2>
                            <div className="space-y-4">
                                {orderItems.map((item) => (
                                    <div key={item.id} className="flex items-center">
                                        <img
                                            src={item.image}
                                            alt={item.name}
                                            className="w-20 h-20 object-cover rounded-lg mr-4"
                                        />
                                        <div className="flex-1">
                                            <p className="text-sm text-gray-500">{item.storeName}</p>
                                            <p className="font-medium text-gray-900">{item.name}</p>
                                            <p className="text-sm text-gray-600">수량: {item.quantity}개</p>
                                        </div>
                                        <p className="font-semibold text-gray-900">
                                            {(item.price * item.quantity).toLocaleString()}원
                                        </p>
                                    </div>
                                ))}
                            </div>
                        </Card>

                        {/* 쿠폰 선택 */}
                        <Card>
                            <div className="flex items-center justify-between mb-4">
                                <h2 className="text-xl font-bold text-gray-900">쿠폰</h2>
                                <Badge variant="default">{availableCoupons.length}개 사용 가능</Badge>
                            </div>
                            <div className="space-y-3">
                                <button
                                    onClick={() => setSelectedCoupon(null)}
                                    className={`w-full p-4 border-2 rounded-lg text-left transition ${
                                        selectedCoupon === null
                                            ? 'border-primary-500 bg-primary-50'
                                            : 'border-gray-200 hover:border-gray-300'
                                    }`}
                                >
                                    <p className="font-medium text-gray-900">쿠폰 사용 안 함</p>
                                </button>
                                {availableCoupons.map((coupon) => (
                                    <button
                                        key={coupon.id}
                                        onClick={() => setSelectedCoupon(coupon.id)}
                                        disabled={productTotal < coupon.minAmount}
                                        className={`w-full p-4 border-2 rounded-lg text-left transition disabled:opacity-50 disabled:cursor-not-allowed ${
                                            selectedCoupon === coupon.id
                                                ? 'border-primary-500 bg-primary-50'
                                                : 'border-gray-200 hover:border-gray-300'
                                        }`}
                                    >
                                        <div className="flex items-center justify-between">
                                            <div className="flex items-center">
                                                <Tag className="w-5 h-5 text-primary-500 mr-2" />
                                                <div>
                                                    <p className="font-medium text-gray-900">{coupon.name}</p>
                                                    <p className="text-sm text-gray-600">
                                                        {coupon.minAmount.toLocaleString()}원 이상 구매 시
                                                    </p>
                                                </div>
                                            </div>
                                            {selectedCoupon === coupon.id && (
                                                <Badge variant="success">적용됨</Badge>
                                            )}
                                        </div>
                                    </button>
                                ))}
                            </div>
                        </Card>

                        {/* 결제 수단 */}
                        <Card>
                            <h2 className="text-xl font-bold text-gray-900 mb-4">결제 수단</h2>
                            <div className="space-y-3">
                                <button
                                    onClick={() => setPaymentMethod('card')}
                                    className={`w-full p-4 border-2 rounded-lg text-left transition ${
                                        paymentMethod === 'card'
                                            ? 'border-primary-500 bg-primary-50'
                                            : 'border-gray-200 hover:border-gray-300'
                                    }`}
                                >
                                    <div className="flex items-center">
                                        <CreditCard className="w-5 h-5 text-gray-600 mr-3" />
                                        <span className="font-medium text-gray-900">신용카드</span>
                                    </div>
                                </button>
                                <button
                                    onClick={() => setPaymentMethod('kakao')}
                                    className={`w-full p-4 border-2 rounded-lg text-left transition ${
                                        paymentMethod === 'kakao'
                                            ? 'border-primary-500 bg-primary-50'
                                            : 'border-gray-200 hover:border-gray-300'
                                    }`}
                                >
                                    <div className="flex items-center">
                                        <div className="w-5 h-5 bg-[#FEE500] rounded mr-3"></div>
                                        <span className="font-medium text-gray-900">카카오페이</span>
                                    </div>
                                </button>
                                <button
                                    onClick={() => setPaymentMethod('toss')}
                                    className={`w-full p-4 border-2 rounded-lg text-left transition ${
                                        paymentMethod === 'toss'
                                            ? 'border-primary-500 bg-primary-50'
                                            : 'border-gray-200 hover:border-gray-300'
                                    }`}
                                >
                                    <div className="flex items-center">
                                        <div className="w-5 h-5 bg-blue-500 rounded mr-3"></div>
                                        <span className="font-medium text-gray-900">토스페이</span>
                                    </div>
                                </button>
                            </div>
                        </Card>
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
                                    <div className="flex justify-between text-gray-700">
                                        <span>배송비</span>
                                        <span className="text-green-600 font-medium">무료</span>
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

                                <div className="mt-4 p-4 bg-blue-50 rounded-lg">
                                    <p className="text-sm text-blue-800">
                                        💡 주문 완료 후 30분 이내 픽업 또는 배송됩니다
                                    </p>
                                </div>
                            </Card>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
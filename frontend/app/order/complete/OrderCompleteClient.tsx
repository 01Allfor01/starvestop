// app/order/complete/OrderCompleteClient.tsx
'use client';

import { useState, useEffect } from 'react';
import { useSearchParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { CheckCircle, Store, MapPin, Clock, Home, FileText, MessageCircle, Loader2 } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { ordersApi, OrderResponse, OrderProductResponse } from '@/lib/api/orders';
import { storesApi } from '@/lib/api/stores';

export default function OrderCompleteClient() {
    const searchParams = useSearchParams();
    const router = useRouter();

    const orderIdParam = searchParams.get('orderId');

    const [loading, setLoading] = useState(true);
    const [orderData, setOrderData] = useState<OrderResponse | null>(null);
    const [orderItems, setOrderItems] = useState<OrderProductResponse[]>([]);
    const [storeData, setStoreData] = useState<any>(null);

    useEffect(() => {
        const fetchCompleteData = async () => {
            if (!orderIdParam) {
                alert('잘못된 접근입니다.');
                router.push('/');
                return;
            }

            try {
                setLoading(true);
                const orderId = Number(orderIdParam);

                const [order, items] = await Promise.all([ordersApi.getOrder(orderId), ordersApi.getOrderProducts(orderId)]);

                setOrderData(order);
                setOrderItems(items);

                if (order.storeId) {
                    try {
                        const store = await storesApi.getStore(order.storeId);
                        setStoreData(store);
                    } catch {
                        console.warn('가게 정보 로드 실패');
                    }
                }
            } catch (error) {
                console.error('주문 정보 조회 실패:', error);
                alert('주문 정보를 불러오지 못했습니다.');
                router.push('/mypage/orders');
            } finally {
                setLoading(false);
            }
        };

        fetchCompleteData();
    }, [orderIdParam, router]);

    const getStatusText = (status: string) => {
        const statusMap: Record<string, string> = {
            REQUESTED: '주문 완료',
            CONFIRMED: '주문 확인',
            COMPLETED: '수령 완료',
            CANCELLED: '주문 취소',
        };
        return statusMap[status] || status;
    };

    if (loading) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center">
                <Loader2 className="w-10 h-10 text-primary-500 animate-spin mb-4" />
                <p className="text-gray-600">주문 내역을 불러오는 중입니다...</p>
            </div>
        );
    }

    if (!orderData) return null;

    const itemsTotal = orderItems.reduce((sum, item) => sum + item.productPrice * item.quantity, 0);
    const discount = orderData.discountedPrice || 0;
    const finalTotal = Math.max(0, itemsTotal - discount);

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 성공 메시지 */}
                <div className="text-center mb-8">
                    <div className="inline-flex items-center justify-center w-20 h-20 bg-green-100 rounded-full mb-4">
                        <CheckCircle className="w-12 h-12 text-green-500" />
                    </div>
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">주문이 완료되었습니다!</h1>
                    <p className="text-gray-600">주문번호: {orderData.orderKey}</p>
                </div>

                {/* 주문 상태 */}
                <Card className="mb-6">
                    <div className="flex items-center justify-between mb-6">
                        <h2 className="text-xl font-bold text-gray-900">주문 상태</h2>
                        <Badge variant="success">{getStatusText(orderData.status)}</Badge>
                    </div>

                    <div className="flex items-center p-4 bg-green-50 rounded-lg">
                        <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mr-4">
                            <CheckCircle className="w-7 h-7 text-green-500" />
                        </div>
                        <div className="flex-1">
                            <p className="font-semibold text-gray-900">주문 접수 완료</p>
                            <p className="text-sm text-gray-600">{new Date(orderData.createdAt).toLocaleString()}</p>
                            <p className="text-sm text-primary-700 font-medium mt-1">
                                <Clock className="w-4 h-4 inline mr-1" />
                                30분 이내에 픽업 부탁드립니다
                            </p>
                        </div>
                    </div>
                </Card>

                {/* 픽업 매장 정보 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">픽업 매장</h2>
                    <div className="space-y-3">
                        <div className="flex items-start">
                            <Store className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                            <div className="flex-1">
                                <Link href={`/stores/${orderData.storeId}`}>
                                    <p className="font-semibold text-gray-900 hover:text-primary-600">{storeData?.name || '매장 정보 확인'}</p>
                                </Link>
                            </div>
                        </div>
                        <div className="flex items-start">
                            <MapPin className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                            <div>
                                <p className="font-medium text-gray-900">매장 위치</p>
                                <p className="text-sm text-gray-600">{storeData?.address || ''}</p>
                            </div>
                        </div>
                    </div>
                </Card>

                {/* 주문 상품 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">주문 상품</h2>
                    <div className="space-y-4">
                        {orderItems.map((item) => (
                            <div key={item.id} className="flex items-center">
                                <div className="w-16 h-16 bg-gray-100 rounded-lg flex items-center justify-center mr-4">
                                    <Store className="w-8 h-8 text-gray-400" />
                                </div>
                                <div className="flex-1">
                                    <p className="font-medium text-gray-900">{item.productName}</p>
                                    <p className="text-sm text-gray-600">수량: {item.quantity}개</p>
                                </div>
                                <p className="font-semibold text-gray-900">{Math.round(item.productPrice * item.quantity).toLocaleString()}원</p>
                            </div>
                        ))}
                    </div>

                    <div className="mt-6 pt-6 border-t border-gray-200 space-y-3">
                        <div className="flex justify-between items-center text-gray-600">
                            <span>상품 금액</span>
                            <span>{Math.round(itemsTotal).toLocaleString()}원</span>
                        </div>

                        {discount > 0 && (
                            <div className="flex justify-between items-center">
                                <span className="text-gray-600">쿠폰 할인</span>
                                <span className="text-lg font-semibold text-red-600">-{Math.round(discount).toLocaleString()}원</span>
                            </div>
                        )}

                        <div className="flex justify-between items-center pt-3 border-t border-gray-200">
                            <span className="text-lg font-semibold text-gray-900">총 결제 금액</span>
                            <span className="text-2xl font-bold text-primary-600">{Math.round(finalTotal).toLocaleString()}원</span>
                        </div>
                    </div>
                </Card>

                {/* 버튼 영역 */}
                <div className="space-y-3">
                    <Link href={`/mypage/orders/${orderData.id}`}>
                        <Button fullWidth size="lg">
                            <FileText className="w-5 h-5 mr-2" />
                            주문 상세보기
                        </Button>
                    </Link>

                    <Link href={`/chat/${orderData.storeId}`}>
                        <Button variant="outline" fullWidth size="lg">
                            <MessageCircle className="w-5 h-5 mr-2" />
                            가게에 문의하기
                        </Button>
                    </Link>
                </div>

                <div className="mt-8">
                    <Link href="/" className="block text-center">
                        <Button variant="ghost" fullWidth size="lg" className="text-gray-600 hover:text-gray-900">
                            <Home className="w-5 h-5 mr-2" />
                            홈으로 돌아가기
                        </Button>
                    </Link>
                </div>
            </div>
        </div>
    );
}

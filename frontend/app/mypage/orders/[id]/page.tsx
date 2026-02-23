'use client';

import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, Store, MapPin, Clock, MessageCircle, Loader2 } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { ordersApi } from '@/lib/api/orders';
import { productsApi } from '@/lib/api/products';
import { openOrCreateChatRoom } from '@/lib/helpers/chat';

export default function OrderDetailPage() {
    const params = useParams();
    const router = useRouter();
    const orderId = Number(params.id);

    const [order, setOrder] = useState<any>(null);
    const [orderItems, setOrderItems] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string>('');

    useEffect(() => {
        const fetchOrder = async () => {
            try {
                console.log('🔍 주문 ID:', orderId);
                setLoading(true);
                setError('');

                if (!orderId || isNaN(orderId)) {
                    throw new Error('유효하지 않은 주문 ID입니다.');
                }

                // 1. 주문 정보 조회
                const orderData = await ordersApi.getOrder(orderId);
                console.log('✅ 주문 데이터:', orderData);

                if (!orderData) {
                    throw new Error('주문 정보를 찾을 수 없습니다.');
                }

                setOrder(orderData);

                // 2. 주문 상품 목록 조회
                try {
                    const items = await ordersApi.getOrderProducts(orderId);
                    console.log('✅ 주문 상품:', items);

                    // ✅ 각 상품의 status 확인 및 가격 결정
                    const itemsWithPrice = await Promise.all(
                        (items || []).map(async (item: any) => {
                            try {
                                if (!item.productId) {
                                    return { ...item, displayPrice: item.productPrice || 0 };
                                }

                                // 상품 상세 정보 조회
                                const productDetail = await productsApi.getProduct(item.productId);
                                const isSale = productDetail.status === 'SALE';
                                const displayPrice = isSale ? productDetail.salePrice : productDetail.price;

                                return {
                                    ...item,
                                    displayPrice: displayPrice,
                                    status: productDetail.status
                                };
                            } catch (error) {
                                console.error(`상품 ${item.productId} 정보 조회 실패:`, error);
                                // 조회 실패 시 기존 productPrice 사용
                                return { ...item, displayPrice: item.productPrice || 0 };
                            }
                        })
                    );

                    setOrderItems(itemsWithPrice);
                } catch (itemError) {
                    console.error('⚠️ 주문 상품 로딩 실패:', itemError);
                    setOrderItems([]);
                }

            } catch (error: any) {
                console.error('❌ 주문 상세 로딩 실패:', error);
                console.error('❌ 에러 상세:', error.response?.data);

                const errorMsg = error.response?.data?.message || error.message || '주문 정보를 불러올 수 없습니다.';
                setError(errorMsg);
            } finally {
                setLoading(false);
            }
        };

        if (orderId) {
            fetchOrder();
        } else {
            setError('유효하지 않은 주문 ID입니다.');
            setLoading(false);
        }
    }, [orderId]);

    const getStatusColor = (status: string): "default" | "success" | "warning" => {
        switch (status) {
            case 'PAID':
                return 'success';
            case 'PENDING':
                return 'warning';
            default:
                return 'default';
        }
    };

    const getStatusText = (status: string): string => {
        switch (status) {
            case 'PAID':
                return '결제 완료';
            case 'PENDING':
                return '결제 대기';
            case 'FAILED':
                return '결제 실패';
            case 'CANCELED':
                return '주문 취소';
            default:
                return status;
        }
    };

    const handleContactStore = async () => {
        if (!order?.storeId) {
            alert('매장 정보를 불러올 수 없습니다');
            return;
        }
        await openOrCreateChatRoom(order.storeId, router);
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <Loader2 className="w-12 h-12 text-primary-500 animate-spin mx-auto mb-4" />
                    <p className="text-gray-600">주문 정보를 불러오는 중...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <p className="text-red-600 mb-4">{error}</p>
                    <Button onClick={() => router.push('/mypage/orders')}>
                        주문 목록으로 돌아가기
                    </Button>
                </div>
            </div>
        );
    }

    if (!order) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <p className="text-gray-600 mb-4">주문 정보를 찾을 수 없습니다</p>
                    <Button onClick={() => router.push('/mypage/orders')}>
                        주문 목록으로 돌아가기
                    </Button>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href="/mypage/orders" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>주문 목록으로</span>
                    </Link>
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">주문 상세</h1>
                            <p className="text-gray-600">주문번호: {order.orderKey || order.id}</p>
                        </div>
                        <Badge variant={getStatusColor(order.status || '')}>
                            {getStatusText(order.status || '')}
                        </Badge>
                    </div>
                </div>

                {/* 픽업 매장 정보 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">픽업 매장</h2>
                    <div className="space-y-3">
                        <div className="flex items-start">
                            <Store className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                            <div className="flex-1">
                                {order.storeId ? (
                                    <Link href={`/stores/${order.storeId}`}>
                                        <p className="font-semibold text-gray-900 hover:text-primary-600">
                                            {order.storeName || '매장 정보 없음'}
                                        </p>
                                    </Link>
                                ) : (
                                    <p className="font-semibold text-gray-900">
                                        {order.storeName || '매장 정보 없음'}
                                    </p>
                                )}
                            </div>
                        </div>
                        {order.storeAddress && (
                            <div className="flex items-start">
                                <MapPin className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                                <div>
                                    <p className="font-medium text-gray-900">매장 위치</p>
                                    <p className="text-sm text-gray-600">{order.storeAddress}</p>
                                </div>
                            </div>
                        )}
                        {order.request && (
                            <div className="flex items-start">
                                <MessageCircle className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                                <div>
                                    <p className="font-medium text-gray-900">요청사항</p>
                                    <p className="text-sm text-gray-600">{order.request}</p>
                                </div>
                            </div>
                        )}
                        {order.status === 'PAID' && (
                            <div className="mt-4 p-3 bg-green-50 rounded-lg">
                                <p className="text-sm text-green-700 font-medium flex items-center">
                                    <Clock className="w-4 h-4 inline mr-1.5" />
                                    30분 이내에 픽업 부탁드립니다
                                </p>
                            </div>
                        )}
                    </div>
                </Card>

                {/* 주문 상품 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">주문 상품</h2>
                    {orderItems && orderItems.length > 0 ? (
                        <div className="space-y-4">
                            {orderItems.map((item: any, index: number) => (
                                <div key={item.id || index} className="flex items-center pb-4 border-b border-gray-100 last:border-0 last:pb-0">
                                    {item.productId ? (
                                        <Link href={`/products/${item.productId}`}>
                                            <img
                                                src={item.imageUrl || 'https://images.unsplash.com/photo-1555507036-ab1f4038808a'}
                                                alt={item.productName || '상품'}
                                                className="w-20 h-20 object-cover rounded-lg mr-4 hover:opacity-80 transition"
                                            />
                                        </Link>
                                    ) : (
                                        <img
                                            src={item.imageUrl || 'https://images.unsplash.com/photo-1555507036-ab1f4038808a'}
                                            alt={item.productName || '상품'}
                                            className="w-20 h-20 object-cover rounded-lg mr-4"
                                        />
                                    )}
                                    <div className="flex-1">
                                        {item.productId ? (
                                            <Link href={`/products/${item.productId}`}>
                                                <p className="font-medium text-gray-900 hover:text-primary-500">
                                                    {item.productName || '상품명 없음'}
                                                </p>
                                            </Link>
                                        ) : (
                                            <p className="font-medium text-gray-900">
                                                {item.productName || '상품명 없음'}
                                            </p>
                                        )}
                                        <p className="text-sm text-gray-600">수량: {item.quantity || 0}개</p>
                                    </div>
                                    <div className="text-right">
                                        <p className="font-semibold text-gray-900">
                                            {((item.displayPrice || 0) * (item.quantity || 0)).toLocaleString()}원
                                        </p>
                                        <p className="text-sm text-gray-500">
                                            개당 {(item.displayPrice || 0).toLocaleString()}원
                                        </p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p className="text-gray-500 text-center py-4">주문 상품 정보를 불러올 수 없습니다</p>
                    )}
                </Card>

                {/* 결제 정보 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">결제 정보</h2>
                    <div className="space-y-3 mb-6">
                        <div className="flex justify-between text-gray-700">
                            <span>상품 금액</span>
                            <span>{(order.amount || 0).toLocaleString()}원</span>
                        </div>
                        {order.discountedPrice > 0 && (
                            <div className="flex justify-between text-red-500">
                                <span>할인 금액</span>
                                <span>-{order.discountedPrice.toLocaleString()}원</span>
                            </div>
                        )}
                    </div>

                    <div className="pt-4 border-t border-gray-200 mb-4">
                        <div className="flex justify-between items-center">
                            <span className="text-lg font-semibold text-gray-900">총 결제 금액</span>
                            <span className="text-2xl font-bold text-primary-600">
                                {((order.amount || 0) - (order.discountedPrice || 0)).toLocaleString()}원
                            </span>
                        </div>
                    </div>

                    {order.createdAt && (
                        <div className="text-sm text-gray-500">
                            결제일시: {new Date(order.createdAt).toLocaleString('ko-KR')}
                        </div>
                    )}
                </Card>

                {/* 가게에 문의하기 버튼 */}
                <div>
                    <Button
                        variant="outline"
                        fullWidth
                        size="lg"
                        onClick={handleContactStore}
                        disabled={!order.storeId}
                    >
                        <MessageCircle className="w-5 h-5 mr-2" />
                        가게에 문의하기
                    </Button>
                </div>
            </div>
        </div>
    );
}
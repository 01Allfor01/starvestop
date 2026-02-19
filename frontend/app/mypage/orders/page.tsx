'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { ArrowLeft, Loader2, FileText, ShoppingBag } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { ordersApi, OrderResponse, OrderProductResponse } from '@/lib/api/orders';

// 주문 + 상품 요약 타입
interface OrderWithItems extends OrderResponse {
    itemsSummary: string;
}

// 주문 상태 매핑
const getStatusText = (status: string): string => {
    const statusMap: Record<string, string> = {
        PENDING: '결제 미진행',
        PAID: '주문 완료',
        FAILED: '결제 실패',
        CANCELED: '주문 취소',
    };
    return statusMap[status] || status;
};

// 주문 상태별 뱃지 variant
type OrderStatus = 'PENDING' | 'PAID' | 'FAILED' | 'CANCELED';

const getStatusVariant = (
    status: OrderStatus
): 'default' | 'success' | 'warning' | 'sale' => {
    const variantMap: Record<
        OrderStatus,
        'default' | 'success' | 'warning' | 'sale'
    > = {
        PENDING: 'warning',
        PAID: 'success',
        FAILED: 'sale',
        CANCELED: 'default',
    };

    return variantMap[status];
};


export default function OrdersPage() {
    const [orders, setOrders] = useState<OrderWithItems[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchOrders = async () => {
            try {
                setLoading(true);
                const ordersData: OrderResponse[] = await ordersApi.getOrders();

                const ordersWithItems = await Promise.all(
                    ordersData.map(async (order: OrderResponse) => {
                        try {
                            const products: OrderProductResponse[] = await ordersApi.getOrderProducts(order.id);

                            let summary = '상품 정보 없음';
                            if (products.length > 0) {
                                summary =
                                    products.length > 1
                                        ? `${products[0].productName} 외 ${products.length - 1}건`
                                        : products[0].productName;
                            }

                            return { ...order, itemsSummary: summary };
                        } catch {
                            return { ...order, itemsSummary: '상품 정보 로딩 실패' };
                        }
                    })
                );

                setOrders(ordersWithItems);
            } catch (error) {
                console.error('❌ 주문 목록 로딩 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchOrders();
    }, []);

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <Loader2 className="w-12 h-12 text-primary-500 animate-spin" />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link
                        href="/mypage"
                        className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4 transition-colors"
                    >
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>마이페이지로</span>
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900">주문 내역</h1>
                </div>

                {/* 주문 목록 */}
                {orders.length > 0 ? (
                    <div className="space-y-4">
                        {orders.map((order) => (
                            <Card key={order.id} hover padding="md">
                                <div className="flex justify-between items-start mb-3">
                                    <div className="flex-1">
                                        <div className="flex items-center gap-2 mb-2">
                                            <Badge variant={getStatusVariant(order.status)}>
                                                {getStatusText(order.status)}
                                            </Badge>
                                            <span className="text-sm text-gray-500">
                                                {new Date(order.createdAt).toLocaleDateString('ko-KR', {
                                                    year: 'numeric',
                                                    month: 'long',
                                                    day: 'numeric',
                                                })}
                                            </span>
                                        </div>
                                        <h3 className="font-semibold text-gray-900 text-lg mb-1">
                                            {order.storeName}
                                        </h3>
                                        <p className="text-sm text-gray-600">{order.itemsSummary}</p>
                                    </div>
                                </div>

                                <div className="flex justify-between items-center pt-3 border-t border-gray-100">
                                    <div>
                                        {order.discountedPrice > 0 && (
                                            <p className="text-sm text-gray-500 line-through mb-1">
                                                {(order.amount + order.discountedPrice).toLocaleString()}원
                                            </p>
                                        )}
                                        <span className="text-xl font-bold text-gray-900">
                                            {order.amount.toLocaleString()}원
                                        </span>
                                    </div>
                                    <Link href={`/receipts/${order.id}`}>
                                        <Button variant="outline" size="sm">
                                            <FileText className="w-4 h-4 mr-1" />
                                            영수증
                                        </Button>
                                    </Link>
                                </div>
                            </Card>
                        ))}
                    </div>
                ) : (
                    <Card className="text-center py-16">
                        <ShoppingBag className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600 mb-4">주문 내역이 없습니다</p>
                        <Link href="/">
                            <Button variant="outline">상품 둘러보기</Button>
                        </Link>
                    </Card>
                )}
            </div>
        </div>
    );
}
'use client';

import { useState } from 'react';
import Link from 'next/link';
import { ArrowLeft, Package, ChevronRight } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function OrdersPage() {
    const [filter, setFilter] = useState<'all' | 'pending' | 'completed'>('all');

    // TODO: 실제 API 데이터로 교체
    const orders = [
        {
            id: 1,
            orderNumber: '20260209001',
            date: '2026.02.09 14:30',
            storeName: '파리바게뜨 강남점',
            storeId: 1,
            items: '크루아상 외 2건',
            totalPrice: 15000,
            status: 'COMPLETED',
            statusText: '픽업 완료',
        },
        {
            id: 2,
            orderNumber: '20260208001',
            date: '2026.02.08 10:20',
            storeName: '파리바게뜨 강남점',
            storeId: 1,
            items: '바게트 외 1건',
            totalPrice: 12000,
            status: 'COMPLETED',
            statusText: '픽업 완료',
        },
        {
            id: 3,
            orderNumber: '20260207001',
            date: '2026.02.07 15:45',
            storeName: '파리바게뜨 강남점',
            storeId: 1,
            items: '소금빵 외 3건',
            totalPrice: 18500,
            status: 'COMPLETED',
            statusText: '픽업 완료',
        },
        {
            id: 4,
            orderNumber: '20260206001',
            date: '2026.02.06 12:30',
            storeName: '파리바게뜨 강남점',
            storeId: 1,
            items: '단팥빵 외 2건',
            totalPrice: 14000,
            status: 'PENDING',
            statusText: '주문 완료',
        },
    ];

    const filteredOrders = orders.filter((order) => {
        if (filter === 'all') return true;
        if (filter === 'pending') return order.status === 'PENDING';
        if (filter === 'completed') return order.status === 'COMPLETED';
        return true;
    });

    const getStatusVariant = (status: string) => {
        switch (status) {
            case 'PENDING':
                return 'default';
            case 'COMPLETED':
                return 'success';
            default:
                return 'default';
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href="/mypage" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>마이페이지로</span>
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900">주문 내역</h1>
                </div>

                {/* 필터 */}
                <div className="mb-6">
                    <div className="flex space-x-2">
                        <Button
                            variant={filter === 'all' ? 'primary' : 'outline'}
                            size="sm"
                            onClick={() => setFilter('all')}
                        >
                            전체
                        </Button>
                        <Button
                            variant={filter === 'pending' ? 'primary' : 'outline'}
                            size="sm"
                            onClick={() => setFilter('pending')}
                        >
                            진행중
                        </Button>
                        <Button
                            variant={filter === 'completed' ? 'primary' : 'outline'}
                            size="sm"
                            onClick={() => setFilter('completed')}
                        >
                            완료
                        </Button>
                    </div>
                </div>

                {/* 주문 목록 */}
                {filteredOrders.length > 0 ? (
                    <div className="space-y-4">
                        {filteredOrders.map((order) => (
                            <Card key={order.id} hover padding="none" className="overflow-hidden">
                                <div className="p-6">
                                    <div className="flex justify-between items-start mb-4">
                                        <div>
                                            <div className="flex items-center space-x-3 mb-2">
                                                <Badge variant={getStatusVariant(order.status) as any}>
                                                    {order.statusText}
                                                </Badge>
                                                <span className="text-sm text-gray-500">{order.date}</span>
                                            </div>
                                            <p className="text-sm text-gray-600 mb-1">
                                                주문번호: {order.orderNumber}
                                            </p>
                                            <Link href={`/stores/${order.storeId}`}>
                                                <h3 className="text-lg font-semibold text-gray-900 hover:text-primary-600">
                                                    {order.storeName}
                                                </h3>
                                            </Link>
                                            <p className="text-sm text-gray-600 mt-1">{order.items}</p>
                                        </div>
                                        <div className="text-right">
                                            <p className="text-2xl font-bold text-gray-900">
                                                {order.totalPrice.toLocaleString()}원
                                            </p>
                                        </div>
                                    </div>

                                    <div className="flex justify-end space-x-2 pt-4 border-t border-gray-100">
                                        <Link href={`/mypage/order/${order.id}`}>
                                            <Button variant="outline" size="sm">
                                                상세보기
                                                <ChevronRight className="w-4 h-4 ml-1" />
                                            </Button>
                                        </Link>
                                        <Button size="sm">재주문</Button>
                                    </div>
                                </div>
                            </Card>
                        ))}
                    </div>
                ) : (
                    <Card className="text-center py-16">
                        <Package className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <h3 className="text-xl font-semibold text-gray-900 mb-2">
                            주문 내역이 없습니다
                        </h3>
                        <p className="text-gray-600 mb-6">
                            첫 주문을 시작해보세요!
                        </p>
                        <Link href="/">
                            <Button>쇼핑 시작하기</Button>
                        </Link>
                    </Card>
                )}
            </div>
        </div>
    );
}
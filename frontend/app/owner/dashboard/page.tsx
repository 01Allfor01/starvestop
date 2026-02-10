'use client';

import Link from 'next/link';
import { Store, Package, ShoppingCart, TrendingUp, DollarSign, Users, Clock, AlertCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function OwnerDashboardPage() {
    // TODO: 실제 API 데이터로 교체
    const stats = {
        todaySales: 450000,
        todayOrders: 23,
        totalProducts: 45,
        activeProducts: 38,
        pendingOrders: 5,
        subscribers: 156,
    };

    const recentOrders = [
        {
            id: 1,
            orderNumber: '20260209001',
            customerName: '홍**',
            items: '크루아상 외 1건',
            total: 13200,
            status: 'PENDING',
            time: '10분 전',
        },
        {
            id: 2,
            orderNumber: '20260209002',
            customerName: '김**',
            items: '샐러드 외 2건',
            total: 25000,
            status: 'PREPARING',
            time: '25분 전',
        },
        {
            id: 3,
            orderNumber: '20260209003',
            customerName: '이**',
            items: '도시락',
            total: 9750,
            status: 'READY',
            time: '1시간 전',
        },
    ];

    const lowStockProducts = [
        {
            id: 1,
            name: '프리미엄 크루아상',
            stock: 2,
            image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
        },
        {
            id: 2,
            name: '샐러드',
            stock: 1,
            image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
        },
    ];

    const getStatusBadge = (status: string) => {
        switch (status) {
            case 'PENDING':
                return <Badge variant="warning">접수대기</Badge>;
            case 'PREPARING':
                return <Badge variant="subscription">준비중</Badge>;
            case 'READY':
                return <Badge variant="success">준비완료</Badge>;
            default:
                return <Badge>알 수 없음</Badge>;
        }
    };

    return (
        <div className="min-h-screen bg-gray-50">
            {/* 헤더 */}
            <div className="bg-white border-b border-gray-200">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900">대시보드</h1>
                            <p className="text-gray-600 mt-1">파리바게뜨 강남점</p>
                        </div>
                        <div className="flex items-center space-x-3">
                            <Link href="/owner/stores">
                                <Button variant="outline">
                                    <Store className="w-4 h-4 mr-2" />
                                    가게 관리
                                </Button>
                            </Link>
                            <Link href="/owner/products/new">
                                <Button>
                                    상품 등록
                                </Button>
                            </Link>
                        </div>
                    </div>
                </div>
            </div>

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 통계 카드 */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                    <Card>
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">오늘 매출</p>
                                <p className="text-2xl font-bold text-gray-900">
                                    {stats.todaySales.toLocaleString()}원
                                </p>
                            </div>
                            <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                                <DollarSign className="w-6 h-6 text-green-600" />
                            </div>
                        </div>
                    </Card>

                    <Card>
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">오늘 주문</p>
                                <p className="text-2xl font-bold text-gray-900">{stats.todayOrders}건</p>
                            </div>
                            <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                                <ShoppingCart className="w-6 h-6 text-blue-600" />
                            </div>
                        </div>
                    </Card>

                    <Card>
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">전체 상품</p>
                                <p className="text-2xl font-bold text-gray-900">{stats.totalProducts}개</p>
                                <p className="text-xs text-gray-500 mt-1">판매중 {stats.activeProducts}개</p>
                            </div>
                            <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center">
                                <Package className="w-6 h-6 text-purple-600" />
                            </div>
                        </div>
                    </Card>

                    <Card>
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">구독자</p>
                                <p className="text-2xl font-bold text-gray-900">{stats.subscribers}명</p>
                            </div>
                            <div className="w-12 h-12 bg-secondary-100 rounded-full flex items-center justify-center">
                                <Users className="w-6 h-6 text-secondary-600" />
                            </div>
                        </div>
                    </Card>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    {/* 최근 주문 */}
                    <Card>
                        <div className="flex items-center justify-between mb-6">
                            <h2 className="text-xl font-bold text-gray-900">최근 주문</h2>
                            <Link href="/owner/orders">
                                <Button variant="outline" size="sm">전체보기</Button>
                            </Link>
                        </div>

                        {stats.pendingOrders > 0 && (
                            <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg flex items-center">
                                <AlertCircle className="w-5 h-5 text-red-500 mr-2" />
                                <span className="text-sm text-red-700">
                  처리 대기 중인 주문이 <span className="font-semibold">{stats.pendingOrders}건</span> 있습니다
                </span>
                            </div>
                        )}

                        <div className="space-y-3">
                            {recentOrders.map((order) => (
                                <Link key={order.id} href={`/owner/orders/${order.id}`}>
                                    <div className="p-4 border border-gray-200 rounded-lg hover:border-primary-300 hover:bg-primary-50 transition cursor-pointer">
                                        <div className="flex items-center justify-between mb-2">
                                            <div className="flex items-center space-x-2">
                                                <span className="font-medium text-gray-900">{order.orderNumber}</span>
                                                {getStatusBadge(order.status)}
                                            </div>
                                            <span className="text-sm text-gray-500">{order.time}</span>
                                        </div>
                                        <p className="text-sm text-gray-600 mb-1">
                                            {order.customerName} · {order.items}
                                        </p>
                                        <p className="text-lg font-bold text-primary-600">
                                            {order.total.toLocaleString()}원
                                        </p>
                                    </div>
                                </Link>
                            ))}
                        </div>
                    </Card>

                    {/* 재고 부족 상품 */}
                    <Card>
                        <div className="flex items-center justify-between mb-6">
                            <h2 className="text-xl font-bold text-gray-900">재고 부족 상품</h2>
                            <Link href="/owner/products">
                                <Button variant="outline" size="sm">전체보기</Button>
                            </Link>
                        </div>

                        {lowStockProducts.length > 0 ? (
                            <div className="space-y-3">
                                {lowStockProducts.map((product) => (
                                    <Link key={product.id} href={`/owner/products/${product.id}/edit`}>
                                        <div className="p-4 border border-yellow-200 bg-yellow-50 rounded-lg hover:bg-yellow-100 transition cursor-pointer">
                                            <div className="flex items-center">
                                                <img
                                                    src={product.image}
                                                    alt={product.name}
                                                    className="w-16 h-16 object-cover rounded-lg mr-4"
                                                />
                                                <div className="flex-1">
                                                    <p className="font-medium text-gray-900">{product.name}</p>
                                                    <p className="text-sm text-red-600 font-semibold mt-1">
                                                        재고: {product.stock}개
                                                    </p>
                                                </div>
                                                <Button size="sm">재고 추가</Button>
                                            </div>
                                        </div>
                                    </Link>
                                ))}
                            </div>
                        ) : (
                            <div className="text-center py-8">
                                <Package className="w-12 h-12 text-gray-300 mx-auto mb-3" />
                                <p className="text-gray-600">재고 부족 상품이 없습니다</p>
                            </div>
                        )}
                    </Card>
                </div>

                {/* 빠른 링크 */}
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-8">
                    <Link href="/owner/products/new">
                        <Card hover className="text-center cursor-pointer">
                            <Package className="w-8 h-8 text-primary-500 mx-auto mb-2" />
                            <p className="font-medium text-gray-900">상품 등록</p>
                        </Card>
                    </Link>
                    <Link href="/owner/orders">
                        <Card hover className="text-center cursor-pointer">
                            <ShoppingCart className="w-8 h-8 text-primary-500 mx-auto mb-2" />
                            <p className="font-medium text-gray-900">주문 관리</p>
                        </Card>
                    </Link>
                    <Link href="/owner/sales">
                        <Card hover className="text-center cursor-pointer">
                            <TrendingUp className="w-8 h-8 text-primary-500 mx-auto mb-2" />
                            <p className="font-medium text-gray-900">매출 통계</p>
                        </Card>
                    </Link>
                    <Link href="/owner/stores">
                        <Card hover className="text-center cursor-pointer">
                            <Store className="w-8 h-8 text-primary-500 mx-auto mb-2" />
                            <p className="font-medium text-gray-900">가게 설정</p>
                        </Card>
                    </Link>
                </div>
            </div>
        </div>
    );
}
'use client';

import Link from 'next/link';
import { Users, Store, ShoppingCart, DollarSign, TrendingUp, AlertCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function AdminDashboardPage() {
    // TODO: 실제 API 데이터로 교체
    const stats = {
        totalUsers: 12456,
        totalStores: 234,
        totalOrders: 5678,
        totalRevenue: 125000000,
        newUsersToday: 45,
        newStoresToday: 3,
        ordersToday: 189,
        revenueToday: 4500000,
    };

    const recentUsers = [
        { id: 1, name: '홍길동', email: 'hong@example.com', joinDate: '2026.02.09' },
        { id: 2, name: '김철수', email: 'kim@example.com', joinDate: '2026.02.09' },
        { id: 3, name: '이영희', email: 'lee@example.com', joinDate: '2026.02.08' },
    ];

    const recentStores = [
        { id: 1, name: '파리바게뜨 강남점', owner: '박사장', status: 'PENDING' },
        { id: 2, name: '샐러디 역삼점', owner: '최사장', status: 'ACTIVE' },
        { id: 3, name: '본도시락 선릉점', owner: '정사장', status: 'ACTIVE' },
    ];

    const alerts = [
        { id: 1, type: 'warning', message: '신규 가게 승인 대기 중 (3건)' },
        { id: 2, type: 'info', message: '오늘 신규 회원 45명 가입' },
    ];

    return (
        <div className="min-h-screen bg-gray-50">
            {/* 헤더 */}
            <div className="bg-white border-b border-gray-200">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900">관리자 대시보드</h1>
                            <p className="text-gray-600 mt-1">Starve Stop 전체 현황</p>
                        </div>
                        <div className="flex items-center space-x-3">
                            <Link href="/admin/api-logs">
                                <Button variant="outline">API 로그</Button>
                            </Link>
                        </div>
                    </div>
                </div>
            </div>

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 알림 */}
                {alerts.length > 0 && (
                    <div className="mb-6 space-y-2">
                        {alerts.map((alert) => (
                            <Card key={alert.id} className={`border-2 ${
                                alert.type === 'warning' ? 'border-yellow-200 bg-yellow-50' : 'border-blue-200 bg-blue-50'
                            }`}>
                                <div className="flex items-center">
                                    <AlertCircle className={`w-5 h-5 mr-3 ${
                                        alert.type === 'warning' ? 'text-yellow-600' : 'text-blue-600'
                                    }`} />
                                    <p className={alert.type === 'warning' ? 'text-yellow-800' : 'text-blue-800'}>
                                        {alert.message}
                                    </p>
                                </div>
                            </Card>
                        ))}
                    </div>
                )}

                {/* 전체 통계 */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                    <Card>
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">전체 회원</p>
                                <p className="text-2xl font-bold text-gray-900">
                                    {stats.totalUsers.toLocaleString()}명
                                </p>
                                <p className="text-sm text-green-600 mt-1">+{stats.newUsersToday} 오늘</p>
                            </div>
                            <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                                <Users className="w-6 h-6 text-blue-600" />
                            </div>
                        </div>
                    </Card>

                    <Card>
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">전체 가게</p>
                                <p className="text-2xl font-bold text-gray-900">{stats.totalStores}개</p>
                                <p className="text-sm text-green-600 mt-1">+{stats.newStoresToday} 오늘</p>
                            </div>
                            <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center">
                                <Store className="w-6 h-6 text-purple-600" />
                            </div>
                        </div>
                    </Card>

                    <Card>
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">전체 주문</p>
                                <p className="text-2xl font-bold text-gray-900">
                                    {stats.totalOrders.toLocaleString()}건
                                </p>
                                <p className="text-sm text-green-600 mt-1">+{stats.ordersToday} 오늘</p>
                            </div>
                            <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                                <ShoppingCart className="w-6 h-6 text-green-600" />
                            </div>
                        </div>
                    </Card>

                    <Card>
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">전체 매출</p>
                                <p className="text-2xl font-bold text-gray-900">
                                    {(stats.totalRevenue / 100000000).toFixed(1)}억
                                </p>
                                <p className="text-sm text-green-600 mt-1">
                                    +{(stats.revenueToday / 1000000).toFixed(1)}M 오늘
                                </p>
                            </div>
                            <div className="w-12 h-12 bg-yellow-100 rounded-full flex items-center justify-center">
                                <DollarSign className="w-6 h-6 text-yellow-600" />
                            </div>
                        </div>
                    </Card>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    {/* 최근 가입 회원 */}
                    <Card>
                        <div className="flex items-center justify-between mb-6">
                            <h2 className="text-xl font-bold text-gray-900">최근 가입 회원</h2>
                            <Button variant="outline" size="sm">전체보기</Button>
                        </div>
                        <div className="space-y-3">
                            {recentUsers.map((user) => (
                                <div key={user.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                                    <div>
                                        <p className="font-medium text-gray-900">{user.name}</p>
                                        <p className="text-sm text-gray-600">{user.email}</p>
                                    </div>
                                    <span className="text-sm text-gray-500">{user.joinDate}</span>
                                </div>
                            ))}
                        </div>
                    </Card>

                    {/* 최근 등록 가게 */}
                    <Card>
                        <div className="flex items-center justify-between mb-6">
                            <h2 className="text-xl font-bold text-gray-900">최근 등록 가게</h2>
                            <Button variant="outline" size="sm">전체보기</Button>
                        </div>
                        <div className="space-y-3">
                            {recentStores.map((store) => (
                                <div key={store.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                                    <div>
                                        <div className="flex items-center space-x-2 mb-1">
                                            <p className="font-medium text-gray-900">{store.name}</p>
                                            {store.status === 'PENDING' ? (
                                                <Badge variant="warning">승인대기</Badge>
                                            ) : (
                                                <Badge variant="success">승인완료</Badge>
                                            )}
                                        </div>
                                        <p className="text-sm text-gray-600">사장님: {store.owner}</p>
                                    </div>
                                    {store.status === 'PENDING' && (
                                        <Button size="sm">승인</Button>
                                    )}
                                </div>
                            ))}
                        </div>
                    </Card>
                </div>

                {/* 빠른 링크 */}
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-8">
                    <Link href="/admin/users">
                        <Card hover className="text-center cursor-pointer">
                            <Users className="w-8 h-8 text-primary-500 mx-auto mb-2" />
                            <p className="font-medium text-gray-900">회원 관리</p>
                        </Card>
                    </Link>
                    <Link href="/admin/stores">
                        <Card hover className="text-center cursor-pointer">
                            <Store className="w-8 h-8 text-primary-500 mx-auto mb-2" />
                            <p className="font-medium text-gray-900">가게 관리</p>
                        </Card>
                    </Link>
                    <Link href="/admin/orders">
                        <Card hover className="text-center cursor-pointer">
                            <ShoppingCart className="w-8 h-8 text-primary-500 mx-auto mb-2" />
                            <p className="font-medium text-gray-900">주문 관리</p>
                        </Card>
                    </Link>
                    <Link href="/admin/api-logs">
                        <Card hover className="text-center cursor-pointer">
                            <TrendingUp className="w-8 h-8 text-primary-500 mx-auto mb-2" />
                            <p className="font-medium text-gray-900">API 로그</p>
                        </Card>
                    </Link>
                </div>
            </div>
        </div>
    );
}
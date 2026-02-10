'use client';

import { useState } from 'react';
import Link from 'next/link';
import { Package, Heart, Gift, User, ChevronRight, LogOut, CreditCard, Bell } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function MyPage() {
    // TODO: 나중에 실제 사용자 데이터로 교체
    const user = {
        name: '홍길동',
        email: 'hong@example.com',
        phone: '010-1234-5678',
        point: 5000,
    };

    const recentOrders = [
        {
            id: 1,
            date: '2026.02.09',
            storeName: '파리바게뜨 강남점',
            items: '크루아상 외 2건',
            totalPrice: 15000,
            status: '배달완료',
        },
        {
            id: 2,
            date: '2026.02.08',
            storeName: '샐러디 역삼점',
            items: '샐러드 외 1건',
            totalPrice: 12000,
            status: '배달완료',
        },
    ];

    const subscriptions = [
        {
            id: 1,
            name: '샐러드 정기구독',
            price: 28000,
            period: '주 1회',
            nextDelivery: '2026.02.12',
            status: 'ACTIVE',
        },
    ];

    const coupons = [
        {
            id: 1,
            name: '5,000원 할인 쿠폰',
            discount: 5000,
            minAmount: 30000,
            expiryDate: '2026.03.31',
        },
        {
            id: 2,
            name: '10% 할인 쿠폰',
            discountRate: 10,
            minAmount: 20000,
            expiryDate: '2026.02.28',
        },
    ];

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <h1 className="text-3xl font-bold text-gray-900">마이페이지</h1>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* 왼쪽: 사용자 정보 & 메뉴 */}
                    <div className="lg:col-span-1 space-y-6">
                        {/* 프로필 카드 */}
                        <Card>
                            <div className="text-center mb-6">
                                <div className="w-20 h-20 bg-gradient-to-br from-primary-500 to-primary-600 rounded-full flex items-center justify-center mx-auto mb-4">
                                    <User className="w-10 h-10 text-white" />
                                </div>
                                <h2 className="text-xl font-bold text-gray-900 mb-1">{user.name}</h2>
                                <p className="text-sm text-gray-600">{user.email}</p>
                            </div>

                            <div className="bg-gradient-to-br from-primary-50 to-primary-100 rounded-lg p-4 mb-6">
                                <div className="flex items-center justify-between">
                                    <span className="text-sm text-gray-700">보유 포인트</span>
                                    <span className="text-2xl font-bold text-primary-600">
                    {user.point.toLocaleString()}P
                  </span>
                                </div>
                            </div>

                            <Link href="/mypage/edit">
                                <Button variant="outline" fullWidth>
                                    <User className="w-4 h-4 mr-2" />
                                    회원정보 수정
                                </Button>
                            </Link>
                        </Card>

                        {/* 메뉴 */}
                        <Card padding="none">
                            <Link href="/mypage/orders" className="flex items-center justify-between p-4 hover:bg-gray-50 transition border-b border-gray-100">
                                <div className="flex items-center">
                                    <Package className="w-5 h-5 text-gray-600 mr-3" />
                                    <span className="font-medium text-gray-900">주문 내역</span>
                                </div>
                                <ChevronRight className="w-5 h-5 text-gray-400" />
                            </Link>

                            <Link href="/mypage/subscriptions" className="flex items-center justify-between p-4 hover:bg-gray-50 transition border-b border-gray-100">
                                <div className="flex items-center">
                                    <CreditCard className="w-5 h-5 text-gray-600 mr-3" />
                                    <span className="font-medium text-gray-900">구독 관리</span>
                                </div>
                                <ChevronRight className="w-5 h-5 text-gray-400" />
                            </Link>

                            <Link href="/mypage/coupons" className="flex items-center justify-between p-4 hover:bg-gray-50 transition border-b border-gray-100">
                                <div className="flex items-center">
                                    <Gift className="w-5 h-5 text-gray-600 mr-3" />
                                    <span className="font-medium text-gray-900">쿠폰함</span>
                                </div>
                                <Badge variant="default">{coupons.length}</Badge>
                            </Link>

                            <Link href="/mypage/favorites" className="flex items-center justify-between p-4 hover:bg-gray-50 transition border-b border-gray-100">
                                <div className="flex items-center">
                                    <Heart className="w-5 h-5 text-gray-600 mr-3" />
                                    <span className="font-medium text-gray-900">찜 목록</span>
                                </div>
                                <ChevronRight className="w-5 h-5 text-gray-400" />
                            </Link>

                            <Link href="/mypage/notifications" className="flex items-center justify-between p-4 hover:bg-gray-50 transition">
                                <div className="flex items-center">
                                    <Bell className="w-5 h-5 text-gray-600 mr-3" />
                                    <span className="font-medium text-gray-900">알림 설정</span>
                                </div>
                                <ChevronRight className="w-5 h-5 text-gray-400" />
                            </Link>
                        </Card>

                        <Button variant="ghost" fullWidth className="text-red-500 hover:bg-red-50">
                            <LogOut className="w-4 h-4 mr-2" />
                            로그아웃
                        </Button>
                    </div>

                    {/* 오른쪽: 콘텐츠 */}
                    <div className="lg:col-span-2 space-y-8">
                        {/* 최근 주문 */}
                        <div>
                            <div className="flex items-center justify-between mb-4">
                                <h2 className="text-2xl font-bold text-gray-900">최근 주문</h2>
                                <Link href="/mypage/orders" className="text-primary-500 hover:text-primary-600 text-sm font-medium">
                                    전체보기 →
                                </Link>
                            </div>

                            <div className="space-y-4">
                                {recentOrders.map((order) => (
                                    <Card key={order.id} hover padding="md">
                                        <div className="flex justify-between items-start mb-3">
                                            <div>
                                                <p className="text-sm text-gray-500 mb-1">{order.date}</p>
                                                <h3 className="font-semibold text-gray-900">{order.storeName}</h3>
                                                <p className="text-sm text-gray-600 mt-1">{order.items}</p>
                                            </div>
                                            <Badge variant="success">{order.status}</Badge>
                                        </div>
                                        <div className="flex justify-between items-center pt-3 border-t border-gray-100">
                      <span className="text-lg font-bold text-gray-900">
                        {order.totalPrice.toLocaleString()}원
                      </span>
                                            <div className="space-x-2">
                                                <Button variant="outline" size="sm">상세보기</Button>
                                                <Button size="sm">재주문</Button>
                                            </div>
                                        </div>
                                    </Card>
                                ))}
                            </div>
                        </div>

                        {/* 구독 관리 */}
                        <div>
                            <div className="flex items-center justify-between mb-4">
                                <h2 className="text-2xl font-bold text-gray-900">내 구독</h2>
                                <Link href="/subscriptions" className="text-primary-500 hover:text-primary-600 text-sm font-medium">
                                    구독 상품 보기 →
                                </Link>
                            </div>

                            {subscriptions.length > 0 ? (
                                <div className="space-y-4">
                                    {subscriptions.map((sub) => (
                                        <Card key={sub.id} className="border-2 border-secondary-200">
                                            <div className="flex justify-between items-start mb-4">
                                                <div>
                                                    <Badge variant="subscription" className="mb-2">{sub.period}</Badge>
                                                    <h3 className="text-lg font-semibold text-gray-900">{sub.name}</h3>
                                                </div>
                                                <span className="text-xl font-bold text-secondary-600">
                          {sub.price.toLocaleString()}원
                        </span>
                                            </div>
                                            <div className="bg-secondary-50 rounded-lg p-3 mb-4">
                                                <p className="text-sm text-gray-700">
                                                    다음 배송일: <span className="font-semibold text-secondary-700">{sub.nextDelivery}</span>
                                                </p>
                                            </div>
                                            <div className="flex space-x-2">
                                                <Button variant="outline" size="sm" className="flex-1">일시정지</Button>
                                                <Button variant="outline" size="sm" className="flex-1">배송일 변경</Button>
                                                <Button variant="secondary" size="sm" className="flex-1">관리</Button>
                                            </div>
                                        </Card>
                                    ))}
                                </div>
                            ) : (
                                <Card className="text-center py-12">
                                    <CreditCard className="w-12 h-12 text-gray-300 mx-auto mb-3" />
                                    <p className="text-gray-600 mb-4">구독 중인 상품이 없습니다</p>
                                    <Link href="/subscriptions">
                                        <Button variant="outline">구독 상품 보기</Button>
                                    </Link>
                                </Card>
                            )}
                        </div>

                        {/* 쿠폰 */}
                        <div>
                            <div className="flex items-center justify-between mb-4">
                                <h2 className="text-2xl font-bold text-gray-900">보유 쿠폰</h2>
                                <Link href="/mypage/coupons" className="text-primary-500 hover:text-primary-600 text-sm font-medium">
                                    전체보기 →
                                </Link>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                {coupons.slice(0, 2).map((coupon) => (
                                    <Card key={coupon.id} className="border-2 border-primary-200 bg-gradient-to-br from-primary-50 to-white">
                                        <div className="flex items-start justify-between mb-3">
                                            <Gift className="w-8 h-8 text-primary-500" />
                                            <Badge variant="default" className="text-xs">
                                                ~{coupon.expiryDate}
                                            </Badge>
                                        </div>
                                        <h3 className="font-bold text-gray-900 mb-2">{coupon.name}</h3>
                                        <p className="text-sm text-gray-600">
                                            {coupon.minAmount.toLocaleString()}원 이상 구매 시
                                        </p>
                                    </Card>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
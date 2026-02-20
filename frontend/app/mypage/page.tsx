'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Package, Gift, User, ChevronRight, LogOut, CreditCard, Loader2 } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

// API Imports
import { usersApi, GetUserResponse } from '@/lib/api/users';
import { ordersApi, OrderResponse } from '@/lib/api/orders';
import { subscriptionsApi, UserSubscription, Subscription } from '@/lib/api/subscriptions';
import { couponsApi, UserCoupon } from '@/lib/api/coupons';

// 병합된 구독 데이터 타입 (UserSubscription + Subscription Detail)
interface MergedSubscription extends UserSubscription {
    detail?: Subscription;
}

// 주문 UI 표시용 타입
interface OrderUI extends OrderResponse {
    itemsSummary: string;
}

interface Order {
    id: number;
}

export default function MyPage() {
    const router = useRouter();
    const [loading, setLoading] = useState(true);

    // Data States
    const [user, setUser] = useState<GetUserResponse | null>(null);
    const [recentOrders, setRecentOrders] = useState<OrderUI[]>([]);
    const [subscriptions, setSubscriptions] = useState<MergedSubscription[]>([]);
    const [coupons, setCoupons] = useState<UserCoupon[]>([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);

                // 1. 유저 정보 조회
                const userData = await usersApi.getMyInfo();
                setUser(userData);

                // 2. 쿠폰 조회
                const couponData = await couponsApi.getMyCoupons();
                setCoupons(couponData);

                // 3. 구독 목록 조회 및 상세 정보 병합
                const mySubs = await subscriptionsApi.getMySubscriptions();
                // 각 구독의 상세 정보(요일, 식사시간 등)를 가져와서 병합
                const mergedSubs = await Promise.all(
                    mySubs.map(async (sub) => {
                        try {
                            const detail = await subscriptionsApi.getSubscription(sub.subscriptionId);
                            return { ...sub, detail };
                        } catch (e) {
                            return sub; // 상세 조회 실패 시 기본 정보만 반환
                        }
                    })
                );
                setSubscriptions(mergedSubs);

                // 4. 주문 목록 조회 및 상품명 요약 생성
                const ordersData = await ordersApi.getOrders();
                // 최근 3개만, 그리고 각 주문의 상품 목록을 조회하여 "OOO 외 N건" 문자열 생성
                const recentOrdersList = ordersData.slice(0, 3);

                const ordersWithItems = await Promise.all(
                    recentOrdersList.map(async (order: Order) => {
                        try {
                            const products = await ordersApi.getOrderProducts(order.id);
                            let summary = '상품 정보 없음';
                            if (products.length > 0) {
                                summary = products.length > 1
                                    ? `${products[0].productName} 외 ${products.length - 1}건`
                                    : products[0].productName;
                            }
                            return { ...order, itemsSummary: summary };
                        } catch (e) {
                            return { ...order, itemsSummary: '상품 정보 로딩 실패' };
                        }
                    })
                );
                setRecentOrders(ordersWithItems);

            } catch (error) {
                console.error("마이페이지 데이터 로딩 실패:", error);
                // 에러 처리 로직 (필요 시)
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    // 구독 취소 핸들러
    const handleUnsubscribe = async (id: number) => {
        if (confirm('정말로 구독을 취소하시겠습니까?')) {
            try {
                await subscriptionsApi.unsubscribe(id);
                alert('구독이 취소되었습니다.');
                // 목록 갱신
                setSubscriptions(prev => prev.filter(sub => sub.id !== id));
            } catch (error) {
                console.error(error);
                alert('구독 취소 실패');
            }
        }
    };

    // 다음 결제일 계산 (만료일 + 1일)
    const getNextPaymentDate = (expiresAt: string) => {
        const date = new Date(expiresAt);
        date.setDate(date.getDate() + 1);
        return date.toLocaleDateString();
    };

    // 요일/식사시간 포맷팅
    const formatSubscriptionDetails = (detail?: Subscription) => {
        if (!detail) return '';
        const days = detail.dayList.map(d => {
            const map: Record<string, string> = { MONDAY: '월', TUESDAY: '화', WEDNESDAY: '수', THURSDAY: '목', FRIDAY: '금', SATURDAY: '토', SUNDAY: '일' };
            return map[d] || d;
        }).join(', ');

        const times = detail.mealTimeList.map(t => {
            const map: Record<string, string> = { BREAKFAST: '조식', LUNCH: '중식', DINNER: '석식' };
            return map[t] || t;
        }).join(', ');

        return `${days} / ${times}`;
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-gray-50">
                <Loader2 className="w-10 h-10 text-primary-500 animate-spin" />
            </div>
        );
    }

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
                                <div className="w-20 h-20 bg-gradient-to-br from-primary-500 to-primary-600 rounded-full flex items-center justify-center mx-auto mb-4 overflow-hidden">
                                    {user?.imageUrl ? (
                                        <img src={user.imageUrl} alt="Profile" className="w-full h-full object-cover" />
                                    ) : (
                                        <User className="w-10 h-10 text-white" />
                                    )}
                                </div>
                                <h2 className="text-xl font-bold text-gray-900 mb-1">{user?.nickname || user?.username || '사용자'}</h2>
                                <p className="text-sm text-gray-600">{user?.email}</p>
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

                            <Link href="/mypage/coupons" className="flex items-center justify-between p-4 hover:bg-gray-50 transition">
                                <div className="flex items-center">
                                    <Gift className="w-5 h-5 text-gray-600 mr-3" />
                                    <span className="font-medium text-gray-900">쿠폰함</span>
                                </div>
                                <Badge variant="default">{coupons.filter(c => c.couponName).length}</Badge>
                            </Link>
                        </Card>
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
                                {recentOrders.length > 0 ? recentOrders.map((order) => (
                                    <Link key={order.id} href={`/mypage/orders/${order.id}`}>
                                        <Card hover padding="md" className="cursor-pointer">
                                            <div className="flex justify-between items-start mb-3">
                                                <div>
                                                    <p className="text-sm text-gray-500 mb-1">
                                                        {new Date(order.createdAt).toLocaleDateString()}
                                                    </p>
                                                    <h3 className="font-semibold text-gray-900">
                                                        {order.storeName}
                                                    </h3>
                                                    <p className="text-sm text-gray-600 mt-1">{order.itemsSummary}</p>
                                                </div>
                                            </div>
                                            <div className="flex justify-between items-center pt-3 border-t border-gray-100">
                                                <span className="text-lg font-bold text-gray-900">
                                                    {order.amount.toLocaleString()}원
                                                </span>
                                            </div>
                                        </Card>
                                    </Link>
                                )) : (
                                    <div className="text-center py-8 bg-white rounded-lg border border-gray-200">
                                        <p className="text-gray-500">최근 주문 내역이 없습니다.</p>
                                    </div>
                                )}
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
                                                    <Badge variant="subscription" className="mb-2">
                                                        {sub.status}
                                                    </Badge>
                                                    <h3 className="text-lg font-semibold text-gray-900">{sub.subscriptionName}</h3>
                                                    <p className="text-sm text-gray-600">{sub.storeName}</p>
                                                </div>
                                                <span className="text-xl font-bold text-secondary-600">
                                                    {sub.price.toLocaleString()}원
                                                </span>
                                            </div>

                                            {/* 요일 및 식사 시간 정보 */}
                                            <div className="bg-white border border-secondary-100 rounded-lg p-3 mb-3">
                                                <p className="text-sm text-gray-700 font-medium">
                                                    {formatSubscriptionDetails(sub.detail)}
                                                </p>
                                            </div>

                                            {/* 다음 결제일 정보 */}
                                            <div className="bg-secondary-50 rounded-lg p-3 mb-4">
                                                <p className="text-sm text-gray-700">
                                                    다음 결제일: <span className="font-semibold text-secondary-700">
                                                        {getNextPaymentDate(sub.expiresAt)}
                                                    </span>
                                                </p>
                                            </div>

                                            <div className="flex space-x-2">
                                                <Link href={`/mypage/subscriptions/${sub.id}`} className="flex-1">
                                                    <Button variant="secondary" size="sm" fullWidth>
                                                        상세 정보
                                                    </Button>
                                                </Link>
                                                <Button
                                                    variant="outline"
                                                    size="sm"
                                                    className="flex-1 text-red-500 hover:bg-red-50 border-red-200"
                                                    onClick={() => handleUnsubscribe(sub.id)}
                                                >
                                                    구독 취소
                                                </Button>
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
                                {coupons.length > 0 ? coupons.slice(0, 2).map((coupon) => (
                                    <Card key={coupon.id} className="border-2 border-primary-200 bg-gradient-to-br from-primary-50 to-white">
                                        <div className="flex items-start justify-between mb-3">
                                            <Gift className="w-8 h-8 text-primary-500" />
                                            <Badge variant="default" className="text-xs">
                                                ~{new Date(coupon.expiresAt).toLocaleDateString()}
                                            </Badge>
                                        </div>
                                        <h3 className="font-bold text-gray-900 mb-2">{coupon.couponName}</h3>
                                        <p className="text-sm text-gray-600">
                                            {coupon.minAmount.toLocaleString()}원 이상 구매 시
                                            <br/>
                                            <span className="text-primary-600 font-bold">
                                                {coupon.discountAmount.toLocaleString()}원 할인
                                            </span>
                                        </p>
                                    </Card>
                                )) : (
                                    <div className="col-span-2 text-center py-8 bg-white rounded-lg border border-gray-200">
                                        <p className="text-gray-500">사용 가능한 쿠폰이 없습니다.</p>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
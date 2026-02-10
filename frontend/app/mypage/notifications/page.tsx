'use client';

import { useState } from 'react';
import Link from 'next/link';
import { ArrowLeft, Bell, Package, CreditCard, Gift, ShoppingBag, Clock, Settings } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

interface Notification {
    id: number;
    type: 'subscription' | 'order' | 'sale' | 'coupon';
    title: string;
    message: string;
    time: string;
    read: boolean;
    link?: string;
}

export default function NotificationsPage() {
    const [activeTab, setActiveTab] = useState<'all' | 'unread'>('all');

    // TODO: 실제 API 데이터로 교체
    const [notifications, setNotifications] = useState<Notification[]>([
        {
            id: 1,
            type: 'subscription',
            title: '🚚 구독 배송 예정',
            message: '샐러드 정기구독 상품이 내일 오전 배송됩니다.',
            time: '1시간 전',
            read: false,
            link: '/subscriptions/1',
        },
        {
            id: 2,
            type: 'subscription',
            title: '⏰ 구독 배송 알림',
            message: '과일 정기구독 상품이 2시간 후 배송됩니다. 배송지를 확인해주세요.',
            time: '3시간 전',
            read: false,
            link: '/subscriptions/2',
        },
        {
            id: 3,
            type: 'order',
            title: '📦 배달 완료',
            message: '주문하신 상품이 배달 완료되었습니다.',
            time: '5시간 전',
            read: true,
            link: '/mypage/orders/1',
        },
        {
            id: 4,
            type: 'sale',
            title: '🔥 마감세일 시작!',
            message: '파리바게뜨 강남점에서 50% 마감세일이 시작되었습니다.',
            time: '1일 전',
            read: true,
            link: '/products/sale',
        },
        {
            id: 5,
            type: 'coupon',
            title: '🎁 쿠폰 발급',
            message: '5,000원 할인 쿠폰이 발급되었습니다. 쿠폰함을 확인해보세요!',
            time: '2일 전',
            read: true,
            link: '/mypage/coupons',
        },
        {
            id: 6,
            type: 'subscription',
            title: '💚 구독 갱신 완료',
            message: '샐러드 정기구독이 자동 갱신되었습니다.',
            time: '3일 전',
            read: true,
            link: '/mypage/subscriptions',
        },
        {
            id: 7,
            type: 'order',
            title: '🚛 배달 시작',
            message: '주문하신 상품이 배달 중입니다.',
            time: '3일 전',
            read: true,
            link: '/mypage/orders/2',
        },
        {
            id: 8,
            type: 'coupon',
            title: '⚠️ 쿠폰 만료 임박',
            message: '10% 할인 쿠폰이 3일 후 만료됩니다.',
            time: '4일 전',
            read: true,
            link: '/mypage/coupons',
        },
    ]);

    const getIcon = (type: string) => {
        switch (type) {
            case 'subscription':
                return <CreditCard className="w-6 h-6 text-secondary-500" />;
            case 'order':
                return <Package className="w-6 h-6 text-primary-500" />;
            case 'sale':
                return <ShoppingBag className="w-6 h-6 text-red-500" />;
            case 'coupon':
                return <Gift className="w-6 h-6 text-yellow-500" />;
            default:
                return <Bell className="w-6 h-6 text-gray-500" />;
        }
    };

    const markAsRead = (id: number) => {
        setNotifications(notifications.map(n =>
            n.id === id ? { ...n, read: true } : n
        ));
    };

    const markAllAsRead = () => {
        setNotifications(notifications.map(n => ({ ...n, read: true })));
    };

    const deleteNotification = (id: number) => {
        setNotifications(notifications.filter(n => n.id !== id));
    };

    const filteredNotifications = activeTab === 'unread'
        ? notifications.filter(n => !n.read)
        : notifications;

    const unreadCount = notifications.filter(n => !n.read).length;

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href="/mypage" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>마이페이지로</span>
                    </Link>
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">알림</h1>
                            <p className="text-gray-600">
                                읽지 않은 알림 <span className="text-primary-600 font-semibold">{unreadCount}개</span>
                            </p>
                        </div>
                        <Link href="/mypage/notifications/settings">
                            <Button variant="outline" size="sm">
                                <Settings className="w-4 h-4 mr-2" />
                                알림 설정
                            </Button>
                        </Link>
                    </div>
                </div>

                {/* 탭 & 전체 읽음 */}
                <div className="flex items-center justify-between mb-6">
                    <div className="flex space-x-4 border-b border-gray-200">
                        <button
                            onClick={() => setActiveTab('all')}
                            className={`pb-3 px-1 font-semibold transition ${
                                activeTab === 'all'
                                    ? 'text-primary-600 border-b-2 border-primary-600'
                                    : 'text-gray-600 hover:text-gray-900'
                            }`}
                        >
                            전체 ({notifications.length})
                        </button>
                        <button
                            onClick={() => setActiveTab('unread')}
                            className={`pb-3 px-1 font-semibold transition ${
                                activeTab === 'unread'
                                    ? 'text-primary-600 border-b-2 border-primary-600'
                                    : 'text-gray-600 hover:text-gray-900'
                            }`}
                        >
                            읽지 않음 ({unreadCount})
                        </button>
                    </div>
                    {unreadCount > 0 && (
                        <Button variant="ghost" size="sm" onClick={markAllAsRead}>
                            전체 읽음 처리
                        </Button>
                    )}
                </div>

                {/* 알림 목록 */}
                {filteredNotifications.length > 0 ? (
                    <div className="space-y-3">
                        {filteredNotifications.map((notification) => (
                            <Card
                                key={notification.id}
                                className={`transition cursor-pointer ${
                                    !notification.read ? 'bg-blue-50 border-blue-200' : 'hover:bg-gray-50'
                                }`}
                                onClick={() => {
                                    markAsRead(notification.id);
                                    if (notification.link) {
                                        window.location.href = notification.link;
                                    }
                                }}
                            >
                                <div className="flex items-start">
                                    {/* 아이콘 */}
                                    <div className="flex-shrink-0 mr-4">
                                        {getIcon(notification.type)}
                                    </div>

                                    {/* 내용 */}
                                    <div className="flex-1 min-w-0">
                                        <div className="flex items-start justify-between mb-1">
                                            <h3 className={`font-semibold ${!notification.read ? 'text-gray-900' : 'text-gray-700'}`}>
                                                {notification.title}
                                            </h3>
                                            {!notification.read && (
                                                <Badge variant="subscription" className="ml-2 flex-shrink-0">NEW</Badge>
                                            )}
                                        </div>
                                        <p className="text-gray-600 text-sm mb-2">{notification.message}</p>
                                        <div className="flex items-center justify-between">
                      <span className="text-xs text-gray-500 flex items-center">
                        <Clock className="w-3 h-3 mr-1" />
                          {notification.time}
                      </span>
                                            <button
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    deleteNotification(notification.id);
                                                }}
                                                className="text-xs text-gray-400 hover:text-red-500 transition"
                                            >
                                                삭제
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </Card>
                        ))}
                    </div>
                ) : (
                    <Card className="text-center py-16">
                        <Bell className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600">
                            {activeTab === 'all' ? '알림이 없습니다' : '읽지 않은 알림이 없습니다'}
                        </p>
                    </Card>
                )}
            </div>
        </div>
    );
}
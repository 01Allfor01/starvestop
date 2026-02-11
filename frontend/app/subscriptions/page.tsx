'use client';

import Link from 'next/link';
import { Utensils, Calendar, TrendingUp } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function SubscriptionsPage() {
    // TODO: 나중에 실제 API 데이터로 교체
    const subscriptions = [
        {
            id: 1,
            name: '샐러드 정기구독',
            storeName: '샐러디 역삼점',
            description: '신선한 샐러드를 정기적으로 받아보세요',
            price: 28000,
            pickupSchedule: '월수금 아침 픽업',
            pickupTime: '월/수/금 07:00-09:00',
            image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
            subscribers: 1234,
        },
        {
            id: 2,
            name: '과일 정기구독',
            storeName: '과일천국 강남점',
            description: '제철 과일을 정기적으로 받아보세요',
            price: 45000,
            pickupSchedule: '화목 아침 픽업',
            pickupTime: '화/목 08:00-10:00',
            image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff',
            subscribers: 892,
        },
        {
            id: 3,
            name: '베이커리 정기구독',
            storeName: '파리바게뜨 강남점',
            description: '갓 구운 빵을 정기적으로 받아보세요',
            price: 32000,
            pickupSchedule: '수금 아침 픽업',
            pickupTime: '수/금 07:00-09:00',
            image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
            subscribers: 2156,
        },
        {
            id: 4,
            name: '도시락 정기구독',
            storeName: '본도시락 선릉점',
            description: '건강한 도시락을 정기적으로 받아보세요',
            price: 85000,
            pickupSchedule: '월수금 점심 픽업',
            pickupTime: '월/수/금 11:00-13:00',
            image: 'https://images.unsplash.com/photo-1608198399988-841b3c6f76d2',
            subscribers: 567,
        },
        {
            id: 5,
            name: '유기농 채소 정기구독',
            storeName: '마켓컬리 강남점',
            description: '엄선된 유기농 채소를 정기적으로 받아보세요',
            price: 38000,
            pickupSchedule: '금 아침 픽업',
            pickupTime: '금 07:00-09:00',
            image: 'https://images.unsplash.com/photo-1540420773420-3366772f4999',
            subscribers: 1089,
        },
        {
            id: 6,
            name: '프리미엄 우유 정기구독',
            storeName: '목장우유 직영점',
            description: '신선한 우유를 정기적으로 받아보세요',
            price: 24000,
            pickupSchedule: '화목 아침 픽업',
            pickupTime: '화/목 07:00-09:00',
            image: 'https://images.unsplash.com/photo-1563636619-e9143da7973b',
            subscribers: 3421,
        },
    ];

    return (
        <div className="min-h-screen bg-gradient-to-br from-secondary-50 to-secondary-100 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="text-center mb-12">
                    <h1 className="text-4xl font-bold text-gray-900 mb-4">💚 정기 구독</h1>
                    <p className="text-xl text-gray-600 max-w-2xl mx-auto">
                        매달 신선한 식재료를 정기적으로 받아보세요
                    </p>
                </div>

                {/* 구독 목록 */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {subscriptions.map((subscription) => (
                        <Card
                            key={subscription.id}
                            hover
                            className="border-2 border-secondary-200 bg-white"
                        >
                            {/* 이미지 */}
                            <div className="relative -m-6 mb-4">
                                <img
                                    src={subscription.image}
                                    alt={subscription.name}
                                    className="w-full h-48 object-cover rounded-t-xl"
                                />
                                <Badge
                                    variant="subscription"
                                    className="absolute top-3 left-3 bg-white/90 backdrop-blur-sm text-secondary-700 border border-secondary-200"
                                >
                                    {subscription.pickupSchedule}
                                </Badge>
                            </div>

                            {/* 내용 */}
                            <div className="space-y-4">
                                <div>
                                    <p className="text-sm text-gray-500 mb-1">{subscription.storeName}</p>
                                    <h3 className="text-xl font-bold text-gray-900 mb-2">
                                        {subscription.name}
                                    </h3>
                                    <p className="text-gray-600 text-sm mb-3">
                                        {subscription.description}
                                    </p>
                                </div>

                                {/* 픽업 시간 */}
                                <div className="flex items-center text-sm text-gray-700 bg-secondary-50 px-3 py-2 rounded-lg">
                                    <Calendar className="w-4 h-4 text-secondary-600 mr-2" />
                                    <span>{subscription.pickupTime}</span>
                                </div>

                                {/* 가격 */}
                                <div className="pt-4 border-t border-gray-200">
                                    <div className="flex items-baseline justify-between mb-4">
                                        <div>
                      <span className="text-3xl font-bold text-secondary-600">
                        {subscription.price.toLocaleString()}원
                      </span>
                                            <span className="text-gray-500 ml-2">/월</span>
                                        </div>
                                    </div>

                                    {/* 구독자 수 */}
                                    <div className="flex items-center text-sm text-gray-500 mb-4">
                                        <TrendingUp className="w-4 h-4 mr-1" />
                                        <span>{subscription.subscribers.toLocaleString()}명 구독 중</span>
                                    </div>

                                    <Link href={`/subscriptions/${subscription.id}`}>
                                        <Button variant="secondary" fullWidth>
                                            구독하기
                                        </Button>
                                    </Link>
                                </div>
                            </div>
                        </Card>
                    ))}
                </div>

                {/* 안내 섹션 */}
                <div className="mt-16 grid grid-cols-1 md:grid-cols-3 gap-6">
                    <Card className="text-center">
                        <div className="w-12 h-12 bg-secondary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                            <Calendar className="w-6 h-6 text-secondary-600" />
                        </div>
                        <h3 className="font-semibold text-gray-900 mb-2">정기 구독</h3>
                        <p className="text-sm text-gray-600">
                            기재된 요일과 시간에 음식을 받아볼 수 있습니다
                        </p>
                    </Card>

                    <Card className="text-center">
                        <div className="w-12 h-12 bg-secondary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                            <Utensils className="w-6 h-6 text-secondary-600" />
                        </div>
                        <h3 className="font-semibold text-gray-900 mb-2">끼니 걱정 해결</h3>
                        <p className="text-sm text-gray-600">
                            매일 식사 고민 없이 건강하게
                        </p>
                    </Card>

                    <Card className="text-center">
                        <div className="w-12 h-12 bg-secondary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                            <TrendingUp className="w-6 h-6 text-secondary-600" />
                        </div>
                        <h3 className="font-semibold text-gray-900 mb-2">할인 혜택</h3>
                        <p className="text-sm text-gray-600">
                            일반 구매보다 최대 20% 저렴합니다
                        </p>
                    </Card>
                </div>
            </div>
        </div>
    );
}
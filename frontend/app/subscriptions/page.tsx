'use client';

import { useState } from 'react';
import Link from 'next/link';
import { TrendingUp, Calendar, Check } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function SubscriptionsPage() {
    const [filterPeriod, setFilterPeriod] = useState<'all' | 'weekly' | 'monthly'>('all');

    // TODO: 나중에 실제 API 데이터로 교체
    const subscriptions = [
        {
            id: 1,
            name: '샐러드 정기구독',
            storeName: '샐러디 역삼점',
            description: '신선한 샐러드를 매주 화요일에 받아보세요',
            price: 28000,
            period: 'weekly' as const,
            periodText: '주 1회',
            deliveryDays: ['화요일'],
            image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
            features: ['매주 다른 메뉴', '무료 배송', '언제든 해지 가능'],
            subscribers: 1234,
        },
        {
            id: 2,
            name: '과일 정기구독',
            storeName: '과일천국 강남점',
            description: '제철 과일을 매주 목요일에 받아보세요',
            price: 45000,
            period: 'monthly' as const,
            periodText: '월 4회',
            deliveryDays: ['목요일'],
            image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff',
            features: ['제철 과일 선별', '무료 배송', '신선도 보장'],
            subscribers: 892,
        },
        {
            id: 3,
            name: '베이커리 정기구독',
            storeName: '파리바게뜨 강남점',
            description: '갓 구운 빵을 격주로 받아보세요',
            price: 32000,
            period: 'monthly' as const,
            periodText: '월 2회',
            deliveryDays: ['수요일'],
            image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
            features: ['당일 아침 제조', '무료 배송', '빵 종류 선택 가능'],
            subscribers: 2156,
        },
        {
            id: 4,
            name: '도시락 정기구독',
            storeName: '본도시락 선릉점',
            description: '건강한 도시락을 매주 월, 수, 금요일에 받아보세요',
            price: 85000,
            period: 'weekly' as const,
            periodText: '주 3회',
            deliveryDays: ['월요일', '수요일', '금요일'],
            image: 'https://images.unsplash.com/photo-1608198399988-841b3c6f76d2',
            features: ['영양사 설계 메뉴', '무료 배송', '알레르기 대응'],
            subscribers: 567,
        },
        {
            id: 5,
            name: '유기농 채소 정기구독',
            storeName: '마켓컬리 강남점',
            description: '엄선된 유기농 채소를 매주 금요일에 받아보세요',
            price: 38000,
            period: 'weekly' as const,
            periodText: '주 1회',
            deliveryDays: ['금요일'],
            image: 'https://images.unsplash.com/photo-1540420773420-3366772f4999',
            features: ['유기농 인증', '무료 배송', '신선도 보장'],
            subscribers: 1089,
        },
        {
            id: 6,
            name: '프리미엄 우유 정기구독',
            storeName: '목장우유 직영점',
            description: '신선한 우유를 매주 화, 목요일에 받아보세요',
            price: 24000,
            period: 'weekly' as const,
            periodText: '주 2회',
            deliveryDays: ['화요일', '목요일'],
            image: 'https://images.unsplash.com/photo-1563636619-e9143da7973b',
            features: ['당일 착유', '무료 배송', '유리병 제공'],
            subscribers: 3421,
        },
    ];

    const filteredSubscriptions = subscriptions.filter((sub) =>
        filterPeriod === 'all' ? true : sub.period === filterPeriod
    );

    return (
        <div className="min-h-screen bg-gradient-to-br from-secondary-50 to-secondary-100 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="text-center mb-12">
                    <h1 className="text-4xl font-bold text-gray-900 mb-4">💚 정기 구독</h1>
                    <p className="text-xl text-gray-600 max-w-2xl mx-auto">
                        매주 또는 매월 신선한 식재료를 정기적으로 받아보세요
                    </p>
                </div>

                {/* 필터 */}
                <div className="flex justify-center space-x-4 mb-8">
                    <button
                        onClick={() => setFilterPeriod('all')}
                        className={`px-6 py-3 rounded-lg font-medium transition ${
                            filterPeriod === 'all'
                                ? 'bg-secondary-500 text-white shadow-lg'
                                : 'bg-white text-gray-700 hover:bg-gray-50'
                        }`}
                    >
                        전체
                    </button>
                    <button
                        onClick={() => setFilterPeriod('weekly')}
                        className={`px-6 py-3 rounded-lg font-medium transition ${
                            filterPeriod === 'weekly'
                                ? 'bg-secondary-500 text-white shadow-lg'
                                : 'bg-white text-gray-700 hover:bg-gray-50'
                        }`}
                    >
                        주간 구독
                    </button>
                    <button
                        onClick={() => setFilterPeriod('monthly')}
                        className={`px-6 py-3 rounded-lg font-medium transition ${
                            filterPeriod === 'monthly'
                                ? 'bg-secondary-500 text-white shadow-lg'
                                : 'bg-white text-gray-700 hover:bg-gray-50'
                        }`}
                    >
                        월간 구독
                    </button>
                </div>

                {/* 구독 목록 */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {filteredSubscriptions.map((subscription) => (
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
                                <Badge variant="subscription" className="absolute top-4 left-4">
                                    {subscription.periodText}
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

                                {/* 배송일 */}
                                <div className="flex items-center text-sm text-gray-700">
                                    <Calendar className="w-4 h-4 text-secondary-500 mr-2" />
                                    <span>{subscription.deliveryDays.join(', ')} 배송</span>
                                </div>

                                {/* 특징 */}
                                <ul className="space-y-2">
                                    {subscription.features.map((feature, index) => (
                                        <li key={index} className="flex items-center text-sm text-gray-600">
                                            <Check className="w-4 h-4 text-secondary-500 mr-2 flex-shrink-0" />
                                            {feature}
                                        </li>
                                    ))}
                                </ul>

                                {/* 가격 */}
                                <div className="pt-4 border-t border-gray-200">
                                    <div className="flex items-baseline justify-between mb-4">
                                        <div>
                      <span className="text-3xl font-bold text-secondary-600">
                        {subscription.price.toLocaleString()}원
                      </span>
                                            <span className="text-gray-500 ml-2">
                        /{subscription.period === 'weekly' ? '주' : '월'}
                      </span>
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
                        <h3 className="font-semibold text-gray-900 mb-2">정기 배송</h3>
                        <p className="text-sm text-gray-600">
                            선택한 요일에 자동으로 배송됩니다
                        </p>
                    </Card>

                    <Card className="text-center">
                        <div className="w-12 h-12 bg-secondary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                            <Check className="w-6 h-6 text-secondary-600" />
                        </div>
                        <h3 className="font-semibold text-gray-900 mb-2">언제든 해지</h3>
                        <p className="text-sm text-gray-600">
                            위약금 없이 언제든 해지 가능합니다
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
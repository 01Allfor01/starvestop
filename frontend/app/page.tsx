'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { Clock, MapPin, TrendingUp } from 'lucide-react';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import Button from '@/components/ui/Button';

export default function HomePage() {
    return (
        <div className="min-h-screen bg-gray-50">
            {/* 히어로 섹션 */}
            <section className="bg-gradient-to-r from-primary-500 to-primary-600 text-white py-20">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
                    <h1 className="text-4xl md:text-5xl font-bold mb-4">
                        합리적인 가격으로 신선한 음식을
                    </h1>
                    <p className="text-xl mb-8 text-primary-100">
                        마감 세일과 정기구독으로 똑똑하게 장보기
                    </p>
                    <div className="flex flex-col sm:flex-row gap-4 justify-center">
                        <Link href="/products/sale">
                            <Button size="lg" variant="secondary">
                                마감세일 구경하기
                            </Button>
                        </Link>
                        <Link href="/subscriptions">
                            <Button size="lg" variant="outline" className="bg-white text-primary-600 hover:bg-gray-100">
                                정기구독 알아보기
                            </Button>
                        </Link>
                    </div>
                </div>
            </section>

            {/* 마감 세일 */}
            <section className="py-16">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex items-center justify-between mb-8">
                        <div>
                            <h2 className="text-3xl font-bold text-gray-900 mb-2">🔥 오늘의 마감세일</h2>
                            <p className="text-gray-600">지금 바로 픽업 가능한 특가 상품</p>
                        </div>
                        <Link href="/products/sale">
                            <Button variant="outline">전체보기</Button>
                        </Link>
                    </div>
                    <SaleProducts />
                </div>
            </section>

            {/* 정기구독 */}
            <section className="py-16 bg-gradient-to-br from-secondary-50 to-secondary-100">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex items-center justify-between mb-8">
                        <div>
                            <h2 className="text-3xl font-bold text-gray-900 mb-2">💚 정기구독</h2>
                            <p className="text-gray-600">매월 정기적으로 신선한 상품을</p>
                        </div>
                        <Link href="/subscriptions">
                            <Button variant="outline">전체보기</Button>
                        </Link>
                    </div>
                    <SubscriptionProducts />
                </div>
            </section>

            {/* 내 근처 가게 */}
            <section className="py-16">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex items-center justify-between mb-8">
                        <div>
                            <h2 className="text-3xl font-bold text-gray-900 mb-2">📍 내 근처 가게</h2>
                            <p className="text-gray-600">가까운 곳에서 신선하게</p>
                        </div>
                        <Link href="/stores">
                            <Button variant="outline">전체보기</Button>
                        </Link>
                    </div>
                    <NearbyStores />
                </div>
            </section>
        </div>
    );
}

// 마감세일 상품 컴포넌트
function SaleProducts() {
    const products = [
        {
            id: 1,
            name: '프리미엄 크루아상 3입',
            storeName: '파리바게뜨 강남점',
            originalPrice: 6000,
            salePrice: 3000,
            discount: 3000,
            image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
            endTime: new Date(Date.now() + 2 * 60 * 60 * 1000), // 2시간 후
            distance: 1.2,
        },
        {
            id: 2,
            name: '프리미엄 닭가슴살 샐러드',
            storeName: '샐러디 역삼점',
            originalPrice: 12000,
            salePrice: 7200,
            discount: 4800,
            image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
            endTime: new Date(Date.now() + 3 * 60 * 60 * 1000), // 3시간 후
            distance: 0.8,
        },
        {
            id: 3,
            name: '스페셜 연어 도시락',
            storeName: '본도시락 선릉점',
            originalPrice: 13000,
            salePrice: 9750,
            discount: 3250,
            image: 'https://images.unsplash.com/photo-1608198399988-841b3c6f76d2',
            endTime: new Date(Date.now() + 1.5 * 60 * 60 * 1000), // 1.5시간 후
            distance: 1.5,
        },
        {
            id: 4,
            name: '신선 과일 박스',
            storeName: '과일천국 강남점',
            originalPrice: 15000,
            salePrice: 12000,
            discount: 3000,
            image: 'https://images.unsplash.com/photo-1488459716781-31db52582fe9',
            endTime: new Date(Date.now() + 4 * 60 * 60 * 1000), // 4시간 후
            distance: 2.1,
        },
    ];

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {products.map((product) => (
                <Link key={product.id} href={`/products/${product.id}`}>
                    <Card hover padding="none" className="overflow-hidden h-full">
                        <div className="relative">
                            <img
                                src={product.image}
                                alt={product.name}
                                className="w-full h-48 object-cover"
                            />
                            <Badge variant="sale" className="absolute top-3 left-3">
                                {product.discount.toLocaleString()}원 할인
                            </Badge>
                            <CountdownTimer endTime={product.endTime} />
                        </div>
                        <div className="p-4">
                            <p className="text-sm text-gray-500 mb-1">{product.storeName}</p>
                            <h3 className="font-semibold text-gray-900 mb-2 line-clamp-2">{product.name}</h3>
                            <div className="flex items-center text-sm text-gray-600 mb-3">
                                <MapPin className="w-4 h-4 mr-1" />
                                <span>{product.distance}km</span>
                            </div>
                            <div className="flex items-baseline space-x-2">
                <span className="text-sm text-gray-400 line-through">
                  {product.originalPrice.toLocaleString()}원
                </span>
                                <span className="text-xl font-bold text-primary-600">
                  {product.salePrice.toLocaleString()}원
                </span>
                            </div>
                        </div>
                    </Card>
                </Link>
            ))}
        </div>
    );
}

// 카운트다운 타이머 컴포넌트 - products/sale과 동일하게 수정
function CountdownTimer({ endTime }: { endTime: Date }) {
    const [timeLeft, setTimeLeft] = useState('');

    useEffect(() => {
        const timer = setInterval(() => {
            const now = new Date().getTime();
            const distance = endTime.getTime() - now;

            if (distance < 0) {
                setTimeLeft('종료');
                clearInterval(timer);
                return;
            }

            const hours = Math.floor(distance / (1000 * 60 * 60));
            const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((distance % (1000 * 60)) / 1000);

            setTimeLeft(`${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`);
        }, 1000);

        return () => clearInterval(timer);
    }, [endTime]);

    return (
        <div className="absolute bottom-3 left-3 bg-white/90 backdrop-blur-sm px-3 py-1.5 rounded-lg flex items-center space-x-1">
            <Clock className="w-4 h-4 text-red-500" />
            <span className="text-sm font-semibold text-gray-900">{timeLeft}</span>
        </div>
    );
}

// 정기구독 상품 컴포넌트
function SubscriptionProducts() {
    const subscriptions = [
        {
            id: 1,
            name: '샐러드 정기구독',
            storeName: '샐러디 역삼점',
            price: 28000,
            image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
            days: ['월', '수', '금'],
            time: '아침',
            subscribers: 1234,
        },
        {
            id: 2,
            name: '빵 정기구독',
            storeName: '파리바게뜨 강남점',
            price: 32000,
            image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
            days: ['화', '목', '토'],
            time: '저녁',
            subscribers: 2156,
        },
        {
            id: 3,
            name: '과일 정기구독',
            storeName: '과일천국 강남점',
            price: 45000,
            image: 'https://images.unsplash.com/photo-1488459716781-31db52582fe9',
            days: ['월', '목'],
            time: '점심',
            subscribers: 892,
        },
        {
            id: 4,
            name: '도시락 정기구독',
            storeName: '본도시락 선릉점',
            price: 38000,
            image: 'https://images.unsplash.com/photo-1608198399988-841b3c6f76d2',
            days: ['월', '화', '수', '목', '금'],
            time: '점심',
            subscribers: 3421,
        },
    ];

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {subscriptions.map((sub) => (
                <Link key={sub.id} href={`/subscriptions/${sub.id}`}>
                    <Card hover padding="none" className="overflow-hidden h-full border-2 border-secondary-200">
                        <div className="relative">
                            <img
                                src={sub.image}
                                alt={sub.name}
                                className="w-full h-48 object-cover"
                            />
                            <Badge variant="subscription" className="absolute top-3 left-3">
                                {sub.days.join('.')} {sub.time} 픽업
                            </Badge>
                        </div>
                        <div className="p-4">
                            <p className="text-sm text-gray-500 mb-1">{sub.storeName}</p>
                            <h3 className="font-semibold text-gray-900 mb-2">{sub.name}</h3>
                            <div className="flex items-center text-sm text-gray-600 mb-3">
                                <TrendingUp className="w-4 h-4 mr-1 text-secondary-500" />
                                <span>{sub.subscribers.toLocaleString()}명 구독중</span>
                            </div>
                            <div className="flex items-baseline">
                <span className="text-2xl font-bold text-secondary-600">
                  {sub.price.toLocaleString()}원
                </span>
                                <span className="text-gray-500 ml-1">/월</span>
                            </div>
                        </div>
                    </Card>
                </Link>
            ))}
        </div>
    );
}

// 내 근처 가게 컴포넌트
function NearbyStores() {
    const stores = [
        {
            id: 1,
            name: '파리바게뜨 강남점',
            category: '베이커리',
            distance: 1.2,
            image: 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4',
            saleCount: 3,
        },
        {
            id: 2,
            name: '샐러디 역삼점',
            category: '샐러드',
            distance: 0.8,
            image: 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1',
            saleCount: 2,
        },
        {
            id: 3,
            name: '본도시락 선릉점',
            category: '도시락',
            distance: 1.5,
            image: 'https://images.unsplash.com/photo-1504674900247-0877df9cc836',
            saleCount: 5,
        },
        {
            id: 4,
            name: '과일천국 강남점',
            category: '과일',
            distance: 2.1,
            image: 'https://images.unsplash.com/photo-1488459716781-31db52582fe9',
            saleCount: 4,
        },
    ];

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {stores.map((store) => (
                <Link key={store.id} href={`/stores/${store.id}`}>
                    <Card hover padding="none" className="overflow-hidden h-full">
                        <div className="relative">
                            <img
                                src={store.image}
                                alt={store.name}
                                className="w-full h-48 object-cover"
                            />
                            {store.saleCount > 0 && (
                                <Badge variant="sale" className="absolute top-3 left-3">
                                    마감세일 {store.saleCount}개
                                </Badge>
                            )}
                        </div>
                        <div className="p-4">
                            <h3 className="font-semibold text-gray-900 mb-1">{store.name}</h3>
                            <p className="text-sm text-gray-500 mb-3">{store.category}</p>
                            <div className="flex items-center text-sm text-gray-600">
                                <MapPin className="w-4 h-4 mr-1" />
                                <span>{store.distance}km</span>
                            </div>
                        </div>
                    </Card>
                </Link>
            ))}
        </div>
    );
}
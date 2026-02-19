'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { Clock, MapPin, Loader2, CalendarCheck } from 'lucide-react';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import Button from '@/components/ui/Button';
import HeroBanner from '@/components/HeroBanner';
import { productsApi } from '@/lib/api/products';
import { subscriptionsApi } from '@/lib/api/subscriptions';
import { storesApi } from '@/lib/api/stores';

// ─── 공통 훅: 브라우저 GPS 위치 가져오기 ─────────────────────────────────────
function useGeolocation() {
    const [location, setLocation] = useState<{ lat: number; lng: number } | null>(null);
    const [denied, setDenied] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!navigator.geolocation) {
            setDenied(true);
            setLoading(false);
            return;
        }

        navigator.geolocation.getCurrentPosition(
            (pos) => {
                setLocation({ lat: pos.coords.latitude, lng: pos.coords.longitude });
                setLoading(false);
            },
            (err) => {
                console.warn('📍 위치 권한 거부:', err.message);
                setDenied(true);
                setLoading(false);
            }
        );
    }, []);

    return { location, denied, gpsLoading: loading };
}

// ─── 거리 포맷 함수 ───────────────────────────────────────────────────────────
function formatDistance(distanceKm: number): string {
    if (distanceKm < 1) {
        return `${Math.round(distanceKm * 1000)}m`;
    }
    return `${distanceKm.toFixed(1)}km`;
}

// ─── 요일/식사시간 매핑 ───────────────────────────────────────────────────────
const dayMap: Record<string, string> = {
    'MONDAY': '월',
    'TUESDAY': '화',
    'WEDNESDAY': '수',
    'THURSDAY': '목',
    'FRIDAY': '금',
    'SATURDAY': '토',
    'SUNDAY': '일'
};

const mealTimeMap: Record<string, string> = {
    'BREAKFAST': '아침',
    'LUNCH': '점심',
    'DINNER': '저녁'
};

export default function HomePage() {
    return (
        <div className="min-h-screen bg-gray-50">
            {/* ✅ 히어로 배너 */}
            <HeroBanner />

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
                            <h2 className="text-3xl font-bold text-gray-900 mb-2 flex items-center">
                                <CalendarCheck className="w-8 h-8 text-secondary-600 mr-3" />
                                정기구독
                            </h2>
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

// ─── 마감세일 상품 컴포넌트 ───────────────────────────────────────────────────
function SaleProducts() {
    const [products, setProducts] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const { location, denied, gpsLoading } = useGeolocation();

    useEffect(() => {
        if (gpsLoading) return;

        if (denied || !location) {
            setLoading(false);
            return;
        }

        const fetchProducts = async () => {
            try {
                setLoading(true);
                const data = await productsApi.getSaleProducts(location.lat, location.lng, 100);

                const filtered = data.filter((item: any) => item.distance <= 5);

                const parseEndTime = (timeStr: string): Date | null => {
                    if (!timeStr) return null;
                    const [hours, minutes, seconds] = timeStr.split(':').map(Number);
                    if (isNaN(hours)) return null;

                    const now = new Date();
                    const endDate = new Date(
                        now.getFullYear(),
                        now.getMonth(),
                        now.getDate(),
                        hours,
                        minutes,
                        seconds || 0
                    );
                    if (endDate <= now) {
                        endDate.setDate(endDate.getDate() + 1);
                    }
                    return endDate;
                };

                const mappedProducts = filtered.map((item: any) => ({
                    id: item.id,
                    name: item.name,
                    storeName: item.storeName || '가게명 미상',
                    originalPrice: item.price,
                    salePrice: item.salePrice,
                    discount: item.price - item.salePrice,
                    image: item.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
                    endTime: parseEndTime(item.endTime),
                    distance: item.distance,
                }));

                const shuffled = [...mappedProducts].sort(() => Math.random() - 0.5);
                const random4 = shuffled.slice(0, 4);

                setProducts(random4);
            } catch (error) {
                console.error('❌ 마감세일 로딩 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, [location, gpsLoading, denied]);

    if (loading || gpsLoading) {
        return (
            <div className="flex justify-center items-center py-20">
                <Loader2 className="w-10 h-10 text-orange-500 animate-spin" />
            </div>
        );
    }

    if (denied) {
        return (
            <div className="text-center py-20 text-gray-500">
                위치 권한을 허용하면 근처 마감세일 상품을 볼 수 있어요
            </div>
        );
    }

    if (products.length === 0) {
        return (
            <div className="text-center py-20 text-gray-500">
                5km 이내 마감세일 상품이 없습니다
            </div>
        );
    }

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
                            {product.endTime && <CountdownTimer endTime={product.endTime} />}
                        </div>
                        <div className="p-4">
                            <p className="text-sm text-gray-500 mb-1">{product.storeName}</p>
                            <h3 className="font-semibold text-gray-900 mb-2 line-clamp-2">{product.name}</h3>
                            <div className="flex items-center text-sm text-gray-600 mb-2">
                                <MapPin className="w-4 h-4 mr-1" />
                                <span>{formatDistance(product.distance)}</span>
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

// ─── 카운트다운 타이머 컴포넌트 ──────────────────────────────────────────────
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

// ─── 정기구독 상품 컴포넌트 ──────────────────────────────────────────────────
function SubscriptionProducts() {
    const [subscriptions, setSubscriptions] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const { location, denied, gpsLoading } = useGeolocation();

    useEffect(() => {
        if (gpsLoading) return;

        if (denied || !location) {
            setLoading(false);
            return;
        }

        const fetchSubscriptions = async () => {
            try {
                setLoading(true);
                const data = await subscriptionsApi.getSubscriptions(location.lat, location.lng, 100);

                const filtered = data.filter((item: any) => item.distance <= 5);

                const mappedSubscriptions = filtered.map((item: any) => ({
                    id: item.id,
                    name: item.name,
                    storeName: item.storeName || '가게명 미상',
                    price: item.price,
                    image: item.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
                    days: item.dayList?.map((d: string) => dayMap[d] || d) || ['월', '수', '금'],
                    time: mealTimeMap[item.mealTimeList?.[0]] || '점심',
                    distance: item.distance,
                }));

                const shuffled = [...mappedSubscriptions].sort(() => Math.random() - 0.5);
                const random4 = shuffled.slice(0, 4);

                setSubscriptions(random4);
            } catch (error) {
                console.error('❌ 정기구독 로딩 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchSubscriptions();
    }, [location, gpsLoading, denied]);

    if (loading || gpsLoading) {
        return (
            <div className="flex justify-center items-center py-20">
                <Loader2 className="w-10 h-10 text-green-500 animate-spin" />
            </div>
        );
    }

    if (denied) {
        return (
            <div className="text-center py-20 text-gray-500">
                위치 권한을 허용하면 근처 정기구독 상품을 볼 수 있어요
            </div>
        );
    }

    if (subscriptions.length === 0) {
        return (
            <div className="text-center py-20 text-gray-500">
                5km 이내 정기구독 상품이 없습니다
            </div>
        );
    }

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
                            <p className="text-sm text-gray-500 mb-2">{sub.storeName}</p>
                            <h3 className="font-bold text-lg text-gray-900 mb-2 line-clamp-2">{sub.name}</h3>
                            <div className="flex items-center text-sm text-gray-600 mb-3">
                                <MapPin className="w-4 h-4 mr-1" />
                                <span>{formatDistance(sub.distance)}</span>
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

// ─── 내 근처 가게 컴포넌트 ───────────────────────────────────────────────────
function NearbyStores() {
    const [stores, setStores] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const { location, denied, gpsLoading } = useGeolocation();

    useEffect(() => {
        if (gpsLoading) return;

        if (denied || !location) {
            setLoading(false);
            return;
        }

        const fetchStores = async () => {
            try {
                setLoading(true);
                const data = await storesApi.getStores({
                    nowLatitude: location.lat,
                    nowLongitude: location.lng,
                    size: 20,
                });

                const sorted = [...data].sort((a, b) => {
                    const distA = a.distance ?? Infinity;
                    const distB = b.distance ?? Infinity;
                    return distA - distB;
                });

                const nearest20 = sorted.slice(0, 20);
                const random4 = [...nearest20].sort(() => Math.random() - 0.5).slice(0, 4);

                const storesWithSaleCount = await Promise.all(
                    random4.map(async (item: any) => {
                        let saleCount = 0;
                        try {
                            const products = await productsApi.getStoreProducts(item.id);
                            saleCount = products.length;
                        } catch {
                            saleCount = 0;
                        }

                        return {
                            id: item.id,
                            name: item.name,
                            category: item.category || '음식점',
                            distance: item.distance ?? 0,
                            image: item.imageUrl || 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4',
                            saleCount,
                        };
                    })
                );

                setStores(storesWithSaleCount);
            } catch (error) {
                console.error('❌ 매장 로딩 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchStores();
    }, [location, denied, gpsLoading]);

    if (denied) {
        return (
            <div className="text-center py-20 text-gray-500">
                위치 권한을 허용해야 근처 가게를 볼 수 있어요
            </div>
        );
    }

    if (gpsLoading || loading) {
        return (
            <div className="flex flex-col justify-center items-center py-20 gap-3">
                <Loader2 className="w-10 h-10 text-blue-500 animate-spin" />
                {gpsLoading && (
                    <p className="text-sm text-gray-500">위치 정보를 가져오는 중...</p>
                )}
            </div>
        );
    }

    if (stores.length === 0) {
        return (
            <div className="text-center py-20 text-gray-500">
                근처에 가게가 없습니다
            </div>
        );
    }

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
                        </div>
                        <div className="p-4">
                            <h3 className="font-semibold text-gray-900 mb-1">{store.name}</h3>
                            <p className="text-sm text-gray-500 mb-3">{store.category}</p>
                            <div className="flex items-center text-sm text-gray-600">
                                <MapPin className="w-4 h-4 mr-1" />
                                <span>{formatDistance(store.distance)}</span>
                            </div>
                        </div>
                    </Card>
                </Link>
            ))}
        </div>
    );
}
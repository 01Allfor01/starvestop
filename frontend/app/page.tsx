'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { Clock, MapPin, TrendingUp, Loader2, CalendarCheck } from 'lucide-react';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import Button from '@/components/ui/Button';
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

// ─── 거리 계산 함수 (Haversine formula) ────────────────────────────────────
function calculateDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {
    const R = 6371; // 지구 반지름 (km)
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
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
            {/* 히어로 섹션 - 이미지 배경으로 개선 */}
            <section className="relative bg-gradient-to-r from-amber-50 to-orange-50 overflow-hidden">
                {/* 배경 이미지 */}
                <div className="absolute inset-0 opacity-20">
                    <img
                        src="https://images.unsplash.com/photo-1495195134817-aeb325a55b65?w=1200"
                        alt="배경"
                        className="w-full h-full object-cover"
                    />
                </div>

                {/* 그라데이션 오버레이 */}
                <div className="absolute inset-0 bg-gradient-to-r from-amber-50/95 to-orange-50/95"></div>

                {/* 컨텐츠 */}
                <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
                    <div className="grid md:grid-cols-2 gap-12 items-center">
                        {/* 왼쪽: 텍스트 */}
                        <div>
                            <div className="inline-block mb-4">
                                <div className="flex items-center space-x-2 bg-white px-4 py-2 rounded-full shadow-sm">
                                    <div className="w-8 h-8 bg-gradient-to-br from-orange-500 to-orange-600 rounded-full flex items-center justify-center">
                                        <span className="text-white text-lg font-bold">S</span>
                                    </div>
                                    <span className="font-bold text-gray-900">Starve Stop</span>
                                </div>
                            </div>

                            <h1 className="text-4xl md:text-5xl font-bold text-gray-900 mb-4 leading-tight">
                                합리적인 가격으로<br />
                                <span className="text-orange-600">든든한 한끼</span>
                            </h1>

                            <p className="text-xl text-gray-700 mb-8">
                                마감 세일과 정기구독으로 똑똑하게 장보기
                            </p>

                            {/* 아이콘 포인트 */}
                            <div className="flex items-center space-x-8 mb-8">
                                <div className="flex items-center space-x-2">
                                    <div className="w-12 h-12 bg-white rounded-full flex items-center justify-center shadow-md">
                                        <Clock className="w-6 h-6 text-orange-600" />
                                    </div>
                                    <span className="font-semibold text-gray-900">마감 세일</span>
                                </div>
                                <div className="flex items-center space-x-2">
                                    <div className="w-12 h-12 bg-white rounded-full flex items-center justify-center shadow-md">
                                        <TrendingUp className="w-6 h-6 text-green-600" />
                                    </div>
                                    <span className="font-semibold text-gray-900">정기 구독</span>
                                </div>
                            </div>

                            {/* 버튼 */}
                            <div className="flex flex-col sm:flex-row gap-4">
                                <Link href="/products/sale">
                                    <Button size="lg" className="bg-orange-600 hover:bg-orange-700 text-white shadow-lg">
                                        마감세일 구경하기
                                    </Button>
                                </Link>
                                <Link href="/subscriptions">
                                    <Button size="lg" variant="outline" className="border-2 border-orange-600 text-orange-600 hover:bg-orange-50">
                                        정기구독 알아보기
                                    </Button>
                                </Link>
                            </div>
                        </div>

                        {/* 오른쪽: 이미지들 */}
                        <div className="relative hidden md:block">
                            <div className="relative">
                                {/* 메인 도시락 이미지 */}
                                <div className="relative z-10 bg-white rounded-2xl shadow-2xl p-4 transform rotate-2">
                                    <img
                                        src="https://images.unsplash.com/photo-1608198399988-841b3c6f76d2?w=400"
                                        alt="도시락"
                                        className="rounded-xl w-full h-64 object-cover"
                                    />
                                </div>

                                {/* 샐러드 이미지 */}
                                <div className="absolute -top-4 -right-4 z-20 bg-white rounded-2xl shadow-xl p-3 transform -rotate-6">
                                    <img
                                        src="https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=200"
                                        alt="샐러드"
                                        className="rounded-lg w-32 h-32 object-cover"
                                    />
                                </div>

                                {/* 샌드위치 이미지 */}
                                <div className="absolute -bottom-4 -left-4 z-20 bg-white rounded-2xl shadow-xl p-3 transform rotate-6">
                                    <img
                                        src="https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=200"
                                        alt="샌드위치"
                                        className="rounded-lg w-32 h-32 object-cover"
                                    />
                                </div>
                            </div>
                        </div>
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
                            {/* ✅ 💚 대신 CalendarCheck 아이콘 사용 */}
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

        const fetchProducts = async () => {
            try {
                setLoading(true);
                const data = await productsApi.getSaleProducts();

                console.log('📦 마감세일 데이터:', data);

                // ✅ 거리 계산 및 5km 이내 필터링
                const productsWithDistance = data
                    .map((item: any) => {
                        let distance = null;
                        if (location && item.location && item.location.coordinates) {
                            distance = calculateDistance(
                                location.lat,
                                location.lng,
                                item.location.coordinates[1],
                                item.location.coordinates[0]
                            );
                        }

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

                        return {
                            id: item.id,
                            name: item.name,
                            storeName: item.storeName || '가게명 미상',
                            originalPrice: item.price,
                            salePrice: item.salePrice,
                            discount: item.price - item.salePrice,
                            image: item.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
                            endTime: parseEndTime(item.endTime),
                            distance,
                        };
                    })
                    .filter((item: any) => item.distance === null || item.distance <= 5); // ✅ 5km 이내만

                // ✅ 랜덤 4개 선택
                const shuffled = [...productsWithDistance].sort(() => Math.random() - 0.5);
                const random4 = shuffled.slice(0, 4);

                setProducts(random4);
            } catch (error) {
                console.error('❌ 마감세일 로딩 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, [location, gpsLoading]);

    if (loading || gpsLoading) {
        return (
            <div className="flex justify-center items-center py-20">
                <Loader2 className="w-10 h-10 text-orange-500 animate-spin" />
            </div>
        );
    }

    if (products.length === 0) {
        return (
            <div className="text-center py-20 text-gray-500">
                {denied ? '위치 권한을 허용하면 근처 마감세일 상품을 볼 수 있어요' : '현재 진행 중인 마감세일이 없습니다'}
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
                            {/* ✅ 거리 표시 추가 */}
                            {product.distance !== null && (
                                <div className="flex items-center text-sm text-gray-600 mb-2">
                                    <MapPin className="w-4 h-4 mr-1" />
                                    <span>{formatDistance(product.distance)}</span>
                                </div>
                            )}
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

        const fetchSubscriptions = async () => {
            try {
                setLoading(true);
                const data = await subscriptionsApi.getSubscriptions();

                console.log('📦 정기구독 데이터:', data);

                // ✅ 거리 계산 및 5km 이내 필터링
                const subscriptionsWithDistance = data
                    .map((item: any) => {
                        let distance = null;
                        if (location && item.location && item.location.coordinates) {
                            distance = calculateDistance(
                                location.lat,
                                location.lng,
                                item.location.coordinates[1],
                                item.location.coordinates[0]
                            );
                        }

                        return {
                            id: item.id,
                            name: item.name,
                            storeName: item.storeName || '가게명 미상',
                            price: item.price,
                            image: item.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
                            days: item.dayList?.map((d: string) => dayMap[d] || d) || ['월', '수', '금'],
                            time: mealTimeMap[item.mealTimeList?.[0]] || '점심',
                            distance,
                        };
                    })
                    .filter((item: any) => item.distance === null || item.distance <= 5); // ✅ 5km 이내만

                // ✅ 랜덤 4개 선택
                const shuffled = [...subscriptionsWithDistance].sort(() => Math.random() - 0.5);
                const random4 = shuffled.slice(0, 4);

                setSubscriptions(random4);
            } catch (error) {
                console.error('❌ 정기구독 로딩 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchSubscriptions();
    }, [location, gpsLoading]);

    if (loading || gpsLoading) {
        return (
            <div className="flex justify-center items-center py-20">
                <Loader2 className="w-10 h-10 text-green-500 animate-spin" />
            </div>
        );
    }

    if (subscriptions.length === 0) {
        return (
            <div className="text-center py-20 text-gray-500">
                {denied ? '위치 권한을 허용하면 근처 정기구독 상품을 볼 수 있어요' : '현재 진행 중인 정기구독이 없습니다'}
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
                            {/* ✅ 거리 표시 추가 */}
                            {sub.distance !== null && (
                                <div className="flex items-center text-sm text-gray-600 mb-3">
                                    <MapPin className="w-4 h-4 mr-1" />
                                    <span>{formatDistance(sub.distance)}</span>
                                </div>
                            )}
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

                console.log('📦 매장 데이터:', data);

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
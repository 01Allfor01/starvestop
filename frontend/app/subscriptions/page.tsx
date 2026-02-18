'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { Calendar, TrendingUp, Utensils, Loader2, MapPin, CalendarCheck } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
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

// 식사시간 → 시간대 변환
const mealTimeRange: Record<string, string> = {
    'BREAKFAST': '07:00-12:00',
    'LUNCH': '12:00-17:00',
    'DINNER': '17:00-22:00'
};

export default function SubscriptionsPage() {
    const [subscriptions, setSubscriptions] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const { location, denied, gpsLoading } = useGeolocation();

    useEffect(() => {
        if (gpsLoading) return;

        const fetchSubscriptions = async () => {
            try {
                setLoading(true);
                const data = await subscriptionsApi.getSubscriptions();

                console.log('📦 정기구독 전체 데이터:', data);

                // ✅ 각 구독의 매장 정보 병렬로 가져오기 + 거리 계산
                const subscriptionsWithImages = await Promise.all(
                    data.map(async (item: any) => {
                        let storeImageUrl = 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c';

                        try {
                            const storeDetail = await storesApi.getStore(item.storeId);
                            storeImageUrl = storeDetail.imageUrl || storeImageUrl;
                        } catch (error) {
                            console.warn(`⚠️ 매장 ${item.storeId} 이미지 로드 실패`);
                        }

                        // ✅ 거리 계산
                        let distance = null;
                        if (location && item.location && item.location.coordinates) {
                            distance = calculateDistance(
                                location.lat,
                                location.lng,
                                item.location.coordinates[1],  // 위도
                                item.location.coordinates[0]   // 경도
                            );
                        }

                        // 요일 한글 변환
                        const days = item.dayList?.map((d: string) => dayMap[d] || d) || [];

                        // 식사시간 (첫 번째 값 사용)
                        const mealTimeKey = item.mealTimeList?.[0] || 'LUNCH';
                        const mealTime = mealTimeMap[mealTimeKey] || '점심';
                        const timeRange = mealTimeRange[mealTimeKey] || '12:00-17:00';

                        return {
                            id: item.id,
                            name: item.name,
                            storeName: item.storeName || '가게명 미상',
                            description: item.description || `${mealTime} 식사를 정기적으로 받아보세요`,
                            price: item.price,
                            image: storeImageUrl,
                            days,
                            mealTime,
                            timeRange,
                            pickupSchedule: `${days.join('.')} ${mealTime}`,
                            isJoinable: item.joinable,
                            distance,
                        };
                    })
                );

                setSubscriptions(subscriptionsWithImages);
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
            <div className="min-h-screen bg-gradient-to-br from-secondary-50 to-secondary-100 flex items-center justify-center">
                <Loader2 className="w-12 h-12 text-secondary-500 animate-spin" />
            </div>
        );
    }

    if (subscriptions.length === 0) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-secondary-50 to-secondary-100 flex items-center justify-center">
                <div className="text-center text-gray-500">
                    현재 진행 중인 정기구독이 없습니다
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-secondary-50 to-secondary-100 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="text-center mb-12">
                    <h1 className="text-4xl font-bold text-gray-900 mb-4 flex items-center justify-center">
                        <CalendarCheck className="w-9 h-9 text-secondary-600 mr-3" />
                        정기 구독
                    </h1>
                    <p className="text-xl text-gray-600 max-w-2xl mx-auto">
                        신선한 식재료를 정기적으로 받아보세요
                    </p>
                </div>

                {/* 구독 목록 */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {subscriptions.map((subscription) => (
                        <Link key={subscription.id} href={`/subscriptions/${subscription.id}`}>
                            <Card
                                hover
                                className="border-2 border-secondary-200 bg-white h-full"
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
                                    {!subscription.isJoinable && (
                                        <Badge
                                            variant="sale"
                                            className="absolute top-3 right-3 bg-gray-500/90 backdrop-blur-sm text-white"
                                        >
                                            구독 마감
                                        </Badge>
                                    )}
                                </div>

                                {/* 내용 */}
                                <div className="space-y-4">
                                    <div>
                                        <p className="text-sm text-gray-500 mb-1">{subscription.storeName}</p>
                                        <h3 className="text-xl font-bold text-gray-900 mb-2">
                                            {subscription.name}
                                        </h3>
                                        <p className="text-gray-600 text-sm mb-3 line-clamp-2">
                                            {subscription.description}
                                        </p>
                                    </div>

                                    {/* ✅ 거리 표시 추가 */}
                                    {subscription.distance !== null && (
                                        <div className="flex items-center text-sm text-gray-600">
                                            <MapPin className="w-4 h-4 mr-1 text-secondary-600" />
                                            <span>{formatDistance(subscription.distance)}</span>
                                        </div>
                                    )}

                                    {/* 픽업 시간 */}
                                    <div className="space-y-2">
                                        <div className="flex items-center text-sm text-gray-700 bg-secondary-50 px-3 py-2 rounded-lg">
                                            <Calendar className="w-4 h-4 text-secondary-600 mr-2" />
                                            <span>{subscription.days.join('/')} {subscription.timeRange}</span>
                                        </div>
                                        <p className="text-xs text-gray-500 px-1">
                                            * 가게마다 실제 픽업 가능 시간이 다를 수 있습니다.<br />
                                            상세 페이지에서 확인해주세요.
                                        </p>
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

                                        <Button
                                            variant="secondary"
                                            fullWidth
                                            disabled={!subscription.isJoinable}
                                        >
                                            {subscription.isJoinable ? '구독하기' : '구독 마감'}
                                        </Button>
                                    </div>
                                </div>
                            </Card>
                        </Link>
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
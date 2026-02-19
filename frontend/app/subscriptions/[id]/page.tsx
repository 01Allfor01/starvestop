'use client';

import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, Calendar, MapPin, Clock, Loader2, Store, Users } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { subscriptionsApi } from '@/lib/api/subscriptions';
import { storesApi } from '@/lib/api/stores';

// ─── GPS 위치 훅 ─────────────────────────────────────────────────────────────
function useGeolocation() {
    const [location, setLocation] = useState<{ lat: number; lng: number } | null>(null);

    useEffect(() => {
        if (!navigator.geolocation) return;

        navigator.geolocation.getCurrentPosition(
            (pos) => {
                setLocation({ lat: pos.coords.latitude, lng: pos.coords.longitude });
            },
            (err) => {
                console.warn('📍 위치 권한 거부:', err.message);
            }
        );
    }, []);

    return location;
}

// ─── 거리 계산 함수 (Haversine formula) ────────────────────────────────────
function calculateDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {
    const R = 6371;
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
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

const mealTimeRange: Record<string, string> = {
    'BREAKFAST': '07:00-12:00',
    'LUNCH': '12:00-17:00',
    'DINNER': '17:00-22:00'
};

export default function SubscriptionDetailPage() {
    const params = useParams();
    const router = useRouter();
    const subscriptionId = Number(params.id);

    const location = useGeolocation();

    const [subscription, setSubscription] = useState<any>(null);
    const [storeInfo, setStoreInfo] = useState<any>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);

                // 구독 정보 가져오기
                const subData = await subscriptionsApi.getSubscription(subscriptionId);

                // 매장 정보 가져오기
                const storeData = await storesApi.getStore(subData.storeId);

                // ✅ 거리 계산
                let distance = null;
                if (location && subData.location && subData.location.coordinates) {
                    distance = calculateDistance(
                        location.lat,
                        location.lng,
                        subData.location.coordinates[1],  // 위도
                        subData.location.coordinates[0]   // 경도
                    );
                }

                // 요일/시간 변환
                const days = subData.dayList?.map((d: string) => dayMap[d] || d) || [];
                const mealTimeKey = subData.mealTimeList?.[0] || 'LUNCH';
                const mealTime = mealTimeMap[mealTimeKey] || '점심';
                const timeRange = mealTimeRange[mealTimeKey] || '12:00-17:00';

                setSubscription({
                    ...subData,
                    days,
                    mealTime,
                    timeRange,
                    pickupSchedule: `${days.join('.')} ${mealTime}`,
                    image: storeData.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
                    distance,
                });

                setStoreInfo(storeData);
            } catch (error) {
                console.error('❌ 구독 상세 정보 로딩 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [subscriptionId, location]);

    const handleSubscribe = async () => {
        try {
            await subscriptionsApi.subscribe(subscriptionId);
            alert('구독 신청이 완료되었습니다!');
            router.push('/mypage/subscriptions');
        } catch (error) {
            console.error('❌ 구독 신청 실패:', error);
            alert('구독 신청에 실패했습니다. 다시 시도해주세요.');
        }
    };

    const handleContactStore = () => {
        alert('가게 문의 기능은 준비 중입니다.');
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-secondary-50 to-secondary-100 flex items-center justify-center">
                <Loader2 className="w-12 h-12 text-secondary-500 animate-spin" />
            </div>
        );
    }

    if (!subscription) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-secondary-50 to-secondary-100 flex items-center justify-center">
                <div className="text-center text-gray-500">
                    구독 정보를 찾을 수 없습니다
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-secondary-50 to-secondary-100 py-8">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href="/subscriptions" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>구독 목록으로</span>
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">{subscription.name}</h1>
                    <p className="text-gray-600">{subscription.storeName}</p>
                </div>

                {/* 구독 정보 카드 */}
                <Card className="border-2 border-secondary-200 mb-6">
                    <div className="flex flex-col md:flex-row gap-6">
                        <img
                            src={subscription.image}
                            alt={subscription.name}
                            className="w-full md:w-64 h-64 object-cover rounded-xl flex-shrink-0"
                        />
                        <div className="flex-1 flex flex-col justify-between">
                            <div>
                                {/* ✅ Badge와 거리를 가로로 배치 */}
                                <div className="flex items-center justify-between mb-3">
                                    <Badge variant="subscription">
                                        {subscription.pickupSchedule}
                                    </Badge>
                                    {subscription.distance !== null && (
                                        <div className="flex items-center text-sm text-gray-600">
                                            <MapPin className="w-4 h-4 mr-1 text-secondary-600" />
                                            <span className="font-medium">
                                                {subscription.distance < 1
                                                    ? `${Math.round(subscription.distance * 1000)}m`
                                                    : `${subscription.distance.toFixed(1)}km`}
                                            </span>
                                        </div>
                                    )}
                                </div>

                                <p className="text-gray-700 mb-4 whitespace-pre-wrap">
                                    {subscription.description}
                                </p>
                            </div>

                            {/* 가격을 하단에 배치 */}
                            <div className="flex items-baseline mt-auto">
                                <span className="text-4xl font-bold text-secondary-600">
                                    {subscription.price.toLocaleString()}원
                                </span>
                                <span className="text-gray-500 ml-2">/월</span>
                            </div>
                        </div>
                    </div>
                </Card>

                {/* 매장 정보 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4 flex items-center">
                        <Store className="w-5 h-5 mr-2 text-secondary-600" />
                        매장 정보
                    </h2>
                    <div className="space-y-3">
                        <div className="flex items-start">
                            <span className="text-sm text-gray-500 w-24 flex-shrink-0">매장명</span>
                            <span className="font-medium text-gray-900">{storeInfo?.name}</span>
                        </div>
                        <div className="flex items-start">
                            <span className="text-sm text-gray-500 w-24 flex-shrink-0">주소</span>
                            <span className="text-gray-900">{storeInfo?.address}</span>
                        </div>
                        {storeInfo?.openTime && storeInfo?.closeTime && (
                            <div className="flex items-start">
                                <span className="text-sm text-gray-500 w-24 flex-shrink-0">영업 시간</span>
                                <span className="text-gray-900">
                                    {storeInfo.openTime.slice(0, 5)} - {storeInfo.closeTime.slice(0, 5)}
                                </span>
                            </div>
                        )}
                    </div>
                </Card>

                {/* 픽업 정보 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4 flex items-center">
                        <Calendar className="w-5 h-5 mr-2 text-secondary-600" />
                        픽업 정보
                    </h2>
                    <div className="space-y-3">
                        <div className="flex items-center justify-between bg-secondary-50 px-4 py-3 rounded-lg">
                            <div>
                                <p className="text-sm text-gray-500 mb-1">픽업 요일</p>
                                <p className="font-medium text-gray-900">{subscription.days.join(', ')}</p>
                            </div>
                        </div>
                        <div className="flex items-center justify-between bg-secondary-50 px-4 py-3 rounded-lg">
                            <div>
                                <p className="text-sm text-gray-500 mb-1">픽업 시간</p>
                                <p className="font-medium text-gray-900">{subscription.mealTime}</p>
                            </div>
                            <Clock className="w-5 h-5 text-secondary-600" />
                        </div>
                        <p className="text-xs text-gray-500 px-1 mt-2">
                            * 가게 사정에 따라 픽업 시간이 변경될 수 있습니다.
                        </p>
                    </div>
                </Card>

                {/* 구독 관리 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">구독 관련 문의</h2>
                    <Button variant="outline" fullWidth onClick={handleContactStore}>
                        <Store className="w-5 h-5 mr-2" />
                        가게에 문의하기
                    </Button>
                </Card>

                {/* 구독 가능 인원 */}
                {subscription.stock > 0 && (
                    <div className="text-center mb-4">
                        <p className="text-sm text-gray-600 flex items-center justify-center">
                            <Users className="w-4 h-4 mr-1 text-secondary-600" />
                            구독 가능 인원 <span className="font-semibold text-secondary-600 mx-1">{subscription.stock}명</span> 남았어요
                        </p>
                    </div>
                )}

                {/* 구독하기 버튼 */}
                <Link href={`/billing/register?subscriptionId=${subscription.id}`}>
                <Button
                    variant="secondary"
                    size="lg"
                    fullWidth
                    onClick={handleSubscribe}
                    disabled={!subscription.joinable || subscription.stock === 0}
                    className="mb-4"
                >
                    {subscription.joinable && subscription.stock > 0 ? '구독하기' : '구독 마감'}
                </Button>
                </Link>

                {/* 주의사항 */}
                <Card className="bg-gray-50 border-gray-200">
                    <h3 className="font-semibold text-gray-900 mb-3">주의사항</h3>
                    <ul className="space-y-2 text-sm text-gray-600">
                        <li className="flex items-start">
                            <span className="mr-2">•</span>
                            <span>구독 기간 중 중도 해지 시, 정상가 기준으로 재계산되어 차액이 환불됩니다.</span>
                        </li>
                        <li className="flex items-start">
                            <span className="mr-2">•</span>
                            <span>픽업 시간 및 장소는 매장 사정에 따라 변경될 수 있습니다.</span>
                        </li>
                        <li className="flex items-start">
                            <span className="mr-2">•</span>
                            <span>구독 신청 후 영업일 기준 1일 이내 승인됩니다.</span>
                        </li>
                    </ul>
                </Card>
            </div>
        </div>
    );
}
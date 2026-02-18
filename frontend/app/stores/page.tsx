'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { MapPin, Search, Loader2 } from 'lucide-react';
import Card from '@/components/ui/Card';
import { storesApi } from '@/lib/api/stores';

// ─── GPS 위치 훅 ─────────────────────────────────────────────────────────────
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

// ─── 카테고리 매핑 ────────────────────────────────────────────────────────────
const categoryMap: Record<string, string> = {
    'KOREAN_FOOD': '한식',
    'JAPANESE_FOOD': '일식',
    'CHINESE_FOOD': '중식',
    'WESTERN_FOOD': '양식',
    'ASIAN_FOOD': '아시안',
    'FAST_FOOD': '패스트푸드',
    'CAFE': '카페',
    'DESSERT': '디저트'
};

const categories = [
    { value: 'all', label: '전체' },
    { value: 'KOREAN_FOOD', label: '한식' },
    { value: 'JAPANESE_FOOD', label: '일식' },
    { value: 'CHINESE_FOOD', label: '중식' },
    { value: 'WESTERN_FOOD', label: '양식' },
    { value: 'ASIAN_FOOD', label: '아시안' },
    { value: 'FAST_FOOD', label: '패스트푸드' },
    { value: 'CAFE', label: '카페' },
    { value: 'DESSERT', label: '디저트' }
];

export default function StoresPage() {
    const [searchQuery, setSearchQuery] = useState('');
    const [selectedCategory, setSelectedCategory] = useState('all');
    const [stores, setStores] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    const { location, denied, gpsLoading } = useGeolocation();

    useEffect(() => {
        if (gpsLoading || denied || !location) return;

        fetchStores();
    }, [location, gpsLoading, denied, selectedCategory]);

    const fetchStores = async () => {
        if (!location) return;

        try {
            setLoading(true);

            const params: any = {
                nowLatitude: location.lat,
                nowLongitude: location.lng,
                size: 100,
            };

            if (searchQuery.trim()) {
                params.keyword = searchQuery.trim();
            }

            if (selectedCategory !== 'all') {
                params.category = selectedCategory;
            }

            const data = await storesApi.getStores(params);

            console.log('📦 매장 데이터:', data);

            const mappedStores = data.map((item: any) => ({
                id: item.id,
                name: item.name,
                category: categoryMap[item.category] || item.category,
                distance: item.distance ?? 0,
                image: item.imageUrl || 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4',
            }));

            setStores(mappedStores);
        } catch (error) {
            console.error('❌ 매장 로딩 실패:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = (e: React.FormEvent) => {
        e.preventDefault();
        fetchStores();
    };

    if (denied) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <Card className="max-w-md text-center">
                    <MapPin className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                    <h2 className="text-xl font-bold text-gray-900 mb-2">위치 권한이 필요합니다</h2>
                    <p className="text-gray-600 mb-4">
                        내 근처 가게를 보려면 위치 권한을 허용해주세요.
                    </p>
                    <p className="text-sm text-gray-500">
                        브라우저 설정에서 위치 권한을 허용한 후 페이지를 새로고침해주세요.
                    </p>
                </Card>
            </div>
        );
    }

    if (gpsLoading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <Loader2 className="w-12 h-12 text-primary-500 animate-spin mx-auto mb-4" />
                    <p className="text-gray-600">위치 정보를 가져오는 중...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">내 근처 가게</h1>
                    <p className="text-gray-600">
                        총 <span className="text-primary-600 font-semibold">{stores.length}개</span>의 가게
                    </p>
                </div>

                {/* 검색 */}
                <Card className="mb-6">
                    <form onSubmit={handleSearch}>
                        <div className="flex gap-3">
                            <div className="relative flex-1">
                                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                                <input
                                    type="text"
                                    placeholder="가게를 검색하세요"
                                    value={searchQuery}
                                    onChange={(e) => setSearchQuery(e.target.value)}
                                    className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                                />
                            </div>
                            <button
                                type="submit"
                                className="px-6 py-3 bg-primary-500 text-white rounded-lg hover:bg-primary-600 transition"
                            >
                                검색
                            </button>
                        </div>
                    </form>
                </Card>

                {/* 카테고리 필터 */}
                <div className="flex items-center space-x-2 overflow-x-auto pb-2 mb-6">
                    {categories.map((category) => (
                        <button
                            key={category.value}
                            onClick={() => setSelectedCategory(category.value)}
                            className={`px-4 py-2 rounded-lg whitespace-nowrap transition ${
                                selectedCategory === category.value
                                    ? 'bg-primary-500 text-white'
                                    : 'bg-white text-gray-700 hover:bg-gray-100'
                            }`}
                        >
                            {category.label}
                        </button>
                    ))}
                </div>

                {/* 로딩 상태 */}
                {loading && (
                    <div className="flex justify-center items-center py-20">
                        <Loader2 className="w-10 h-10 text-primary-500 animate-spin" />
                    </div>
                )}

                {/* 가게 목록 */}
                {!loading && stores.length > 0 && (
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
                )}

                {/* 결과 없음 */}
                {!loading && stores.length === 0 && (
                    <Card className="text-center py-16">
                        <Search className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600 mb-2">가게가 없습니다</p>
                        <p className="text-sm text-gray-500">다른 카테고리나 검색어로 시도해보세요</p>
                    </Card>
                )}
            </div>
        </div>
    );
}
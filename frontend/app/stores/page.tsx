'use client';

export const dynamic = 'force-dynamic';

import { useState, useEffect, Suspense } from 'react'; // Suspense 추가
import { useSearchParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { Search, MapPin, Loader2, X } from 'lucide-react';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { storesApi } from '@/lib/api/stores';

// 카테고리 매핑
const categoryMap: Record<string, string> = {
    '전체': '',
    '한식': 'KOREAN_FOOD',
    '일식': 'JAPANESE_FOOD',
    '중식': 'CHINESE_FOOD',
    '양식': 'WESTERN_FOOD',
    '아시안': 'ASIAN_FOOD',
    '패스트푸드': 'FAST_FOOD',
    '카페': 'CAFE',
    '디저트': 'DESSERT',
};

// 역방향 매핑 (KOREAN_FOOD -> 한식)
const reverseCategoryMap: Record<string, string> = {
    'KOREAN_FOOD': '한식',
    'JAPANESE_FOOD': '일식',
    'CHINESE_FOOD': '중식',
    'WESTERN_FOOD': '양식',
    'ASIAN_FOOD': '아시안',
    'FAST_FOOD': '패스트푸드',
    'CAFE': '카페',
    'DESSERT': '디저트',
};

// 거리 포맷 함수
function formatDistance(distanceKm: number): string {
    if (distanceKm < 1) {
        return `${Math.round(distanceKm * 1000)}m`;
    }
    return `${distanceKm.toFixed(1)}km`;
}

// 1. 기존 로직을 별도의 컨텐츠 컴포넌트로 분리
function StoresContent() {
    const router = useRouter();
    const searchParams = useSearchParams();

    // ✅ URL의 category 파라미터를 읽어서 초기값 설정
    const categoryFromUrl = searchParams.get('category');
    const initialCategory = categoryFromUrl
        ? (reverseCategoryMap[categoryFromUrl] || '전체')
        : '전체';

    const [stores, setStores] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const [selectedCategory, setSelectedCategory] = useState(initialCategory);
    const [searchKeyword, setSearchKeyword] = useState('');
    const [myLocation, setMyLocation] = useState<{ lat: number; lng: number } | null>(null);

    // ✅ URL 파라미터가 변경되면 selectedCategory 업데이트
    useEffect(() => {
        if (categoryFromUrl) {
            const koreanCategory = reverseCategoryMap[categoryFromUrl] || '전체';
            setSelectedCategory(koreanCategory);
        } else {
            setSelectedCategory('전체');
        }
    }, [categoryFromUrl]);

    // GPS 위치 가져오기
    useEffect(() => {
        if (!navigator.geolocation) return;

        navigator.geolocation.getCurrentPosition(
            (pos) => {
                setMyLocation({ lat: pos.coords.latitude, lng: pos.coords.longitude });
            },
            (err) => {
                console.warn('📍 위치 권한 거부:', err.message);
            }
        );
    }, []);

    // 매장 목록 조회
    useEffect(() => {
        if (!myLocation) return;

        const fetchStores = async () => {
            try {
                setLoading(true);
                const categoryParam = categoryMap[selectedCategory];

                const data = await storesApi.getStores({
                    nowLatitude: myLocation.lat,
                    nowLongitude: myLocation.lng,
                    keyword: searchKeyword || undefined,
                    category: categoryParam || undefined,
                    size: 50,
                });

                setStores(data);
            } catch (error) {
                console.error('❌ 매장 목록 로딩 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchStores();
    }, [myLocation, selectedCategory, searchKeyword]);

    // ✅ 카테고리 변경 핸들러 (URL도 업데이트)
    const handleCategoryChange = (category: string) => {
        setSelectedCategory(category);

        // URL 업데이트
        if (category === '전체') {
            router.push('/stores');
        } else {
            const englishCategory = categoryMap[category];
            router.push(`/stores?category=${englishCategory}`);
        }
    };

    // 검색어 초기화
    const handleClearSearch = () => {
        setSearchKeyword('');
    };

    if (!myLocation) {
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
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">가게 둘러보기</h1>
                    <p className="text-gray-600">
                        총 <span className="text-primary-600 font-semibold">{stores.length}개</span>의 가게
                    </p>
                </div>

                {/* 검색 */}
                <div className="mb-6">
                    <div className="relative">
                        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                        <input
                            type="text"
                            placeholder="가게 이름으로 검색"
                            value={searchKeyword}
                            onChange={(e) => setSearchKeyword(e.target.value)}
                            className="w-full pl-10 pr-10 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                        />
                        {searchKeyword && (
                            <button
                                onClick={handleClearSearch}
                                className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600"
                            >
                                <X className="w-5 h-5" />
                            </button>
                        )}
                    </div>
                </div>

                {/* 카테고리 탭 */}
                <div className="mb-6 flex overflow-x-auto space-x-2 pb-2">
                    {Object.keys(categoryMap).map((category) => (
                        <button
                            key={category}
                            onClick={() => handleCategoryChange(category)}
                            className={`px-4 py-2 rounded-lg font-medium whitespace-nowrap transition-colors ${
                                selectedCategory === category
                                    ? 'bg-primary-500 text-white'
                                    : 'bg-white text-gray-700 hover:bg-gray-100 border border-gray-300'
                            }`}
                        >
                            {category}
                        </button>
                    ))}
                </div>

                {/* 매장 목록 */}
                {loading ? (
                    <div className="flex justify-center items-center py-20">
                        <Loader2 className="w-10 h-10 text-primary-500 animate-spin" />
                    </div>
                ) : stores.length === 0 ? (
                    <div className="text-center py-20">
                        <p className="text-gray-500 mb-4">검색 결과가 없습니다</p>
                        <button
                            onClick={() => {
                                setSearchKeyword('');
                                handleCategoryChange('전체');
                            }}
                            className="text-primary-500 hover:text-primary-600 font-medium"
                        >
                            전체 가게 보기
                        </button>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {stores.map((store) => (
                            <Link key={store.id} href={`/stores/${store.id}`}>
                                <Card hover padding="none" className="overflow-hidden h-full">
                                    <div className="relative">
                                        <img
                                            src={store.imageUrl || 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4'}
                                            alt={store.name}
                                            className="w-full h-48 object-cover"
                                        />
                                        <Badge variant="default" className="absolute top-3 left-3">
                                            {reverseCategoryMap[store.category] || store.category}
                                        </Badge>
                                    </div>
                                    <div className="p-4">
                                        <h3 className="font-semibold text-gray-900 mb-2 line-clamp-1">
                                            {store.name}
                                        </h3>
                                        <p className="text-sm text-gray-600 mb-3 line-clamp-2">
                                            {store.description || '맛있는 음식을 제공하는 가게입니다'}
                                        </p>
                                        <div className="flex items-center text-sm text-gray-600">
                                            <MapPin className="w-4 h-4 mr-1 text-primary-500" />
                                            <span className="font-medium text-primary-600">
                                                {store.distance !== undefined && store.distance !== null
                                                    ? formatDistance(store.distance)
                                                    : '-'}
                                            </span>
                                        </div>
                                    </div>
                                </Card>
                            </Link>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}

// 2. 기본 export 페이지에서 Suspense로 감싸기
export default function StoresPage() {
    return (
        <Suspense fallback={
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <Loader2 className="w-12 h-12 text-primary-500 animate-spin" />
            </div>
        }>
            <StoresContent />
        </Suspense>
    );
}
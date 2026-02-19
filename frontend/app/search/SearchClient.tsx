// app/search/SearchClient.tsx
'use client';

import { useState, useEffect } from 'react';
import { useSearchParams } from 'next/navigation';
import Link from 'next/link';
import { Search, MapPin, Loader2 } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import { storesApi } from '@/lib/api/stores';

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

function formatDistance(distanceKm: number): string {
    if (distanceKm < 1) return `${Math.round(distanceKm * 1000)}m`;
    return `${distanceKm.toFixed(1)}km`;
}

export default function SearchClient() {
    const searchParams = useSearchParams();
    const initialQuery = searchParams.get('q') || '';

    const [searchQuery, setSearchQuery] = useState(initialQuery);
    const [stores, setStores] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);
    const [searched, setSearched] = useState(false);

    const { location, denied, gpsLoading } = useGeolocation();

    // URL 파라미터(q) 변경 시 input 동기화
    useEffect(() => {
        setSearchQuery(initialQuery);
    }, [initialQuery]);

    // URL 파라미터 변경 + GPS 준비되면 자동 검색
    useEffect(() => {
        if (initialQuery && location && !denied) {
            handleSearch();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [initialQuery, location, denied]);

    const handleSearch = async (e?: React.FormEvent) => {
        if (e) e.preventDefault();

        if (!searchQuery.trim()) {
            alert('검색어를 입력하세요');
            return;
        }

        if (denied || !location) {
            alert('위치 권한이 필요합니다');
            return;
        }

        try {
            setLoading(true);
            setSearched(true);

            const data = await storesApi.getStores({
                keyword: searchQuery.trim(),
                nowLatitude: location.lat,
                nowLongitude: location.lng,
                size: 100,
            });

            const mappedStores = data.map((item: any) => ({
                id: item.id,
                name: item.name,
                category: item.category || '음식점',
                distance: item.distance ?? 0,
                image: item.imageUrl || 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4',
            }));

            setStores(mappedStores);
        } catch (error) {
            console.error('❌ 검색 실패:', error);
            alert('검색에 실패했습니다');
        } finally {
            setLoading(false);
        }
    };

    if (denied) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <Card className="max-w-md text-center">
                    <MapPin className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                    <h2 className="text-xl font-bold text-gray-900 mb-2">위치 권한이 필요합니다</h2>
                    <p className="text-gray-600 mb-4">검색을 사용하려면 위치 권한을 허용해주세요.</p>
                    <p className="text-sm text-gray-500">브라우저 설정에서 위치 권한을 허용한 후 페이지를 새로고침해주세요.</p>
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
                            <Button type="submit" disabled={loading || !searchQuery.trim()}>
                                검색
                            </Button>
                        </div>
                    </form>
                </Card>

                {searched && (
                    <div className="mb-6">
                        <h1 className="text-2xl font-bold text-gray-900 mb-2">'{searchQuery}' 검색 결과</h1>
                        <p className="text-gray-600">
                            총 <span className="text-primary-600 font-semibold">{stores.length}개</span>의 가게
                        </p>
                        <p className="text-sm text-gray-500 mt-1">* 5km 이내 매장만 검색됩니다.</p>
                    </div>
                )}

                {loading && (
                    <div className="flex justify-center items-center py-20">
                        <Loader2 className="w-10 h-10 text-primary-500 animate-spin" />
                    </div>
                )}

                {!searched && !loading && (
                    <Card className="text-center py-16">
                        <Search className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600 mb-2">검색어를 입력하세요</p>
                        <p className="text-sm text-gray-500">가게 이름, 음식 종류 등을 검색해보세요</p>
                    </Card>
                )}

                {searched && !loading && stores.length > 0 && (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                        {stores.map((store) => (
                            <Link key={store.id} href={`/stores/${store.id}`}>
                                <Card hover padding="none" className="overflow-hidden h-full">
                                    <div className="relative">
                                        <img src={store.image} alt={store.name} className="w-full h-48 object-cover" />
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

                {searched && !loading && stores.length === 0 && (
                    <Card className="text-center py-16">
                        <Search className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600 mb-2">검색 결과가 없습니다</p>
                        <p className="text-sm text-gray-500">다른 검색어로 시도해보세요</p>
                    </Card>
                )}
            </div>
        </div>
    );
}

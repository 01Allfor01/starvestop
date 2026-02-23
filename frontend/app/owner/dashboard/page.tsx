'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { Store, Plus, MapPin, Clock, Loader2, MessageCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { ownerApi } from '@/lib/api/owner';
import { STORE_CATEGORY_LABELS, STORE_STATUS_LABELS } from '@/lib/helpers/bitmask';
import type { GetStoreForOwnerResponse, PageResponse } from '@/types/owner';

export default function OwnerDashboardPage() {
    const router = useRouter();
    const [stores, setStores] = useState<GetStoreForOwnerResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        fetchStores();
    }, [page]);

    const fetchStores = async () => {
        try {
            setLoading(true);
            const response: PageResponse<GetStoreForOwnerResponse> = await ownerApi.getMyStores(page, 10);
            setStores(response.content);
            setTotalPages(response.totalPages);
        } catch (error: any) {
            console.error('매장 목록 조회 실패:', error);
            if (error.response?.status === 401) {
                alert('로그인이 필요합니다');
                router.push('/owner/login');
            }
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <Loader2 className="w-12 h-12 text-primary-500 animate-spin" />
            </div>
        );
    }

    return (
        <div className="py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 매장 목록 헤더 */}
                <div className="flex items-center justify-between mb-6">
                    <h2 className="text-2xl font-bold text-gray-900">내 매장</h2>
                    <Link href="/owner/stores/new">
                        <Button>
                            <Plus className="w-5 h-5 mr-2" />
                            매장 추가
                        </Button>
                    </Link>
                </div>

                {/* 매장 목록 */}
                {stores.length === 0 ? (
                    <Card className="text-center py-16">
                        <Store className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600 mb-6">등록된 매장이 없습니다</p>
                        <Link href="/owner/stores/new">
                            <Button>
                                <Plus className="w-5 h-5 mr-2" />
                                첫 매장 등록하기
                            </Button>
                        </Link>
                    </Card>
                ) : (
                    <>
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                            {stores.map((store) => (
                                <Link key={store.id} href={`/owner/stores/${store.id}`}>
                                    <Card hover className="h-full">
                                        <div className="relative -m-6 mb-4">
                                            <img
                                                src={store.ImageUrl || 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4'}
                                                alt={store.name}
                                                className="w-full h-48 object-cover rounded-t-xl"
                                            />
                                            <Badge
                                                variant={store.status === 'OPENED' ? 'default' : 'sale'}
                                                className="absolute top-3 left-3"
                                            >
                                                {STORE_STATUS_LABELS[store.status]}
                                            </Badge>
                                            {store.unreadCount > 0 && (
                                                <div className="absolute top-3 right-3 flex items-center bg-red-500 text-white px-2 py-1 rounded-full text-xs font-semibold">
                                                    <MessageCircle className="w-3 h-3 mr-1" />
                                                    {store.unreadCount}
                                                </div>
                                            )}
                                        </div>

                                        <div>
                                            <div className="flex items-center justify-between mb-2">
                                                <h3 className="text-lg font-bold text-gray-900">{store.name}</h3>
                                                <Badge variant={"default" as any} className="text-xs">
                                                    {STORE_CATEGORY_LABELS[store.category]}
                                                </Badge>
                                            </div>

                                            <div className="flex items-start text-sm text-gray-600 mb-2">
                                                <MapPin className="w-4 h-4 mr-1 mt-0.5 flex-shrink-0" />
                                                <span className="line-clamp-1">{store.address}</span>
                                            </div>

                                            <div className="flex items-center text-sm text-gray-600">
                                                <Clock className="w-4 h-4 mr-1" />
                                                <span>
                                                    {store.openTime.slice(0, 5)} - {store.closeTime.slice(0, 5)}
                                                </span>
                                            </div>
                                        </div>
                                    </Card>
                                </Link>
                            ))}
                        </div>

                        {totalPages > 1 && (
                            <div className="flex items-center justify-center mt-8 space-x-2">
                                <Button
                                    variant="outline"
                                    onClick={() => setPage(prev => Math.max(0, prev - 1))}
                                    disabled={page === 0}
                                >
                                    이전
                                </Button>
                                <span className="text-sm text-gray-600">
                                    {page + 1} / {totalPages}
                                </span>
                                <Button
                                    variant="outline"
                                    onClick={() => setPage(prev => Math.min(totalPages - 1, prev + 1))}
                                    disabled={page >= totalPages - 1}
                                >
                                    다음
                                </Button>
                            </div>
                        )}
                    </>
                )}
            </div>
        </div>
    );
}
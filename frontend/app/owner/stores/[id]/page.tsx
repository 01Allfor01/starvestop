'use client';

import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, Edit, Trash2, Plus, Loader2, MapPin, Clock, Package, CalendarCheck } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { storesApi } from '@/lib/api/stores';
import { ownerApi } from '@/lib/api/owner';
import { STORE_CATEGORY_LABELS, STORE_STATUS_LABELS, PRODUCT_STATUS_LABELS, DAY_LABELS, MEAL_TIME_LABELS } from '@/lib/helpers/bitmask';
import type { GetStoreDetailResponse } from '@/lib/api/stores';
import type { GetProductResponse, GetSubscriptionResponse } from '@/types/owner';

export default function StoreDetailPage() {
    const params = useParams();
    const router = useRouter();
    const storeId = Number(params.id);

    const [activeTab, setActiveTab] = useState<'products' | 'subscriptions' | 'info'>('products');
    const [store, setStore] = useState<GetStoreDetailResponse | null>(null);
    const [products, setProducts] = useState<GetProductResponse[]>([]);
    const [subscriptions, setSubscriptions] = useState<GetSubscriptionResponse[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchStoreData();
    }, [storeId]);

    const fetchStoreData = async () => {
        try {
            setLoading(true);

            const storeData = (await storesApi.getStore(storeId)) as any;
            setStore(storeData);

            const productsData = await ownerApi.getStoreProducts(storeId);
            setProducts(productsData.content);

            const subsData = await ownerApi.getStoreSubscriptions(storeId);
            setSubscriptions(subsData);
        } catch (error: any) {
            console.error('매장 정보 로딩 실패:', error);
            alert('매장 정보를 불러올 수 없습니다');
            router.push('/owner/dashboard');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async () => {
        if (!confirm('정말로 이 매장을 삭제하시겠습니까?\n삭제된 매장은 복구할 수 없습니다.')) return;

        try {
            await ownerApi.deleteStore(storeId);
            alert('매장이 삭제되었습니다');
            router.push('/owner/dashboard');
        } catch (error: any) {
            console.error('매장 삭제 실패:', error);
            alert(error.response?.data?.message || '매장 삭제에 실패했습니다');
        }
    };

    const handleDeleteProduct = async (productId: number) => {
        if (!confirm('이 상품을 삭제하시겠습니까?')) return;

        try {
            await ownerApi.deleteProduct(productId);
            alert('상품이 삭제되었습니다');
            fetchStoreData();
        } catch (error: any) {
            console.error('상품 삭제 실패:', error);
            alert(error.response?.data?.message || '상품 삭제에 실패했습니다');
        }
    };

    const handleDeleteSubscription = async (subscriptionId: number) => {
        if (!confirm('이 구독을 삭제하시겠습니까?')) return;

        try {
            await ownerApi.deleteSubscription(subscriptionId);
            alert('구독이 삭제되었습니다');
            fetchStoreData();
        } catch (error: any) {
            console.error('구독 삭제 실패:', error);
            alert(error.response?.data?.message || '구독 삭제에 실패했습니다');
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <Loader2 className="w-12 h-12 text-primary-500 animate-spin" />
            </div>
        );
    }

    if (!store) return null;

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href="/owner/dashboard" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        대시보드로
                    </Link>

                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">{store.name}</h1>
                            <div className="flex items-center space-x-3">
                                <Badge variant={(store.status === 'OPENED' ? 'default' : 'sale') as any}>
                                    {STORE_STATUS_LABELS[store.status as keyof typeof STORE_STATUS_LABELS]}
                                </Badge>
                                <Badge variant={"default" as any} className="border border-gray-300 bg-transparent text-gray-600 text-xs">
                                    {STORE_CATEGORY_LABELS[store.category as keyof typeof STORE_CATEGORY_LABELS]}
                                </Badge>
                            </div>
                        </div>

                        <div className="flex space-x-3">
                            <Link href={`/owner/stores/${storeId}/edit`}>
                                <Button variant="outline">
                                    <Edit className="w-4 h-4 mr-2" />
                                    수정
                                </Button>
                            </Link>
                            <Button variant="danger" onClick={handleDelete}>
                                <Trash2 className="w-4 h-4 mr-2" />
                                삭제
                            </Button>
                        </div>
                    </div>
                </div>

                {/* 탭 */}
                <div className="border-b border-gray-200 mb-6">
                    <div className="flex space-x-8">
                        <button
                            onClick={() => setActiveTab('products')}
                            className={`pb-4 border-b-2 transition ${
                                activeTab === 'products'
                                    ? 'border-primary-500 text-primary-600 font-semibold'
                                    : 'border-transparent text-gray-600 hover:text-gray-900'
                            }`}
                        >
                            <Package className="w-5 h-5 inline mr-2" />
                            상품 ({products.length})
                        </button>
                        <button
                            onClick={() => setActiveTab('subscriptions')}
                            className={`pb-4 border-b-2 transition ${
                                activeTab === 'subscriptions'
                                    ? 'border-primary-500 text-primary-600 font-semibold'
                                    : 'border-transparent text-gray-600 hover:text-gray-900'
                            }`}
                        >
                            <CalendarCheck className="w-5 h-5 inline mr-2" />
                            구독 ({subscriptions.length})
                        </button>
                        <button
                            onClick={() => setActiveTab('info')}
                            className={`pb-4 border-b-2 transition ${
                                activeTab === 'info'
                                    ? 'border-primary-500 text-primary-600 font-semibold'
                                    : 'border-transparent text-gray-600 hover:text-gray-900'
                            }`}
                        >
                            매장 정보
                        </button>
                    </div>
                </div>

                {/* 탭 내용 */}
                {activeTab === 'products' && (
                    <div>
                        <div className="flex justify-end mb-4">
                            <Link href={`/owner/stores/${storeId}/products/new`}>
                                <Button>
                                    <Plus className="w-4 h-4 mr-2" />
                                    상품 추가
                                </Button>
                            </Link>
                        </div>

                        {products.length === 0 ? (
                            <Card className="text-center py-16">
                                <Package className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                                <p className="text-gray-600 mb-6">등록된 상품이 없습니다</p>
                                <Link href={`/owner/stores/${storeId}/products/new`}>
                                    <Button>
                                        <Plus className="w-4 h-4 mr-2" />
                                        상품 추가
                                    </Button>
                                </Link>
                            </Card>
                        ) : (
                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                                {products.map((product) => (
                                    <Card key={product.id}>
                                        <div className="mb-4">
                                            <img
                                                src={product.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c'}
                                                alt={product.name}
                                                className="w-full h-48 object-cover rounded-lg"
                                            />
                                        </div>

                                        <div className="mb-3">
                                            <Badge variant={product.status === 'SALE' ? 'sale' : 'default'} className="mb-2">
                                                {PRODUCT_STATUS_LABELS[product.status]}
                                            </Badge>
                                            <h3 className="font-bold text-gray-900">{product.name}</h3>
                                            <p className="text-sm text-gray-600 mt-1 line-clamp-2">{product.description}</p>
                                        </div>

                                        <div className="mb-4">
                                            {product.status === 'SALE' && (
                                                <p className="text-sm text-gray-400 line-through">
                                                    {product.price.toLocaleString()}원
                                                </p>
                                            )}
                                            <p className="text-xl font-bold text-primary-600">
                                                {product.salePrice.toLocaleString()}원
                                            </p>
                                            <p className="text-sm text-gray-600">재고: {product.stock}개</p>
                                        </div>

                                        <div className="flex space-x-2">
                                            <Link href={`/owner/stores/${storeId}/products/${product.id}/edit`} className="flex-1">
                                                <Button variant="outline" size="sm" fullWidth>
                                                    수정
                                                </Button>
                                            </Link>
                                            <Button
                                                variant="outline"
                                                size="sm"
                                                className="text-red-500 hover:bg-red-50"
                                                onClick={() => handleDeleteProduct(product.id)}
                                            >
                                                삭제
                                            </Button>
                                        </div>
                                    </Card>
                                ))}
                            </div>
                        )}
                    </div>
                )}

                {activeTab === 'subscriptions' && (
                    <div>
                        <div className="flex justify-end mb-4">
                            <Link href={`/owner/stores/${storeId}/subscriptions/new`}>
                                <Button>
                                    <Plus className="w-4 h-4 mr-2" />
                                    구독 추가
                                </Button>
                            </Link>
                        </div>

                        {subscriptions.length === 0 ? (
                            <Card className="text-center py-16">
                                <CalendarCheck className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                                <p className="text-gray-600 mb-6">등록된 구독이 없습니다</p>
                                <Link href={`/owner/stores/${storeId}/subscriptions/new`}>
                                    <Button>
                                        <Plus className="w-4 h-4 mr-2" />
                                        구독 추가
                                    </Button>
                                </Link>
                            </Card>
                        ) : (
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                {subscriptions.map((sub) => (
                                    <Card key={sub.id}>
                                        <div className="mb-3">
                                            <Badge variant={sub.isJoinable ? 'default' : 'sale'} className="mb-2">
                                                {sub.isJoinable ? '가입 가능' : '가입 마감'}
                                            </Badge>
                                            <h3 className="text-lg font-bold text-gray-900">{sub.name}</h3>
                                            <p className="text-sm text-gray-600 mt-1">{sub.description}</p>
                                        </div>

                                        <div className="mb-4 space-y-2">
                                            <p className="text-sm text-gray-700">
                                                <span className="font-medium">요일:</span>{' '}
                                                {sub.dayList.map(d => DAY_LABELS[d]).join(', ')}
                                            </p>
                                            <p className="text-sm text-gray-700">
                                                <span className="font-medium">식사:</span>{' '}
                                                {sub.mealTimeList.map(m => MEAL_TIME_LABELS[m]).join(', ')}
                                            </p>
                                            <p className="text-xl font-bold text-secondary-600">
                                                {sub.price.toLocaleString()}원/월
                                            </p>
                                            <p className="text-sm text-gray-600">재고: {sub.stock}명</p>
                                        </div>

                                        <Button
                                            variant="outline"
                                            size="sm"
                                            fullWidth
                                            className="text-red-500 hover:bg-red-50"
                                            onClick={() => handleDeleteSubscription(sub.id)}
                                        >
                                            삭제
                                        </Button>
                                    </Card>
                                ))}
                            </div>
                        )}
                    </div>
                )}

                {activeTab === 'info' && (
                    <Card>
                        <div className="space-y-6">
                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">주소</h3>
                                <div className="flex items-start">
                                    <MapPin className="w-5 h-5 text-gray-400 mr-2 mt-0.5" />
                                    <p className="text-gray-900">{store.address}</p>
                                </div>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">위치</h3>
                                <p className="text-gray-900">
                                    위도: {store.latitude}, 경도: {store.longitude}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">영업 시간</h3>
                                <div className="flex items-center">
                                    <Clock className="w-5 h-5 text-gray-400 mr-2" />
                                    <p className="text-gray-900">
                                        {store.openTime.slice(0, 5)} - {store.closeTime.slice(0, 5)}
                                    </p>
                                </div>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">매장 설명</h3>
                                <p className="text-gray-900">{store.description}</p>
                            </div>
                        </div>
                    </Card>
                )}
            </div>
        </div>
    );
}
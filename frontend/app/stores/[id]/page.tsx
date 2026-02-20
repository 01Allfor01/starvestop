'use client';

import { useState, useEffect } from 'react';
import { useParams } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, MapPin, Clock, MessageCircle, Loader2, Calendar } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { storesApi } from '@/lib/api/stores';
import { productsApi } from '@/lib/api/products';
import { subscriptionsApi } from '@/lib/api/subscriptions';

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

export default function StoreDetailPage() {
    const params = useParams();
    const storeId = Number(params.id);

    const [activeTab, setActiveTab] = useState<'products' | 'subscriptions'>('products');
    const [store, setStore] = useState<any>(null);
    const [saleProducts, setSaleProducts] = useState<any[]>([]);
    const [normalProducts, setNormalProducts] = useState<any[]>([]);
    const [subscriptions, setSubscriptions] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);

                // 매장 정보
                const storeData = await storesApi.getStore(storeId);

                setStore({
                    ...storeData,
                    categoryKo: categoryMap[storeData.category] || storeData.category,
                });

                // 상품 목록
                const productsData = await productsApi.getStoreProducts(storeId);

                // ✅ SALE 상품과 일반 상품 분리
                const sale = productsData
                    .filter((p: any) => p.status === 'SALE')
                    .map((p: any) => {
                        const isSale = p.status === 'SALE';
                        const displayPrice = isSale ? p.salePrice : p.price;
                        const discount = isSale ? (p.price - p.salePrice) : 0;

                        return {
                            id: p.id,
                            name: p.name,
                            originalPrice: p.price,
                            salePrice: p.salePrice,
                            displayPrice: displayPrice,
                            discount: discount,
                            stock: p.stock,
                            image: p.imageUrl || 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
                            endTime: p.endTime,
                            status: p.status,
                        };
                    });

                const normal = productsData
                    .filter((p: any) => p.status !== 'SALE')
                    .map((p: any) => {
                        const isSale = p.status === 'SALE';
                        const displayPrice = isSale ? p.salePrice : p.price;

                        return {
                            id: p.id,
                            name: p.name,
                            price: p.price,
                            displayPrice: displayPrice,
                            image: p.imageUrl || 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
                            status: p.status,
                        };
                    });

                setSaleProducts(sale);
                setNormalProducts(normal);

                // 정기구독 목록
                const subscriptionsData = await subscriptionsApi.getStoreSubscriptions(storeId);

                const mappedSubs = subscriptionsData.map((item: any) => {
                    const days = item.dayList?.map((d: string) => dayMap[d] || d) || [];
                    const mealTimeKey = item.mealTimeList?.[0] || 'LUNCH';
                    const mealTime = mealTimeMap[mealTimeKey] || '점심';
                    const timeRange = mealTimeRange[mealTimeKey] || '12:00-17:00';

                    return {
                        id: item.id,
                        name: item.name,
                        storeName: item.storeName,
                        description: item.description,
                        price: item.price,
                        image: storeData.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
                        days,
                        mealTime,
                        timeRange,
                        pickupSchedule: `${days.join('.')} ${mealTime}`,
                        isJoinable: item.joinable,
                    };
                });

                setSubscriptions(mappedSubs);
            } catch (error) {
                console.error('❌ 매장 정보 로딩 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [storeId]);

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <Loader2 className="w-12 h-12 text-primary-500 animate-spin" />
            </div>
        );
    }

    if (!store) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="text-center text-gray-500">
                    매장 정보를 찾을 수 없습니다
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 pb-20">
            {/* 헤더 */}
            <div className="sticky top-0 z-10 bg-white border-b border-gray-200">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
                    <Link href="/stores" className="flex items-center text-gray-600 hover:text-gray-900">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>뒤로</span>
                    </Link>
                </div>
            </div>

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 가게 정보 */}
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 mb-8">
                    {/* 이미지 */}
                    <div className="lg:col-span-1">
                        <img
                            src={store.imageUrl || 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4'}
                            alt={store.name}
                            className="w-full aspect-square object-cover rounded-2xl"
                        />
                    </div>

                    {/* 정보 */}
                    <div className="lg:col-span-2">
                        <div className="mb-4">
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">{store.name}</h1>
                            <p className="text-gray-600">{store.categoryKo}</p>
                        </div>

                        {/* 운영 시간 */}
                        <Card padding="sm" className="mb-6">
                            <div className="flex items-center">
                                <Clock className="w-5 h-5 text-gray-400 mr-2" />
                                <span className="font-bold text-gray-900 mr-2">운영 시간</span>
                                <span className="font-medium text-gray-900">
                                    {store.openTime?.slice(0, 5)} ~ {store.closeTime?.slice(0, 5)}
                                </span>
                            </div>
                        </Card>

                        {/* 가게 소개 */}
                        {store.description && (
                            <Card className="mb-6">
                                <h3 className="text-lg font-semibold text-gray-900 mb-2">가게 소개</h3>
                                <p className="text-gray-700 whitespace-pre-wrap">{store.description}</p>
                            </Card>
                        )}

                        {/* 주소 */}
                        <Card className="mb-6">
                            <div className="flex items-start">
                                <MapPin className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                                <div className="flex-1">
                                    <span className="font-bold text-gray-900 mr-2">주소</span>
                                    <span className="text-gray-900">{store.address}</span>
                                </div>
                            </div>
                        </Card>

                        {/* 버튼 */}
                        <div>
                            <Button size="lg" fullWidth>
                                <MessageCircle className="w-5 h-5 mr-2" />
                                가게 문의
                            </Button>
                        </div>
                    </div>
                </div>

                {/* 탭 네비게이션 */}
                <div className="border-b border-gray-200 mb-8">
                    <nav className="flex space-x-8">
                        <button
                            onClick={() => setActiveTab('products')}
                            className={`py-4 px-1 border-b-2 font-semibold transition ${
                                activeTab === 'products'
                                    ? 'border-primary-500 text-primary-600'
                                    : 'border-transparent text-gray-600 hover:text-gray-900'
                            }`}
                        >
                            상품
                        </button>
                        <button
                            onClick={() => setActiveTab('subscriptions')}
                            className={`py-4 px-1 border-b-2 font-semibold transition ${
                                activeTab === 'subscriptions'
                                    ? 'border-primary-500 text-primary-600'
                                    : 'border-transparent text-gray-600 hover:text-gray-900'
                            }`}
                        >
                            정기구독
                        </button>
                    </nav>
                </div>

                {/* 탭 콘텐츠 - 상품 */}
                {activeTab === 'products' && (
                    <div className="space-y-8">
                        {/* 마감 세일 */}
                        {saleProducts.length > 0 && (
                            <div>
                                <h2 className="text-2xl font-bold text-gray-900 mb-4">🔥 마감 세일</h2>
                                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                                    {saleProducts.map((product) => (
                                        <Card key={product.id} hover padding="none" className="overflow-hidden">
                                            <Link href={`/products/${product.id}`}>
                                                <div className="relative">
                                                    <img
                                                        src={product.image}
                                                        alt={product.name}
                                                        className="w-full h-48 object-cover"
                                                    />
                                                    <Badge variant="sale" className="absolute top-3 left-3">
                                                        {product.discount.toLocaleString()}원 할인
                                                    </Badge>
                                                </div>
                                                <div className="p-4">
                                                    <h3 className="font-semibold text-gray-900 mb-2">{product.name}</h3>
                                                    <div className="flex items-baseline space-x-2">
                                                        <span className="text-sm text-gray-400 line-through">
                                                            {product.originalPrice.toLocaleString()}원
                                                        </span>
                                                        <span className="text-xl font-bold text-primary-600">
                                                            {/* ✅ displayPrice 사용 */}
                                                            {product.displayPrice.toLocaleString()}원
                                                        </span>
                                                    </div>
                                                </div>
                                            </Link>
                                        </Card>
                                    ))}
                                </div>
                            </div>
                        )}

                        {/* 일반 상품 */}
                        {normalProducts.length > 0 && (
                            <div>
                                <h2 className="text-2xl font-bold text-gray-900 mb-4">일반 상품</h2>
                                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                                    {normalProducts.map((product) => (
                                        <Card key={product.id} hover padding="none" className="overflow-hidden">
                                            <Link href={`/products/${product.id}`}>
                                                <img
                                                    src={product.image}
                                                    alt={product.name}
                                                    className="w-full h-48 object-cover"
                                                />
                                                <div className="p-4">
                                                    <h3 className="font-semibold text-gray-900 mb-2">{product.name}</h3>
                                                    <span className="text-lg font-bold text-gray-900">
                                                        {/* ✅ displayPrice 사용 */}
                                                        {product.displayPrice.toLocaleString()}원
                                                    </span>
                                                </div>
                                            </Link>
                                        </Card>
                                    ))}
                                </div>
                            </div>
                        )}

                        {/* 상품 없음 */}
                        {saleProducts.length === 0 && normalProducts.length === 0 && (
                            <div className="text-center py-16 text-gray-500">
                                등록된 상품이 없습니다
                            </div>
                        )}
                    </div>
                )}

                {/* 탭 콘텐츠 - 정기구독 */}
                {activeTab === 'subscriptions' && (
                    <div>
                        {subscriptions.length > 0 ? (
                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                                {subscriptions.map((subscription) => (
                                    <Link key={subscription.id} href={`/subscriptions/${subscription.id}`}>
                                        <Card hover className="border-2 border-secondary-200 bg-white h-full">
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

                                            <div className="space-y-4">
                                                <div>
                                                    <h3 className="text-xl font-bold text-gray-900 mb-2">
                                                        {subscription.name}
                                                    </h3>
                                                    <p className="text-gray-600 text-sm mb-3 line-clamp-2">
                                                        {subscription.description}
                                                    </p>
                                                </div>

                                                <div className="space-y-2">
                                                    <div className="flex items-center text-sm text-gray-700 bg-secondary-50 px-3 py-2 rounded-lg">
                                                        <Calendar className="w-4 h-4 text-secondary-600 mr-2" />
                                                        <span>{subscription.days.join('/')} {subscription.timeRange}</span>
                                                    </div>
                                                </div>

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
                        ) : (
                            <div className="text-center py-16 text-gray-500">
                                등록된 정기구독이 없습니다
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}
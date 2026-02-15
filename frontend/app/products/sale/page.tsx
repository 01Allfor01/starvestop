'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { Grid, List, Clock, Loader2 } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { productsApi } from '@/lib/api/products';

// 실시간 카운트다운 컴포넌트
function CountdownTimer({ targetDate }: { targetDate: Date }) {
    const [timeLeft, setTimeLeft] = useState('');
    const [isClosed, setIsClosed] = useState(false);

    useEffect(() => {
        if (!targetDate) return;

        const calculateTime = () => {
            const now = new Date().getTime();
            const end = new Date(targetDate).getTime();
            const distance = end - now;

            if (distance < 0) {
                setTimeLeft('영업종료');
                setIsClosed(true);
                return;
            }

            const hours = Math.floor(distance / (1000 * 60 * 60));
            const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((distance % (1000 * 60)) / 1000);

            setTimeLeft(`${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`);
        };

        calculateTime();
        const timer = setInterval(calculateTime, 1000);

        return () => clearInterval(timer);
    }, [targetDate]);

    return (
        <div className={`absolute bottom-3 left-3 backdrop-blur-sm px-3 py-1.5 rounded-lg flex items-center space-x-1 ${
            isClosed ? 'bg-gray-800/90 text-white' : 'bg-white/90 text-gray-900'
        }`}>
            <Clock className={`w-4 h-4 ${isClosed ? 'text-gray-400' : 'text-red-500'}`} />
            <span className="text-sm font-semibold">{timeLeft}</span>
        </div>
    );
}

export default function SaleProductsPage() {
    const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');
    const [sortBy, setSortBy] = useState('deadline');
    const [products, setProducts] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                setLoading(true);
                const data = await productsApi.getSaleProducts();

                // 🔍 디버깅: 백엔드에서 온 데이터 확인 (F12 콘솔에서 확인 가능)
                if (data.length > 0) {
                    console.log("📦 첫 번째 상품 데이터 확인:", data[0]);
                    console.log("🕒 넘어온 endTime 값:", data[0].endTime);
                }

                // ✅ 시간 파싱 함수 (강화됨)
                const parseEndTime = (timeData: any) => {
                    const now = new Date();

                    // 1. 데이터가 아예 없으면 -> 오늘 밤 23:59:59로 설정 (영업종료 방지)
                    if (!timeData) {
                        return new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59);
                    }

                    let hours = 0;
                    let minutes = 0;
                    let seconds = 0;

                    // 2. 문자열 형식 ("22:00:00" 또는 "22:00")
                    if (typeof timeData === 'string') {
                        const parts = timeData.split(':').map(Number);
                        hours = parts[0];
                        minutes = parts[1];
                        seconds = parts[2] || 0;
                    }
                    // 3. 배열 형식 ([22, 0, 0]) - Jackson 설정에 따라 이렇게 올 수 있음
                    else if (Array.isArray(timeData)) {
                        hours = timeData[0];
                        minutes = timeData[1];
                        seconds = timeData[2] || 0;
                    }

                    // 4. 오늘 날짜에 해당 시간 적용
                    const closingDate = new Date(now.getFullYear(), now.getMonth(), now.getDate(), hours, minutes, seconds);

                    // (옵션) 만약 마감 시간이 새벽 2시(02:00)이고 현재가 오후라면, '내일 새벽'으로 처리해야 함
                    // 하지만 보통 '당일 마감'을 의미하므로 일단 그대로 둡니다.

                    return closingDate;
                };

                const mappedProducts = data.map((item) => ({
                    id: item.id,
                    name: item.name,
                    storeName: item.storeName || '가게명 미상',
                    originalPrice: item.price * 1.3,
                    salePrice: item.salePrice || item.price,
                    discount: (item.price * 1.3) - (item.salePrice || item.price),
                    stock: item.stock || 0,
                    image: item.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
                    category: '마감세일',


                    endTime: parseEndTime(item.endTime),
                }));

                setProducts(mappedProducts);
            } catch (error) {
                console.error("상품 목록 로딩 실패:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, []);

    // 정렬 로직
    const sortedProducts = [...products].sort((a, b) => {
        const now = new Date().getTime();
        const aTime = a.endTime.getTime();
        const bTime = b.endTime.getTime();

        // 영업 종료 여부 확인
        const aIsClosed = aTime < now;
        const bIsClosed = bTime < now;

        // 1순위: 영업 상태 (종료된 건 뒤로)
        if (aIsClosed && !bIsClosed) return 1;
        if (!aIsClosed && bIsClosed) return -1;

        // 2순위: 사용자 선택 정렬 기준
        switch (sortBy) {
            case 'price-low':
                return a.salePrice - b.salePrice;
            case 'price-high':
                return b.salePrice - a.salePrice;
            case 'discount':
                return b.discount - a.discount;
            case 'deadline':
            default:
                // 마감 임박순 (시간이 적게 남은 순서대로)
                return aTime - bTime;
        }
    });

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="flex flex-col items-center">
                    <Loader2 className="w-10 h-10 text-orange-500 animate-spin mb-4" />
                    <p className="text-gray-500">맛있는 상품을 찾고 있어요...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">🔥 마감 세일</h1>
                    <p className="text-gray-600">
                        오늘 마감되는 <span className="text-primary-600 font-semibold">{products.length}개</span>의 특가 상품
                    </p>
                </div>

                {/* 정렬 & 뷰모드 */}
                <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-6">
                    <div></div>

                    <div className="flex items-center space-x-3">
                        <select
                            value={sortBy}
                            onChange={(e) => setSortBy(e.target.value)}
                            className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                        >
                            <option value="deadline">마감 임박순</option>
                            <option value="discount">할인 금액 높은순</option>
                            <option value="price-low">낮은 가격순</option>
                            <option value="price-high">높은 가격순</option>
                        </select>

                        <div className="flex border border-gray-300 rounded-lg overflow-hidden">
                            <button
                                onClick={() => setViewMode('grid')}
                                className={`p-2 ${viewMode === 'grid' ? 'bg-primary-500 text-white' : 'bg-white text-gray-600'}`}
                            >
                                <Grid className="w-5 h-5" />
                            </button>
                            <button
                                onClick={() => setViewMode('list')}
                                className={`p-2 ${viewMode === 'list' ? 'bg-primary-500 text-white' : 'bg-white text-gray-600'}`}
                            >
                                <List className="w-5 h-5" />
                            </button>
                        </div>
                    </div>
                </div>

                {/* 상품 목록 */}
                {products.length === 0 ? (
                    <div className="text-center py-20 bg-white rounded-lg shadow-sm">
                        <p className="text-xl text-gray-500 mb-2">현재 진행 중인 마감 세일 상품이 없습니다. 😭</p>
                        <p className="text-gray-400">나중에 다시 확인해주세요!</p>
                    </div>
                ) : (
                    <>
                        {/* Grid View */}
                        {viewMode === 'grid' && (
                            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                                {sortedProducts.map((product) => (
                                    <Card key={product.id} hover padding="none" className="overflow-hidden h-full flex flex-col">
                                        <Link href={`/products/${product.id}`} className="flex-1">
                                            <div className="relative h-48 bg-gray-200">
                                                <img
                                                    src={product.image}
                                                    alt={product.name}
                                                    className="w-full h-full object-cover"
                                                />
                                                <Badge variant="sale" className="absolute top-3 left-3">
                                                    {Math.round(product.discount).toLocaleString()}원 할인
                                                </Badge>
                                                {/* 타이머 */}
                                                <CountdownTimer targetDate={product.endTime} />
                                            </div>
                                            <div className="p-4">
                                                <p className="text-sm text-gray-500 mb-1">{product.storeName}</p>
                                                <h3 className="font-semibold text-gray-900 mb-2 line-clamp-2">{product.name}</h3>

                                                <div className="flex items-center space-x-1 mb-3">
                                                    <span className="text-xs font-medium text-orange-600 bg-orange-50 px-2 py-0.5 rounded">
                                                        남은 재고 {product.stock}개
                                                    </span>
                                                </div>

                                                <div className="flex items-center justify-between mt-auto">
                                                    <div>
                                                        <span className="text-sm text-gray-400 line-through mr-2">
                                                          {Math.round(product.originalPrice).toLocaleString()}원
                                                        </span>
                                                        <span className="text-xl font-bold text-primary-600">
                                                          {product.salePrice.toLocaleString()}원
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </Link>
                                    </Card>
                                ))}
                            </div>
                        )}

                        {/* List View */}
                        {viewMode === 'list' && (
                            <div className="space-y-4">
                                {sortedProducts.map((product) => (
                                    <Card key={product.id} hover padding="none" className="overflow-hidden">
                                        <Link href={`/products/${product.id}`} className="flex flex-col sm:flex-row">
                                            <div className="relative w-full sm:w-48 h-48 sm:h-auto flex-shrink-0 bg-gray-200">
                                                <img
                                                    src={product.image}
                                                    alt={product.name}
                                                    className="w-full h-full object-cover"
                                                />
                                                <Badge variant="sale" className="absolute top-3 left-3">
                                                    {Math.round(product.discount).toLocaleString()}원 할인
                                                </Badge>
                                                <CountdownTimer targetDate={product.endTime} />
                                            </div>
                                            <div className="flex-1 p-6 flex flex-col justify-between">
                                                <div className="flex justify-between items-start mb-4">
                                                    <div className="flex-1">
                                                        <Badge variant="default" className="mb-2">{product.category}</Badge>
                                                        <h3 className="text-xl font-semibold text-gray-900 mb-2">{product.name}</h3>
                                                        <p className="text-gray-600 mb-3">{product.storeName}</p>
                                                        <div className="flex items-center space-x-4 text-sm text-gray-600">
                                                            <span className="text-orange-600 font-medium">남은 재고 {product.stock}개</span>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div className="flex items-center justify-between">
                                                    <div className="flex items-baseline space-x-3">
                                                        <span className="text-xl text-gray-400 line-through">
                                                          {Math.round(product.originalPrice).toLocaleString()}원
                                                        </span>
                                                        <span className="text-3xl font-bold text-primary-600">
                                                          {product.salePrice.toLocaleString()}원
                                                        </span>
                                                    </div>
                                                    <Button>구매하기</Button>
                                                </div>
                                            </div>
                                        </Link>
                                    </Card>
                                ))}
                            </div>
                        )}
                    </>
                )}
            </div>
        </div>
    );
}
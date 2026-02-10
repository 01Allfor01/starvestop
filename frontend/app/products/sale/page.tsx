'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { Grid, List, Clock, MapPin } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

// 실시간 카운트다운 컴포넌트
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

export default function SaleProductsPage() {
    const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');
    const [sortBy, setSortBy] = useState('deadline');

    // TODO: 나중에 실제 API 데이터로 교체
    const products = [
        {
            id: 1,
            name: '프리미엄 크루아상 3입',
            storeName: '파리바게뜨 강남점',
            originalPrice: 6000,
            salePrice: 3000,
            discount: 3000,
            stock: 5,
            distance: 1.2,
            image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
            category: '베이커리',
            endTime: new Date(Date.now() + 2 * 60 * 60 * 1000), // 2시간 후
        },
        {
            id: 2,
            name: '프리미엄 닭가슴살 샐러드',
            storeName: '샐러디 역삼점',
            originalPrice: 12000,
            salePrice: 7200,
            discount: 4800,
            stock: 3,
            distance: 0.8,
            image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
            category: '샐러드',
            endTime: new Date(Date.now() + 1.5 * 60 * 60 * 1000), // 1.5시간 후
        },
        {
            id: 3,
            name: '스페셜 연어 도시락',
            storeName: '본도시락 선릉점',
            originalPrice: 15000,
            salePrice: 9750,
            discount: 5250,
            stock: 8,
            distance: 1.5,
            image: 'https://images.unsplash.com/photo-1608198399988-841b3c6f76d2',
            category: '도시락',
            endTime: new Date(Date.now() + 3.5 * 60 * 60 * 1000), // 3.5시간 후
        },
        {
            id: 4,
            name: '프리미엄 과일 박스',
            storeName: '과일천국 강남점',
            originalPrice: 20000,
            salePrice: 11000,
            discount: 9000,
            stock: 6,
            distance: 2.1,
            image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff',
            category: '과일',
            endTime: new Date(Date.now() + 4 * 60 * 60 * 1000), // 4시간 후
        },
        {
            id: 5,
            name: '수제 햄버거 세트',
            storeName: '버거킹 역삼점',
            originalPrice: 18000,
            salePrice: 10800,
            discount: 7200,
            stock: 4,
            distance: 1.8,
            image: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd',
            category: '패스트푸드',
            endTime: new Date(Date.now() + 5 * 60 * 60 * 1000), // 5시간 후
        },
        {
            id: 6,
            name: '신선 야채 세트',
            storeName: '마켓컬리 강남점',
            originalPrice: 25000,
            salePrice: 13750,
            discount: 11250,
            stock: 10,
            distance: 2.5,
            image: 'https://images.unsplash.com/photo-1540420773420-3366772f4999',
            category: '채소',
            endTime: new Date(Date.now() + 6 * 60 * 60 * 1000), // 6시간 후
        },
    ];

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

                    {/* 정렬 & 뷰모드 */}
                    <div className="flex items-center space-x-3">
                        {/* 정렬 */}
                        <select
                            value={sortBy}
                            onChange={(e) => setSortBy(e.target.value)}
                            className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                        >
                            <option value="deadline">마감 임박순</option>
                            <option value="discount">할인 금액 높은순</option>
                            <option value="price-low">낮은 가격순</option>
                            <option value="price-high">높은 가격순</option>
                            <option value="distance">가까운 거리순</option>
                        </select>

                        {/* 뷰모드 */}
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

                {/* 상품 목록 - Grid View */}
                {viewMode === 'grid' && (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {products.map((product) => (
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
                                        <CountdownTimer endTime={product.endTime} />
                                    </div>
                                    <div className="p-4">
                                        <p className="text-sm text-gray-500 mb-1">{product.storeName}</p>
                                        <h3 className="font-semibold text-gray-900 mb-2 line-clamp-2">{product.name}</h3>
                                        <div className="flex items-center space-x-1 mb-3">
                                            <MapPin className="w-4 h-4 text-gray-400" />
                                            <span className="text-sm text-gray-600">{product.distance}km</span>
                                            <span className="text-sm text-gray-400">•</span>
                                            <span className="text-sm text-gray-600">재고 {product.stock}개</span>
                                        </div>
                                        <div className="flex items-center justify-between">
                                            <div>
                        <span className="text-sm text-gray-400 line-through mr-2">
                          {product.originalPrice.toLocaleString()}원
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

                {/* 상품 목록 - List View */}
                {viewMode === 'list' && (
                    <div className="space-y-4">
                        {products.map((product) => (
                            <Card key={product.id} hover padding="none" className="overflow-hidden">
                                <Link href={`/products/${product.id}`} className="flex">
                                    <div className="relative w-48 flex-shrink-0">
                                        <img
                                            src={product.image}
                                            alt={product.name}
                                            className="w-full h-full object-cover"
                                        />
                                        <Badge variant="sale" className="absolute top-3 left-3">
                                            {product.discount.toLocaleString()}원 할인
                                        </Badge>
                                        <CountdownTimer endTime={product.endTime} />
                                    </div>
                                    <div className="flex-1 p-6">
                                        <div className="flex justify-between items-start mb-4">
                                            <div className="flex-1">
                                                <Badge variant="default" className="mb-2">{product.category}</Badge>
                                                <h3 className="text-xl font-semibold text-gray-900 mb-2">{product.name}</h3>
                                                <p className="text-gray-600 mb-3">{product.storeName}</p>
                                                <div className="flex items-center space-x-4 text-sm text-gray-600">
                                                    <div className="flex items-center">
                                                        <MapPin className="w-4 h-4 mr-1" />
                                                        {product.distance}km
                                                    </div>
                                                    <span>재고 {product.stock}개</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="flex items-center justify-between">
                                            <div className="flex items-baseline space-x-3">
                        <span className="text-xl text-gray-400 line-through">
                          {product.originalPrice.toLocaleString()}원
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
            </div>
        </div>
    );
}
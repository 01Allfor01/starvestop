'use client';

import { useState } from 'react';
import Link from 'next/link';
import { MapPin, Star, SlidersHorizontal, Search, Clock } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function StoresPage() {
    const [searchQuery, setSearchQuery] = useState('');
    const [selectedCategory, setSelectedCategory] = useState('all');
    const [sortBy, setSortBy] = useState('distance');

    // TODO: 실제 API 데이터로 교체
    const categories = ['all', '베이커리', '샐러드', '도시락', '과일', '채소', '정육'];

    const stores = [
        {
            id: 1,
            name: '파리바게뜨 강남점',
            category: '베이커리',
            rating: 4.8,
            reviewCount: 234,
            distance: 1.2,
            address: '서울특별시 강남구 테헤란로 123',
            image: 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4',
            isOpen: true,
            saleCount: 3,
        },
        {
            id: 2,
            name: '샐러디 역삼점',
            category: '샐러드',
            rating: 4.9,
            reviewCount: 512,
            distance: 0.8,
            address: '서울특별시 강남구 역삼동 456',
            image: 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1',
            isOpen: true,
            saleCount: 2,
        },
        {
            id: 3,
            name: '본도시락 선릉점',
            category: '도시락',
            rating: 4.7,
            reviewCount: 328,
            distance: 1.5,
            address: '서울특별시 강남구 선릉로 789',
            image: 'https://images.unsplash.com/photo-1504674900247-0877df9cc836',
            isOpen: true,
            saleCount: 5,
        },
        {
            id: 4,
            name: '과일천국 강남점',
            category: '과일',
            rating: 4.6,
            reviewCount: 189,
            distance: 2.1,
            address: '서울특별시 강남구 강남대로 321',
            image: 'https://images.unsplash.com/photo-1488459716781-31db52582fe9',
            isOpen: true,
            saleCount: 4,
        },
        {
            id: 5,
            name: '마켓컬리 강남점',
            category: '채소',
            rating: 4.8,
            reviewCount: 445,
            distance: 2.5,
            address: '서울특별시 강남구 논현로 654',
            image: 'https://images.unsplash.com/photo-1542838132-92c53300491e',
            isOpen: false,
            saleCount: 0,
        },
        {
            id: 6,
            name: '정육각 선릉점',
            category: '정육',
            rating: 4.9,
            reviewCount: 276,
            distance: 1.8,
            address: '서울특별시 강남구 선릉로 987',
            image: 'https://images.unsplash.com/photo-1588347818036-74e8880b9c3b',
            isOpen: true,
            saleCount: 1,
        },
    ];

    const filteredStores = stores
        .filter(store =>
            (selectedCategory === 'all' || store.category === selectedCategory) &&
            (searchQuery === '' || store.name.toLowerCase().includes(searchQuery.toLowerCase()))
        )
        .sort((a, b) => {
            switch (sortBy) {
                case 'distance':
                    return a.distance - b.distance;
                case 'rating':
                    return b.rating - a.rating;
                case 'review':
                    return b.reviewCount - a.reviewCount;
                default:
                    return 0;
            }
        });

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">내 근처 가게</h1>
                    <p className="text-gray-600">
                        총 <span className="text-primary-600 font-semibold">{filteredStores.length}개</span>의 가게
                    </p>
                </div>

                {/* 검색 */}
                <Card className="mb-6">
                    <div className="relative">
                        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                        <input
                            type="text"
                            placeholder="가게 이름을 검색하세요"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                        />
                    </div>
                </Card>

                {/* 필터 & 정렬 */}
                <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-6">
                    {/* 카테고리 필터 */}
                    <div className="flex items-center space-x-2 overflow-x-auto pb-2">
                        {categories.map((category) => (
                            <button
                                key={category}
                                onClick={() => setSelectedCategory(category)}
                                className={`px-4 py-2 rounded-lg whitespace-nowrap transition ${
                                    selectedCategory === category
                                        ? 'bg-primary-500 text-white'
                                        : 'bg-white text-gray-700 hover:bg-gray-100'
                                }`}
                            >
                                {category === 'all' ? '전체' : category}
                            </button>
                        ))}
                    </div>

                    {/* 정렬 */}
                    <select
                        value={sortBy}
                        onChange={(e) => setSortBy(e.target.value)}
                        className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                    >
                        <option value="distance">가까운 거리순</option>
                        <option value="rating">별점 높은순</option>
                        <option value="review">리뷰 많은순</option>
                    </select>
                </div>

                {/* 가게 목록 */}
                {filteredStores.length > 0 ? (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {filteredStores.map((store) => (
                            <Card key={store.id} hover padding="none" className="overflow-hidden">
                                <Link href={`/stores/${store.id}`}>
                                    <div className="relative">
                                        <img
                                            src={store.image}
                                            alt={store.name}
                                            className="w-full h-48 object-cover"
                                        />
                                        {store.saleCount > 0 && (
                                            <Badge variant="sale" className="absolute top-3 left-3">
                                                마감세일 {store.saleCount}개
                                            </Badge>
                                        )}
                                        <Badge
                                            variant={store.isOpen ? 'success' : 'default'}
                                            className="absolute top-3 right-3"
                                        >
                                            {store.isOpen ? '영업중' : '영업종료'}
                                        </Badge>
                                    </div>
                                    <div className="p-4">
                                        <div className="flex items-start justify-between mb-2">
                                            <h3 className="font-semibold text-gray-900 text-lg">{store.name}</h3>
                                        </div>
                                        <p className="text-sm text-gray-500 mb-3">{store.category}</p>

                                        <div className="flex items-center space-x-3 text-sm text-gray-600 mb-3">
                                            <div className="flex items-center">
                                                <Star className="w-4 h-4 text-yellow-400 mr-1" />
                                                <span className="font-medium">{store.rating}</span>
                                                <span className="text-gray-400 ml-1">({store.reviewCount})</span>
                                            </div>
                                            <div className="flex items-center">
                                                <MapPin className="w-4 h-4 text-gray-400 mr-1" />
                                                <span>{store.distance}km</span>
                                            </div>
                                        </div>

                                        <p className="text-sm text-gray-500">{store.address}</p>
                                    </div>
                                </Link>
                            </Card>
                        ))}
                    </div>
                ) : (
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
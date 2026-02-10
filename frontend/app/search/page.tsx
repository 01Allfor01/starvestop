'use client';

import { useState, useEffect } from 'react';
import { useSearchParams } from 'next/navigation';
import Link from 'next/link';
import { Search, MapPin, Clock, Star, Filter } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function SearchPage() {
    const searchParams = useSearchParams();
    const query = searchParams.get('q') || '';
    const [searchQuery, setSearchQuery] = useState(query);
    const [filterType, setFilterType] = useState<'all' | 'products' | 'stores'>('all');

    // TODO: 실제 API 데이터로 교체
    const searchResults = {
        products: [
            {
                id: 1,
                name: '프리미엄 크루아상 3입',
                storeName: '파리바게뜨 강남점',
                originalPrice: 6000,
                salePrice: 3000,
                discount: 50,
                image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
                distance: 1.2,
            },
            {
                id: 2,
                name: '프리미엄 닭가슴살 샐러드',
                storeName: '샐러디 역삼점',
                originalPrice: 12000,
                salePrice: 7200,
                discount: 40,
                image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
                distance: 0.8,
            },
        ],
        stores: [
            {
                id: 1,
                name: '파리바게뜨 강남점',
                category: '베이커리',
                rating: 4.8,
                reviewCount: 234,
                distance: 1.2,
                image: 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4',
            },
            {
                id: 2,
                name: '샐러디 역삼점',
                category: '샐러드',
                rating: 4.9,
                reviewCount: 512,
                distance: 0.8,
                image: 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1',
            },
        ],
    };

    const totalResults = searchResults.products.length + searchResults.stores.length;

    const handleSearch = (e: React.FormEvent) => {
        e.preventDefault();
        // TODO: 실제 검색 API 호출
        console.log('검색:', searchQuery);
    };

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 검색바 */}
                <Card className="mb-6">
                    <form onSubmit={handleSearch}>
                        <div className="relative">
                            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                            <input
                                type="text"
                                placeholder="상품, 가게를 검색하세요"
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                            />
                        </div>
                    </form>
                </Card>

                {/* 검색 결과 헤더 */}
                <div className="mb-6">
                    <h1 className="text-2xl font-bold text-gray-900 mb-2">
                        '{query}' 검색 결과
                    </h1>
                    <p className="text-gray-600">
                        총 <span className="text-primary-600 font-semibold">{totalResults}개</span>의 결과
                    </p>
                </div>

                {/* 필터 탭 */}
                <div className="flex space-x-4 mb-6 border-b border-gray-200">
                    <button
                        onClick={() => setFilterType('all')}
                        className={`pb-3 px-1 font-semibold transition ${
                            filterType === 'all'
                                ? 'text-primary-600 border-b-2 border-primary-600'
                                : 'text-gray-600 hover:text-gray-900'
                        }`}
                    >
                        전체 ({totalResults})
                    </button>
                    <button
                        onClick={() => setFilterType('products')}
                        className={`pb-3 px-1 font-semibold transition ${
                            filterType === 'products'
                                ? 'text-primary-600 border-b-2 border-primary-600'
                                : 'text-gray-600 hover:text-gray-900'
                        }`}
                    >
                        상품 ({searchResults.products.length})
                    </button>
                    <button
                        onClick={() => setFilterType('stores')}
                        className={`pb-3 px-1 font-semibold transition ${
                            filterType === 'stores'
                                ? 'text-primary-600 border-b-2 border-primary-600'
                                : 'text-gray-600 hover:text-gray-900'
                        }`}
                    >
                        가게 ({searchResults.stores.length})
                    </button>
                </div>

                {/* 상품 결과 */}
                {(filterType === 'all' || filterType === 'products') && searchResults.products.length > 0 && (
                    <div className="mb-8">
                        <h2 className="text-xl font-bold text-gray-900 mb-4">상품</h2>
                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                            {searchResults.products.map((product) => (
                                <Card key={product.id} hover padding="none" className="overflow-hidden">
                                    <Link href={`/products/${product.id}`}>
                                        <div className="relative">
                                            <img
                                                src={product.image}
                                                alt={product.name}
                                                className="w-full h-48 object-cover"
                                            />
                                            <Badge variant="sale" className="absolute top-3 left-3">
                                                {product.discount}% OFF
                                            </Badge>
                                        </div>
                                        <div className="p-4">
                                            <p className="text-sm text-gray-500 mb-1">{product.storeName}</p>
                                            <h3 className="font-semibold text-gray-900 mb-2">{product.name}</h3>
                                            <div className="flex items-center space-x-1 mb-3">
                                                <MapPin className="w-4 h-4 text-gray-400" />
                                                <span className="text-sm text-gray-600">{product.distance}km</span>
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
                    </div>
                )}

                {/* 가게 결과 */}
                {(filterType === 'all' || filterType === 'stores') && searchResults.stores.length > 0 && (
                    <div>
                        <h2 className="text-xl font-bold text-gray-900 mb-4">가게</h2>
                        <div className="space-y-4">
                            {searchResults.stores.map((store) => (
                                <Card key={store.id} hover padding="none" className="flex overflow-hidden">
                                    <Link href={`/stores/${store.id}`} className="flex w-full">
                                        <img
                                            src={store.image}
                                            alt={store.name}
                                            className="w-32 h-32 object-cover"
                                        />
                                        <div className="p-4 flex-1">
                                            <h3 className="font-semibold text-gray-900 text-lg mb-1">{store.name}</h3>
                                            <p className="text-sm text-gray-500 mb-3">{store.category}</p>
                                            <div className="flex items-center space-x-3 text-sm text-gray-600">
                                                <div className="flex items-center">
                                                    <Star className="w-4 h-4 text-yellow-400 mr-1" />
                                                    <span>{store.rating} ({store.reviewCount})</span>
                                                </div>
                                                <div className="flex items-center">
                                                    <MapPin className="w-4 h-4 text-gray-400 mr-1" />
                                                    <span>{store.distance}km</span>
                                                </div>
                                            </div>
                                        </div>
                                    </Link>
                                </Card>
                            ))}
                        </div>
                    </div>
                )}

                {/* 결과 없음 */}
                {totalResults === 0 && (
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
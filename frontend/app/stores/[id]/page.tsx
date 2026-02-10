'use client';

import { useState } from 'react';
import { useParams } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, MapPin, Clock, MessageCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function StoreDetailPage() {
    const params = useParams();
    const storeId = params.id;

    const [activeTab, setActiveTab] = useState<'products' | 'info'>('products');

    // TODO: 나중에 실제 API 데이터로 교체
    const store = {
        id: storeId,
        name: '파리바게뜨 강남점',
        category: '베이커리',
        address: '서울특별시 강남구 테헤란로 123',
        distance: 1.2,
        openTime: '07:00',
        closeTime: '22:00',
        isOpen: true,
        image: 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4',
        description: '매일 아침 신선하게 구워내는 프리미엄 베이커리입니다. 최상급 재료만을 사용하여 건강하고 맛있는 빵을 만듭니다.',
        breakTime: '15:00 - 16:00',
        holiday: '매주 일요일',
    };

    const saleProducts = [
        {
            id: 1,
            name: '프리미엄 크루아상 3입',
            originalPrice: 6000,
            salePrice: 3000,
            discount: 3000,
            stock: 5,
            image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
            timeLeft: '02:34:12',
        },
        {
            id: 2,
            name: '바게트 2입',
            originalPrice: 5000,
            salePrice: 3500,
            discount: 1500,
            stock: 8,
            image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff',
            timeLeft: '03:15:22',
        },
    ];

    const normalProducts = [
        {
            id: 3,
            name: '소금빵 5입',
            price: 8000,
            image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff',
        },
        {
            id: 4,
            name: '단팥빵 3입',
            price: 4500,
            image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff',
        },
    ];

    return (
        <div className="min-h-screen bg-gray-50 pb-20">
            {/* 헤더 */}
            <div className="sticky top-0 z-10 bg-white border-b border-gray-200">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
                    <Link href="/" className="flex items-center text-gray-600 hover:text-gray-900">
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
                            src={store.image}
                            alt={store.name}
                            className="w-full aspect-square object-cover rounded-2xl"
                        />
                    </div>

                    {/* 정보 */}
                    <div className="lg:col-span-2">
                        <div className="flex items-start justify-between mb-4">
                            <div>
                                <Badge variant={store.isOpen ? 'success' : 'default'} className="mb-2">
                                    {store.isOpen ? '영업중' : '영업종료'}
                                </Badge>
                                <h1 className="text-3xl font-bold text-gray-900 mb-2">{store.name}</h1>
                                <p className="text-gray-600">{store.category}</p>
                            </div>
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
                            {/* 거리 */}
                            <Card padding="sm">
                                <div className="flex items-center">
                                    <MapPin className="w-5 h-5 text-gray-400 mr-2" />
                                    <span className="font-medium text-gray-900">{store.distance}km</span>
                                </div>
                            </Card>

                            {/* 운영 시간 */}
                            <Card padding="sm">
                                <div className="flex items-center">
                                    <Clock className="w-5 h-5 text-gray-400 mr-2" />
                                    <span className="font-bold text-gray-900 mr-2">운영 시간</span>
                                    <span className="font-medium text-gray-900">
            {store.openTime}~{store.closeTime}
        </span>
                                </div>
                            </Card>
                        </div>

                        {/* 주소 */}
                        <Card className="mb-6">
                            <div className="flex items-start">
                                <MapPin className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                                <div className="flex-1">
                                    <p className="text-gray-900 mb-2">{store.address}</p>
                                    <Button variant="outline" size="sm">지도 보기</Button>
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
                            onClick={() => setActiveTab('info')}
                            className={`py-4 px-1 border-b-2 font-semibold transition ${
                                activeTab === 'info'
                                    ? 'border-primary-500 text-primary-600'
                                    : 'border-transparent text-gray-600 hover:text-gray-900'
                            }`}
                        >
                            정보
                        </button>
                    </nav>
                </div>

                {/* 탭 콘텐츠 */}
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
                                                    <div className="absolute bottom-3 left-3 bg-white/90 backdrop-blur-sm px-3 py-1.5 rounded-lg flex items-center space-x-1">
                                                        <Clock className="w-4 h-4 text-red-500" />
                                                        <span className="text-sm font-semibold text-gray-900">{product.timeLeft}</span>
                                                    </div>
                                                </div>
                                                <div className="p-4">
                                                    <h3 className="font-semibold text-gray-900 mb-2">{product.name}</h3>
                                                    <div className="flex items-center justify-between">
                                                        <div>
                              <span className="text-sm text-gray-400 line-through mr-2">
                                {product.originalPrice.toLocaleString()}원
                              </span>
                                                            <span className="text-lg font-bold text-primary-600">
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

                        {/* 일반 상품 */}
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
                          {product.price.toLocaleString()}원
                        </span>
                                            </div>
                                        </Link>
                                    </Card>
                                ))}
                            </div>
                        </div>
                    </div>
                )}

                {activeTab === 'info' && (
                    <div className="space-y-6">
                        <Card>
                            <h3 className="text-lg font-semibold text-gray-900 mb-4">가게 소개</h3>
                            <p className="text-gray-700">{store.description}</p>
                        </Card>

                        <Card>
                            <h3 className="text-lg font-semibold text-gray-900 mb-4">운영 정보</h3>
                            <div className="space-y-3 text-gray-700">
                                <div className="flex">
                                    <span className="w-24 text-gray-500">운영 시간</span>
                                    <span>{store.openTime} - {store.closeTime}</span>
                                </div>
                                <div className="flex">
                                    <span className="w-24 text-gray-500">브레이크타임</span>
                                    <span>{store.breakTime}</span>
                                </div>
                                <div className="flex">
                                    <span className="w-24 text-gray-500">정기휴무</span>
                                    <span>{store.holiday}</span>
                                </div>
                            </div>
                        </Card>
                    </div>
                )}
            </div>
        </div>
    );
}
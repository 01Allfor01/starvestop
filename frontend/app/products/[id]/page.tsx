'use client';

import { useState } from 'react';
import { useParams } from 'next/navigation';
import { ArrowLeft, Heart, MapPin, Clock, Minus, Plus, ShoppingCart, Store, MessageCircle } from 'lucide-react';
import Link from 'next/link';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function ProductDetailPage() {
    const params = useParams();
    const productId = params.id;

    const [quantity, setQuantity] = useState(1);
    const [isFavorite, setIsFavorite] = useState(false);

    // TODO: 나중에 실제 API에서 상품 데이터 가져오기
    const product = {
        id: productId,
        name: '프리미엄 크루아상 3입',
        storeName: '파리바게뜨 강남점',
        storeId: 1,
        originalPrice: 6000,
        salePrice: 3000,
        discount: 3000,
        stock: 5,
        distance: 1.2,
        description: '갓 구운 신선한 크루아상입니다. 바삭한 겉면과 부드러운 속살이 일품입니다.',
        expiryTime: '2:34:12',
        image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
        category: '베이커리',
    };

    const handleQuantityChange = (delta: number) => {
        const newQuantity = quantity + delta;
        if (newQuantity >= 1 && newQuantity <= product.stock) {
            setQuantity(newQuantity);
        }
    };

    const handleAddToCart = () => {
        console.log('장바구니 담기:', { productId, quantity });
        alert('장바구니에 담았습니다!');
    };

    return (
        <div className="min-h-screen bg-gray-50 pb-20">
            {/* 헤더 */}
            <div className="sticky top-0 z-10 bg-white border-b border-gray-200">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex items-center justify-between">
                    <Link href="/" className="flex items-center text-gray-600 hover:text-gray-900">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>뒤로</span>
                    </Link>
                    <button
                        onClick={() => setIsFavorite(!isFavorite)}
                        className="p-2 rounded-full hover:bg-gray-100 transition"
                    >
                        <Heart
                            className={`w-6 h-6 ${isFavorite ? 'fill-red-500 text-red-500' : 'text-gray-600'}`}
                        />
                    </button>
                </div>
            </div>

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    {/* 왼쪽: 이미지 */}
                    <div>
                        {/* 메인 이미지 */}
                        <div className="relative aspect-square rounded-2xl overflow-hidden bg-white">
                            <img
                                src={product.image}
                                alt={product.name}
                                className="w-full h-full object-cover"
                            />
                            {/* 할인 배지 */}
                            <Badge variant="sale" className="absolute top-4 left-4 text-base px-3 py-1">
                                {product.discount.toLocaleString()}원 할인
                            </Badge>
                            {/* 타이머 */}
                            <div className="absolute bottom-4 left-4 bg-white/95 backdrop-blur-sm px-4 py-2 rounded-lg flex items-center space-x-2 shadow-lg">
                                <Clock className="w-5 h-5 text-red-500" />
                                <span className="font-semibold text-gray-900">{product.expiryTime}</span>
                            </div>
                        </div>
                    </div>

                    {/* 오른쪽: 상품 정보 */}
                    <div className="space-y-6">
                        {/* 가게 정보 */}
                        <Link href={`/stores/${product.storeId}`}>
                            <Card hover padding="sm" className="flex items-center space-x-3">
                                <Store className="w-10 h-10 text-primary-500" />
                                <div className="flex-1">
                                    <h3 className="font-semibold text-gray-900">{product.storeName}</h3>
                                    <div className="flex items-center text-sm text-gray-600">
                                        <MapPin className="w-4 h-4 mr-1" />
                                        {product.distance}km
                                    </div>
                                </div>
                            </Card>
                        </Link>

                        {/* 상품명 */}
                        <div>
                            <Badge variant="default" className="mb-2">{product.category}</Badge>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">{product.name}</h1>
                            <p className="text-gray-600">{product.description}</p>
                        </div>

                        {/* 가격 정보 */}
                        <div className="bg-gradient-to-br from-primary-50 to-primary-100 rounded-2xl p-6">
                            <div className="flex items-baseline space-x-3 mb-2">
                <span className="text-2xl text-gray-400 line-through">
                  {product.originalPrice.toLocaleString()}원
                </span>
                                <Badge variant="sale" className="text-lg px-3 py-1">
                                    {product.discount.toLocaleString()}원 할인
                                </Badge>
                            </div>
                            <div className="text-4xl font-bold text-primary-600">
                                {product.salePrice.toLocaleString()}원
                            </div>
                            <p className="text-sm text-gray-600 mt-2">
                                재고: <span className="font-semibold text-primary-600">{product.stock}개</span> 남음
                            </p>
                        </div>

                        {/* 수량 선택 */}
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">수량</label>
                            <div className="flex items-center space-x-4">
                                <div className="flex items-center border border-gray-300 rounded-lg">
                                    <button
                                        onClick={() => handleQuantityChange(-1)}
                                        disabled={quantity <= 1}
                                        className="p-3 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition"
                                    >
                                        <Minus className="w-5 h-5" />
                                    </button>
                                    <span className="px-6 text-lg font-semibold">{quantity}</span>
                                    <button
                                        onClick={() => handleQuantityChange(1)}
                                        disabled={quantity >= product.stock}
                                        className="p-3 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition"
                                    >
                                        <Plus className="w-5 h-5" />
                                    </button>
                                </div>
                                <div className="flex-1 text-right">
                                    <p className="text-sm text-gray-600">총 금액</p>
                                    <p className="text-2xl font-bold text-gray-900">
                                        {(product.salePrice * quantity).toLocaleString()}원
                                    </p>
                                </div>
                            </div>
                        </div>

                        {/* 버튼 */}
                        <div className="flex space-x-3">
                            <Button
                                variant="outline"
                                size="lg"
                                className="flex-1"
                                onClick={handleAddToCart}
                            >
                                <ShoppingCart className="w-5 h-5 mr-2" />
                                장바구니
                            </Button>
                            <Button size="lg" className="flex-1">
                                바로 구매
                            </Button>
                        </div>

                        {/* 가게 문의 */}
                        <Button variant="ghost" fullWidth className="border border-gray-300">
                            <MessageCircle className="w-5 h-5 mr-2" />
                            가게에 문의하기
                        </Button>
                    </div>
                </div>

                {/* 상세 정보 */}
                <div className="mt-12">
                    <div className="border-b border-gray-200">
                        <div className="py-4 px-1 border-b-2 border-primary-500 text-primary-600 font-semibold inline-block">
                            상세정보
                        </div>
                    </div>

                    <div className="py-8">
                        <Card>
                            <h3 className="text-lg font-semibold text-gray-900 mb-4">상품 설명</h3>
                            <div className="prose max-w-none text-gray-600">
                                <p>
                                    {product.description}
                                </p>
                                <p className="mt-4">
                                    매일 아침 신선하게 구워내는 프리미엄 크루아상입니다.
                                    최상급 프랑스산 버터를 사용하여 풍부한 풍미와 바삭한 식감을 자랑합니다.
                                </p>
                                <ul className="mt-4 space-y-2">
                                    <li>✓ 당일 제조</li>
                                    <li>✓ 프랑스산 프리미엄 버터 사용</li>
                                    <li>✓ 인공 첨가물 무첨가</li>
                                    <li>✓ 냉동 보관 가능 (최대 7일)</li>
                                </ul>
                            </div>
                        </Card>
                    </div>
                </div>
            </div>
        </div>
    );
}
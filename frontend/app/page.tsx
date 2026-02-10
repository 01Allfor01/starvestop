'use client';

import { Clock, Heart, MapPin, Star, TrendingUp } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function Home() {
    return (
        <div className="bg-gray-50">
            {/* 히어로 섹션 */}
            <section className="bg-gradient-to-br from-primary-500 to-primary-600 text-white">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
                    <div className="text-center">
                        <h1 className="text-4xl md:text-5xl font-bold mb-4">
                            오늘의 마감 세일 🔥
                        </h1>
                        <p className="text-xl md:text-2xl text-white/90 mb-8">
                            신선한 음식을 더 저렴하게, 지금 바로 확인하세요!
                        </p>
                        <Button variant="secondary" size="lg">
                            마감세일 전체보기
                        </Button>
                    </div>
                </div>
            </section>

            {/* 마감 세일 섹션 */}
            <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
                <div className="flex items-center justify-between mb-8">
                    <div>
                        <h2 className="text-3xl font-bold text-gray-900 mb-2">
                            🔥 마감 세일
                        </h2>
                        <p className="text-gray-600">
                            오늘 마감되는 신선한 상품들을 특가로 만나보세요
                        </p>
                    </div>
                    <Button variant="outline">전체보기</Button>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                    {/* 상품 카드 1 */}
                    <Card hover padding="none" className="overflow-hidden">
                        <div className="relative">
                            <img
                                src="https://images.unsplash.com/photo-1555507036-ab1f4038808a"
                                alt="크루아상"
                                className="w-full h-48 object-cover"
                            />
                            <Badge variant="sale" className="absolute top-3 left-3">
                                50% OFF
                            </Badge>
                            <button className="absolute top-3 right-3 w-8 h-8 bg-white rounded-full flex items-center justify-center shadow-md hover:bg-gray-100 transition">
                                <Heart className="w-5 h-5 text-gray-600" />
                            </button>
                            <div className="absolute bottom-3 left-3 bg-white/90 backdrop-blur-sm px-3 py-1.5 rounded-lg flex items-center space-x-1">
                                <Clock className="w-4 h-4 text-red-500" />
                                <span className="text-sm font-semibold text-gray-900">2:34:12</span>
                            </div>
                        </div>
                        <div className="p-4">
                            <p className="text-sm text-gray-500 mb-1">파리바게뜨 강남점</p>
                            <h3 className="font-semibold text-gray-900 mb-2">프리미엄 크루아상 3입</h3>
                            <div className="flex items-center space-x-1 mb-3">
                                <MapPin className="w-4 h-4 text-gray-400" />
                                <span className="text-sm text-gray-600">1.2km</span>
                                <span className="text-sm text-gray-400">•</span>
                                <span className="text-sm text-gray-600">재고 5개</span>
                            </div>
                            <div className="flex items-center justify-between">
                                <div>
                                    <span className="text-sm text-gray-400 line-through mr-2">6,000원</span>
                                    <span className="text-xl font-bold text-primary-600">3,000원</span>
                                </div>
                            </div>
                        </div>
                    </Card>

                    {/* 상품 카드 2 */}
                    <Card hover padding="none" className="overflow-hidden">
                        <div className="relative">
                            <img
                                src="https://images.unsplash.com/photo-1546069901-ba9599a7e63c"
                                alt="샐러드"
                                className="w-full h-48 object-cover"
                            />
                            <Badge variant="sale" className="absolute top-3 left-3">
                                40% OFF
                            </Badge>
                            <button className="absolute top-3 right-3 w-8 h-8 bg-white rounded-full flex items-center justify-center shadow-md hover:bg-gray-100 transition">
                                <Heart className="w-5 h-5 text-gray-600" />
                            </button>
                            <div className="absolute bottom-3 left-3 bg-white/90 backdrop-blur-sm px-3 py-1.5 rounded-lg flex items-center space-x-1">
                                <Clock className="w-4 h-4 text-red-500" />
                                <span className="text-sm font-semibold text-gray-900">1:15:33</span>
                            </div>
                        </div>
                        <div className="p-4">
                            <p className="text-sm text-gray-500 mb-1">샐러디 역삼점</p>
                            <h3 className="font-semibold text-gray-900 mb-2">프리미엄 닭가슴살 샐러드</h3>
                            <div className="flex items-center space-x-1 mb-3">
                                <MapPin className="w-4 h-4 text-gray-400" />
                                <span className="text-sm text-gray-600">0.8km</span>
                                <span className="text-sm text-gray-400">•</span>
                                <span className="text-sm text-gray-600">재고 3개</span>
                            </div>
                            <div className="flex items-center justify-between">
                                <div>
                                    <span className="text-sm text-gray-400 line-through mr-2">12,000원</span>
                                    <span className="text-xl font-bold text-primary-600">7,200원</span>
                                </div>
                            </div>
                        </div>
                    </Card>

                    {/* 상품 카드 3 */}
                    <Card hover padding="none" className="overflow-hidden">
                        <div className="relative">
                            <img
                                src="https://images.unsplash.com/photo-1608198399988-841b3c6f76d2"
                                alt="도시락"
                                className="w-full h-48 object-cover"
                            />
                            <Badge variant="sale" className="absolute top-3 left-3">
                                35% OFF
                            </Badge>
                            <button className="absolute top-3 right-3 w-8 h-8 bg-white rounded-full flex items-center justify-center shadow-md hover:bg-gray-100 transition">
                                <Heart className="w-5 h-5 text-gray-600" />
                            </button>
                            <div className="absolute bottom-3 left-3 bg-white/90 backdrop-blur-sm px-3 py-1.5 rounded-lg flex items-center space-x-1">
                                <Clock className="w-4 h-4 text-red-500" />
                                <span className="text-sm font-semibold text-gray-900">3:42:08</span>
                            </div>
                        </div>
                        <div className="p-4">
                            <p className="text-sm text-gray-500 mb-1">본도시락 선릉점</p>
                            <h3 className="font-semibold text-gray-900 mb-2">스페셜 연어 도시락</h3>
                            <div className="flex items-center space-x-1 mb-3">
                                <MapPin className="w-4 h-4 text-gray-400" />
                                <span className="text-sm text-gray-600">1.5km</span>
                                <span className="text-sm text-gray-400">•</span>
                                <span className="text-sm text-gray-600">재고 8개</span>
                            </div>
                            <div className="flex items-center justify-between">
                                <div>
                                    <span className="text-sm text-gray-400 line-through mr-2">15,000원</span>
                                    <span className="text-xl font-bold text-primary-600">9,750원</span>
                                </div>
                            </div>
                        </div>
                    </Card>

                    {/* 상품 카드 4 */}
                    <Card hover padding="none" className="overflow-hidden">
                        <div className="relative">
                            <img
                                src="https://images.unsplash.com/photo-1509440159596-0249088772ff"
                                alt="과일"
                                className="w-full h-48 object-cover"
                            />
                            <Badge variant="sale" className="absolute top-3 left-3">
                                45% OFF
                            </Badge>
                            <button className="absolute top-3 right-3 w-8 h-8 bg-white rounded-full flex items-center justify-center shadow-md hover:bg-gray-100 transition">
                                <Heart className="w-5 h-5 text-gray-600" />
                            </button>
                            <div className="absolute bottom-3 left-3 bg-white/90 backdrop-blur-sm px-3 py-1.5 rounded-lg flex items-center space-x-1">
                                <Clock className="w-4 h-4 text-red-500" />
                                <span className="text-sm font-semibold text-gray-900">4:21:45</span>
                            </div>
                        </div>
                        <div className="p-4">
                            <p className="text-sm text-gray-500 mb-1">과일천국 강남점</p>
                            <h3 className="font-semibold text-gray-900 mb-2">프리미엄 과일 박스</h3>
                            <div className="flex items-center space-x-1 mb-3">
                                <MapPin className="w-4 h-4 text-gray-400" />
                                <span className="text-sm text-gray-600">2.1km</span>
                                <span className="text-sm text-gray-400">•</span>
                                <span className="text-sm text-gray-600">재고 6개</span>
                            </div>
                            <div className="flex items-center justify-between">
                                <div>
                                    <span className="text-sm text-gray-400 line-through mr-2">20,000원</span>
                                    <span className="text-xl font-bold text-primary-600">11,000원</span>
                                </div>
                            </div>
                        </div>
                    </Card>
                </div>
            </section>

            {/* 구독 섹션 */}
            <section className="bg-gradient-to-br from-secondary-50 to-secondary-100 py-16">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex items-center justify-between mb-8">
                        <div>
                            <h2 className="text-3xl font-bold text-gray-900 mb-2">
                                💚 정기 구독
                            </h2>
                            <p className="text-gray-600">
                                매주 신선한 식재료를 정기적으로 받아보세요
                            </p>
                        </div>
                        <Button variant="outline">전체보기</Button>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        {/* 구독 카드 1 */}
                        <Card hover className="border-2 border-secondary-200">
                            <Badge variant="subscription" className="mb-4">주 1회 배송</Badge>
                            <h3 className="text-xl font-bold text-gray-900 mb-2">
                                샐러드 정기구독
                            </h3>
                            <p className="text-gray-600 mb-4">
                                신선한 샐러드를 매주 화요일에 받아보세요
                            </p>
                            <div className="flex items-baseline mb-6">
                                <span className="text-3xl font-bold text-secondary-600">28,000원</span>
                                <span className="text-gray-500 ml-2">/주</span>
                            </div>
                            <ul className="space-y-2 mb-6">
                                <li className="flex items-center text-sm text-gray-600">
                                    <TrendingUp className="w-4 h-4 text-secondary-500 mr-2" />
                                    매주 다른 메뉴
                                </li>
                                <li className="flex items-center text-sm text-gray-600">
                                    <TrendingUp className="w-4 h-4 text-secondary-500 mr-2" />
                                    무료 배송
                                </li>
                                <li className="flex items-center text-sm text-gray-600">
                                    <TrendingUp className="w-4 h-4 text-secondary-500 mr-2" />
                                    언제든 해지 가능
                                </li>
                            </ul>
                            <Button fullWidth variant="secondary">구독하기</Button>
                        </Card>

                        {/* 구독 카드 2 */}
                        <Card hover className="border-2 border-secondary-200">
                            <Badge variant="subscription" className="mb-4">월 4회 배송</Badge>
                            <h3 className="text-xl font-bold text-gray-900 mb-2">
                                과일 정기구독
                            </h3>
                            <p className="text-gray-600 mb-4">
                                제철 과일을 매주 목요일에 받아보세요
                            </p>
                            <div className="flex items-baseline mb-6">
                                <span className="text-3xl font-bold text-secondary-600">45,000원</span>
                                <span className="text-gray-500 ml-2">/월</span>
                            </div>
                            <ul className="space-y-2 mb-6">
                                <li className="flex items-center text-sm text-gray-600">
                                    <TrendingUp className="w-4 h-4 text-secondary-500 mr-2" />
                                    제철 과일 선별
                                </li>
                                <li className="flex items-center text-sm text-gray-600">
                                    <TrendingUp className="w-4 h-4 text-secondary-500 mr-2" />
                                    무료 배송
                                </li>
                                <li className="flex items-center text-sm text-gray-600">
                                    <TrendingUp className="w-4 h-4 text-secondary-500 mr-2" />
                                    신선도 보장
                                </li>
                            </ul>
                            <Button fullWidth variant="secondary">구독하기</Button>
                        </Card>

                        {/* 구독 카드 3 */}
                        <Card hover className="border-2 border-secondary-200">
                            <Badge variant="subscription" className="mb-4">월 2회 배송</Badge>
                            <h3 className="text-xl font-bold text-gray-900 mb-2">
                                베이커리 정기구독
                            </h3>
                            <p className="text-gray-600 mb-4">
                                갓 구운 빵을 격주로 받아보세요
                            </p>
                            <div className="flex items-baseline mb-6">
                                <span className="text-3xl font-bold text-secondary-600">32,000원</span>
                                <span className="text-gray-500 ml-2">/월</span>
                            </div>
                            <ul className="space-y-2 mb-6">
                                <li className="flex items-center text-sm text-gray-600">
                                    <TrendingUp className="w-4 h-4 text-secondary-500 mr-2" />
                                    당일 아침 제조
                                </li>
                                <li className="flex items-center text-sm text-gray-600">
                                    <TrendingUp className="w-4 h-4 text-secondary-500 mr-2" />
                                    무료 배송
                                </li>
                                <li className="flex items-center text-sm text-gray-600">
                                    <TrendingUp className="w-4 h-4 text-secondary-500 mr-2" />
                                    빵 종류 선택 가능
                                </li>
                            </ul>
                            <Button fullWidth variant="secondary">구독하기</Button>
                        </Card>
                    </div>
                </div>
            </section>

            {/* 근처 가게 섹션 */}
            <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
                <div className="flex items-center justify-between mb-8">
                    <div>
                        <h2 className="text-3xl font-bold text-gray-900 mb-2">
                            📍 내 근처 가게
                        </h2>
                        <p className="text-gray-600">
                            가까운 곳에서 신선한 음식을 빠르게 받아보세요
                        </p>
                    </div>
                    <Button variant="outline">전체보기</Button>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {/* 가게 카드 1 */}
                    <Card hover padding="none" className="flex overflow-hidden">
                        <img
                            src="https://images.unsplash.com/photo-1517248135467-4c7edcad34c4"
                            alt="파리바게뜨"
                            className="w-32 h-32 object-cover"
                        />
                        <div className="p-4 flex-1">
                            <div className="flex items-start justify-between mb-2">
                                <h3 className="font-semibold text-gray-900">파리바게뜨 강남점</h3>
                                <Badge variant="success">영업중</Badge>
                            </div>
                            <div className="flex items-center space-x-3 text-sm text-gray-600 mb-2">
                                <div className="flex items-center">
                                    <MapPin className="w-4 h-4 mr-1" />
                                    1.2km
                                </div>
                                <div className="flex items-center">
                                    <Star className="w-4 h-4 text-yellow-400 mr-1" />
                                    4.8 (234)
                                </div>
                            </div>
                            <p className="text-sm text-gray-500">베이커리 • 디저트</p>
                        </div>
                    </Card>

                    {/* 가게 카드 2 */}
                    <Card hover padding="none" className="flex overflow-hidden">
                        <img
                            src="https://images.unsplash.com/photo-1555939594-58d7cb561ad1"
                            alt="샐러디"
                            className="w-32 h-32 object-cover"
                        />
                        <div className="p-4 flex-1">
                            <div className="flex items-start justify-between mb-2">
                                <h3 className="font-semibold text-gray-900">샐러디 역삼점</h3>
                                <Badge variant="success">영업중</Badge>
                            </div>
                            <div className="flex items-center space-x-3 text-sm text-gray-600 mb-2">
                                <div className="flex items-center">
                                    <MapPin className="w-4 h-4 mr-1" />
                                    0.8km
                                </div>
                                <div className="flex items-center">
                                    <Star className="w-4 h-4 text-yellow-400 mr-1" />
                                    4.9 (512)
                                </div>
                            </div>
                            <p className="text-sm text-gray-500">샐러드 • 건강식</p>
                        </div>
                    </Card>

                    {/* 가게 카드 3 */}
                    <Card hover padding="none" className="flex overflow-hidden">
                        <img
                            src="https://images.unsplash.com/photo-1504674900247-0877df9cc836"
                            alt="본도시락"
                            className="w-32 h-32 object-cover"
                        />
                        <div className="p-4 flex-1">
                            <div className="flex items-start justify-between mb-2">
                                <h3 className="font-semibold text-gray-900">본도시락 선릉점</h3>
                                <Badge variant="success">영업중</Badge>
                            </div>
                            <div className="flex items-center space-x-3 text-sm text-gray-600 mb-2">
                                <div className="flex items-center">
                                    <MapPin className="w-4 h-4 mr-1" />
                                    1.5km
                                </div>
                                <div className="flex items-center">
                                    <Star className="w-4 h-4 text-yellow-400 mr-1" />
                                    4.7 (328)
                                </div>
                            </div>
                            <p className="text-sm text-gray-500">도시락 • 한식</p>
                        </div>
                    </Card>

                    {/* 가게 카드 4 */}
                    <Card hover padding="none" className="flex overflow-hidden">
                        <img
                            src="https://images.unsplash.com/photo-1488459716781-31db52582fe9"
                            alt="과일천국"
                            className="w-32 h-32 object-cover"
                        />
                        <div className="p-4 flex-1">
                            <div className="flex items-start justify-between mb-2">
                                <h3 className="font-semibold text-gray-900">과일천국 강남점</h3>
                                <Badge variant="success">영업중</Badge>
                            </div>
                            <div className="flex items-center space-x-3 text-sm text-gray-600 mb-2">
                                <div className="flex items-center">
                                    <MapPin className="w-4 h-4 mr-1" />
                                    2.1km
                                </div>
                                <div className="flex items-center">
                                    <Star className="w-4 h-4 text-yellow-400 mr-1" />
                                    4.6 (189)
                                </div>
                            </div>
                            <p className="text-sm text-gray-500">과일 • 청과</p>
                        </div>
                    </Card>
                </div>
            </section>
        </div>
    );
}
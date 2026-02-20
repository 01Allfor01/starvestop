'use client';

import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { ArrowLeft, Heart, MapPin, Clock, Minus, Plus, ShoppingCart, Store, MessageCircle, Loader2 } from 'lucide-react';
import Link from 'next/link';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { productsApi } from '@/lib/api/products';
import { cartApi } from '@/lib/api/cart';
import { openOrCreateChatRoom } from '@/lib/helpers/chat';

// ✅ 거리 계산 함수 (직접 구현)
function calculateDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {
    const R = 6371;
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}

// ✅ 거리 포맷 함수
function formatDistance(distanceKm: number): string {
    if (distanceKm < 1) {
        return `${Math.round(distanceKm * 1000)}m`;
    }
    return `${distanceKm.toFixed(1)}`;
}

export default function ProductDetailPage() {
    const params = useParams();
    const router = useRouter();
    const productId = params.id;

    const [quantity, setQuantity] = useState(1);
    const [isFavorite, setIsFavorite] = useState(false);

    // 상품 데이터 및 상태
    const [product, setProduct] = useState<any>(null);
    const [distance, setDistance] = useState<string>('-');
    const [loading, setLoading] = useState(true);
    const [myLocation, setMyLocation] = useState<{ lat: number; lng: number } | null>(null);

    // ✅ 타이머 및 영업 상태 관리
    const [timeLeft, setTimeLeft] = useState<string>('');
    const [isClosed, setIsClosed] = useState<boolean>(false);

    // 1️⃣ 내 위치 가져오기
    useEffect(() => {
        if (!navigator.geolocation) return;

        navigator.geolocation.getCurrentPosition(
            (pos) => {
                setMyLocation({ lat: pos.coords.latitude, lng: pos.coords.longitude });
            },
            (err) => {
                console.warn('📍 위치 권한 거부:', err.message);
            }
        );
    }, []);

    // 2️⃣ 상품 정보 API 호출
    useEffect(() => {
        const fetchProduct = async () => {
            if (!productId) return;

            try {
                setLoading(true);
                const data = await productsApi.getProduct(Number(productId));

                // ✅ 시간 파싱 함수
                const parseEndTime = (timeData: any) => {
                    const now = new Date();

                    if (!timeData) {
                        return new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59);
                    }

                    let hours = 23;
                    let minutes = 59;
                    let seconds = 59;

                    if (typeof timeData === 'string') {
                        const parts = timeData.split(':').map(Number);
                        hours = parts[0];
                        minutes = parts[1];
                        seconds = parts[2] || 0;
                    } else if (Array.isArray(timeData)) {
                        hours = timeData[0];
                        minutes = timeData[1];
                        seconds = timeData[2] || 0;
                    }

                    return new Date(now.getFullYear(), now.getMonth(), now.getDate(), hours, minutes, seconds);
                };

                const rawTime = data.endTime;
                const isSale = data.status === 'SALE';

                // ✅ status에 따라 표시 가격 결정
                const displayPrice = isSale ? data.salePrice : data.price;
                const discount = isSale ? (data.price - data.salePrice) : 0;

                const mappedProduct = {
                    id: data.id,
                    name: data.name,
                    storeName: data.storeName,
                    storeId: data.storeId,
                    location: (data as any).location,
                    originalPrice: data.price,
                    salePrice: data.salePrice,
                    displayPrice: displayPrice,  // ✅ 실제 표시할 가격
                    discount: discount,           // ✅ SALE일 때만 할인액
                    stock: data.stock,
                    description: data.description,
                    endTime: parseEndTime(rawTime),
                    image: data.imageUrl || 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
                    category: isSale ? '마감세일' : '일반상품',
                    status: data.status,
                };

                setProduct(mappedProduct);
            } catch (error) {
                console.error("상품 상세 불러오기 실패:", error);
                alert("상품 정보를 찾을 수 없습니다.");
                router.back();
            } finally {
                setLoading(false);
            }
        };

        fetchProduct();
    }, [productId, router]);

    // 3️⃣ 거리 계산
    useEffect(() => {
        if (myLocation && product?.location) {
            let storeLat = 0;
            let storeLng = 0;
            const loc = product.location;

            if ('y' in loc && 'x' in loc) {
                storeLat = loc.y;
                storeLng = loc.x;
            } else if ('coordinates' in loc) {
                storeLat = loc.coordinates[1];
                storeLng = loc.coordinates[0];
            } else if ('lat' in loc && 'lng' in loc) {
                storeLat = loc.lat;
                storeLng = loc.lng;
            }

            if (storeLat && storeLng) {
                const dist = calculateDistance(myLocation.lat, myLocation.lng, storeLat, storeLng);
                setDistance(formatDistance(dist));
            }
        }
    }, [myLocation, product]);

    // 4️⃣ ✅ 실시간 타이머 로직 (SALE 상품만)
    useEffect(() => {
        if (!product?.endTime || product?.status !== 'SALE') return;

        const calculateTime = () => {
            const now = new Date().getTime();
            const end = new Date(product.endTime).getTime();
            const distance = end - now;

            if (distance < 0) {
                setTimeLeft('영업종료');
                setIsClosed(true);
                return;
            }

            setIsClosed(false);

            const hours = Math.floor(distance / (1000 * 60 * 60));
            const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((distance % (1000 * 60)) / 1000);

            setTimeLeft(`${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`);
        };

        calculateTime();
        const timer = setInterval(calculateTime, 1000);

        return () => clearInterval(timer);
    }, [product]);

    const handleQuantityChange = (delta: number) => {
        if (!product) return;
        const newQuantity = quantity + delta;
        if (newQuantity >= 1 && newQuantity <= product.stock) {
            setQuantity(newQuantity);
        }
    };

    const handleAddToCart = async () => {
        if (!product) return;

        if (product.status === 'SALE' && isClosed) {
            alert("영업이 종료되어 주문할 수 없습니다.");
            return;
        }

        try {
            const currentCart = await cartApi.getCart();

            if (currentCart.length > 0) {
                const firstProduct = await productsApi.getProduct(currentCart[0].productId);
                const cartStoreId = firstProduct.storeId;

                if (cartStoreId !== product.storeId) {
                    const confirmReplace = confirm(
                        "장바구니에는 같은 가게의 메뉴만 담을 수 있습니다.\n\n" +
                        "선택하신 메뉴를 장바구니에 담을 경우\n" +
                        "이전에 담은 메뉴가 삭제됩니다.\n\n" +
                        "계속하시겠습니까?"
                    );

                    if (!confirmReplace) {
                        return;
                    }

                    await cartApi.clearCart();
                }
            }

            await cartApi.addToCart({
                productId: product.id,
                quantity: quantity
            });

            if (confirm(`${product.name} ${quantity}개를 장바구니에 담았습니다!\n장바구니로 이동하시겠습니까?`)) {
                router.push('/cart');
            }
        } catch (error: any) {
            console.error("장바구니 담기 실패:", error);
            const msg = error.response?.data?.message || "장바구니 담기에 실패했습니다.";
            alert(msg);
        }
    };

    const handleBuyNow = async () => {
        if (!product) return;

        if (product.status === 'SALE' && isClosed) {
            alert("영업이 종료되어 주문할 수 없습니다.");
            return;
        }

        if (!confirm("바로 구매 시 장바구니의 다른 상품은 제외하고 이 상품만 주문합니다. 진행하시겠습니까?")) return;

        try {
            setLoading(true);
            await cartApi.clearCart();
            await cartApi.addToCart({
                productId: product.id,
                quantity: quantity
            });
            router.push('/order');
        } catch (error) {
            console.error("바로 구매 처리 실패:", error);
            alert("구매 진행 중 오류가 발생했습니다.");
            setLoading(false);
        }
    };

    // ✅ 가게 문의하기 함수
    const handleContactStore = async () => {
        if (!product?.storeId) {
            alert('매장 정보를 불러오는 중입니다');
            return;
        }
        await openOrCreateChatRoom(product.storeId, router);
    };

    if (loading) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50">
                <Loader2 className="w-10 h-10 text-primary-500 animate-spin mb-4" />
                <p className="text-gray-500">맛있는 상품 정보를 가져오는 중...</p>
            </div>
        );
    }

    if (!product) return null;

    // ✅ SALE 상품인지 확인
    const isSaleProduct = product.status === 'SALE';

    return (
        <div className="min-h-screen bg-gray-50 pb-20">
            {/* 헤더 */}
            <div className="sticky top-0 z-10 bg-white border-b border-gray-200">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex items-center justify-between">
                    <button
                        onClick={() => router.back()}
                        className="flex items-center text-gray-600 hover:text-gray-900 transition-colors"
                    >
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>뒤로</span>
                    </button>
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
                        <div className="relative aspect-square rounded-2xl overflow-hidden bg-white shadow-sm">
                            <img
                                src={product.image}
                                alt={product.name}
                                className="w-full h-full object-cover"
                            />
                            {/* ✅ SALE 상품만 할인 뱃지 표시 */}
                            {isSaleProduct && product.discount > 0 && (
                                <Badge variant="sale" className="absolute top-4 left-4 text-base px-3 py-1">
                                    {product.discount.toLocaleString()}원 할인
                                </Badge>
                            )}

                            {/* ✅ SALE 상품만 타이머 표시 */}
                            {isSaleProduct && (
                                <div className={`absolute bottom-4 left-4 backdrop-blur-sm px-4 py-2 rounded-lg flex items-center space-x-2 shadow-lg ${
                                    isClosed ? 'bg-gray-800/90 text-white' : 'bg-white/95 text-gray-900'
                                }`}>
                                    <Clock className={`w-5 h-5 ${isClosed ? 'text-gray-400' : 'text-red-500'}`} />
                                    <span className="font-semibold">{timeLeft}</span>
                                </div>
                            )}
                        </div>
                    </div>

                    {/* 오른쪽: 상품 정보 */}
                    <div className="space-y-6">
                        {/* 가게 정보 */}
                        <Link href={`/stores/${product.storeId}`}>
                            <Card hover padding="sm" className="flex items-center space-x-3 cursor-pointer">
                                <div className="p-2 bg-primary-50 rounded-full">
                                    <Store className="w-6 h-6 text-primary-500" />
                                </div>
                                <div className="flex-1">
                                    <h3 className="font-semibold text-gray-900">{product.storeName}</h3>
                                    <div className="flex items-center text-sm text-gray-600">
                                        <MapPin className="w-4 h-4 mr-1 text-primary-500" />
                                        <span className="font-medium text-primary-600">
                                            {distance !== '-' ? `${distance}km` : (myLocation ? '가게 위치 정보 없음' : '위치 권한 필요')}
                                        </span>
                                    </div>
                                </div>
                            </Card>
                        </Link>

                        {/* 상품명 */}
                        <div>
                            <Badge variant="default" className="mb-2">{product.category}</Badge>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">{product.name}</h1>
                            <p className="text-gray-600 line-clamp-2">{product.description}</p>
                        </div>

                        {/* 가격 정보 */}
                        <div className="bg-gradient-to-br from-primary-50 to-primary-100 rounded-2xl p-6">
                            {/* ✅ SALE 상품이고 할인이 있을 때만 할인 정보 표시 */}
                            {isSaleProduct && product.discount > 0 && (
                                <div className="flex items-baseline space-x-3 mb-2">
                                    <span className="text-2xl text-gray-400 line-through">
                                        {product.originalPrice.toLocaleString()}원
                                    </span>
                                    <Badge variant="sale" className="text-lg px-3 py-1 bg-red-500 text-white border-none">
                                        {product.discount.toLocaleString()}원 할인
                                    </Badge>
                                </div>
                            )}
                            {/* ✅ displayPrice 사용 */}
                            <div className="text-4xl font-bold text-primary-600">
                                {product.displayPrice.toLocaleString()}원
                            </div>
                            <p className="text-sm text-gray-600 mt-2 flex items-center">
                                재고: <span className="font-bold text-primary-600 ml-1 text-lg">{product.stock}개</span>
                                <span className="ml-1">남음</span>
                            </p>
                        </div>

                        {/* 수량 선택 */}
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">수량</label>
                            <div className="flex items-center space-x-4">
                                <div className="flex items-center border border-gray-300 rounded-lg bg-white">
                                    <button
                                        onClick={() => handleQuantityChange(-1)}
                                        disabled={quantity <= 1 || (isSaleProduct && isClosed)}
                                        className="p-3 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition rounded-l-lg"
                                    >
                                        <Minus className="w-5 h-5" />
                                    </button>
                                    <span className="px-6 text-lg font-semibold min-w-[3rem] text-center">{quantity}</span>
                                    <button
                                        onClick={() => handleQuantityChange(1)}
                                        disabled={quantity >= product.stock || (isSaleProduct && isClosed)}
                                        className="p-3 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition rounded-r-lg"
                                    >
                                        <Plus className="w-5 h-5" />
                                    </button>
                                </div>
                                <div className="flex-1 text-right">
                                    <p className="text-sm text-gray-600">총 금액</p>
                                    <p className="text-2xl font-bold text-gray-900">
                                        {/* ✅ displayPrice 사용 */}
                                        {(product.displayPrice * quantity).toLocaleString()}원
                                    </p>
                                </div>
                            </div>
                        </div>

                        {/* 버튼 영역 */}
                        <div className="flex space-x-3">
                            <Button
                                variant="outline"
                                size="lg"
                                className="flex-1"
                                onClick={handleAddToCart}
                                disabled={product.stock <= 0 || (isSaleProduct && isClosed)}
                            >
                                <ShoppingCart className="w-5 h-5 mr-2" />
                                {(isSaleProduct && isClosed) ? '영업 종료' : '장바구니'}
                            </Button>

                            <Button
                                size="lg"
                                className="flex-1"
                                disabled={product.stock <= 0 || (isSaleProduct && isClosed)}
                                onClick={handleBuyNow}
                            >
                                {product.stock <= 0 ? '품절' : (isSaleProduct && isClosed) ? '영업 종료' : '바로 구매'}
                            </Button>
                        </div>

                        {/* 가게 문의 */}
                        <Button size="lg" fullWidth onClick={handleContactStore}>
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
                            <div className="prose max-w-none text-gray-600 whitespace-pre-wrap">
                                {product.description}
                                {/* ✅ SALE 상품만 주의사항 표시 */}
                                {isSaleProduct && (
                                    <>
                                        <br /><br />
                                        <p className="text-sm text-gray-500">
                                            * 본 상품은 마감 임박 상품으로 환불이 어려울 수 있습니다.<br/>
                                            * 매장 방문 수령 시간을 꼭 확인해주세요.
                                        </p>
                                    </>
                                )}
                            </div>
                        </Card>
                    </div>
                </div>
            </div>
        </div>
    );
}
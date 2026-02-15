'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { Trash2, Plus, Minus, ShoppingBag, ArrowRight, Store, Loader2 } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { cartApi, CartItemDetail } from '@/lib/api/cart';
import { productsApi } from '@/lib/api/products';
import { useRouter } from 'next/navigation';

export default function CartPage() {
    const router = useRouter();
    const [cartItems, setCartItems] = useState<CartItemDetail[]>([]);
    const [loading, setLoading] = useState(true);
    const [selectedItems, setSelectedItems] = useState<number[]>([]);


    // 데이터 불러오기 (Cart + Product 정보 병합)
    useEffect(() => {
        const fetchCartData = async () => {
            try {
                setLoading(true);
                // 1. 장바구니 목록 조회 (가격 정보 없음)
                const simpleCartItems = await cartApi.getCart();

                if (!simpleCartItems || simpleCartItems.length === 0) {
                    setCartItems([]);
                    return;
                }

                // 2. 각 상품의 상세 정보 조회 (가격, 이미지 등을 얻기 위해)
                const detailedItems = await Promise.all(
                    simpleCartItems.map(async (item) => {
                        try {
                            const productInfo = await productsApi.getProduct(item.productId);
                            return {
                                ...item, // id(cartId), productId, quantity, productName
                                price: productInfo.salePrice,
                                originalPrice: productInfo.price,
                                image: productInfo.imageUrl || 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
                                stock: productInfo.stock,
                                storeName: productInfo.storeName,
                                storeId: productInfo.storeId,
                                discount: productInfo.price - productInfo.salePrice,
                            };
                        } catch (e) {
                            // 상품 정보 조회 실패 시 기본값 처리
                            console.error(`상품 정보 로딩 실패 (ID: ${item.productId})`, e);
                            return {
                                ...item,
                                price: 0,
                                originalPrice: 0,
                                image: '',
                                stock: 0,
                                storeName: '정보 없음',
                                storeId: 0,
                                discount: 0,
                            };
                        }
                    })
                );

                setCartItems(detailedItems);
                // 초기에는 전체 선택
                setSelectedItems(detailedItems.map(item => item.id));

            } catch (error) {
                console.error("장바구니 조회 실패:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchCartData();
    }, []);

    // 현재 장바구니의 매장 정보 (첫 번째 아이템 기준)
    const currentStore = cartItems.length > 0 ? {
        id: cartItems[0].storeId,
        name: cartItems[0].storeName,
    } : null;

    // 전체 선택/해제
    const toggleSelectAll = () => {
        if (selectedItems.length === cartItems.length) {
            setSelectedItems([]);
        } else {
            setSelectedItems(cartItems.map((item) => item.id));
        }
    };

    // 개별 선택/해제
    const toggleSelectItem = (id: number) => {
        if (selectedItems.includes(id)) {
            setSelectedItems(selectedItems.filter((itemId) => itemId !== id));
        } else {
            setSelectedItems([...selectedItems, id]);
        }
    };

    // 수량 변경 API 연결
    const updateQuantity = async (cartId: number, currentQty: number, delta: number, maxStock: number) => {
        const newQuantity = currentQty + delta;

        if (newQuantity < 1) return;
        if (newQuantity > maxStock) {
            alert(`재고가 ${maxStock}개뿐입니다.`);
            return;
        }

        try {
            // UI 선반영 (빠른 반응성)
            setCartItems(prev => prev.map(item =>
                item.id === cartId ? { ...item, quantity: newQuantity } : item
            ));

            // API 호출
            await cartApi.updateCart({ id: cartId, quantity: newQuantity });
        } catch (error) {
            console.error("수량 변경 실패:", error);
            alert("수량 변경에 실패했습니다.");
            // 실패 시 롤백 (원래대로 되돌리기) -> 여기선 생략하거나 다시 fetch
        }
    };

    // 상품 삭제 API 연결
    const removeItem = async (cartId: number) => {
        if (!confirm("정말 삭제하시겠습니까?")) return;

        try {
            await cartApi.removeCartItem(cartId);
            setCartItems(prev => prev.filter(item => item.id !== cartId));
            setSelectedItems(prev => prev.filter(id => id !== cartId));
        } catch (error) {
            console.error("삭제 실패:", error);
            alert("삭제 중 오류가 발생했습니다.");
        }
    };

    // 선택 삭제
    const removeSelectedItems = async () => {
        if (!confirm(`선택한 ${selectedItems.length}개 상품을 삭제하시겠습니까?`)) return;

        try {
            // 백엔드에 일괄 삭제 API가 없으므로 반복문으로 처리
            await Promise.all(selectedItems.map(id => cartApi.removeCartItem(id)));

            setCartItems(prev => prev.filter(item => !selectedItems.includes(item.id)));
            setSelectedItems([]);
        } catch (error) {
            console.error("일괄 삭제 실패:", error);
        }
    };

    // 선택된 상품 총 금액 계산
    const selectedTotal = cartItems
        .filter((item) => selectedItems.includes(item.id))
        .reduce((sum, item) => sum + item.price * item.quantity, 0);

    const selectedDiscount = cartItems
        .filter((item) => selectedItems.includes(item.id))
        .reduce((sum, item) => sum + item.discount * item.quantity, 0);


    if (loading) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50">
                <Loader2 className="w-10 h-10 text-primary-500 animate-spin mb-4" />
                <p className="text-gray-500">장바구니 정보를 불러오는 중...</p>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">장바구니</h1>
                    <p className="text-gray-600">
                        총 <span className="text-primary-600 font-semibold">{cartItems.length}개</span>의 상품이 담겨있습니다
                    </p>
                </div>

                {cartItems.length === 0 ? (
                    <Card className="text-center py-16">
                        <ShoppingBag className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <h3 className="text-xl font-semibold text-gray-900 mb-2">
                            장바구니가 비어있습니다
                        </h3>
                        <p className="text-gray-600 mb-6">
                            마음에 드는 상품을 담아보세요!
                        </p>
                        <Link href="/products/sale">
                            <Button>쇼핑 계속하기</Button>
                        </Link>
                    </Card>
                ) : (
                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                        {/* 왼쪽: 상품 목록 */}
                        <div className="lg:col-span-2 space-y-4">
                            {/* 매장 정보 표시 */}
                            {currentStore && (
                                <Card padding="sm" className="bg-primary-50 border-primary-200">
                                    <div className="flex items-center">
                                        <Store className="w-5 h-5 text-primary-600 mr-2" />
                                        <div className="flex-1">
                                            <span className="font-semibold text-primary-900">
                                                {currentStore.name}
                                            </span>
                                            <p className="text-sm text-primary-700 mt-1">
                                                💡 한 매장의 상품만 함께 주문할 수 있습니다
                                            </p>
                                        </div>
                                    </div>
                                </Card>
                            )}

                            {/* 컨트롤 바 */}
                            <Card padding="sm" className="flex items-center justify-between">
                                <label className="flex items-center cursor-pointer">
                                    <input
                                        type="checkbox"
                                        checked={selectedItems.length === cartItems.length}
                                        onChange={toggleSelectAll}
                                        className="w-5 h-5 text-primary-500 border-gray-300 rounded focus:ring-primary-500 cursor-pointer"
                                    />
                                    <span className="ml-3 text-gray-700 font-medium">
                                        전체 선택 ({selectedItems.length}/{cartItems.length})
                                    </span>
                                </label>
                                <Button
                                    variant="ghost"
                                    size="sm"
                                    onClick={removeSelectedItems}
                                    disabled={selectedItems.length === 0}
                                >
                                    <Trash2 className="w-4 h-4 mr-2" />
                                    선택 삭제
                                </Button>
                            </Card>

                            {/* 상품 리스트 */}
                            {cartItems.map((item) => (
                                <Card key={item.id} padding="none" className="overflow-hidden">
                                    <div className="flex p-4">
                                        <div className="flex items-start pt-2">
                                            <input
                                                type="checkbox"
                                                checked={selectedItems.includes(item.id)}
                                                onChange={() => toggleSelectItem(item.id)}
                                                className="w-5 h-5 text-primary-500 border-gray-300 rounded focus:ring-primary-500 cursor-pointer"
                                            />
                                        </div>

                                        <Link href={`/products/${item.productId}`} className="ml-4">
                                            <img
                                                src={item.image}
                                                alt={item.productName}
                                                className="w-24 h-24 object-cover rounded-lg bg-gray-100"
                                            />
                                        </Link>

                                        <div className="flex-1 ml-4">
                                            <Link href={`/products/${item.productId}`}>
                                                <h3 className="font-semibold text-gray-900 mb-2 hover:text-primary-600">
                                                    {item.productName}
                                                </h3>
                                            </Link>

                                            {item.discount > 0 && (
                                                <div className="flex items-center space-x-2 mb-3">
                                                    <span className="text-sm text-gray-400 line-through">
                                                        {item.originalPrice.toLocaleString()}원
                                                    </span>
                                                    <Badge variant="sale" className="text-xs">
                                                        {item.discount.toLocaleString()}원 할인
                                                    </Badge>
                                                </div>
                                            )}

                                            <div className="flex items-center justify-between">
                                                <div className="flex items-center border border-gray-300 rounded-lg">
                                                    <button
                                                        onClick={() => updateQuantity(item.id, item.quantity, -1, item.stock)}
                                                        disabled={item.quantity <= 1}
                                                        className="p-2 hover:bg-gray-50 disabled:opacity-50 transition"
                                                    >
                                                        <Minus className="w-4 h-4" />
                                                    </button>
                                                    <span className="px-4 text-sm font-semibold">{item.quantity}</span>
                                                    <button
                                                        onClick={() => updateQuantity(item.id, item.quantity, 1, item.stock)}
                                                        disabled={item.quantity >= item.stock}
                                                        className="p-2 hover:bg-gray-50 disabled:opacity-50 transition"
                                                    >
                                                        <Plus className="w-4 h-4" />
                                                    </button>
                                                </div>

                                                <div className="text-right">
                                                    <p className="text-xl font-bold text-gray-900">
                                                        {(item.price * item.quantity).toLocaleString()}원
                                                    </p>
                                                    <p className="text-xs text-gray-500">
                                                        재고 {item.stock}개
                                                    </p>
                                                </div>
                                            </div>
                                        </div>

                                        <button
                                            onClick={() => removeItem(item.id)}
                                            className="ml-4 p-2 text-gray-400 hover:text-red-500 transition"
                                        >
                                            <Trash2 className="w-5 h-5" />
                                        </button>
                                    </div>
                                </Card>
                            ))}
                        </div>

                        {/* 오른쪽: 주문 요약 */}
                        <div className="lg:col-span-1">
                            <div className="sticky top-20">
                                <Card>
                                    <h2 className="text-xl font-bold text-gray-900 mb-6">주문 요약</h2>

                                    <div className="space-y-3 mb-6">
                                        <div className="flex justify-between text-gray-600">
                                            <span>상품 금액</span>
                                            <span>{(selectedTotal + selectedDiscount).toLocaleString()}원</span>
                                        </div>
                                        <div className="flex justify-between text-red-500">
                                            <span>할인 금액</span>
                                            <span>-{selectedDiscount.toLocaleString()}원</span>
                                        </div>
                                    </div>

                                    <div className="pt-6 border-t border-gray-200">
                                        <div className="flex justify-between items-center mb-6">
                                            <span className="text-lg font-semibold text-gray-900">총 결제 금액</span>
                                            <span className="text-2xl font-bold text-primary-600">
                                                {selectedTotal.toLocaleString()}원
                                            </span>
                                        </div>

                                        <Button
                                            fullWidth
                                            size="lg"
                                            disabled={selectedItems.length === 0}
                                            onClick={() => router.push('/order')}
                                            className="mb-3"
                                        >
                                            {selectedItems.length > 0
                                                ? `${selectedItems.length}개 상품 주문하기`
                                                : '상품을 선택해주세요'}
                                            <ArrowRight className="w-5 h-5 ml-2" />
                                        </Button>

                                        <Link href="/products/sale">
                                            <Button variant="outline" fullWidth>
                                                쇼핑 계속하기
                                            </Button>
                                        </Link>
                                    </div>

                                    {selectedDiscount > 0 && (
                                        <div className="mt-6 p-4 bg-primary-50 rounded-lg">
                                            <p className="text-sm text-primary-800">
                                                💰 <span className="font-semibold">{selectedDiscount.toLocaleString()}원</span> 절약했어요!
                                            </p>
                                        </div>
                                    )}
                                </Card>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
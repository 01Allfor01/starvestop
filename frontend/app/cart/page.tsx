'use client';

import { useState } from 'react';
import Link from 'next/link';
import { Trash2, Plus, Minus, ShoppingBag, ArrowRight, Store } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

interface CartItem {
    id: number;
    productId: number;
    name: string;
    storeId: number;
    storeName: string;
    price: number;
    originalPrice: number;
    quantity: number;
    stock: number;
    image: string;
    discount: number;
}

export default function CartPage() {
    // TODO: 나중에 실제 장바구니 데이터로 교체
    const [cartItems, setCartItems] = useState<CartItem[]>([
        {
            id: 1,
            productId: 1,
            name: '프리미엄 크루아상 3입',
            storeId: 1,
            storeName: '파리바게뜨 강남점',
            price: 3000,
            originalPrice: 6000,
            quantity: 2,
            stock: 5,
            image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
            discount: 3000,
        },
        {
            id: 2,
            productId: 2,
            name: '바게트 2입',
            storeId: 1,
            storeName: '파리바게뜨 강남점',
            price: 3500,
            originalPrice: 5000,
            quantity: 1,
            stock: 3,
            image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff',
            discount: 1500,
        },
        {
            id: 3,
            productId: 5,
            name: '소금빵 5입',
            storeId: 1,
            storeName: '파리바게뜨 강남점',
            price: 8000,
            originalPrice: 8000,
            quantity: 1,
            stock: 8,
            image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff',
            discount: 0,
        },
    ]);

    const [selectedItems, setSelectedItems] = useState<number[]>(
        cartItems.map((item) => item.id)
    );

    // 현재 장바구니의 매장 정보
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

    // 수량 변경
    const updateQuantity = (id: number, delta: number) => {
        setCartItems(
            cartItems.map((item) => {
                if (item.id === id) {
                    const newQuantity = item.quantity + delta;
                    if (newQuantity >= 1 && newQuantity <= item.stock) {
                        return { ...item, quantity: newQuantity };
                    }
                }
                return item;
            })
        );
    };

    // 상품 삭제
    const removeItem = (id: number) => {
        setCartItems(cartItems.filter((item) => item.id !== id));
        setSelectedItems(selectedItems.filter((itemId) => itemId !== id));
    };

    // 선택 삭제
    const removeSelectedItems = () => {
        setCartItems(cartItems.filter((item) => !selectedItems.includes(item.id)));
        setSelectedItems([]);
    };

    // 선택된 상품 총 금액
    const selectedTotal = cartItems
        .filter((item) => selectedItems.includes(item.id))
        .reduce((sum, item) => sum + item.price * item.quantity, 0);

    // 선택된 상품 총 할인 금액
    const selectedDiscount = cartItems
        .filter((item) => selectedItems.includes(item.id))
        .reduce((sum, item) => sum + item.discount * item.quantity, 0);

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
                    // 장바구니 비어있을 때
                    <Card className="text-center py-16">
                        <ShoppingBag className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <h3 className="text-xl font-semibold text-gray-900 mb-2">
                            장바구니가 비어있습니다
                        </h3>
                        <p className="text-gray-600 mb-6">
                            마음에 드는 상품을 담아보세요!
                        </p>
                        <Link href="/">
                            <Button>쇼핑 계속하기</Button>
                        </Link>
                    </Card>
                ) : (
                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                        {/* 왼쪽: 상품 목록 */}
                        <div className="lg:col-span-2 space-y-4">
                            {/* 현재 매장 정보 */}
                            {currentStore && (
                                <Card padding="sm" className="bg-primary-50 border-primary-200">
                                    <div className="flex items-center">
                                        <Store className="w-5 h-5 text-primary-600 mr-2" />
                                        <div className="flex-1">
                                            <Link href={`/stores/${currentStore.id}`}>
                                                <span className="font-semibold text-primary-900 hover:text-primary-600">
                                                    {currentStore.name}
                                                </span>
                                            </Link>
                                            <p className="text-sm text-primary-700 mt-1">
                                                💡 한 매장의 상품만 함께 주문할 수 있습니다
                                            </p>
                                        </div>
                                    </div>
                                </Card>
                            )}

                            {/* 전체 선택 & 선택 삭제 */}
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

                            {/* 상품 카드들 */}
                            {cartItems.map((item) => (
                                <Card key={item.id} padding="none" className="overflow-hidden">
                                    <div className="flex p-4">
                                        {/* 체크박스 */}
                                        <div className="flex items-start pt-2">
                                            <input
                                                type="checkbox"
                                                checked={selectedItems.includes(item.id)}
                                                onChange={() => toggleSelectItem(item.id)}
                                                className="w-5 h-5 text-primary-500 border-gray-300 rounded focus:ring-primary-500 cursor-pointer"
                                            />
                                        </div>

                                        {/* 상품 이미지 */}
                                        <Link href={`/products/${item.productId}`} className="ml-4">
                                            <img
                                                src={item.image}
                                                alt={item.name}
                                                className="w-24 h-24 object-cover rounded-lg"
                                            />
                                        </Link>

                                        {/* 상품 정보 */}
                                        <div className="flex-1 ml-4">
                                            <Link href={`/products/${item.productId}`}>
                                                <h3 className="font-semibold text-gray-900 mb-2 hover:text-primary-600">
                                                    {item.name}
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
                                                {/* 수량 조절 */}
                                                <div className="flex items-center border border-gray-300 rounded-lg">
                                                    <button
                                                        onClick={() => updateQuantity(item.id, -1)}
                                                        disabled={item.quantity <= 1}
                                                        className="p-2 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition"
                                                    >
                                                        <Minus className="w-4 h-4" />
                                                    </button>
                                                    <span className="px-4 text-sm font-semibold">{item.quantity}</span>
                                                    <button
                                                        onClick={() => updateQuantity(item.id, 1)}
                                                        disabled={item.quantity >= item.stock}
                                                        className="p-2 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition"
                                                    >
                                                        <Plus className="w-4 h-4" />
                                                    </button>
                                                </div>

                                                {/* 가격 */}
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

                                        {/* 삭제 버튼 */}
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
                                            className="mb-3"
                                        >
                                            {selectedItems.length > 0
                                                ? `${selectedItems.length}개 상품 주문하기`
                                                : '상품을 선택해주세요'}
                                            <ArrowRight className="w-5 h-5 ml-2" />
                                        </Button>

                                        <Link href="/">
                                            <Button variant="outline" fullWidth>
                                                쇼핑 계속하기
                                            </Button>
                                        </Link>
                                    </div>

                                    {/* 혜택 정보 */}
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
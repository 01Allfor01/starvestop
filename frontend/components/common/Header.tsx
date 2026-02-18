'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { ShoppingCart, Search, User, Menu, CalendarCheck, Ticket } from 'lucide-react';
import Button from '@/components/ui/Button';
import { cartApi } from '@/lib/api/cart';

export default function Header() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [cartCount, setCartCount] = useState(0);

    useEffect(() => {
        const checkLoginStatus = () => {
            if (typeof window === 'undefined') return;
            const token = localStorage.getItem('accessToken');
            setIsLoggedIn(!!token);

            if (token) {
                fetchCartCount();
            }
        };

        checkLoginStatus();

        // ✅ 로그인/로그아웃 이벤트 리스너 추가
        window.addEventListener('login', checkLoginStatus);
        window.addEventListener('logout', () => {
            setIsLoggedIn(false);
            setCartCount(0);
        });

        return () => {
            window.removeEventListener('login', checkLoginStatus);
            window.removeEventListener('logout', () => {
                setIsLoggedIn(false);
                setCartCount(0);
            });
        };
    }, []);

    const fetchCartCount = async () => {
        try {
            console.log('🛒 장바구니 조회 시작');
            const cartItems = await cartApi.getCart();
            console.log('🛒 장바구니 데이터:', cartItems);
            console.log('🛒 장바구니 개수:', cartItems.length);
            setCartCount(cartItems.length);
        } catch (error) {
            console.error('❌ 장바구니 개수 조회 실패:', error);
            setCartCount(0);
        }
    };

    return (
        <header className="sticky top-0 z-50 w-full bg-white border-b border-gray-200 shadow-sm">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex items-center justify-between h-16">
                    {/* 로고 */}
                    <Link href="/" className="flex items-center space-x-2">
                        <div className="w-10 h-10 bg-gradient-to-br from-primary-500 to-primary-600 rounded-xl flex items-center justify-center shadow-md">
                            <span className="text-white text-xl font-bold">S</span>
                        </div>
                        <span className="text-xl font-bold bg-gradient-to-r from-primary-500 to-primary-600 bg-clip-text text-transparent">
                            Starve Stop
                        </span>
                    </Link>

                    {/* 검색바 (데스크탑) */}
                    <div className="hidden md:flex flex-1 max-w-xl mx-8">
                        <div className="relative w-full">
                            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                            <input
                                type="text"
                                placeholder="상품, 가게를 검색하세요"
                                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                                onKeyPress={(e) => {
                                    if (e.key === 'Enter') {
                                        const query = (e.target as HTMLInputElement).value;
                                        window.location.href = `/search?q=${query}`;
                                    }
                                }}
                            />
                        </div>
                    </div>

                    {/* 우측 메뉴 */}
                    <div className="flex items-center space-x-4">
                        {/* 장바구니 */}
                        <Link href="/cart" className="relative p-2 text-gray-700 hover:text-primary-500 transition-colors">
                            <ShoppingCart className="w-6 h-6" />
                            {cartCount > 0 && (
                                <span className="absolute top-0 right-0 w-5 h-5 bg-primary-500 text-white text-xs font-bold rounded-full flex items-center justify-center">
                                    {cartCount}
                                </span>
                            )}
                        </Link>

                        {/* 프로필 */}
                        <Link href="/mypage" className="p-2 text-gray-700 hover:text-primary-500 transition-colors">
                            <User className="w-6 h-6" />
                        </Link>

                        {!isLoggedIn && (
                            <div className="hidden lg:block">
                                <Link href="/login">
                                    <Button variant="outline" size="sm">
                                        로그인
                                    </Button>
                                </Link>
                            </div>
                        )}

                        {/* 모바일 메뉴 */}
                        <button className="md:hidden p-2 text-gray-700">
                            <Menu className="w-6 h-6" />
                        </button>
                    </div>
                </div>

                {/* 검색바 (모바일) */}
                <div className="md:hidden pb-3">
                    <div className="relative">
                        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                        <input
                            type="text"
                            placeholder="상품, 가게를 검색하세요"
                            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                            onKeyPress={(e) => {
                                if (e.key === 'Enter') {
                                    const query = (e.target as HTMLInputElement).value;
                                    window.location.href = `/search?q=${query}`;
                                }
                            }}
                        />
                    </div>
                </div>
            </div>

            {/* 네비게이션 메뉴 */}
            <nav className="border-t border-gray-100">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <ul className="flex items-center space-x-8 h-12 text-sm">
                        <li>
                            <Link href="/products/sale" className="text-red-500 font-semibold hover:text-red-600">
                                🔥 마감세일
                            </Link>
                        </li>
                        <li>
                            <Link href="/subscriptions" className="text-gray-700 hover:text-primary-500 inline-flex items-center">
                                <CalendarCheck className="w-4 h-4 mr-1.5 text-secondary-500" />
                                정기구독
                            </Link>
                        </li>
                        {/* ✅ 쿠폰 탭 추가 */}
                        <li>
                            <Link href="/coupons" className="text-gray-700 hover:text-primary-500 inline-flex items-center">
                                <Ticket className="w-4 h-4 mr-1.5 text-amber-500" />
                                쿠폰
                            </Link>
                        </li>
                        <li>
                            <Link href="/stores" className="text-gray-700 hover:text-primary-500">
                                📍 내 근처
                            </Link>
                        </li>
                    </ul>
                </div>
            </nav>
        </header>
    );
}
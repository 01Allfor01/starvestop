'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { ShoppingCart, Search, User, Menu, CalendarCheck, Ticket, MessageCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import { cartApi } from '@/lib/api/cart';
import { chatApi } from '@/lib/api/chat';

export default function Header() {
    const router = useRouter();
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [cartCount, setCartCount] = useState(0);
    const [unreadCount, setUnreadCount] = useState(0); // ✅ 안읽은 메시지 수

    useEffect(() => {
        const checkLoginStatus = () => {
            if (typeof window === 'undefined') return;
            const token = localStorage.getItem('accessToken');
            setIsLoggedIn(!!token);

            if (token) {
                fetchCartCount();
                fetchUnreadCount(); // ✅ 안읽은 메시지 수 조회
            }
        };

        checkLoginStatus();

        // 로그인/로그아웃 이벤트 리스너
        window.addEventListener('login', checkLoginStatus);
        window.addEventListener('logout', () => {
            setIsLoggedIn(false);
            setCartCount(0);
            setUnreadCount(0); // ✅ 초기화
        });

        return () => {
            window.removeEventListener('login', checkLoginStatus);
            window.removeEventListener('logout', () => {
                setIsLoggedIn(false);
                setCartCount(0);
                setUnreadCount(0);
            });
        };
    }, []);

    // 장바구니 개수 조회
    const fetchCartCount = async () => {
        try {
            const cartItems = await cartApi.getCart();
            setCartCount(cartItems.length);
        } catch (error) {
            console.error('❌ 장바구니 개수 조회 실패:', error);
            setCartCount(0);
        }
    };

    // ✅ 안읽은 메시지 수 조회
    const fetchUnreadCount = async () => {
        try {
            const chatRooms = await chatApi.getChatRooms();
            const totalUnread = chatRooms.reduce((sum, room) => sum + (room.unreadCount || 0), 0);
            setUnreadCount(totalUnread);
        } catch (error) {
            console.error('❌ 안읽은 메시지 수 조회 실패:', error);
            setUnreadCount(0);
        }
    };

    // ✅ 로그아웃 핸들러
    const handleLogout = () => {
        if (confirm('로그아웃 하시겠습니까?')) {
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');

            // 로그아웃 이벤트 발생
            window.dispatchEvent(new Event('logout'));

            // 로그인 페이지로 이동
            router.push('/login');
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
                        {/* ✅ 채팅 버튼 (장바구니 왼쪽) */}
                        <Link href="/chat" className="relative p-2 text-gray-700 hover:text-primary-500 transition-colors">
                            <MessageCircle className="w-6 h-6" />
                            {unreadCount > 0 && (
                                <span className="absolute top-0 right-0 w-5 h-5 bg-red-500 text-white text-xs font-bold rounded-full flex items-center justify-center">
                                    {unreadCount > 99 ? '99+' : unreadCount}
                                </span>
                            )}
                        </Link>

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

                        {/* ✅ 로그인/로그아웃 버튼 */}
                        <div className="hidden lg:block">
                            {isLoggedIn ? (
                                <Button variant="outline" size="sm" onClick={handleLogout}>
                                    로그아웃
                                </Button>
                            ) : (
                                <Link href="/login">
                                    <Button variant="outline" size="sm">
                                        로그인
                                    </Button>
                                </Link>
                            )}
                        </div>

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
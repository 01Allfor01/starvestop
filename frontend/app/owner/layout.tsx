'use client';

import { useState, useEffect } from 'react';
import { usePathname } from 'next/navigation';
import Link from 'next/link';
import { Store, LogOut, MessageCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import { chatApi } from '@/lib/api/chat';

export default function OwnerLayout({ children }: { children: React.ReactNode }) {
    const pathname = usePathname();
    const [unreadCount, setUnreadCount] = useState(0);

    // (auth) 그룹은 헤더 없이
    const isAuthPage = pathname?.includes('/login') || pathname?.includes('/signup');

    useEffect(() => {
        if (!isAuthPage) {
            fetchUnreadCount();
        }
    }, [isAuthPage]);

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

    const handleLogout = () => {
        if (confirm('로그아웃 하시겠습니까?')) {
            localStorage.removeItem('accessToken');
            window.location.href = '/owner/login';
        }
    };

    // 로그인/회원가입 페이지는 헤더 없음
    if (isAuthPage) {
        return <>{children}</>;
    }

    return (
        <div className="min-h-screen bg-gray-50">
            {/* 헤더 */}
            <header className="bg-white border-b border-gray-200 sticky top-0 z-10">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex items-center justify-between h-16">
                        {/* 로고 */}
                        <Link
                            href="/owner/dashboard"
                            className="flex items-center space-x-3 hover:opacity-80 transition"
                        >
                            <div className="w-10 h-10 bg-primary-500 rounded-lg flex items-center justify-center shadow-md">
                                <Store className="w-6 h-6 text-white" />
                            </div>
                            <div>
                                <h1 className="text-xl font-bold text-gray-900">사장님 페이지</h1>
                                <p className="text-xs text-gray-500">Starve Stop</p>
                            </div>
                        </Link>

                        {/* 우측 메뉴 */}
                        <div className="flex items-center space-x-4">
                            {/* 채팅 버튼 */}
                            <Link
                                href="/owner/chat"
                                className="relative p-2 text-gray-700 hover:text-primary-500 transition-colors"
                            >
                                <MessageCircle className="w-6 h-6" />
                                {unreadCount > 0 && (
                                    <span className="absolute -top-1 -right-1 w-5 h-5 bg-red-500 text-white text-xs font-bold rounded-full flex items-center justify-center">
                                    {unreadCount > 99 ? '99+' : unreadCount}
                                    </span>
                                )}
                            </Link>

                            {/* 로그아웃 */}
                            <Button variant="outline" size="sm" onClick={handleLogout}>
                                <LogOut className="w-4 h-4 mr-2" />
                                로그아웃
                            </Button>
                        </div>
                    </div>
                </div>
            </header>

            {/* 페이지 콘텐츠 */}
            <main>{children}</main>
        </div>
    );
}
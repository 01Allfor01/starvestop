'use client';

import { useState } from 'react';
import Link from 'next/link';
import { MessageCircle, Search, Store } from 'lucide-react';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function ChatPage() {
    const [searchQuery, setSearchQuery] = useState('');

    // TODO: 실제 API 데이터로 교체
    const chatRooms = [
        {
            id: 1,
            storeName: '파리바게뜨 강남점',
            lastMessage: '배송 완료했습니다!',
            lastTime: '5분 전',
            unreadCount: 2,
            storeImage: 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4',
        },
        {
            id: 2,
            storeName: '샐러디 역삼점',
            lastMessage: '문의하신 샐러드 재고 있습니다.',
            lastTime: '1시간 전',
            unreadCount: 0,
            storeImage: 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1',
        },
        {
            id: 3,
            storeName: '본도시락 선릉점',
            lastMessage: '감사합니다!',
            lastTime: '3시간 전',
            unreadCount: 0,
            storeImage: 'https://images.unsplash.com/photo-1504674900247-0877df9cc836',
        },
    ];

    const filteredRooms = chatRooms.filter(room =>
        room.storeName.toLowerCase().includes(searchQuery.toLowerCase())
    );

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                <h1 className="text-3xl font-bold text-gray-900 mb-6">채팅</h1>

                {/* 검색 */}
                <Card className="mb-6">
                    <div className="relative">
                        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                        <input
                            type="text"
                            placeholder="가게 이름 검색"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                        />
                    </div>
                </Card>

                {/* 채팅 목록 */}
                {filteredRooms.length > 0 ? (
                    <div className="space-y-3">
                        {filteredRooms.map((room) => (
                            <Link key={room.id} href={`/chat/${room.id}`}>
                                <Card hover className="cursor-pointer">
                                    <div className="flex items-center">
                                        <img
                                            src={room.storeImage}
                                            alt={room.storeName}
                                            className="w-14 h-14 rounded-full object-cover mr-4"
                                        />
                                        <div className="flex-1 min-w-0">
                                            <div className="flex items-center justify-between mb-1">
                                                <h3 className="font-semibold text-gray-900 truncate">{room.storeName}</h3>
                                                <span className="text-xs text-gray-500">{room.lastTime}</span>
                                            </div>
                                            <p className="text-sm text-gray-600 truncate">{room.lastMessage}</p>
                                        </div>
                                        {room.unreadCount > 0 && (
                                            <Badge variant="sale" className="ml-3">
                                                {room.unreadCount}
                                            </Badge>
                                        )}
                                    </div>
                                </Card>
                            </Link>
                        ))}
                    </div>
                ) : (
                    <Card className="text-center py-16">
                        <MessageCircle className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600">채팅방이 없습니다</p>
                    </Card>
                )}
            </div>
        </div>
    );
}
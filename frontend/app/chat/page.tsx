'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { MessageCircle, Loader2 } from 'lucide-react';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { chatApi } from '@/lib/api/chat';

// 시간 포맷 함수
function formatTimeAgo(dateString: string): string {
    const now = new Date();
    const past = new Date(dateString);
    const diffMs = now.getTime() - past.getTime();
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffMins < 1) return '방금 전';
    if (diffMins < 60) return `${diffMins}분 전`;
    if (diffHours < 24) return `${diffHours}시간 전`;
    if (diffDays < 7) return `${diffDays}일 전`;

    // 7일 이상은 날짜 표시
    const month = past.getMonth() + 1;
    const day = past.getDate();
    return `${month}월 ${day}일`;
}

export default function ChatPage() {
    const [chatRooms, setChatRooms] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchChatRooms = async () => {
            try {
                setLoading(true);
                const data = await chatApi.getChatRooms();

                console.log('📦 채팅방 데이터:', data);

                // ✅ 각 채팅방의 마지막 메시지 내용 가져오기
                const roomsWithMessages = await Promise.all(
                    data.map(async (room) => {
                        let lastMessageContent = null;

                        if (room.lastMessageId) {
                            try {
                                const message = await chatApi.getMessage(room.lastMessageId);
                                lastMessageContent = message.content;
                            } catch (error) {
                                console.warn(`⚠️ 메시지 ${room.lastMessageId} 로드 실패`);
                            }
                        }

                        return {
                            ...room,
                            lastMessageContent,
                        };
                    })
                );

                // ✅ 정렬: 안읽은 메시지 있는 방 우선 → 최신 메시지 순
                const sorted = [...roomsWithMessages].sort((a, b) => {
                    // 1순위: 안읽은 메시지 개수 (많은 순)
                    if (a.unreadCount > 0 && b.unreadCount === 0) return -1;
                    if (a.unreadCount === 0 && b.unreadCount > 0) return 1;

                    // 2순위: 최신 메시지 시간 (최근 순)
                    const aTime = new Date(a.lastMessageAt || 0).getTime();
                    const bTime = new Date(b.lastMessageAt || 0).getTime();
                    return bTime - aTime;
                });

                setChatRooms(sorted);
            } catch (error) {
                console.error('❌ 채팅방 목록 로딩 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchChatRooms();
    }, []);

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <Loader2 className="w-12 h-12 text-primary-500 animate-spin" />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                <h1 className="text-3xl font-bold text-gray-900 mb-6">채팅</h1>

                {/* 채팅 목록 */}
                {chatRooms.length > 0 ? (
                    <div className="space-y-3">
                        {chatRooms.map((room) => (
                            <Link key={room.id} href={`/chat/${room.id}`}>
                                <Card hover className="cursor-pointer">
                                    <div className="flex items-center">
                                        <div className="w-14 h-14 rounded-full bg-primary-100 flex items-center justify-center mr-4 flex-shrink-0">
                                            <MessageCircle className="w-7 h-7 text-primary-600" />
                                        </div>
                                        <div className="flex-1 min-w-0">
                                            <div className="flex items-center justify-between mb-1">
                                                <h3 className="font-semibold text-gray-900 truncate">
                                                    {room.storeName}
                                                </h3>
                                                <span className="text-xs text-gray-500 ml-2 flex-shrink-0">
                                                    {formatTimeAgo(room.lastMessageAt)}
                                                </span>
                                            </div>
                                            {/* ✅ 마지막 메시지 내용 표시 */}
                                            <p className="text-sm text-gray-600 truncate">
                                                {room.lastMessageContent || (
                                                    <span className="text-gray-400 italic">새 채팅방</span>
                                                )}
                                            </p>
                                        </div>
                                        {room.unreadCount > 0 && (
                                            <Badge variant="sale" className="ml-3 flex-shrink-0">
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
                        <p className="text-gray-600 mb-4">아직 채팅방이 없습니다</p>
                        <p className="text-sm text-gray-500">
                            가게 페이지에서 "가게에 문의하기"를 눌러<br />
                            채팅을 시작해보세요!
                        </p>
                    </Card>
                )}
            </div>
        </div>
    );
}
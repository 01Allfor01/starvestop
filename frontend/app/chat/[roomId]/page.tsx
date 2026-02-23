'use client';

import { useState, useEffect, useRef } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { ArrowLeft, Send, Loader2 } from 'lucide-react';
import Button from '@/components/ui/Button';
import { chatApi, ChatMessage } from '@/lib/api/chat';
import { storesApi } from '@/lib/api/stores';
import { ChatClient } from '@/lib/websocket/chatClient';

export default function ChatRoomPage() {
    const params = useParams();
    const router = useRouter();
    const roomId = Number(params.roomId);

    const [chatRoom, setChatRoom] = useState<any>(null);
    const [storeImage, setStoreImage] = useState<string>('');
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(true);
    const [currentUserId, setCurrentUserId] = useState<number | null>(null);

    const messagesEndRef = useRef<HTMLDivElement>(null);
    const chatClientRef = useRef<ChatClient | null>(null);

    // 메시지 목록 스크롤 맨 아래로
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);

                const roomData = await chatApi.getChatRoom(roomId);
                setChatRoom(roomData);
                setCurrentUserId(roomData.userId);

                try {
                    const storeData = await storesApi.getStore(roomData.storeId);
                    setStoreImage(storeData.imageUrl || 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4');
                } catch (error) {
                    console.warn('매장 이미지 로드 실패');
                    setStoreImage('https://images.unsplash.com/photo-1517248135467-4c7edcad34c4');
                }

                const messagesData = await chatApi.getMessages(roomId);
                setMessages(messagesData.reverse());

                const accessToken = localStorage.getItem('accessToken');
                if (!accessToken) {
                    alert('로그인이 필요합니다');
                    router.push('/login');
                    return;
                }

                const chatClient = new ChatClient();
                chatClientRef.current = chatClient;

                chatClient.connect(accessToken, () => {
                    chatClient.subscribe(roomId, (newMessage: ChatMessage) => {
                        // ✅ 중복 메시지 방지: ID로 체크
                        setMessages((prev) => {
                            const isDuplicate = prev.some(msg => msg.id === newMessage.id);
                            if (isDuplicate) {
                                console.log('⚠️ 중복 메시지 무시:', newMessage.id);
                                return prev;
                            }
                            return [...prev, newMessage];
                        });
                    });
                });
            } catch (error) {
                console.error('❌ 채팅방 로딩 실패:', error);
                alert('채팅방을 불러올 수 없습니다');
                router.push('/chat');
            } finally {
                setLoading(false);
            }
        };

        fetchData();

        // ✅ cleanup 강화
        return () => {
            if (chatClientRef.current) {
                console.log('🔌 WebSocket 연결 해제');
                chatClientRef.current.disconnect();
                chatClientRef.current = null;
            }
        };
    }, [roomId, router]);

    const handleSend = () => {
        if (!message.trim()) return;
        if (!chatClientRef.current) {
            alert('채팅 연결이 끊어졌습니다');
            return;
        }

        // WebSocket으로 메시지 전송
        chatClientRef.current.sendMessage(roomId, message.trim());
        setMessage('');
    };

    // 시간 포맷팅 함수
    const formatTime = (dateString: string) => {
        const date = new Date(dateString);
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');
        return `${hours}:${minutes}`;
    };

    if (loading) {
        return (
            <div className="h-screen flex items-center justify-center">
                <Loader2 className="w-12 h-12 text-primary-500 animate-spin" />
            </div>
        );
    }

    if (!chatRoom) {
        return null;
    }

    return (
        <div className="h-screen flex flex-col bg-gray-50">
            {/* 헤더 */}
            <div className="bg-white border-b border-gray-200 px-4 py-3">
                <div className="max-w-4xl mx-auto flex items-center">
                    <button onClick={() => router.push('/chat')} className="mr-4">
                        <ArrowLeft className="w-6 h-6 text-gray-600" />
                    </button>
                    <img
                        src={storeImage}
                        alt={chatRoom.storeName}
                        className="w-10 h-10 rounded-full object-cover mr-3"
                    />
                    <h1 className="text-lg font-semibold text-gray-900">{chatRoom.storeName}</h1>
                </div>
            </div>

            {/* 메시지 목록 */}
            <div className="flex-1 overflow-y-auto">
                <div className="max-w-4xl mx-auto px-4 py-6 space-y-4">
                    {messages.map((msg) => {
                        const isMyMessage = msg.senderType === 'USER' && msg.senderId === currentUserId;

                        return (
                            <div
                                key={msg.id}
                                className={`flex ${isMyMessage ? 'justify-end' : 'justify-start'}`}
                            >
                                <div
                                    className={`max-w-xs px-4 py-2 rounded-2xl ${
                                        isMyMessage
                                            ? 'bg-primary-500 text-white'
                                            : 'bg-white text-gray-900'
                                    }`}
                                >
                                    <p className="text-sm whitespace-pre-wrap break-words">{msg.content}</p>
                                    <p
                                        className={`text-xs mt-1 ${
                                            isMyMessage ? 'text-primary-100' : 'text-gray-500'
                                        }`}
                                    >
                                        {formatTime(msg.createdAt)}
                                    </p>
                                </div>
                            </div>
                        );
                    })}
                    <div ref={messagesEndRef} />
                </div>
            </div>

            {/* 입력창 */}
            <div className="bg-white border-t border-gray-200 px-4 py-3">
                <div className="max-w-4xl mx-auto flex items-center space-x-2">
                    <input
                        type="text"
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        onKeyPress={(e) => e.key === 'Enter' && !e.shiftKey && handleSend()}
                        placeholder="메시지를 입력하세요"
                        className="flex-1 px-4 py-3 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-primary-500"
                    />
                    <Button
                        onClick={handleSend}
                        className="rounded-full w-12 h-12 p-0"
                        disabled={!message.trim()}
                    >
                        <Send className="w-5 h-5" />
                    </Button>
                </div>
            </div>
        </div>
    );
}
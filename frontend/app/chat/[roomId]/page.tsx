'use client';

import { useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { ArrowLeft, Send } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';

export default function ChatRoomPage() {
    const params = useParams();
    const router = useRouter();
    const roomId = params.roomId;
    const [message, setMessage] = useState('');

    // TODO: 실제 API 데이터로 교체
    const chatRoom = {
        id: roomId,
        storeName: '파리바게뜨 강남점',
        storeImage: 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4',
    };

    const messages = [
        {
            id: 1,
            sender: 'USER',
            content: '안녕하세요! 크루아상 재고 있나요?',
            time: '14:30',
        },
        {
            id: 2,
            sender: 'STORE',
            content: '네 안녕하세요! 크루아상 재고 있습니다 😊',
            time: '14:31',
        },
        {
            id: 3,
            sender: 'USER',
            content: '5개 주문하고 싶은데 가능할까요?',
            time: '14:32',
        },
        {
            id: 4,
            sender: 'STORE',
            content: '네 가능합니다! 주문 부탁드립니다.',
            time: '14:33',
        },
        {
            id: 5,
            sender: 'USER',
            content: '감사합니다!',
            time: '14:34',
        },
    ];

    const handleSend = () => {
        if (!message.trim()) return;

        // TODO: 실제 API 호출
        console.log('메시지 전송:', message);
        setMessage('');
    };

    return (
        <div className="h-screen flex flex-col bg-gray-50">
            {/* 헤더 */}
            <div className="bg-white border-b border-gray-200 px-4 py-3">
                <div className="max-w-4xl mx-auto flex items-center">
                    <button onClick={() => router.back()} className="mr-4">
                        <ArrowLeft className="w-6 h-6 text-gray-600" />
                    </button>
                    <img
                        src={chatRoom.storeImage}
                        alt={chatRoom.storeName}
                        className="w-10 h-10 rounded-full object-cover mr-3"
                    />
                    <h1 className="text-lg font-semibold text-gray-900">{chatRoom.storeName}</h1>
                </div>
            </div>

            {/* 메시지 목록 */}
            <div className="flex-1 overflow-y-auto">
                <div className="max-w-4xl mx-auto px-4 py-6 space-y-4">
                    {messages.map((msg) => (
                        <div
                            key={msg.id}
                            className={`flex ${msg.sender === 'USER' ? 'justify-end' : 'justify-start'}`}
                        >
                            <div
                                className={`max-w-xs px-4 py-2 rounded-2xl ${
                                    msg.sender === 'USER'
                                        ? 'bg-primary-500 text-white'
                                        : 'bg-white text-gray-900'
                                }`}
                            >
                                <p className="text-sm">{msg.content}</p>
                                <p className={`text-xs mt-1 ${
                                    msg.sender === 'USER' ? 'text-primary-100' : 'text-gray-500'
                                }`}>
                                    {msg.time}
                                </p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            {/* 입력창 */}
            <div className="bg-white border-t border-gray-200 px-4 py-3">
                <div className="max-w-4xl mx-auto flex items-center space-x-2">
                    <input
                        type="text"
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        onKeyPress={(e) => e.key === 'Enter' && handleSend()}
                        placeholder="메시지를 입력하세요"
                        className="flex-1 px-4 py-3 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-primary-500"
                    />
                    <Button onClick={handleSend} className="rounded-full w-12 h-12 p-0">
                        <Send className="w-5 h-5" />
                    </Button>
                </div>
            </div>
        </div>
    );
}
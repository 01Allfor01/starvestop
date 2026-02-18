import { apiClient } from './client';

// 채팅방 타입
export interface ChatRoom {
    id: number;
    userId: number;
    ownerId: number;
    storeId: number;
    storeName: string;
    unreadCount: number;
    lastMessageId: number | null;
    lastMessageAt: string;
}

// 채팅 메시지 타입
export interface ChatMessage {
    id: number;
    roomId: number;
    senderType: 'USER' | 'OWNER';
    senderId: number;
    content: string;
    createdAt: string;
}

// 메시지 전송 요청
export interface SendMessageRequest {
    content: string;
}

export const chatApi = {
    // 채팅방 생성
    createChatRoom: async (storeId: number) => {
        const response = await apiClient.post<ChatRoom>(`/stores/${storeId}/chat-rooms`);
        return response.data;
    },

    // 채팅방 목록 조회
    getChatRooms: async () => {
        const response = await apiClient.get<{ content: ChatRoom[] }>('/chat-rooms');
        return response.data.content;
    },

    // 채팅방 상세 조회
    getChatRoom: async (roomId: number) => {
        const response = await apiClient.get<ChatRoom>(`/chat-rooms/${roomId}`);
        return response.data;
    },

    // 메시지 목록 조회 (페이징)
    getMessages: async (roomId: number, cursorId?: number) => {
        const params = cursorId ? { cursorId } : {};
        const response = await apiClient.get<{ content: ChatMessage[] }>(
            `/chat-rooms/${roomId}/messages`,
            { params }
        );
        return response.data.content;
    },
};
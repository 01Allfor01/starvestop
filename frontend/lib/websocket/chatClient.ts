import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export class ChatClient {
    private client: Client | null = null;
    private roomId: number | null = null;

    connect(accessToken: string, onConnected?: () => void) {
        this.client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/ws-stomp'),
            connectHeaders: {
                Authorization: `Bearer ${accessToken}`,
            },
            debug: (str) => {
                console.log('STOMP Debug:', str);
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: () => {
                console.log('✅ WebSocket 연결 성공');
                onConnected?.();
            },
            onStompError: (frame) => {
                console.error('❌ STOMP 에러:', frame);
            },
        });

        this.client.activate();
    }

    subscribe(roomId: number, onMessage: (message: any) => void) {
        if (!this.client) {
            console.error('WebSocket 클라이언트가 초기화되지 않았습니다');
            return;
        }

        this.roomId = roomId;

        this.client.subscribe(`/sub/chat-rooms/${roomId}`, (message) => {
            const data = JSON.parse(message.body);
            onMessage(data);
        });
    }

    sendMessage(roomId: number, content: string) {
        if (!this.client || !this.client.connected) {
            console.error('WebSocket이 연결되지 않았습니다');
            return;
        }

        this.client.publish({
            destination: `/pub/chat-rooms/${roomId}/send`,
            body: JSON.stringify({ content }),
        });
    }

    disconnect() {
        if (this.client) {
            this.client.deactivate();
            this.client = null;
        }
    }
}
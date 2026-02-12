import { apiClient } from './client';

// 주문 타입
export interface CreateOrderRequest {
    // 백엔드 DTO 확인 필요
    storeId: number;
    productIds: number[];
    couponId?: number;
    request?: string;
}

export interface Order {
    id: number;
    orderNumber: string;
    status: string;
    storeId: number;
    storeName: string;
    totalAmount: number;
    discountAmount: number;
    finalAmount: number;
    createdAt: string;
}

export const ordersApi = {
    // 주문 생성
    createOrder: async (data: CreateOrderRequest) => {
        const response = await apiClient.post<Order>('/orders', data);
        return response.data;
    },

    // 주문 목록
    getOrders: async () => {
        const response = await apiClient.get<Order[]>('/orders');
        return response.data;
    },

    // 주문 상세
    getOrder: async (orderId: number) => {
        const response = await apiClient.get<Order>(`/orders/${orderId}`);
        return response.data;
    },

    // 주문 취소
    cancelOrder: async () => {
        const response = await apiClient.patch('/orders/cancel');
        return response.data;
    },

    // 주문 삭제
    deleteOrder: async (orderId: number) => {
        const response = await apiClient.delete(`/orders/${orderId}`);
        return response.data;
    },
};

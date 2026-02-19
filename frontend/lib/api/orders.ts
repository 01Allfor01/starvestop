import { apiClient } from './client';

export type OrderStatus = 'PENDING' | 'PAID' | 'FAILED' | 'CANCELED';

// 주문 생성 요청 DTO
export interface CreateOrderRequest {
    storeId: number;
    userCouponId?: number | null;
}

// 주문 응답 DTO (OrderResponse)
export interface OrderResponse {
    id: number;
    storeId: number;
    storeName: string;
    userId: number;
    orderKey: string;
    status: OrderStatus;
    discountedPrice: number;
    amount: number;
    createdAt: string;
    updatedAt: string;
}

// 주문 상품 응답 DTO (OrderProductResponse)
export interface OrderProductResponse {
    id: number;
    orderId: number;
    productName: string;
    quantity: number;
    productPrice: number;
}

export const ordersApi = {
    // 주문 생성
    createOrder: async (data: CreateOrderRequest) => {
        const response = await apiClient.post('/orders', data);
        return response.data;
    },

    // 주문 목록 조회
    getOrders: async () => {
        const response = await apiClient.get('/orders');
        return response.data;
    },

    // 주문 상세 조회
    getOrder: async (orderId: number) => {
        const response = await apiClient.get(`/orders/${orderId}`);
        return response.data;
    },

    // 주문 상품 목록 조회 (OrderProductController)
    getOrderProducts: async (orderId: number) => {
        const response = await apiClient.get(`/order-products/${orderId}`);
        return response.data;
    },

    // 주문 취소 (URL 오타 수정됨: /cacel -> /cancel)
    cancelOrder: async (orderId: number) => {
        const response = await apiClient.patch('/orders/cancel', { id: orderId });
        return response.data;
    },

    // 주문 삭제
    deleteOrder: async (orderId: number) => {
        const response = await apiClient.delete(`/orders/${orderId}`);
        return response.data; // data 없음 (Void)
    },
};
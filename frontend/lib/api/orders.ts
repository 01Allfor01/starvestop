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

// ✅ 공통: Axios response.data 타입을 명시하는 헬퍼(선택)
type ApiResponse<T> = T;

export const ordersApi = {
    // 주문 생성
    createOrder: async (data: CreateOrderRequest): Promise<ApiResponse<OrderResponse>> => {
        const response = await apiClient.post<ApiResponse<OrderResponse>>('/orders', data);
        return response.data;
    },

    // ✅ 주문 목록 조회 (핵심: 반환 타입 지정)
    getOrders: async (): Promise<ApiResponse<OrderResponse[]>> => {
        const response = await apiClient.get<ApiResponse<OrderResponse[]>>('/orders');
        return response.data;
    },

    // 주문 상세 조회
    getOrder: async (orderId: number): Promise<ApiResponse<OrderResponse>> => {
        const response = await apiClient.get<ApiResponse<OrderResponse>>(`/orders/${orderId}`);
        return response.data;
    },

    // ✅ 주문 상품 목록 조회
    getOrderProducts: async (orderId: number): Promise<ApiResponse<OrderProductResponse[]>> => {
        const response = await apiClient.get<ApiResponse<OrderProductResponse[]>>(
            `/order-products/${orderId}`
        );
        return response.data;
    },

    // 주문 취소
    cancelOrder: async (orderId: number): Promise<ApiResponse<void>> => {
        const response = await apiClient.patch<ApiResponse<void>>('/orders/cancel', { id: orderId });
        return response.data;
    },

    // 주문 삭제
    deleteOrder: async (orderId: number): Promise<ApiResponse<void>> => {
        const response = await apiClient.delete<ApiResponse<void>>(`/orders/${orderId}`);
        return response.data;
    },
};

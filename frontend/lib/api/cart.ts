import { apiClient } from './client';

// 장바구니 타입
export interface CartItem {
    id: number;
    productId: number;
    productName: string;
    price: number;
    discountRate: number;
    quantity: number;
    image: string;
    storeId: number;
    storeName: string;
}

export interface AddToCartRequest {
    productId: number;
    quantity: number;
}

export interface UpdateCartRequest {
    cartId: number;
    quantity: number;
}

export const cartApi = {
    // 장바구니 조회
    getCart: async () => {
        const response = await apiClient.get<CartItem[]>('/cart');
        return response.data;
    },

    // 특정 매장 장바구니 조회
    getStoreCart: async (storeId: number) => {
        const response = await apiClient.get<CartItem[]>(`/cart/stores/${storeId}`);
        return response.data;
    },

    // 장바구니 추가
    addToCart: async (data: AddToCartRequest) => {
        const response = await apiClient.post('/cart', data);
        return response.data;
    },

    // 장바구니 수량 변경
    updateCart: async (data: UpdateCartRequest) => {
        const response = await apiClient.patch('/cart', data);
        return response.data;
    },

    // 장바구니 아이템 삭제
    removeCartItem: async (cartId: number) => {
        const response = await apiClient.delete(`/cart/${cartId}`);
        return response.data;
    },

    // 장바구니 전체 비우기
    clearCart: async () => {
        const response = await apiClient.delete('/cart');
        return response.data;
    },
};

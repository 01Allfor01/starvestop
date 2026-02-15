import { apiClient } from './client';

// 백엔드 CartResponse DTO
export interface CartItemResponse {
    id: number;          // 장바구니 ID (Cart ID)
    productId: number;
    productName: string;
    quantity: number;
}

// 프론트엔드에서 사용할 UI용 타입 (상품 상세 정보 포함)
export interface CartItemDetail extends CartItemResponse {
    price: number;       // 판매가 (salePrice)
    originalPrice: number; // 정가
    image: string;
    stock: number;
    storeName: string;
    storeId: number;
    discount: number;
}

export interface AddToCartRequest {
    productId: number;
    quantity: number;
}

export interface UpdateCartRequest {
    id: number; // Cart ID
    quantity: number;
}

export const cartApi = {
    // 장바구니 목록 조회
    getCart: async () => {
        // 백엔드가 CommonResponse<List<CartResponse>> 형태이므로 data.data로 접근
        const response = await apiClient.get<{ data: CartItemResponse[] }>('/carts');
        return response.data;
    },

    // 장바구니 추가
    addToCart: async (data: AddToCartRequest) => {
        const response = await apiClient.post('/carts', data);
        return response.data;
    },

    // 수량 변경
    updateCart: async (data: UpdateCartRequest) => {
        const response = await apiClient.patch('/carts', data);
        return response.data;
    },

    // 삭제
    removeCartItem: async (cartId: number) => {
        const response = await apiClient.delete(`/carts/${cartId}`);
        return response.data;
    },

    // 전체 삭제
    clearCart: async () => {
        const response = await apiClient.delete('/carts');
        return response.data;
    },
};
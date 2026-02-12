import { apiClient } from './client';

// 상품 타입 (백엔드 Response에 맞춤)
export interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
    discountRate: number;
    image: string;
    stock: number;
    storeId: number;
    storeName: string;
}

export interface ProductSale {
    id: number;
    name: string;
    price: number;
    discountRate: number;
    saleEndTime: string;
    stock: number;
    image: string;
    storeId: number;
    storeName: string;
}

export interface ProductDetail {
    id: number;
    name: string;
    description: string;
    price: number;
    discountRate: number;
    image: string;
    stock: number;
    storeId: number;
    storeName: string;
    saleEndTime?: string;
}

export const productsApi = {
    // 마감세일 상품 목록
    getSaleProducts: async () => {
        const response = await apiClient.get<{ content: ProductSale[] }>('/products/sale');
        return response.data.content;
    },

    // 상품 상세
    getProduct: async (id: number) => {
        const response = await apiClient.get<ProductDetail>(`/products/${id}`);
        return response.data;
    },

    // 특정 매장의 상품 목록
    getStoreProducts: async (storeId: number) => {
        const response = await apiClient.get<{ content: Product[] }>(`/stores/${storeId}/products`);
        return response.data.content;
    },
};

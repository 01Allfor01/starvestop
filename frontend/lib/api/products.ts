import { apiClient } from './client';

// 마감세일 목록용 타입 (GetProductSaleResponse 대응)
export interface ProductSale {
    id: number;
    storeId: number;
    storeName: string;
    name: string;
    description: string;
    stock: number;
    price: number;
    salePrice: number;
    imageUrl: string;
    endTime: string;
    updatedAt: string;
    location: { x: number; y: number } | null;
}

// 상세 조회용 타입 (GetProductDetailResponse 대응)
export interface ProductDetail {
    id: number;
    storeId: number;
    storeName: string;
    location: { x: number, y: number } | null;
    name: string;
    description: string;
    stock: number;
    price: number;
    salePrice: number;
    status: string;
    imageUrl: string;
    endTime: string;
    createdAt: string;
    updatedAt: string;
}

export const productsApi = {
    // 마감세일 목록
    getSaleProducts: async () => {
        const response = await apiClient.get<{ content: ProductSale[] }>('/products/sale');
        return response.data.content;
    },

    // 상품 상세
    getProduct: async (id: number) => {
        const response = await apiClient.get<ProductDetail>(`/products/${id}`);
        return response.data;
    },

    // (선택) 특정 매장 상품 목록
    getStoreProducts: async (storeId: number) => {
        const response = await apiClient.get<{ content: ProductSale[] }>(`/stores/${storeId}/products`);
        return response.data.content;
    }
};
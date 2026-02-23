import { apiClient } from './client';
import type {
    UpdateOwnerRequest,
    UpdateOwnerResponse,
    CreateStoreRequest,
    UpdateStoreRequest,
    CreateStoreResponse,
    GetStoreForOwnerResponse,
    PageResponse,
    CreateProductRequest,
    UpdateProductRequest,
    CreateProductResponse,
    UpdateProductResponse,
    GetProductResponse,
    SliceResponse,
    CreateSubscriptionRequest,
    UpdateSubscriptionRequest,
    CreateSubscriptionResponse,
    GetSubscriptionResponse,
    UpdateSubscriptionResponse,
    CreateSettlementRequest,
    CreateSettlementResponse,
} from '@/types/owner';

export const ownerApi = {
    // ─────────────────────────────────────────────────────────
    // Owner (판매자 정보)
    // ─────────────────────────────────────────────────────────
    updateOwner: async (data: UpdateOwnerRequest) => {
        const response = await apiClient.patch<UpdateOwnerResponse>('/owners', data);
        return response.data;
    },

    deleteOwner: async () => {
        await apiClient.delete('/owners');
    },

    // ─────────────────────────────────────────────────────────
    // Store (매장)
    // ─────────────────────────────────────────────────────────
    createStore: async (data: CreateStoreRequest) => {
        const response = await apiClient.post<CreateStoreResponse>('/stores', data);
        return response.data;
    },

    getMyStores: async (page: number = 0, size: number = 10) => {
        const response = await apiClient.get<PageResponse<GetStoreForOwnerResponse>>(
            `/stores/my?page=${page}&size=${size}`
        );
        return response.data;
    },

    updateStore: async (storeId: number, data: UpdateStoreRequest) => {
        const response = await apiClient.patch<CreateStoreResponse>(`/stores/${storeId}`, data);
        return response.data;
    },

    deleteStore: async (storeId: number) => {
        await apiClient.delete(`/stores/${storeId}`);
    },

    // ─────────────────────────────────────────────────────────
    // Product (상품)
    // ─────────────────────────────────────────────────────────
    createProduct: async (data: CreateProductRequest) => {
        const response = await apiClient.post<CreateProductResponse>('/products', data);
        return response.data;
    },

    getStoreProducts: async (storeId: number, lastId?: number, size: number = 20) => {
        let url = `/stores/${storeId}/products?size=${size}`;
        if (lastId) url += `&lastId=${lastId}`;

        const response = await apiClient.get<SliceResponse<GetProductResponse>>(url);
        return response.data;
    },

    updateProduct: async (productId: number, data: UpdateProductRequest) => {
        const response = await apiClient.patch<UpdateProductResponse>(`/products/${productId}`, data);
        return response.data;
    },

    deleteProduct: async (productId: number) => {
        await apiClient.delete(`/products/${productId}`);
    },

    // ─────────────────────────────────────────────────────────
    // Subscription (구독)
    // ─────────────────────────────────────────────────────────
    createSubscription: async (storeId: number, data: CreateSubscriptionRequest) => {
        const response = await apiClient.post<CreateSubscriptionResponse>(
            `/stores/${storeId}/subscriptions`,
            data
        );
        return response.data;
    },

    getStoreSubscriptions: async (storeId: number) => {
        const response = await apiClient.get<GetSubscriptionResponse[]>(
            `/stores/${storeId}/subscriptions`
        );
        return response.data;
    },

    updateSubscription: async (subscriptionId: number, data: UpdateSubscriptionRequest) => {
        const response = await apiClient.patch<UpdateSubscriptionResponse>(
            `/subscriptions/${subscriptionId}`,
            data
        );
        return response.data;
    },

    deleteSubscription: async (subscriptionId: number) => {
        await apiClient.delete(`/subscriptions/${subscriptionId}`);
    },

    // ─────────────────────────────────────────────────────────
    // Settlement (정산)
    // ─────────────────────────────────────────────────────────
    createSettlement: async (data: CreateSettlementRequest) => {
        const response = await apiClient.post<CreateSettlementResponse>('/settlements', data);
        return response.data;
    },
};
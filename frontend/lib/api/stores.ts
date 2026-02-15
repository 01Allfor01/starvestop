import { apiClient } from './client';

// 매장 타입 (백엔드 Response에 맞춤)
export interface Store {
    id: number;
    name: string;
    address: string;
    category: string;
    latitude: number;
    longitude: number;
    openTime: string;
    closeTime: string;
    status: string;
    imageUrl: string;
    distance?: number;
    updatedAt: string;
}

export interface StoreDetail {
    id: number;
    name: string;
    address: string;
    category: string;
    description: string;
    phone: string;
    openTime: string;
    closeTime: string;
    imageUrl: string;
}

// SliceResponse 타입
interface SliceResponse<T> {
    content: T[];
    pageable: any;
    last: boolean;
    size: number;
    number: number;
}

export const storesApi = {
    // 매장 목록 (검색 조건 가능)
    getStores: async (params?: { keyword?: string; category?: string }) => {
        const queryString = new URLSearchParams(params as any).toString();
        const url = queryString ? `/stores?${queryString}` : '/stores';
        const response = await apiClient.get<SliceResponse<Store>>(url);
        return response.data.content; // content만 반환
    },

    // 매장 상세
    getStore: async (storeId: number) => {
        const response = await apiClient.get<StoreDetail>(`/stores/${storeId}`);
        return response.data;
    },
};

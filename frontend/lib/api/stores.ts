import { apiClient } from './client';

// 매장 타입
export interface Store {
    id: number;
    name: string;
    address: string;
    description: string;
    operatingHours: string;
    phone: string;
    image: string;
}

export const storesApi = {
    // 매장 목록
    getStores: async () => {
        const response = await apiClient.get<Store[]>('/stores');
        return response.data;
    },

    // 매장 상세
    getStore: async (storeId: number) => {
        const response = await apiClient.get<Store>(`/stores/${storeId}`);
        return response.data;
    },
};

import { apiClient } from './client';

export type StoreCategory =
    | 'KOREAN_FOOD'
    | 'JAPANESE_FOOD'
    | 'CHINESE_FOOD'
    | 'WESTERN_FOOD'
    | 'ASIAN_FOOD'
    | 'FAST_FOOD'
    | 'CAFE'
    | 'DESSERT';

/**
 * 백엔드 StoreStatus Enum 매핑
 */
export type StoreStatus = 'OPENED' | 'CLOSED';

/**
 * GetStoreDetailResponse DTO 매핑 인터페이스
 */
export interface GetStoreDetailResponse {
    id: number;
    name: string;
    address: string;
    description: string;
    category: StoreCategory;
    latitude: number;
    longitude: number;
    status: StoreStatus;
    openTime: string;  // LocalTime은 보통 "HH:mm:ss" 형태의 문자열로 넘어옵니다.
    closeTime: string;
    businessRegistrationNumber: string;
    imageUrl: string;
    createdAt: string; // LocalDateTime은 ISO-8601 문자열로 넘어옵니다.
    updatedAt: string;
}

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
    getStores: async (params?: {
        keyword?: string;
        category?: string;
        nowLatitude?: number;
        nowLongitude?: number;
        size?: number;
    }) => {
        const filteredParams = params
            ? Object.fromEntries(
                Object.entries(params).filter(([_, v]) => v !== undefined && v !== null)
            )
            : {};

        const queryString = new URLSearchParams(
            Object.entries(filteredParams).map(([k, v]) => [k, String(v)])
        ).toString();

        const url = queryString ? `/stores?${queryString}` : '/stores';
        const response = await apiClient.get<SliceResponse<Store>>(url);
        return response.data.content;
    },

    getStore: async (storeId: number) => {
        const response = await apiClient.get<GetStoreDetailResponse>(`/stores/${storeId}`);
        return response.data;
    },
};

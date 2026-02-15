import { apiClient } from './client';

// 구독 타입 (백엔드 Response에 정확히 맞춤)
export interface Subscription {
    id: number;
    storeId: number;
    storeName: string;
    name: string;
    description: string;
    dayList: string[]; // ["MONDAY", "WEDNESDAY", "FRIDAY"] 등
    mealTimeList: string[]; // ["BREAKFAST", "LUNCH", "DINNER"] 등
    price: number;
    stock: number;
    isJoinable: boolean; // 가입 가능 여부
}

export interface UserSubscription {
    id: number;
    subscriptionId: number;
    subscriptionName: string;
    storeId: number;
    storeName: string;
    price: number;
    status: string; // UserSubscriptionStatus enum
    createdAt: string; // 구독 시작일
    expiresAt: string; // 만료일
}

export const subscriptionsApi = {
    // 구독 상품 목록 (전체)
    getSubscriptions: async () => {
        const response = await apiClient.get<Subscription[]>('/subscriptions');
        return response.data;
    },

    // 특정 매장의 구독 상품 목록
    getStoreSubscriptions: async (storeId: number) => {
        const response = await apiClient.get<Subscription[]>(`/stores/${storeId}/subscriptions`);
        return response.data;
    },

    // 구독 상품 상세
    getSubscription: async (subscriptionId: number) => {
        const response = await apiClient.get<Subscription>(`/subscriptions/${subscriptionId}`);
        return response.data;
    },

    // 구독하기 (정확한 엔드포인트!)
    subscribe: async (subscriptionId: number) => {
        const response = await apiClient.post(`/user-subscriptions/subscriptions/${subscriptionId}`);
        return response.data;
    },

    // 내 구독 목록
    getMySubscriptions: async () => {
        const response = await apiClient.get<UserSubscription[]>('/user-subscriptions');
        return response.data;
    },

    // 내 구독 상세
    getMySubscription: async (userSubscriptionId: number) => {
        const response = await apiClient.get<UserSubscription>(`/user-subscriptions/${userSubscriptionId}`);
        return response.data;
    },

    // 구독 취소
    unsubscribe: async (userSubscriptionId: number) => {
        const response = await apiClient.delete(`/user-subscriptions/${userSubscriptionId}`);
        return response.data;
    },
};

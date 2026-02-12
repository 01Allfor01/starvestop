import { apiClient } from './client';

// 구독 타입
export interface Subscription {
    id: number;
    name: string;
    description: string;
    price: number;
    period: string;
    storeId: number;
    storeName: string;
}

export interface UserSubscription {
    id: number;
    subscriptionId: number;
    subscriptionName: string;
    storeName: string;
    price: number;
    status: string;
    startDate: string;
    nextPaymentDate: string;
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

    // 구독하기
    subscribe: async (subscriptionId: number) => {
        const response = await apiClient.post(`/subscriptions/${subscriptionId}`);
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

import { apiClient } from './client';

// 빌링 승인 요청
export interface BillingConfirmRequest {
    authKey: string;
    subscriptionId: number;
}

// 빌링 승인 응답
export interface BillingConfirmResponse {
    success: boolean;
}

export const billingApi = {
    // 빌링 승인 (카드 등록 완료 후 백엔드에 최종 확인)
    confirmBilling: async (data: BillingConfirmRequest) => {
        const response = await apiClient.post<BillingConfirmResponse>('/billing/confirm', data);
        return response.data;
    },
};
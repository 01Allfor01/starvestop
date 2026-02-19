import { apiClient } from './client';

export interface PaymentPrepareRequest {
    orderId: number;
}

export interface CreatePaymentResponse {
    orderKey: string;
    amount: number;
}

export interface PaymentPrepareResponse {
    orderKey: string;
    amount: number;
    customerKey: string;
    // 필요한 다른 필드가 있다면 추가하세요.
}

export const paymentsApi = {
    preparePayment: async (orderId: number) => {
        const response = await apiClient.post('/payments', { orderId });
        // ✅ 요청하신 대로 .data 한 번만 사용
        return response.data as CreatePaymentResponse;
    },

    confirmPayment: async (params: { paymentKey: string; orderId: string; amount: string; paymentType: string }) => {
        // 백엔드의 GET /payments/success 엔드포인트 호출
        const response = await apiClient.get('/payments/success', {
            params: {
                paymentKey: params.paymentKey,
                orderId: params.orderId,
                amount: params.amount,
                paymentType: params.paymentType
            }
        });

        // 리다이렉트 된 최종 URL 등을 확인하기 위해 response 객체 전체를 반환
        return response;
    }
};
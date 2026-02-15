import { apiClient } from './client';

// 쿠폰 타입 (백엔드 Response에 정확히 맞춤)
export interface Coupon {
    id: number;
    name: string;
    discountAmount: number; // 할인 금액
    minAmount: number; // 최소 주문 금액
    validDays: number; // 유효 기간 (일)
    expiresAt: string; // 발급 종료일
    status: string; // CouponStatus enum
    stock: number; // 남은 수량
}

export interface UserCoupon {
    id: number;
    userId: number;
    couponId: number;
    couponName: string;
    discountAmount: number;
    minAmount: number;
    startedAt: string; // 발급일
    expiresAt: string; // 만료일
}

export const couponsApi = {
    // 발급 가능한 쿠폰 목록
    getCoupons: async () => {
        const response = await apiClient.get<Coupon[]>('/coupons');
        return response.data;
    },

    // 쿠폰 상세
    getCoupon: async (couponId: number) => {
        const response = await apiClient.get<Coupon>(`/coupons/${couponId}`);
        return response.data;
    },

    // 쿠폰 받기
    receiveCoupon: async (couponId: number) => {
        const response = await apiClient.post(`/coupons/${couponId}/user-coupons`);
        return response.data;
    },

    // 내 쿠폰 목록
    getMyCoupons: async () => {
        const response = await apiClient.get<UserCoupon[]>('/user-coupons');
        return response.data;
    },

    // 내 쿠폰 상세
    getMyCoupon: async (userCouponId: number) => {
        const response = await apiClient.get<UserCoupon>(`/user-coupons/${userCouponId}`);
        return response.data;
    },

    // 쿠폰 삭제
    deleteCoupon: async (userCouponId: number) => {
        const response = await apiClient.delete(`/user-coupons/${userCouponId}`);
        return response.data;
    },
};

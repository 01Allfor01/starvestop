import { apiClient } from './client';

// 쿠폰 타입
export interface Coupon {
    id: number;
    name: string;
    description: string;
    discount: number;
    discountType: string; // FIXED, RATE
    minAmount: number;
    maxDiscount?: number;
    expiryDate: string;
    quantity: number;
}

export interface UserCoupon {
    id: number;
    couponId: number;
    couponName: string;
    discount: number;
    discountType: string;
    minAmount: number;
    expiryDate: string;
    isUsed: boolean;
    usedDate?: string;
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

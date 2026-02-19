export default function Page() {
    return null;
}

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
    joinable: boolean; // 가입 가능 여부
    distance: number; // ✅ 백엔드에서 계산된 거리 (km)
    imageUrl?: string; // ✅ 이미지 URL
}

export interface UserSubscription {
    id: number;
    subscriptionId: number;
    subscriptionName: string;
    storeId: number;
    storeName: string;
    price: number;
    status: string;
    createdAt: string;
    expiresAt: string;
}

// ─────────────────────────────────────────────────────────
// Enums
// ─────────────────────────────────────────────────────────
export enum StoreCategory {
    KOREAN_FOOD = 'KOREAN_FOOD',
    JAPANESE_FOOD = 'JAPANESE_FOOD',
    CHINESE_FOOD = 'CHINESE_FOOD',
    WESTERN_FOOD = 'WESTERN_FOOD',
    ASIAN_FOOD = 'ASIAN_FOOD',
    FAST_FOOD = 'FAST_FOOD',
    CAFE = 'CAFE',
    DESSERT = 'DESSERT',
}

export enum StoreStatus {
    CLOSED = 'CLOSED',
    OPENED = 'OPENED',
}

export enum ProductStatus {
    GENERAL = 'GENERAL',
    SALE = 'SALE',
}

export enum Day {
    MONDAY = 'MONDAY',
    TUESDAY = 'TUESDAY',
    WEDNESDAY = 'WEDNESDAY',
    THURSDAY = 'THURSDAY',
    FRIDAY = 'FRIDAY',
    SATURDAY = 'SATURDAY',
    SUNDAY = 'SUNDAY',
}

export enum MealTime {
    BREAKFAST = 'BREAKFAST',
    LUNCH = 'LUNCH',
    DINNER = 'DINNER',
}

// ─────────────────────────────────────────────────────────
// Owner
// ─────────────────────────────────────────────────────────
export interface UpdateOwnerRequest {
    password: string;
}

export interface UpdateOwnerResponse {
    email: string;
    userName: string;
}

// ─────────────────────────────────────────────────────────
// Store
// ─────────────────────────────────────────────────────────
export interface CreateStoreRequest {
    name: string;
    address: string;
    description: string;
    category: StoreCategory;
    latitude: number;
    longitude: number;
    openTime: string;  // "HH:mm:ss"
    closeTime: string; // "HH:mm:ss"
    status: StoreStatus;
    businessRegistrationNumber: string;
}

export interface UpdateStoreRequest {
    name: string;
    address: string;
    description: string;
    category: StoreCategory;
    latitude: number;
    longitude: number;
    openTime: string;
    closeTime: string;
    status: StoreStatus;
}

export interface CreateStoreResponse {
    id: number;
    name: string;
    address: string;
    description: string;
    category: StoreCategory;
    latitude: number;
    longitude: number;
    status: StoreStatus;
    openTime: string;
    closeTime: string;
    businessRegistrationNumber: string;
    createdAt: string;
    updatedAt: string;
}

export interface GetStoreForOwnerResponse {
    id: number;
    name: string;
    address: string;
    category: StoreCategory;
    latitude: number;
    longitude: number;
    openTime: string;
    closeTime: string;
    status: StoreStatus;
    unreadCount: number;
    ImageUrl: string;
}

// ─────────────────────────────────────────────────────────
// Product
// ─────────────────────────────────────────────────────────
export interface CreateProductRequest {
    storeId: number;
    name: string;
    description: string;
    stock: number;
    price: number;
    salePrice: number;
    status: ProductStatus;
}

export interface UpdateProductRequest {
    name: string;
    description: string;
    stock: number;
    price: number;
    salePrice: number;
    status: ProductStatus;
}

export interface CreateProductResponse {
    id: number;
    storeId: number;
    name: string;
    description: string;
    stock: number;
    price: number;
    salePrice: number;
    status: ProductStatus;
    createdAt: string;
    updatedAt: string;
}

export interface UpdateProductResponse {
    id: number;
    storeId: number;
    name: string;
    description: string;
    stock: number;
    price: number;
    salePrice: number;
    status: ProductStatus;
    updatedAt: string;
}

export interface GetProductResponse {
    id: number;
    storeId: number;
    storeName: string;
    name: string;
    description: string;
    stock: number;
    price: number;
    salePrice: number;
    status: ProductStatus;
    imageUrl?: string;
}

// ─────────────────────────────────────────────────────────
// Subscription
// ─────────────────────────────────────────────────────────
export interface CreateSubscriptionRequest {
    name: string;
    description: string;
    day: number;        // 비트마스크
    mealTime: number;   // 비트마스크
    price: number;
    stock: number;
}

export interface UpdateSubscriptionRequest {
    joinable: boolean;
}

export interface CreateSubscriptionResponse {
    id: number;
    storeId: number;
    name: string;
    description: string;
    dayList: Day[];
    mealTimeList: MealTime[];
    price: number;
    stock: number;
    createdAt: string;
}

export interface GetSubscriptionResponse {
    id: number;
    storeId: number;
    storeName: string;
    location: {
        type: string;
        coordinates: [number, number];
    };
    name: string;
    description: string;
    dayList: Day[];
    mealTimeList: MealTime[];
    price: number;
    stock: number;
    isJoinable: boolean;
}

export interface UpdateSubscriptionResponse {
    id: number;
    storeId: number;
    isJoinable: boolean;
}

// ─────────────────────────────────────────────────────────
// Settlement
// ─────────────────────────────────────────────────────────
export interface CreateSettlementRequest {
    storeId: number;
    period: string;      // "YYYY-MM"
    feeRate: number;
}

export interface CreateSettlementResponse {
    id: number;
}

// ─────────────────────────────────────────────────────────
// Pagination
// ─────────────────────────────────────────────────────────
export interface PageResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
    first: boolean;
    last: boolean;
}

export interface SliceResponse<T> {
    content: T[];
    size: number;
    number: number;
    first: boolean;
    last: boolean;
    hasNext: boolean;
}
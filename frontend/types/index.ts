// ============ 공통 타입 ============

export interface CommonResponse<T> {
  status: 'SUCCESS' | 'ERROR';
  message: string;
  data: T;
}

export interface SliceResponse<T> {
  content: T[];
  hasNext: boolean;
  size: number;
  number: number;
}

// ============ 사용자 타입 ============

export type UserRole = 'USER' | 'OWNER' | 'ADMIN';
export type AuthProvider = 'LOCAL' | 'KAKAO';

export interface User {
  userId: number;
  email: string;
  nickname: string;
  username: string;
  role: UserRole;
  provider: AuthProvider;
  imageUrl?: string;
  createdAt: string;
  updatedAt: string;
}

export interface SignUpRequest {
  email: string;
  password: string;
  nickname: string;
  username: string;
}

export interface SignInRequest {
  email: string;
  password: string;
}

export interface SignInResponse {
  accessToken: string;
  userId: number;
  email: string;
  role: UserRole;
}

// ============ 상품 타입 ============

export type ProductStatus = 'NORMAL' | 'SALE' | 'SOLD_OUT';

export interface Product {
  productId: number;
  name: string;
  description: string;
  price: number;
  salePrice: number;
  stock: number;
  status: ProductStatus;
  imageUrl?: string;
  storeId: number;
  storeName: string;
  distance?: number; // 미터 단위
  createdAt: string;
  updatedAt: string;
}

export interface CreateProductRequest {
  storeId: number;
  name: string;
  description: string;
  price: number;
  salePrice: number;
  stock: number;
  status: ProductStatus;
}

// ============ 가게 타입 ============

export interface Store {
  storeId: number;
  name: string;
  description: string;
  address: string;
  latitude: number;
  longitude: number;
  phone: string;
  openTime: string;
  closeTime: string;
  imageUrl?: string;
  rating?: number;
  reviewCount?: number;
  distance?: number;
  isOpen?: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CreateStoreRequest {
  name: string;
  description: string;
  address: string;
  latitude: number;
  longitude: number;
  phone: string;
  openTime: string;
  closeTime: string;
}

// ============ 장바구니 타입 ============

export interface CartItem {
  cartId: number;
  productId: number;
  productName: string;
  productImage?: string;
  price: number;
  salePrice: number;
  quantity: number;
  stock: number;
  storeId: number;
  storeName: string;
}

export interface AddToCartRequest {
  productId: number;
  quantity: number;
}

// ============ 주문 타입 ============

export type OrderStatus = 'PENDING' | 'CONFIRMED' | 'PREPARING' | 'READY' | 'COMPLETED' | 'CANCELLED';

export interface Order {
  orderId: number;
  storeId: number;
  storeName: string;
  status: OrderStatus;
  totalPrice: number;
  discountAmount: number;
  finalPrice: number;
  products: OrderProduct[];
  createdAt: string;
  updatedAt: string;
}

export interface OrderProduct {
  orderProductId: number;
  productId: number;
  productName: string;
  productImage?: string;
  price: number;
  quantity: number;
}

export interface CreateOrderRequest {
  storeId: number;
  userCouponId?: number;
}

// ============ 구독 타입 ============

export type SubscriptionPeriod = 'WEEKLY' | 'MONTHLY';
export type DeliveryDay = 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY' | 'SATURDAY' | 'SUNDAY';

export interface Subscription {
  subscriptionId: number;
  storeId: number;
  storeName: string;
  name: string;
  description: string;
  price: number;
  period: SubscriptionPeriod;
  deliveryDays: DeliveryDay[];
  imageUrl?: string;
  createdAt: string;
  updatedAt: string;
}

export interface UserSubscription {
  userSubscriptionId: number;
  subscription: Subscription;
  status: 'ACTIVE' | 'PAUSED' | 'CANCELLED';
  startDate: string;
  nextDeliveryDate?: string;
  createdAt: string;
}

// ============ 쿠폰 타입 ============

export type CouponType = 'PERCENTAGE' | 'FIXED';
export type CouponStatus = 'ACTIVE' | 'INACTIVE' | 'EXPIRED';

export interface Coupon {
  couponId: number;
  name: string;
  description: string;
  type: CouponType;
  discountValue: number; // 퍼센트 또는 금액
  minOrderAmount?: number;
  maxDiscountAmount?: number;
  status: CouponStatus;
  validFrom: string;
  validUntil: string;
}

export interface UserCoupon {
  userCouponId: number;
  coupon: Coupon;
  isUsed: boolean;
  usedAt?: string;
  downloadedAt: string;
}

// ============ 채팅 타입 ============

export type SenderType = 'USER' | 'OWNER';

export interface ChatRoom {
  roomId: number;
  storeId: number;
  storeName: string;
  userId: number;
  userName: string;
  lastMessage?: string;
  lastMessageAt?: string;
  unreadCount: number;
  createdAt: string;
}

export interface ChatMessage {
  messageId: number;
  roomId: number;
  content: string;
  senderType: SenderType;
  senderId: number;
  senderName: string;
  isRead: boolean;
  createdAt: string;
}

export interface SendMessageRequest {
  content: string;
}

// ============ 결제 타입 ============

export interface PaymentRequest {
  orderId: number;
  amount: number;
  paymentKey: string;
}

export interface BillingKeyRequest {
  customerKey: string;
  authKey: string;
}

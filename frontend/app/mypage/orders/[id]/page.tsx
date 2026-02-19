'use client';

import { useParams } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, Store, MapPin, Clock, CreditCard, MessageCircle, FileText, CheckCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function OrderDetailPage() {
    const params = useParams();
    const orderId = params.id;

    // TODO: 실제 API 데이터로 교체
    const order = {
        id: orderId,
        orderNumber: '20260209001',
        date: '2026.02.09 14:30',
        status: 'COMPLETED',
        statusText: '주문 완료',
        storeId: 1,
        storeName: '파리바게뜨 강남점',
        storeAddress: '서울특별시 강남구 테헤란로 123',
        items: [
            {
                id: 1,
                productId: 1,
                name: '프리미엄 크루아상 3입',
                quantity: 2,
                price: 3000,
                image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
            },
            {
                id: 2,
                productId: 2,
                name: '바게트 2입',
                quantity: 1,
                price: 3500,
                image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff',
            },
        ],
        request: '포장 꼼꼼히 부탁드려요',
        payment: {
            method: '카카오페이',
            productTotal: 9500,
            couponDiscount: 1000,
            couponName: '1,000원 할인 쿠폰',
            finalTotal: 8500,
            paidAt: '2026.02.09 14:30',
        },
    };

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'PENDING':
                return 'default';
            case 'COMPLETED':
                return 'success';
            case 'CANCELLED':
                return 'default';
            default:
                return 'default';
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href="/mypage/orders" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>주문 목록으로</span>
                    </Link>
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">주문 상세</h1>
                            <p className="text-gray-600">주문번호: {order.orderNumber}</p>
                        </div>
                        <Badge variant={getStatusColor(order.status) as any}>
                            {order.statusText}
                        </Badge>
                    </div>
                </div>

                {/* 주문 상태 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-6">주문 상태</h2>

                    <div className="flex items-center p-4 bg-green-50 rounded-lg">
                        <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mr-4">
                            <CheckCircle className="w-7 h-7 text-green-500" />
                        </div>
                        <div className="flex-1">
                            <p className="font-semibold text-gray-900">주문 접수 완료</p>
                            <p className="text-sm text-gray-600">{order.date}</p>
                            <p className="text-sm text-primary-700 font-medium mt-1">
                                <Clock className="w-4 h-4 inline mr-1" />
                                30분 이내에 픽업 부탁드립니다
                            </p>
                        </div>
                    </div>
                </Card>

                {/* 픽업 매장 정보 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">픽업 매장</h2>
                    <div className="space-y-3">
                        <div className="flex items-start">
                            <Store className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                            <div className="flex-1">
                                <Link href={`/stores/${order.storeId}`}>
                                    <p className="font-semibold text-gray-900 hover:text-primary-600">
                                        {order.storeName}
                                    </p>
                                </Link>
                            </div>
                        </div>
                        <div className="flex items-start">
                            <MapPin className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                            <div>
                                <p className="font-medium text-gray-900">매장 위치</p>
                                <p className="text-sm text-gray-600">{order.storeAddress}</p>
                            </div>
                        </div>
                        {order.request && (
                            <div className="flex items-start">
                                <MessageCircle className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                                <div>
                                    <p className="font-medium text-gray-900">요청사항</p>
                                    <p className="text-sm text-gray-600">{order.request}</p>
                                </div>
                            </div>
                        )}
                    </div>
                </Card>

                {/* 주문 상품 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">주문 상품</h2>
                    <div className="space-y-4">
                        {order.items.map((item) => (
                            <div key={item.id} className="flex items-center pb-4 border-b border-gray-100 last:border-0 last:pb-0">
                                <Link href={`/products/${item.productId}`}>
                                    <img
                                        src={item.image}
                                        alt={item.name}
                                        className="w-20 h-20 object-cover rounded-lg mr-4 hover:opacity-80 transition"
                                    />
                                </Link>
                                <div className="flex-1">
                                    <Link href={`/products/${item.productId}`}>
                                        <p className="font-medium text-gray-900 hover:text-primary-500">{item.name}</p>
                                    </Link>
                                    <p className="text-sm text-gray-600">수량: {item.quantity}개</p>
                                </div>
                                <div className="text-right">
                                    <p className="font-semibold text-gray-900">
                                        {(item.price * item.quantity).toLocaleString()}원
                                    </p>
                                    <p className="text-sm text-gray-500">
                                        개당 {item.price.toLocaleString()}원
                                    </p>
                                </div>
                            </div>
                        ))}
                    </div>
                </Card>

                {/* 결제 정보 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">결제 정보</h2>
                    <div className="space-y-3 mb-6">
                        <div className="flex justify-between text-gray-700">
                            <span>상품 금액</span>
                            <span>{order.payment.productTotal.toLocaleString()}원</span>
                        </div>
                        {order.payment.couponDiscount > 0 && (
                            <div className="flex justify-between text-red-500">
                                <span>쿠폰 할인 ({order.payment.couponName})</span>
                                <span>-{order.payment.couponDiscount.toLocaleString()}원</span>
                            </div>
                        )}
                    </div>

                    <div className="pt-4 border-t border-gray-200 mb-4">
                        <div className="flex justify-between items-center">
                            <span className="text-lg font-semibold text-gray-900">총 결제 금액</span>
                            <span className="text-2xl font-bold text-primary-600">
                {order.payment.finalTotal.toLocaleString()}원
              </span>
                        </div>
                    </div>

                    <div className="flex items-center text-gray-700">
                        <CreditCard className="w-5 h-5 text-gray-400 mr-2" />
                        <span className="text-sm">결제 수단: {order.payment.method}</span>
                        <span className="text-sm text-gray-500 ml-4">결제일시: {order.payment.paidAt}</span>
                    </div>
                </Card>

                {/* 액션 버튼 */}
                <div className="space-y-3">
                    {order.status === 'PENDING' && (
                        <Button variant="outline" fullWidth size="lg" className="text-red-500 border-red-500 hover:bg-red-50">
                            주문 취소
                        </Button>
                    )}
                    {order.status === 'COMPLETED' && (
                        <>
                            <Button fullWidth size="lg">
                                재주문하기
                            </Button>
                            <Button variant="outline" fullWidth size="lg">
                                <FileText className="w-5 h-5 mr-2" />
                                영수증 보기
                            </Button>
                        </>
                    )}
                    <Link href={`/chat/${order.storeId}`}>
                        <Button variant="outline" fullWidth size="lg">
                            <MessageCircle className="w-5 h-5 mr-2" />
                            가게에 문의하기
                        </Button>
                    </Link>
                </div>
            </div>
        </div>
    );
}
'use client';

import { useParams } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, Package, MapPin, Clock, CreditCard, Phone, MessageCircle, FileText } from 'lucide-react';
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
        status: 'DELIVERING',
        statusText: '배달 중',
        items: [
            {
                id: 1,
                productId: 1,
                name: '프리미엄 크루아상 3입',
                storeName: '파리바게뜨 강남점',
                storeId: 1,
                quantity: 2,
                price: 3000,
                image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
            },
            {
                id: 2,
                productId: 2,
                name: '프리미엄 닭가슴살 샐러드',
                storeName: '샐러디 역삼점',
                storeId: 2,
                quantity: 1,
                price: 7200,
                image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
            },
        ],
        delivery: {
            address: '서울특별시 강남구 테헤란로 123',
            phone: '010-1234-5678',
            request: '문 앞에 놓아주세요',
            estimatedTime: '오늘 17:00 ~ 18:00',
        },
        payment: {
            method: '카카오페이',
            productTotal: 13200,
            deliveryFee: 0,
            couponDiscount: 0,
            finalTotal: 13200,
            paidAt: '2026.02.09 14:30',
        },
        timeline: [
            {
                status: '주문 접수',
                time: '2026.02.09 14:30',
                completed: true,
            },
            {
                status: '상품 준비 중',
                time: '2026.02.09 14:45',
                completed: true,
            },
            {
                status: '배달 중',
                time: '2026.02.09 16:30',
                completed: true,
            },
            {
                status: '배달 완료',
                time: '',
                completed: false,
            },
        ],
    };

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'PENDING':
                return 'default';
            case 'PREPARING':
                return 'warning';
            case 'DELIVERING':
                return 'subscription';
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

                {/* 배송 추적 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-6">배송 추적</h2>
                    <div className="relative">
                        {order.timeline.map((item, index) => (
                            <div key={index} className="flex items-start mb-6 last:mb-0">
                                {/* 타임라인 선 */}
                                {index !== order.timeline.length - 1 && (
                                    <div
                                        className={`absolute left-5 top-12 w-0.5 h-12 ${
                                            item.completed ? 'bg-green-500' : 'bg-gray-200'
                                        }`}
                                        style={{ marginTop: '-12px' }}
                                    ></div>
                                )}

                                {/* 아이콘 */}
                                <div
                                    className={`w-10 h-10 rounded-full flex items-center justify-center mr-4 flex-shrink-0 ${
                                        item.completed
                                            ? 'bg-green-100 text-green-600'
                                            : 'bg-gray-100 text-gray-400'
                                    }`}
                                >
                                    {item.completed ? (
                                        <Package className="w-5 h-5" />
                                    ) : (
                                        <Clock className="w-5 h-5" />
                                    )}
                                </div>

                                {/* 내용 */}
                                <div className="flex-1">
                                    <p className={`font-semibold ${item.completed ? 'text-gray-900' : 'text-gray-500'}`}>
                                        {item.status}
                                    </p>
                                    {item.time && (
                                        <p className="text-sm text-gray-600 mt-1">{item.time}</p>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>

                    {order.status === 'DELIVERING' && (
                        <div className="mt-6 p-4 bg-blue-50 rounded-lg">
                            <p className="text-sm text-blue-800">
                                📦 배송 예정 시간: <span className="font-semibold">{order.delivery.estimatedTime}</span>
                            </p>
                        </div>
                    )}
                </Card>

                {/* 배송 정보 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">배송 정보</h2>
                    <div className="space-y-3 text-gray-700">
                        <div className="flex items-start">
                            <MapPin className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                            <div>
                                <p className="font-medium text-gray-900">배송 주소</p>
                                <p className="text-sm">{order.delivery.address}</p>
                            </div>
                        </div>
                        <div className="flex items-start">
                            <Phone className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                            <div>
                                <p className="font-medium text-gray-900">연락처</p>
                                <p className="text-sm">{order.delivery.phone}</p>
                            </div>
                        </div>
                        {order.delivery.request && (
                            <div className="flex items-start">
                                <MessageCircle className="w-5 h-5 text-gray-400 mr-3 mt-0.5" />
                                <div>
                                    <p className="font-medium text-gray-900">요청사항</p>
                                    <p className="text-sm">{order.delivery.request}</p>
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
                                    <Link href={`/stores/${item.storeId}`}>
                                        <p className="text-sm text-gray-500 hover:text-primary-500">{item.storeName}</p>
                                    </Link>
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
                        <div className="flex justify-between text-gray-700">
                            <span>배송비</span>
                            <span className="text-green-600 font-medium">무료</span>
                        </div>
                        {order.payment.couponDiscount > 0 && (
                            <div className="flex justify-between text-red-500">
                                <span>쿠폰 할인</span>
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
                        <Button variant="danger" fullWidth size="lg">
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
                    <Button variant="outline" fullWidth size="lg">
                        <MessageCircle className="w-5 h-5 mr-2" />
                        가게에 문의하기
                    </Button>
                </div>
            </div>
        </div>
    );
}
'use client';

import { useSearchParams } from 'next/navigation';
import Link from 'next/link';
import { CheckCircle, Store, MapPin, Clock, Home, FileText, MessageCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function OrderCompletePage() {
    const searchParams = useSearchParams();
    const orderId = searchParams.get('orderId') || '20260209001';

    // TODO: 나중에 실제 API 데이터로 교체
    const order = {
        id: orderId,
        date: '2026.02.09 14:30',
        status: 'PENDING',
        statusText: '주문 완료',
        totalAmount: 14500,
        paymentMethod: '카카오페이',
        storeId: 1,
        storeName: '파리바게뜨 강남점',
        storeAddress: '서울특별시 강남구 테헤란로 123',
        items: [
            {
                id: 1,
                name: '프리미엄 크루아상 3입',
                quantity: 2,
                price: 3000,
                image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
            },
            {
                id: 2,
                name: '바게트 2입',
                quantity: 1,
                price: 3500,
                image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff',
            },
            {
                id: 3,
                name: '소금빵 5입',
                quantity: 1,
                price: 5000,
                image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff',
            },
        ],
    };

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 성공 메시지 */}
                <div className="text-center mb-8">
                    <div className="inline-flex items-center justify-center w-20 h-20 bg-green-100 rounded-full mb-4">
                        <CheckCircle className="w-12 h-12 text-green-500" />
                    </div>
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">주문이 완료되었습니다!</h1>
                    <p className="text-gray-600">주문번호: {order.id}</p>
                </div>

                {/* 주문 상태 */}
                <Card className="mb-6">
                    <div className="flex items-center justify-between mb-6">
                        <h2 className="text-xl font-bold text-gray-900">주문 상태</h2>
                        <Badge variant="success">{order.statusText}</Badge>
                    </div>

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
                    </div>
                </Card>

                {/* 주문 상품 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">주문 상품</h2>
                    <div className="space-y-4">
                        {order.items.map((item) => (
                            <div key={item.id} className="flex items-center">
                                <img
                                    src={item.image}
                                    alt={item.name}
                                    className="w-16 h-16 object-cover rounded-lg mr-4"
                                />
                                <div className="flex-1">
                                    <p className="font-medium text-gray-900">{item.name}</p>
                                    <p className="text-sm text-gray-600">수량: {item.quantity}개</p>
                                </div>
                                <p className="font-semibold text-gray-900">
                                    {(item.price * item.quantity).toLocaleString()}원
                                </p>
                            </div>
                        ))}
                    </div>

                    <div className="mt-6 pt-6 border-t border-gray-200">
                        <div className="flex justify-between items-center">
                            <span className="text-lg font-semibold text-gray-900">총 결제 금액</span>
                            <span className="text-2xl font-bold text-primary-600">
                {order.totalAmount.toLocaleString()}원
              </span>
                        </div>
                        <p className="text-sm text-gray-500 mt-2 text-right">
                            결제 수단: {order.paymentMethod}
                        </p>
                    </div>
                </Card>

                {/* 안내 메시지 */}
                <Card className="bg-blue-50 border-blue-200 mb-6">
                    <div className="flex items-start">
                        <div className="flex-shrink-0">
                            <svg className="w-6 h-6 text-blue-500" fill="currentColor" viewBox="0 0 20 20">
                                <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
                            </svg>
                        </div>
                        <div className="ml-3">
                            <h3 className="text-sm font-medium text-blue-800">주문 안내</h3>
                            <div className="mt-2 text-sm text-blue-700">
                                <ul className="list-disc pl-5 space-y-1">
                                    <li>주문 상태는 마이페이지에서 확인하실 수 있습니다</li>
                                    <li>문의사항은 아래 "가게에 문의하기" 버튼을 통해 채팅으로 문의하실 수 있습니다</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </Card>

                {/* 버튼 */}
                <div className="space-y-3">
                    <Link href={`/mypage/order/${order.id}`}>
                        <Button fullWidth size="lg">
                            <FileText className="w-5 h-5 mr-2" />
                            주문 상세보기
                        </Button>
                    </Link>
                    <Link href={`/chat/${order.storeId}`}>
                        <Button variant="outline" fullWidth size="lg">
                            <MessageCircle className="w-5 h-5 mr-2" />
                            가게에 문의하기
                        </Button>
                    </Link>
                </div>

                <div className="mt-8">
                    <Link href="/" className="block text-center">
                        <Button variant="ghost" fullWidth size="lg" className="text-gray-600 hover:text-gray-900">
                            <Home className="w-5 h-5 mr-2" />
                            홈으로 돌아가기
                        </Button>
                    </Link>
                </div>
            </div>
        </div>
    );
}
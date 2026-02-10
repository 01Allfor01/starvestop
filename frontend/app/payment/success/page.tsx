'use client';

import { useSearchParams, useRouter } from 'next/navigation';
import { CheckCircle, Package, Clock, Home } from 'lucide-react';
import Card from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import Badge from '@/components/ui/Badge';

export default function PaymentSuccessPage() {
    const searchParams = useSearchParams();
    const router = useRouter();

    const orderId = searchParams.get('orderId') || '20260210001';
    const amount = searchParams.get('amount') || '0';

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 성공 메시지 */}
                <Card className="text-center mb-6">
                    <CheckCircle className="w-20 h-20 text-green-500 mx-auto mb-4" />
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">결제 완료!</h1>
                    <p className="text-gray-600 mb-6">
                        주문이 성공적으로 완료되었습니다
                    </p>

                    <div className="inline-flex items-center justify-center bg-primary-50 px-6 py-3 rounded-lg mb-6">
                        <span className="text-sm text-gray-600 mr-2">결제 금액</span>
                        <span className="text-2xl font-bold text-primary-600">
                            {parseInt(amount).toLocaleString()}원
                        </span>
                    </div>

                    <div className="pt-6 border-t border-gray-200">
                        <div className="flex items-center justify-center text-gray-600 mb-2">
                            <Package className="w-5 h-5 mr-2" />
                            <span>주문번호</span>
                        </div>
                        <p className="text-xl font-semibold text-gray-900">{orderId}</p>
                    </div>
                </Card>

                {/* 픽업 안내 */}
                <Card className="mb-6">
                    <div className="flex items-start">
                        <div className="flex-shrink-0">
                            <div className="w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center">
                                <Clock className="w-6 h-6 text-primary-600" />
                            </div>
                        </div>
                        <div className="ml-4 flex-1">
                            <h3 className="text-lg font-semibold text-gray-900 mb-2">픽업 안내</h3>
                            <div className="space-y-2 text-gray-600">
                                <p className="flex items-center">
                                    <span className="w-2 h-2 bg-primary-500 rounded-full mr-2"></span>
                                    30분 이내에 픽업 부탁드립니다
                                </p>
                                <p className="flex items-center">
                                    <span className="w-2 h-2 bg-primary-500 rounded-full mr-2"></span>
                                    주문번호를 가게에 알려주세요
                                </p>
                                <p className="flex items-center">
                                    <span className="w-2 h-2 bg-primary-500 rounded-full mr-2"></span>
                                    준비 완료 시 알림을 보내드립니다
                                </p>
                            </div>
                        </div>
                    </div>
                </Card>

                {/* 주문 상태 */}
                <Card className="mb-6">
                    <h3 className="text-lg font-semibold text-gray-900 mb-4">주문 상태</h3>
                    <div className="flex items-center justify-between">
                        <div className="flex-1">
                            <div className="flex items-center mb-2">
                                <div className="w-8 h-8 bg-green-500 text-white rounded-full flex items-center justify-center text-sm font-semibold">
                                    1
                                </div>
                                <span className="ml-3 font-medium text-gray-900">주문 접수</span>
                            </div>
                        </div>
                        <div className="w-12 h-0.5 bg-gray-300 mx-2"></div>
                        <div className="flex-1">
                            <div className="flex items-center mb-2">
                                <div className="w-8 h-8 bg-gray-300 text-white rounded-full flex items-center justify-center text-sm font-semibold">
                                    2
                                </div>
                                <span className="ml-3 text-gray-500">준비 중</span>
                            </div>
                        </div>
                        <div className="w-12 h-0.5 bg-gray-300 mx-2"></div>
                        <div className="flex-1">
                            <div className="flex items-center mb-2">
                                <div className="w-8 h-8 bg-gray-300 text-white rounded-full flex items-center justify-center text-sm font-semibold">
                                    3
                                </div>
                                <span className="ml-3 text-gray-500">픽업 대기</span>
                            </div>
                        </div>
                    </div>
                    <div className="mt-4 p-3 bg-green-50 rounded-lg">
                        <p className="text-sm text-green-800 text-center">
                            ✅ 주문이 접수되었습니다
                        </p>
                    </div>
                </Card>

                {/* 버튼 */}
                <div className="space-y-3">
                    <Button
                        fullWidth
                        size="lg"
                        onClick={() => router.push(`/mypage/orders/${orderId}`)}
                    >
                        <Package className="w-5 h-5 mr-2" />
                        주문 상세 보기
                    </Button>
                    <Button
                        variant="outline"
                        fullWidth
                        onClick={() => router.push('/')}
                    >
                        <Home className="w-5 h-5 mr-2" />
                        홈으로 가기
                    </Button>
                </div>
            </div>
        </div>
    );
}
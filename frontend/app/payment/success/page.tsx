'use client';

import { useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { CheckCircle, Loader2 } from 'lucide-react';
import { paymentsApi } from '@/lib/api/payments'; // 결제 승인 API
import { cartApi } from '@/lib/api/cart';         // 장바구니 초기화 API
import Card from '@/components/ui/Card';

export default function PaymentSuccessPage() {
    const router = useRouter();
    const searchParams = useSearchParams();

    // 상태 관리: 'READY' (로딩 중) | 'SUCCESS' (승인 완료)
    const [status, setStatus] = useState<'READY' | 'SUCCESS'>('READY');

    useEffect(() => {
        const confirm = async () => {
            const paymentKey = searchParams.get('paymentKey');
            const orderIdUUID = searchParams.get('orderId'); // 토스에서 온 UUID
            const amount = searchParams.get('amount');
            const paymentType = searchParams.get('paymentType');

            if (!paymentKey || !orderIdUUID || !amount) return;

            try {
                // 1. 백엔드 승인 요청 (결과로 실제 DB ID인 PK를 받아옵니다)
                // response 구조가 CommonResponse<Long> 이므로 response.data에 ID가 들어있을 겁니다.
                const response = await paymentsApi.confirmPayment({
                    paymentKey,
                    orderId: orderIdUUID,
                    amount: Number(amount),
                    paymentType: paymentType || 'NORMAL'
                });

                // 백엔드에서 준 실제 숫자 ID (예: 18)
                const realDbOrderId = response.data;

                await cartApi.clearCart();
                setStatus('SUCCESS');

                // 2. 2초 후 'UUID'가 아닌 '진짜 숫자 ID'를 들고 이동합니다.
                setTimeout(() => {
                    router.push(`/order/complete?orderId=${realDbOrderId}`);
                }, 2000);

            } catch (error) {
                console.error('결제 승인 실패:', error);
                router.push('/payment/fail?message=결제 승인에 실패했습니다');
            }
        };

        confirm();
    }, [searchParams, router]);

    return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
            <Card className="max-w-md w-full text-center py-16 px-8 shadow-lg">
                {status === 'READY' ? (
                    // 단계 1: 백엔드 승인 대기 중
                    <>
                        <Loader2 className="w-20 h-20 text-primary-500 mx-auto mb-6 animate-spin" />
                        <h2 className="text-2xl font-bold text-gray-900 mb-2">결제 승인 중</h2>
                        <p className="text-gray-600">잠시만 기다려주세요...</p>
                    </>
                ) : (
                    // 단계 2: 승인 성공 후 2초간 보여줄 화면
                    <>
                        <div className="flex justify-center mb-6">
                            <div className="p-4 bg-green-100 rounded-full">
                                <CheckCircle className="w-20 h-20 text-green-500" />
                            </div>
                        </div>
                        <h2 className="text-3xl font-bold text-gray-900 mb-4">결제 완료!</h2>
                        <p className="text-gray-600 mb-8 text-lg">
                            곧 주문 상세 페이지로 이동합니다.
                        </p>
                    </>
                )}
            </Card>
        </div>
    );
}
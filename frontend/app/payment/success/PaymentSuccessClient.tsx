// app/payment/success/PaymentSuccessClient.tsx
'use client';

import { useEffect, useState, useRef } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { CheckCircle, Loader2 } from 'lucide-react';
import { paymentsApi } from '@/lib/api/payments';
import { cartApi } from '@/lib/api/cart';
import Card from '@/components/ui/Card';

export default function PaymentSuccessClient() {
    const router = useRouter();
    const searchParams = useSearchParams();

    const calledRef = useRef(false);

    const [status, setStatus] = useState<'READY' | 'SUCCESS'>('READY');

    useEffect(() => {
        if (calledRef.current) return;
        calledRef.current = true;

        const run = async () => {
            const paymentKey = searchParams.get('paymentKey');
            const orderIdUUID = searchParams.get('orderId');
            const amountStr = searchParams.get('amount');
            const paymentType = searchParams.get('paymentType');

            if (!paymentKey || !orderIdUUID || !amountStr) {
                router.push('/payment/fail?message=필수 결제 정보가 누락되었습니다');
                return;
            }

            const amount = Number(amountStr);
            if (!Number.isFinite(amount) || amount <= 0) {
                router.push('/payment/fail?message=결제 금액이 올바르지 않습니다');
                return;
            }

            try {
                const response = await paymentsApi.confirmPayment({
                    paymentKey,
                    orderId: orderIdUUID,
                    amount, // ✅ 백엔드 숫자 요구에 맞춤
                    paymentType: paymentType || 'NORMAL',
                });

                const realDbOrderId = response.data;

                await cartApi.clearCart();
                setStatus('SUCCESS');

                setTimeout(() => {
                    router.push(`/order/complete?orderId=${realDbOrderId}`);
                }, 2000);
            } catch (error) {
                console.error('결제 승인 실패:', error);
                router.push('/payment/fail?message=결제 승인에 실패했습니다');
            }
        };

        run();
    }, [searchParams, router]);

    return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
            <Card className="max-w-md w-full text-center py-16 px-8 shadow-lg">
                {status === 'READY' ? (
                    <>
                        <Loader2 className="w-20 h-20 text-primary-500 mx-auto mb-6 animate-spin" />
                        <h2 className="text-2xl font-bold text-gray-900 mb-2">결제 승인 중</h2>
                        <p className="text-gray-600">잠시만 기다려주세요...</p>
                    </>
                ) : (
                    <>
                        <div className="flex justify-center mb-6">
                            <div className="p-4 bg-green-100 rounded-full">
                                <CheckCircle className="w-20 h-20 text-green-500" />
                            </div>
                        </div>
                        <h2 className="text-3xl font-bold text-gray-900 mb-4">결제 완료!</h2>
                        <p className="text-gray-600 mb-8 text-lg">곧 주문 상세 페이지로 이동합니다.</p>
                    </>
                )}
            </Card>
        </div>
    );
}

'use client';

import { useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { Loader2 } from 'lucide-react';
import Card from '@/components/ui/Card';

export default function PaymentPage() {
    const router = useRouter();
    const searchParams = useSearchParams();

    const orderId = searchParams.get('orderId');
    const amount = searchParams.get('amount');

    useEffect(() => {
        // TODO: 실제 결제 API 호출
        // 지금은 시뮬레이션으로 3초 후 성공 페이지로 이동

        const timer = setTimeout(() => {
            // 실제로는 결제 API 응답에 따라 success 또는 fail로 이동
            router.push(`/payment/success?orderId=${orderId}&amount=${amount}`);

            // 실패 시:
            // router.push('/payment/fail?code=PAY_PROCESS_CANCELED&message=결제가 취소되었습니다');
        }, 3000);

        return () => clearTimeout(timer);
    }, [orderId, amount, router]);

    return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
            <Card className="max-w-md w-full text-center py-12">
                <Loader2 className="w-16 h-16 text-primary-500 mx-auto mb-6 animate-spin" />
                <h2 className="text-2xl font-bold text-gray-900 mb-2">결제 진행 중</h2>
                <p className="text-gray-600 mb-4">
                    잠시만 기다려주세요...
                </p>
                {amount && (
                    <p className="text-xl font-semibold text-primary-600">
                        {parseInt(amount).toLocaleString()}원
                    </p>
                )}
                <div className="mt-8 text-sm text-gray-500">
                    <p>• 창을 닫지 마세요</p>
                    <p>• 결제가 완료될 때까지 기다려주세요</p>
                </div>
            </Card>
        </div>
    );
}
// app/payment/fail/PaymentFailClient.tsx
'use client';

import { useSearchParams, useRouter } from 'next/navigation';
import { XCircle } from 'lucide-react';
import Card from '@/components/ui/Card';
import Button from '@/components/ui/Button';

export default function PaymentFailClient() {
    const searchParams = useSearchParams();
    const router = useRouter();

    const code = searchParams.get('code');
    const message = searchParams.get('message') || '결제에 실패했습니다.';
    const orderId = searchParams.get('orderId'); // 필요하면 사용 (현재 UI엔 미사용)

    const getErrorMessage = (c: string | null) => {
        switch (c) {
            case 'PAY_PROCESS_CANCELED':
                return '사용자가 결제를 취소했습니다.';
            case 'PAY_PROCESS_ABORTED':
                return '결제가 중단되었습니다.';
            case 'REJECT_CARD_PAYMENT':
                return '카드 결제가 거부되었습니다.';
            default:
                return message;
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
            <Card className="max-w-md w-full text-center">
                <XCircle className="w-16 h-16 text-red-500 mx-auto mb-4" />
                <h2 className="text-2xl font-bold text-gray-900 mb-2">결제 실패</h2>
                <p className="text-gray-600 mb-6">{getErrorMessage(code)}</p>

                {code && <p className="text-sm text-gray-500 mb-6">오류 코드: {code}</p>}

                <div className="space-y-3">
                    <Button onClick={() => router.push('/order')} fullWidth>
                        주문서로 돌아가기
                    </Button>
                    <Button variant="outline" onClick={() => router.push('/')} fullWidth>
                        홈으로 가기
                    </Button>
                </div>
            </Card>
        </div>
    );
}

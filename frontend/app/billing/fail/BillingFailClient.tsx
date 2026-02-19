'use client';

import { useRouter, useSearchParams } from 'next/navigation';
import { XCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';

export default function BillingFailClient() {
    const router = useRouter();
    const searchParams = useSearchParams();

    const code = searchParams.get('code');
    const message = searchParams.get('message');

    return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center py-8">
            <div className="max-w-md mx-auto px-4">
                <Card className="text-center py-12">
                    <div className="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
                        <XCircle className="w-10 h-10 text-red-600" />
                    </div>
                    <h2 className="text-2xl font-bold text-gray-900 mb-2">카드 등록 실패</h2>
                    <p className="text-gray-600 mb-6">
                        {message || '카드 등록 중 오류가 발생했습니다.'}
                    </p>
                    {code && <p className="text-sm text-gray-500 mb-6">오류 코드: {code}</p>}
                    <div className="space-y-3">
                        <Button fullWidth onClick={() => router.push('/subscriptions')}>
                            구독 목록으로
                        </Button>
                        <Button variant="outline" fullWidth onClick={() => router.back()}>
                            다시 시도하기
                        </Button>
                    </div>
                </Card>
            </div>
        </div>
    );
}

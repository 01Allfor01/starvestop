'use client';

export const dynamic = 'force-dynamic';

import { useEffect, useState, useRef, Suspense } from 'react'; // Suspense 추가
import { useRouter, useSearchParams } from 'next/navigation';
import { CheckCircle, Loader2 } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import { billingApi } from '@/lib/api/billing';

// 1. 기존 로직을 별도의 컨텐츠 컴포넌트로 분리
function BillingSuccessContent() {
    const calledRef = useRef(false);
    const router = useRouter();
    const searchParams = useSearchParams();

    const authKey = searchParams.get('authKey');
    const subscriptionId = searchParams.get('subscriptionId');

    const [processing, setProcessing] = useState(true);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState('');


    useEffect(() => {
        if (calledRef.current) return;   // 🔥 중복 방지
        calledRef.current = true;

        if (!authKey || !subscriptionId) {
            setError('필수 정보가 누락되었습니다.');
            setProcessing(false);
            return;
        }

        const confirmBilling = async () => {
            try {
                setProcessing(true);

                // 백엔드로 최종 승인 요청
                await billingApi.confirmBilling({
                    authKey: authKey,
                    subscriptionId: Number(subscriptionId),
                });

                setSuccess(true);
            } catch (error: any) {
                console.error('❌ 빌링 승인 실패:', error);
                const msg = error.response?.data?.message || '구독 등록에 실패했습니다.';
                setError(msg);
            } finally {
                setProcessing(false);
            }
        };

        confirmBilling();
    }, [authKey, subscriptionId]);

    if (processing) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <Loader2 className="w-12 h-12 text-primary-500 animate-spin mx-auto mb-4" />
                    <p className="text-gray-600">구독 등록 중...</p>
                    <p className="text-sm text-gray-500 mt-2">잠시만 기다려주세요</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center py-8">
                <div className="max-w-md mx-auto px-4">
                    <Card className="text-center py-12">
                        <div className="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
                            <span className="text-3xl">❌</span>
                        </div>
                        <h2 className="text-2xl font-bold text-gray-900 mb-2">구독 등록 실패</h2>
                        <p className="text-gray-600 mb-6">{error}</p>
                        <div className="space-y-3">
                            <Button
                                fullWidth
                                onClick={() => router.push(`/subscriptions/${subscriptionId}`)}
                            >
                                구독 상품으로 돌아가기
                            </Button>
                            <Button
                                variant="outline"
                                fullWidth
                                onClick={() => router.push('/subscriptions')}
                            >
                                구독 목록 보기
                            </Button>
                        </div>
                    </Card>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center py-8">
            <div className="max-w-md mx-auto px-4">
                <Card className="text-center py-12">
                    <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                        <CheckCircle className="w-10 h-10 text-green-600" />
                    </div>
                    <h2 className="text-2xl font-bold text-gray-900 mb-2">구독 등록 완료!</h2>
                    <p className="text-gray-600 mb-6">
                        정기결제 카드가 등록되었습니다.<br />
                        매월 자동으로 결제됩니다.
                    </p>
                    <div className="space-y-3">
                        <Button
                            fullWidth
                            onClick={() => router.push('/mypage/subscriptions')}
                        >
                            내 구독 관리
                        </Button>
                        <Button
                            variant="outline"
                            fullWidth
                            onClick={() => router.push('/subscriptions')}
                        >
                            다른 구독 둘러보기
                        </Button>
                    </div>
                </Card>

                {/* 안내 */}
                <Card className="mt-6 bg-blue-50 border-blue-200">
                    <div className="text-sm text-blue-700">
                        <p className="font-semibold mb-2">💡 알아두세요</p>
                        <ul className="space-y-1">
                            <li>• 첫 결제는 구독 시작일에 진행됩니다</li>
                            <li>• 마이페이지에서 구독 관리가 가능합니다</li>
                            <li>• 언제든지 구독을 취소할 수 있습니다</li>
                        </ul>
                    </div>
                </Card>
            </div>
        </div>
    );
}

// 2. 기본 export 페이지에서 Suspense로 감싸기
export default function BillingSuccessPage() {
    return (
        <Suspense fallback={
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <Loader2 className="w-12 h-12 text-primary-500 animate-spin" />
            </div>
        }>
            <BillingSuccessContent />
        </Suspense>
    );
}
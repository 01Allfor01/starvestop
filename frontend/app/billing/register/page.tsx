'use client';

import { useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { ArrowLeft, CreditCard, Loader2, Shield } from 'lucide-react';
import Link from 'next/link';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import { usersApi } from '@/lib/api/users';
import { subscriptionsApi } from '@/lib/api/subscriptions';

export default function BillingRegisterPage() {
    const router = useRouter();
    const searchParams = useSearchParams();
    const subscriptionId = searchParams.get('subscriptionId');

    const [loading, setLoading] = useState(true);
    const [processing, setProcessing] = useState(false);
    const [subscription, setSubscription] = useState<any>(null);
    const [userInfo, setUserInfo] = useState<any>(null);

    // 토스 SDK 로드 및 초기화
    useEffect(() => {
        if (!subscriptionId) {
            alert('구독 정보가 없습니다.');
            router.push('/subscriptions');
            return;
        }

        const loadData = async () => {
            try {
                setLoading(true);

                // 구독 정보 로드
                const subData = await subscriptionsApi.getSubscription(Number(subscriptionId));
                setSubscription(subData);

                // 유저 정보 로드
                const userData = await usersApi.getMyInfo();
                setUserInfo(userData);

                // 토스 SDK 스크립트 로드
                const script = document.createElement('script');
                script.src = 'https://js.tosspayments.com/v2/standard';
                script.async = true;
                document.body.appendChild(script);

                script.onload = () => {
                    setLoading(false);
                };

                script.onerror = () => {
                    alert('결제 모듈 로딩에 실패했습니다.');
                    setLoading(false);
                };
            } catch (error) {
                console.error('❌ 데이터 로딩 실패:', error);
                alert('정보를 불러오는데 실패했습니다.');
                router.push('/subscriptions');
            }
        };

        loadData();
    }, [subscriptionId, router]);

    const handleRegisterCard = async () => {
        if (!userInfo || !subscription) {
            alert('사용자 정보를 불러오는 중입니다.');
            return;
        }

        try {
            setProcessing(true);

            // ✅ 1단계: 백엔드에 UserSubscription 생성 요청 먼저 실행
            // 백엔드 로직상 DB에 구독 정보를 먼저 생성해야 한다면 여기서 호출합니다.
            console.log('🔄 구독 정보 생성 중...');
            await subscriptionsApi.subscribe(Number(subscriptionId));

            // 2단계: 토스 페이먼츠 연동 시작
            const clientKey = process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY;
            if (!clientKey) {
                throw new Error('토스 클라이언트 키가 설정되지 않았습니다.');
            }

            // customerKey는 백엔드 User 엔티티의 userKey 사용
            const customerKey = `${userInfo.userKey}`;

            const tossPayments = window.TossPayments(clientKey);
            const payment = tossPayments.payment({ customerKey });

            // 3단계: 카드 등록창 호출 (빌링 키 발급 요청)
            await payment.requestBillingAuth({
                method: 'CARD',
                successUrl: `${window.location.origin}/billing/success?subscriptionId=${subscriptionId}`,
                failUrl: `${window.location.origin}/billing/fail`,
                customerEmail: userInfo.email || 'customer@example.com',
                customerName: userInfo.name || userInfo.nickname || '사용자',
            });

        } catch (error: any) {
            console.error('❌ 프로세스 실패:', error);

            // 구독 생성 API에서 에러가 났는지, 토스에서 났는지에 따라 메시지 분기 가능
            if (error.response) {
                alert(`구독 생성 실패: ${error.response.data.message || '오류가 발생했습니다.'}`);
            } else {
                alert('카드 등록 프로세스 중 오류가 발생했습니다.');
            }
            setProcessing(false);
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <Loader2 className="w-12 h-12 text-primary-500 animate-spin mx-auto mb-4" />
                    <p className="text-gray-600">결제 준비 중...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link
                        href={`/subscriptions/${subscriptionId}`}
                        className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4"
                    >
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>구독 상세로 돌아가기</span>
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">정기결제 카드 등록</h1>
                    <p className="text-gray-600">자동 결제를 위한 카드를 등록해주세요</p>
                </div>

                {/* 구독 정보 */}
                {subscription && (
                    <Card className="mb-6 border-2 border-secondary-200">
                        <h3 className="text-lg font-bold text-gray-900 mb-2">구독 상품</h3>
                        <p className="text-gray-700 mb-1">{subscription.name}</p>
                        <p className="text-2xl font-bold text-secondary-600">
                            {subscription.price.toLocaleString()}원 <span className="text-base text-gray-500">/월</span>
                        </p>
                    </Card>
                )}

                {/* 안내 사항 */}
                <Card className="mb-6 bg-blue-50 border-blue-200">
                    <div className="flex items-start">
                        <Shield className="w-6 h-6 text-blue-500 mr-3 flex-shrink-0 mt-1" />
                        <div>
                            <h3 className="text-sm font-medium text-blue-800 mb-2">정기결제 안내</h3>
                            <ul className="text-sm text-blue-700 space-y-1">
                                <li>• 등록하신 카드로 매월 자동 결제됩니다</li>
                                <li>• 결제일은 구독 시작일 기준 매월 같은 날짜입니다</li>
                                <li>• 카드 정보는 안전하게 암호화되어 저장됩니다</li>
                                <li>• 언제든지 구독을 취소할 수 있습니다</li>
                            </ul>
                        </div>
                    </div>
                </Card>

                {/* 카드 등록 버튼 */}
                <Card className="text-center py-12">
                    <CreditCard className="w-16 h-16 text-primary-500 mx-auto mb-4" />
                    <h3 className="text-xl font-bold text-gray-900 mb-2">카드 등록하기</h3>
                    <p className="text-gray-600 mb-6">
                        토스페이먼츠 안전결제 시스템을 통해<br />
                        카드를 등록합니다
                    </p>
                    <Button
                        size="lg"
                        onClick={handleRegisterCard}
                        disabled={processing}
                        className="mx-auto"
                    >
                        {processing ? (
                            <>
                                <Loader2 className="w-5 h-5 mr-2 animate-spin" />
                                처리 중...
                            </>
                        ) : (
                            <>
                                <CreditCard className="w-5 h-5 mr-2" />
                                카드 등록하기
                            </>
                        )}
                    </Button>
                </Card>

                {/* 보안 안내 */}
                <div className="mt-6 text-center text-sm text-gray-500">
                    <p>🔒 토스페이먼츠의 안전한 결제 시스템을 사용합니다</p>
                </div>
            </div>
        </div>
    );
}
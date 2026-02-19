'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { ArrowLeft, Loader2, CreditCard, Calendar, Clock } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { subscriptionsApi, UserSubscription, Subscription } from '@/lib/api/subscriptions';

// 병합된 구독 데이터 타입
interface MergedSubscription extends UserSubscription {
    detail?: Subscription;
}

// 요일/식사시간 매핑
const dayMap: Record<string, string> = {
    'MONDAY': '월',
    'TUESDAY': '화',
    'WEDNESDAY': '수',
    'THURSDAY': '목',
    'FRIDAY': '금',
    'SATURDAY': '토',
    'SUNDAY': '일'
};

const mealTimeMap: Record<string, string> = {
    'BREAKFAST': '아침',
    'LUNCH': '점심',
    'DINNER': '저녁'
};

// 요일/식사시간 포맷팅
const formatSubscriptionDetails = (detail?: Subscription) => {
    if (!detail) return '';
    const days = detail.dayList.map(d => dayMap[d] || d).join(', ');
    const times = detail.mealTimeList.map(t => mealTimeMap[t] || t).join(', ');
    return `${days} / ${times}`;
};

// 다음 결제일 계산 (만료일 + 1일)
const getNextPaymentDate = (expiresAt: string) => {
    const date = new Date(expiresAt);
    date.setDate(date.getDate() + 1);
    return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
    });
};

export default function MySubscriptionsPage() {
    const router = useRouter();
    const [subscriptions, setSubscriptions] = useState<MergedSubscription[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchSubscriptions();
    }, []);

    const fetchSubscriptions = async () => {
        try {
            setLoading(true);
            const mySubs = await subscriptionsApi.getMySubscriptions();

            // 각 구독의 상세 정보 병합
            const mergedSubs = await Promise.all(
                mySubs.map(async (sub) => {
                    try {
                        const detail = await subscriptionsApi.getSubscription(sub.subscriptionId);
                        return { ...sub, detail };
                    } catch (e) {
                        return sub;
                    }
                })
            );

            setSubscriptions(mergedSubs);
        } catch (error) {
            console.error('❌ 구독 목록 로딩 실패:', error);
        } finally {
            setLoading(false);
        }
    };

    // 구독 취소
    const handleUnsubscribe = async (id: number, subscriptionName: string) => {
        if (confirm(
            `${subscriptionName} 구독을 취소하시겠습니까?\n\n⚠️ 이용 기간에 대해 할인 가격이 아닌 정산가로 차감 후 차액이 환불됩니다.`
        )) {
            try {
                await subscriptionsApi.unsubscribe(id);
                alert('구독이 취소되었습니다.');
                fetchSubscriptions(); // 목록 새로고침
            } catch (error) {
                console.error('❌ 구독 취소 실패:', error);
                alert('구독 취소에 실패했습니다.');
            }
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <Loader2 className="w-12 h-12 text-primary-500 animate-spin" />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href="/mypage" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>마이페이지로</span>
                    </Link>
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">구독 관리</h1>
                            <p className="text-gray-600">
                                총 <span className="text-secondary-600 font-semibold">{subscriptions.length}개</span>의 구독
                            </p>
                        </div>
                        <Link href="/subscriptions">
                            <Button variant="outline">
                                구독 상품 보기
                            </Button>
                        </Link>
                    </div>
                </div>

                {/* 구독 목록 */}
                {subscriptions.length === 0 ? (
                    <Card className="text-center py-16">
                        <CreditCard className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600 mb-6">구독 중인 상품이 없습니다</p>
                        <Link href="/subscriptions">
                            <Button>구독 상품 보기</Button>
                        </Link>
                    </Card>
                ) : (
                    <div className="space-y-4">
                        {subscriptions.map((sub) => (
                            <Card key={sub.id} className="border-2 border-secondary-200">
                                <div className="flex justify-between items-start mb-4">
                                    <div className="flex-1">
                                        <Badge variant="subscription" className="mb-2">
                                            {sub.status}
                                        </Badge>
                                        <h3 className="text-xl font-bold text-gray-900 mb-1">
                                            {sub.subscriptionName}
                                        </h3>
                                        <p className="text-gray-600 mb-2">{sub.storeName}</p>

                                        {/* 요일 및 식사 시간 정보 */}
                                        {sub.detail && (
                                            <div className="flex items-center text-sm text-gray-600 mb-2">
                                                <Calendar className="w-4 h-4 mr-1.5 text-secondary-500" />
                                                <span>{formatSubscriptionDetails(sub.detail)}</span>
                                            </div>
                                        )}

                                        {/* 다음 결제일 */}
                                        <div className="flex items-center text-sm text-gray-600">
                                            <Clock className="w-4 h-4 mr-1.5 text-gray-500" />
                                            <span>다음 결제일: {getNextPaymentDate(sub.expiresAt)}</span>
                                        </div>
                                    </div>

                                    <div className="text-right">
                                        <span className="text-2xl font-bold text-secondary-600">
                                            {sub.price.toLocaleString()}원
                                        </span>
                                        <span className="text-gray-500 ml-1">/월</span>
                                    </div>
                                </div>

                                <div className="bg-secondary-50 rounded-lg p-4 mb-4">
                                    <p className="text-sm text-gray-700">
                                        만료일: <span className="font-semibold">{new Date(sub.expiresAt).toLocaleDateString('ko-KR')}</span>
                                    </p>
                                </div>

                                {/* 버튼 */}
                                <div className="flex space-x-3">
                                    <Link href={`/mypage/subscriptions/${sub.id}`} className="flex-1">
                                        <Button variant="secondary" size="sm" fullWidth>
                                            상세 정보
                                        </Button>
                                    </Link>
                                    <Button
                                        variant="outline"
                                        size="sm"
                                        className="flex-1 text-red-500 hover:bg-red-50 border-red-200"
                                        onClick={() => handleUnsubscribe(sub.id, sub.subscriptionName)}
                                    >
                                        구독 취소
                                    </Button>
                                </div>
                            </Card>
                        ))}
                    </div>
                )}

                {/* 안내 사항 */}
                {subscriptions.length > 0 && (
                    <Card className="mt-8 bg-blue-50 border-blue-200">
                        <div className="flex items-start">
                            <div className="flex-shrink-0">
                                <svg className="w-6 h-6 text-blue-500" fill="currentColor" viewBox="0 0 20 20">
                                    <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
                                </svg>
                            </div>
                            <div className="ml-3">
                                <h3 className="text-sm font-medium text-blue-800">구독 안내</h3>
                                <div className="mt-2 text-sm text-blue-700">
                                    <ul className="list-disc pl-5 space-y-1">
                                        <li>구독은 매월 자동으로 갱신됩니다</li>
                                        <li>구독 취소 시 현재 기간 종료 후 자동 해지됩니다</li>
                                        <li>환불은 이용 기간에 대해 정산가로 차감 후 차액이 환불됩니다</li>
                                        <li>결제 수단은 마이페이지에서 변경 가능합니다</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </Card>
                )}
            </div>
        </div>
    );
}
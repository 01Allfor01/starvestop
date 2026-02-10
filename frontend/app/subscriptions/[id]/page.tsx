'use client';

import { useParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, Calendar, TrendingUp, Check, Pause, Edit, X } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function SubscriptionDetailPage() {
    const params = useParams();
    const router = useRouter();
    const subscriptionId = params.id;

    // TODO: 실제 API 데이터로 교체
    const subscription = {
        id: subscriptionId,
        name: '샐러드 정기구독',
        storeName: '샐러디 역삼점',
        description: '신선한 샐러드를 매주 화요일에 받아보세요',
        price: 28000,
        period: 'weekly',
        periodText: '주 1회',
        deliveryDays: ['화요일'],
        nextDelivery: '2026.02.12',
        status: 'ACTIVE',
        startDate: '2026.01.01',
        image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
        features: ['매주 다른 메뉴', '무료 배송', '언제든 해지 가능'],
        subscribers: 1234,
        deliveryAddress: '서울특별시 강남구 테헤란로 123',
        paymentMethod: '카카오페이',
    };

    const deliveryHistory = [
        {
            id: 1,
            date: '2026.02.05',
            status: 'DELIVERED',
            items: '프리미엄 닭가슴살 샐러드, 시저 샐러드',
        },
        {
            id: 2,
            date: '2026.01.29',
            status: 'DELIVERED',
            items: '연어 샐러드, 그린 샐러드',
        },
        {
            id: 3,
            date: '2026.01.22',
            status: 'DELIVERED',
            items: '참치 샐러드, 치킨 샐러드',
        },
    ];

    const handlePause = () => {
        if (confirm('구독을 일시정지 하시겠습니까?')) {
            alert('구독이 일시정지되었습니다.');
        }
    };

    const handleCancel = () => {
        if (confirm('정말 구독을 해지하시겠습니까? 다음 결제일부터 배송이 중단됩니다.')) {
            alert('구독이 해지되었습니다.');
            router.push('/mypage');
        }
    };

    const handleChangeDeliveryDate = () => {
        alert('배송일 변경 기능은 준비 중입니다.');
    };

    const handleChangeAddress = () => {
        alert('배송지 변경 기능은 준비 중입니다.');
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-secondary-50 to-secondary-100 py-8">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href="/subscriptions" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>구독 목록으로</span>
                    </Link>
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">{subscription.name}</h1>
                            <p className="text-gray-600">{subscription.storeName}</p>
                        </div>
                        <Badge variant={subscription.status === 'ACTIVE' ? 'success' : 'default'}>
                            {subscription.status === 'ACTIVE' ? '구독중' : '일시정지'}
                        </Badge>
                    </div>
                </div>

                {/* 구독 정보 */}
                <Card className="border-2 border-secondary-200 mb-6">
                    <div className="flex flex-col md:flex-row gap-6">
                        <img
                            src={subscription.image}
                            alt={subscription.name}
                            className="w-full md:w-48 h-48 object-cover rounded-xl"
                        />
                        <div className="flex-1">
                            <Badge variant="subscription" className="mb-3">{subscription.periodText}</Badge>
                            <p className="text-gray-600 mb-4">{subscription.description}</p>

                            <div className="space-y-2 mb-4">
                                {subscription.features.map((feature, index) => (
                                    <div key={index} className="flex items-center text-sm text-gray-700">
                                        <Check className="w-4 h-4 text-secondary-500 mr-2 flex-shrink-0" />
                                        {feature}
                                    </div>
                                ))}
                            </div>

                            <div className="flex items-baseline">
                <span className="text-3xl font-bold text-secondary-600">
                  {subscription.price.toLocaleString()}원
                </span>
                                <span className="text-gray-500 ml-2">
                  /{subscription.period === 'weekly' ? '주' : '월'}
                </span>
                            </div>
                        </div>
                    </div>
                </Card>

                {/* 다음 배송 */}
                <Card className="bg-gradient-to-br from-secondary-500 to-secondary-600 text-white mb-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-secondary-100 mb-1">다음 배송일</p>
                            <p className="text-2xl font-bold">{subscription.nextDelivery}</p>
                            <p className="text-secondary-100 mt-2">매주 {subscription.deliveryDays.join(', ')} 배송</p>
                        </div>
                        <Calendar className="w-12 h-12 text-secondary-200" />
                    </div>
                </Card>

                {/* 배송 정보 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">배송 정보</h2>
                    <div className="space-y-3">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-500">배송 주소</p>
                                <p className="font-medium text-gray-900">{subscription.deliveryAddress}</p>
                            </div>
                            <Button variant="outline" size="sm" onClick={handleChangeAddress}>
                                변경
                            </Button>
                        </div>
                        <div className="flex items-center justify-between pt-3 border-t border-gray-100">
                            <div>
                                <p className="text-sm text-gray-500">배송 요일</p>
                                <p className="font-medium text-gray-900">매주 {subscription.deliveryDays.join(', ')}</p>
                            </div>
                            <Button variant="outline" size="sm" onClick={handleChangeDeliveryDate}>
                                변경
                            </Button>
                        </div>
                        <div className="flex items-center justify-between pt-3 border-t border-gray-100">
                            <div>
                                <p className="text-sm text-gray-500">결제 수단</p>
                                <p className="font-medium text-gray-900">{subscription.paymentMethod}</p>
                            </div>
                            <Button variant="outline" size="sm">
                                변경
                            </Button>
                        </div>
                    </div>
                </Card>

                {/* 배송 내역 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">배송 내역</h2>
                    <div className="space-y-3">
                        {deliveryHistory.map((delivery) => (
                            <div key={delivery.id} className="flex items-start justify-between p-4 bg-gray-50 rounded-lg">
                                <div>
                                    <div className="flex items-center space-x-2 mb-1">
                                        <p className="font-medium text-gray-900">{delivery.date}</p>
                                        <Badge variant="success">배송완료</Badge>
                                    </div>
                                    <p className="text-sm text-gray-600">{delivery.items}</p>
                                </div>
                                <Button variant="outline" size="sm">
                                    재주문
                                </Button>
                            </div>
                        ))}
                    </div>
                </Card>

                {/* 구독 관리 */}
                <Card className="mb-6">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">구독 관리</h2>
                    <div className="space-y-3">
                        {subscription.status === 'ACTIVE' ? (
                            <Button variant="outline" fullWidth onClick={handlePause}>
                                <Pause className="w-5 h-5 mr-2" />
                                구독 일시정지
                            </Button>
                        ) : (
                            <Button variant="secondary" fullWidth>
                                구독 재개
                            </Button>
                        )}
                        <Button variant="danger" fullWidth onClick={handleCancel}>
                            <X className="w-5 h-5 mr-2" />
                            구독 해지
                        </Button>
                    </div>
                </Card>

                {/* 구독 정보 */}
                <Card>
                    <h2 className="text-xl font-bold text-gray-900 mb-4">구독 정보</h2>
                    <div className="space-y-2 text-sm text-gray-700">
                        <div className="flex justify-between">
                            <span className="text-gray-500">구독 시작일</span>
                            <span>{subscription.startDate}</span>
                        </div>
                        <div className="flex justify-between">
                            <span className="text-gray-500">구독자 수</span>
                            <span className="flex items-center">
                <TrendingUp className="w-4 h-4 mr-1 text-secondary-500" />
                                {subscription.subscribers.toLocaleString()}명
              </span>
                        </div>
                        <div className="flex justify-between">
                            <span className="text-gray-500">다음 결제일</span>
                            <span>{subscription.nextDelivery}</span>
                        </div>
                        <div className="flex justify-between">
                            <span className="text-gray-500">다음 결제 금액</span>
                            <span className="font-semibold text-secondary-600">
                {subscription.price.toLocaleString()}원
              </span>
                        </div>
                    </div>
                </Card>
            </div>
        </div>
    );
}
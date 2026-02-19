'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { ArrowLeft, Gift, Clock, Plus, Loader2 } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { couponsApi, UserCoupon } from '@/lib/api/coupons';

export default function CouponsPage() {
    const [coupons, setCoupons] = useState<UserCoupon[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchCoupons();
    }, []);

    const fetchCoupons = async () => {
        try {
            setLoading(true);
            const data = await couponsApi.getMyCoupons();

            // 만료일 기준 정렬 (가까운 순)
            const sorted = [...data].sort((a, b) => {
                return new Date(a.expiresAt).getTime() - new Date(b.expiresAt).getTime();
            });

            setCoupons(sorted);
        } catch (error) {
            console.error('❌ 쿠폰 목록 조회 실패:', error);
        } finally {
            setLoading(false);
        }
    };

    // 만료 여부 체크
    const isExpired = (expiryDate: string): boolean => {
        return new Date(expiryDate) < new Date();
    };

    // 사용 가능한 쿠폰 개수
    const availableCount = coupons.filter(c => !isExpired(c.expiresAt)).length;

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
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">쿠폰함</h1>
                            <p className="text-gray-600">
                                사용 가능한 쿠폰 <span className="text-primary-600 font-semibold">{availableCount}개</span>
                            </p>
                        </div>
                        <Link href="/coupons/available">
                            <Button>
                                <Plus className="w-5 h-5 mr-2" />
                                쿠폰 받기
                            </Button>
                        </Link>
                    </div>
                </div>

                {/* 쿠폰 목록 */}
                {coupons.length === 0 ? (
                    <Card className="text-center py-16">
                        <Gift className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600 mb-6">보유한 쿠폰이 없습니다</p>
                        <Link href="/coupons/available">
                            <Button>
                                <Plus className="w-5 h-5 mr-2" />
                                쿠폰 받으러 가기
                            </Button>
                        </Link>
                    </Card>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        {coupons.map((coupon) => {
                            const expired = isExpired(coupon.expiresAt);

                            return (
                                <Card
                                    key={coupon.id}
                                    className={`${
                                        expired
                                            ? 'opacity-40 border-gray-200'
                                            : 'border-2 border-primary-200 bg-gradient-to-br from-primary-50 to-white'
                                    }`}
                                >
                                    <div className="flex items-start justify-between mb-3">
                                        <Gift className={`w-8 h-8 ${expired ? 'text-gray-400' : 'text-primary-500'}`} />
                                        {expired ? (
                                            <Badge variant="default">기간 만료</Badge>
                                        ) : (
                                            <Badge variant="default" className="text-xs">
                                                <Clock className="w-3 h-3 mr-1" />
                                                ~{new Date(coupon.expiresAt).toLocaleDateString('ko-KR', {
                                                year: 'numeric',
                                                month: '2-digit',
                                                day: '2-digit',
                                            }).replace(/\. /g, '.').slice(0, -1)}
                                            </Badge>
                                        )}
                                    </div>
                                    <h3 className="text-lg font-bold text-gray-900 mb-2">{coupon.couponName}</h3>
                                    <p className="text-sm text-gray-600 mb-1">
                                        {coupon.minAmount.toLocaleString()}원 이상 구매 시
                                    </p>
                                    <p className="text-xl font-bold text-primary-600">
                                        {coupon.discountAmount.toLocaleString()}원 할인
                                    </p>
                                </Card>
                            );
                        })}
                    </div>
                )}

                {/* 안내 */}
                {coupons.length > 0 && (
                    <Card className="mt-8 bg-blue-50 border-blue-200">
                        <div className="flex items-start">
                            <div className="flex-shrink-0">
                                <svg className="w-6 h-6 text-blue-500" fill="currentColor" viewBox="0 0 20 20">
                                    <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
                                </svg>
                            </div>
                            <div className="ml-3">
                                <h3 className="text-sm font-medium text-blue-800">쿠폰 사용 안내</h3>
                                <div className="mt-2 text-sm text-blue-700">
                                    <ul className="list-disc pl-5 space-y-1">
                                        <li>주문 시 자동으로 적용 가능한 쿠폰이 표시됩니다</li>
                                        <li>쿠폰은 유효기간 내에만 사용 가능합니다</li>
                                        <li>최소 주문 금액을 충족해야 사용할 수 있습니다</li>
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
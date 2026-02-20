'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { Gift, Users, Clock, CheckCircle, ArrowLeft, Loader2 } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import { couponsApi } from '@/lib/api/coupons';

export default function CouponsAvailablePage() {
    const [coupons, setCoupons] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const [noEventCoupons, setNoEventCoupons] = useState(false);

    useEffect(() => {
        fetchCoupons();
    }, []);

    const fetchCoupons = async () => {
        try {
            setLoading(true);
            setNoEventCoupons(false);

            const allCoupons = await couponsApi.getCoupons();

            let myCoupons: any[] = [];
            try {
                myCoupons = await couponsApi.getMyCoupons();
            } catch (e) {
                // 비로그인 등 -> 무시
            }

            const myCouponIds = new Set(myCoupons.map((c: any) => c.couponId));

            const mappedCoupons = allCoupons.map((coupon: any) => ({
                id: coupon.id,
                name: coupon.name,
                discount: coupon.discountAmount,
                minAmount: coupon.minAmount,
                expiryDate: coupon.expiresAt || '2026-12-31',
                remainingQuantity: coupon.stock || 0,
                isReceived: myCouponIds.has(coupon.id),
                status: coupon.status || 'ACTIVE',
            }));

            const sortedCoupons = mappedCoupons.sort((a, b) => {
                if (a.isReceived && !b.isReceived) return 1;
                if (!a.isReceived && b.isReceived) return -1;
                return 0;
            });

            setCoupons(sortedCoupons);
        } catch (error: any) {
            const status = error?.response?.status;

            // ✅ API 404: 이벤트 쿠폰 없음
            if (status === 404) {
                setCoupons([]);
                setNoEventCoupons(true);
            } else {
                alert('쿠폰 목록을 불러오는데 실패했습니다.');
            }
        } finally {
            setLoading(false);
        }
    };

    const handleReceiveCoupon = async (couponId: number) => {
        try {
            await couponsApi.receiveCoupon(couponId);
            alert('쿠폰을 받았습니다!');
            fetchCoupons();
        } catch (error: any) {
            const msg = error.response?.data?.message || '쿠폰 받기에 실패했습니다.';
            alert(msg);
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
                <div className="mb-8">
                    <Link href="/mypage/coupons" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>내 쿠폰함으로</span>
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">쿠폰 받기</h1>
                    <p className="text-gray-600">선착순으로 쿠폰을 받아가세요!</p>
                </div>

                {/* ✅ API 404 처리 */}
                {noEventCoupons ? (
                    <Card className="text-center py-16">
                        <Gift className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-700 font-medium mb-2">이벤트 쿠폰이 없습니다</p>
                        <p className="text-sm text-gray-500">현재 진행 중인 쿠폰 이벤트가 없어요.</p>
                    </Card>
                ) : coupons.length === 0 ? (
                    <div className="text-center py-20 text-gray-500">현재 발급 가능한 쿠폰이 없습니다</div>
                ) : (
                    <div className="space-y-4">
                        {coupons.map((coupon) => {
                            const isAvailable = coupon.remainingQuantity > 0 && coupon.status === 'USABLE';

                            return (
                                <Card
                                    key={coupon.id}
                                    className={
                                        coupon.isReceived
                                            ? 'border-2 border-green-200 bg-green-50 opacity-60'
                                            : !isAvailable
                                                ? 'opacity-50'
                                                : 'border-2 border-primary-200'
                                    }
                                >
                                    <div className="flex items-start justify-between">
                                        <div className="flex items-start space-x-4 flex-1">
                                            <div
                                                className={`w-12 h-12 rounded-full flex items-center justify-center ${
                                                    coupon.isReceived ? 'bg-green-100' : !isAvailable ? 'bg-gray-100' : 'bg-primary-100'
                                                }`}
                                            >
                                                <Gift
                                                    className={`w-6 h-6 ${
                                                        coupon.isReceived ? 'text-green-600' : !isAvailable ? 'text-gray-400' : 'text-primary-600'
                                                    }`}
                                                />
                                            </div>
                                            <div className="flex-1">
                                                <h3 className="text-lg font-bold text-gray-900 mb-1">{coupon.name}</h3>
                                                <p className="text-sm text-gray-600 mb-2">
                                                    {coupon.minAmount.toLocaleString()}원 이상 구매 시 {coupon.discount.toLocaleString()}원 할인
                                                </p>
                                                <div className="flex items-center space-x-3 text-sm text-gray-600">
                                                    <div className="flex items-center">
                                                        <Clock className="w-4 h-4 mr-1" />
                                                        ~{String(coupon.expiryDate).split('T')[0]}
                                                    </div>
                                                    <div className="flex items-center">
                                                        <Users className="w-4 h-4 mr-1" />
                                                        {coupon.remainingQuantity.toLocaleString()}개 남음
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div>
                                            {coupon.isReceived ? (
                                                <Badge variant="success" className="flex items-center">
                                                    <CheckCircle className="w-4 h-4 mr-1" />
                                                    받음
                                                </Badge>
                                            ) : !isAvailable ? (
                                                <Badge variant="default">품절</Badge>
                                            ) : (
                                                <Button size="sm" onClick={() => handleReceiveCoupon(coupon.id)}>
                                                    받기
                                                </Button>
                                            )}
                                        </div>
                                    </div>
                                </Card>
                            );
                        })}
                    </div>
                )}
            </div>
        </div>
    );
}

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

    useEffect(() => {
        fetchCoupons();
    }, []);

    const fetchCoupons = async () => {
        try {
            setLoading(true);

            // ✅ 발급 가능한 쿠폰 먼저 조회
            const allCoupons = await couponsApi.getCoupons();
            console.log('📋 전체 쿠폰:', allCoupons);

            // ✅ 내 쿠폰 조회 (로그인 안 되어 있으면 빈 배열)
            let myCoupons: any[] = [];
            try {
                myCoupons = await couponsApi.getMyCoupons();
                console.log('🎫 내 쿠폰:', myCoupons);
            } catch (error) {
                console.log('내 쿠폰 조회 실패 (비로그인 상태일 수 있음)');
            }

            // 내가 받은 쿠폰 ID Set
            const myCouponIds = new Set(myCoupons.map((c: any) => c.couponId));

            // 매핑
            const mappedCoupons = allCoupons.map((coupon: any) => ({
                id: coupon.id,
                name: coupon.name,
                discount: coupon.discountAmount,
                minAmount: coupon.minAmount,
                expiryDate: coupon.expiresAt || '2026-12-31', // 기본값 설정
                remainingQuantity: coupon.stock || 0,
                isReceived: myCouponIds.has(coupon.id),
                status: coupon.status || 'ACTIVE',
            }));

            console.log('✅ 매핑된 쿠폰:', mappedCoupons);
            setCoupons(mappedCoupons);
        } catch (error) {
            console.error('❌ 쿠폰 목록 조회 실패:', error);
            alert('쿠폰 목록을 불러오는데 실패했습니다.');
        } finally {
            setLoading(false);
        }
    };

    const handleReceiveCoupon = async (couponId: number) => {
        try {
            await couponsApi.receiveCoupon(couponId);
            alert('쿠폰을 받았습니다!');

            // 목록 새로고침
            fetchCoupons();
        } catch (error: any) {
            console.error('❌ 쿠폰 받기 실패:', error);
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
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href="/mypage/coupons" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>내 쿠폰함으로</span>
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">쿠폰 받기</h1>
                    <p className="text-gray-600">선착순으로 쿠폰을 받아가세요!</p>
                </div>

                {/* 쿠폰 목록 */}
                {coupons.length === 0 ? (
                    <div className="text-center py-20 text-gray-500">
                        현재 발급 가능한 쿠폰이 없습니다
                    </div>
                ) : (
                    <div className="space-y-4">
                        {coupons.map((coupon) => {
                            const isAvailable = coupon.remainingQuantity > 0 && coupon.status === 'USABLE';

                            return (
                                <Card key={coupon.id} className={
                                    coupon.isReceived
                                        ? 'border-2 border-green-200 bg-green-50'
                                        : !isAvailable
                                            ? 'opacity-50'
                                            : 'border-2 border-primary-200'
                                }>
                                    <div className="flex items-start justify-between">
                                        <div className="flex items-start space-x-4 flex-1">
                                            <div className={`w-12 h-12 rounded-full flex items-center justify-center ${
                                                coupon.isReceived
                                                    ? 'bg-green-100'
                                                    : !isAvailable
                                                        ? 'bg-gray-100'
                                                        : 'bg-primary-100'
                                            }`}>
                                                <Gift className={`w-6 h-6 ${
                                                    coupon.isReceived
                                                        ? 'text-green-600'
                                                        : !isAvailable
                                                            ? 'text-gray-400'
                                                            : 'text-primary-600'
                                                }`} />
                                            </div>
                                            <div className="flex-1">
                                                <h3 className="text-lg font-bold text-gray-900 mb-1">
                                                    {coupon.name}
                                                </h3>
                                                <p className="text-sm text-gray-600 mb-2">
                                                    {coupon.minAmount.toLocaleString()}원 이상 구매 시 {coupon.discount.toLocaleString()}원 할인
                                                </p>
                                                <div className="flex items-center space-x-3 text-sm text-gray-600">
                                                    <div className="flex items-center">
                                                        <Clock className="w-4 h-4 mr-1" />
                                                        ~{coupon.expiryDate.split('T')[0]}
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
                                                <Badge variant="default">
                                                    품절
                                                </Badge>
                                            ) : (
                                                <Button
                                                    size="sm"
                                                    onClick={() => handleReceiveCoupon(coupon.id)}
                                                >
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

                {/* 안내 */}
                <Card className="mt-8 bg-blue-50 border-blue-200">
                    <div className="flex items-start">
                        <div className="flex-shrink-0">
                            <svg className="w-6 h-6 text-blue-500" fill="currentColor" viewBox="0 0 20 20">
                                <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
                            </svg>
                        </div>
                        <div className="ml-3">
                            <h3 className="text-sm font-medium text-blue-800">쿠폰 안내</h3>
                            <div className="mt-2 text-sm text-blue-700">
                                <ul className="list-disc pl-5 space-y-1">
                                    <li>쿠폰은 선착순으로 발급되며, 수량이 소진되면 받을 수 없습니다</li>
                                    <li>받은 쿠폰은 마이페이지 &gt; 쿠폰함에서 확인할 수 있습니다</li>
                                    <li>쿠폰은 유효기간 내에만 사용 가능합니다</li>
                                    <li>주문 시 자동으로 적용 가능한 쿠폰이 표시됩니다</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </Card>
            </div>
        </div>
    );
}
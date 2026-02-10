'use client';

import { useState } from 'react';
import Link from 'next/link';
import { ArrowLeft, Gift, Plus, Clock } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import Input from '@/components/ui/Input';

export default function CouponsPage() {
    const [activeTab, setActiveTab] = useState<'available' | 'used' | 'expired'>('available');
    const [couponCode, setCouponCode] = useState('');

    // TODO: 실제 API 데이터로 교체
    const coupons = {
        available: [
            {
                id: 1,
                name: '5,000원 할인 쿠폰',
                discount: 5000,
                minAmount: 30000,
                expiryDate: '2026.03.31',
                code: 'WELCOME5000',
            },
            {
                id: 2,
                name: '10% 할인 쿠폰',
                discountRate: 10,
                minAmount: 20000,
                expiryDate: '2026.02.28',
                code: 'SAVE10',
            },
            {
                id: 3,
                name: '첫 구매 20% 할인',
                discountRate: 20,
                minAmount: 10000,
                expiryDate: '2026.04.15',
                code: 'FIRST20',
            },
        ],
        used: [
            {
                id: 4,
                name: '신규가입 3,000원 할인',
                discount: 3000,
                minAmount: 15000,
                usedDate: '2026.02.01',
                code: 'NEW3000',
            },
        ],
        expired: [
            {
                id: 5,
                name: '1월 특가 5,000원',
                discount: 5000,
                minAmount: 20000,
                expiryDate: '2026.01.31',
                code: 'JAN5000',
            },
        ],
    };

    const handleRegisterCoupon = () => {
        if (!couponCode.trim()) {
            alert('쿠폰 코드를 입력해주세요.');
            return;
        }
        // TODO: 실제 쿠폰 등록 API 호출
        alert(`쿠폰 "${couponCode}"가 등록되었습니다!`);
        setCouponCode('');
    };

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href="/mypage" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>마이페이지로</span>
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">쿠폰함</h1>
                    <p className="text-gray-600">
                        사용 가능한 쿠폰 <span className="text-primary-600 font-semibold">{coupons.available.length}개</span>
                    </p>
                </div>

                {/* 쿠폰 등록 */}
                <Card className="mb-6">
                    <div className="flex items-center space-x-3">
                        <Input
                            placeholder="쿠폰 코드를 입력하세요"
                            value={couponCode}
                            onChange={(e) => setCouponCode(e.target.value)}
                            className="flex-1"
                        />
                        <Button onClick={handleRegisterCoupon}>
                            <Plus className="w-5 h-5 mr-2" />
                            등록
                        </Button>
                    </div>
                </Card>

                {/* 탭 */}
                <div className="flex space-x-4 mb-6 border-b border-gray-200">
                    <button
                        onClick={() => setActiveTab('available')}
                        className={`pb-3 px-1 font-semibold transition ${
                            activeTab === 'available'
                                ? 'text-primary-600 border-b-2 border-primary-600'
                                : 'text-gray-600 hover:text-gray-900'
                        }`}
                    >
                        사용 가능 ({coupons.available.length})
                    </button>
                    <button
                        onClick={() => setActiveTab('used')}
                        className={`pb-3 px-1 font-semibold transition ${
                            activeTab === 'used'
                                ? 'text-primary-600 border-b-2 border-primary-600'
                                : 'text-gray-600 hover:text-gray-900'
                        }`}
                    >
                        사용 완료 ({coupons.used.length})
                    </button>
                    <button
                        onClick={() => setActiveTab('expired')}
                        className={`pb-3 px-1 font-semibold transition ${
                            activeTab === 'expired'
                                ? 'text-primary-600 border-b-2 border-primary-600'
                                : 'text-gray-600 hover:text-gray-900'
                        }`}
                    >
                        기간 만료 ({coupons.expired.length})
                    </button>
                </div>

                {/* 쿠폰 목록 - 사용 가능 */}
                {activeTab === 'available' && (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        {coupons.available.map((coupon) => (
                            <Card key={coupon.id} className="border-2 border-primary-200 bg-gradient-to-br from-primary-50 to-white">
                                <div className="flex items-start justify-between mb-3">
                                    <Gift className="w-8 h-8 text-primary-500" />
                                    <Badge variant="default" className="text-xs">
                                        <Clock className="w-3 h-3 mr-1" />
                                        ~{coupon.expiryDate}
                                    </Badge>
                                </div>
                                <h3 className="text-lg font-bold text-gray-900 mb-2">{coupon.name}</h3>
                                <p className="text-sm text-gray-600 mb-3">
                                    {coupon.minAmount.toLocaleString()}원 이상 구매 시
                                </p>
                                <div className="flex items-center justify-between">
                                    <code className="text-sm font-mono bg-white px-3 py-1 rounded border border-gray-200">
                                        {coupon.code}
                                    </code>
                                    <Button size="sm" variant="outline">사용하기</Button>
                                </div>
                            </Card>
                        ))}
                    </div>
                )}

                {/* 쿠폰 목록 - 사용 완료 */}
                {activeTab === 'used' && (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        {coupons.used.map((coupon) => (
                            <Card key={coupon.id} className="opacity-60">
                                <div className="flex items-start justify-between mb-3">
                                    <Gift className="w-8 h-8 text-gray-400" />
                                    <Badge variant="default">사용 완료</Badge>
                                </div>
                                <h3 className="text-lg font-bold text-gray-900 mb-2">{coupon.name}</h3>
                                <p className="text-sm text-gray-600 mb-3">
                                    사용일: {coupon.usedDate}
                                </p>
                                <code className="text-sm font-mono bg-gray-100 px-3 py-1 rounded border border-gray-200">
                                    {coupon.code}
                                </code>
                            </Card>
                        ))}
                    </div>
                )}

                {/* 쿠폰 목록 - 기간 만료 */}
                {activeTab === 'expired' && (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        {coupons.expired.map((coupon) => (
                            <Card key={coupon.id} className="opacity-40">
                                <div className="flex items-start justify-between mb-3">
                                    <Gift className="w-8 h-8 text-gray-400" />
                                    <Badge variant="default">기간 만료</Badge>
                                </div>
                                <h3 className="text-lg font-bold text-gray-900 mb-2">{coupon.name}</h3>
                                <p className="text-sm text-gray-600 mb-3">
                                    만료일: {coupon.expiryDate}
                                </p>
                                <code className="text-sm font-mono bg-gray-100 px-3 py-1 rounded border border-gray-200">
                                    {coupon.code}
                                </code>
                            </Card>
                        ))}
                    </div>
                )}

                {/* 빈 상태 */}
                {coupons[activeTab].length === 0 && (
                    <Card className="text-center py-16">
                        <Gift className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600">
                            {activeTab === 'available' && '사용 가능한 쿠폰이 없습니다'}
                            {activeTab === 'used' && '사용한 쿠폰이 없습니다'}
                            {activeTab === 'expired' && '만료된 쿠폰이 없습니다'}
                        </p>
                    </Card>
                )}
            </div>
        </div>
    );
}
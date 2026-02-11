'use client';

import { useState } from 'react';
import Link from 'next/link';
import { ArrowLeft, Gift, Clock, Plus } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function CouponsPage() {
    const [activeTab, setActiveTab] = useState<'available' | 'used' | 'expired'>('available');

    // TODO: 실제 API 데이터로 교체
    const coupons = {
        available: [
            {
                id: 1,
                name: '5,000원 할인 쿠폰',
                discount: 5000,
                minAmount: 30000,
                expiryDate: '2026.03.31',
            },
            {
                id: 2,
                name: '10% 할인 쿠폰',
                discountRate: 10,
                minAmount: 20000,
                expiryDate: '2026.02.28',
            },
            {
                id: 3,
                name: '첫 구매 20% 할인',
                discountRate: 20,
                minAmount: 10000,
                expiryDate: '2026.04.15',
            },
        ],
        used: [
            {
                id: 4,
                name: '신규가입 3,000원 할인',
                discount: 3000,
                minAmount: 15000,
                usedDate: '2026.02.01',
            },
        ],
        expired: [
            {
                id: 5,
                name: '1월 특가 5,000원',
                discount: 5000,
                minAmount: 20000,
                expiryDate: '2026.01.31',
            },
        ],
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
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">쿠폰함</h1>
                            <p className="text-gray-600">
                                사용 가능한 쿠폰 <span className="text-primary-600 font-semibold">{coupons.available.length}개</span>
                            </p>
                        </div>
                        <Link href="/coupons">
                            <Button>
                                <Plus className="w-5 h-5 mr-2" />
                                쿠폰 받기
                            </Button>
                        </Link>
                    </div>
                </div>

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
                                <p className="text-sm text-gray-600">
                                    {coupon.minAmount.toLocaleString()}원 이상 구매 시
                                </p>
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
                                <p className="text-sm text-gray-600">
                                    사용일: {coupon.usedDate}
                                </p>
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
                                <p className="text-sm text-gray-600">
                                    만료일: {coupon.expiryDate}
                                </p>
                            </Card>
                        ))}
                    </div>
                )}

                {/* 빈 상태 */}
                {coupons[activeTab].length === 0 && (
                    <Card className="text-center py-16">
                        <Gift className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600 mb-6">
                            {activeTab === 'available' && '사용 가능한 쿠폰이 없습니다'}
                            {activeTab === 'used' && '사용한 쿠폰이 없습니다'}
                            {activeTab === 'expired' && '만료된 쿠폰이 없습니다'}
                        </p>
                        {activeTab === 'available' && (
                            <Link href="/coupons">
                                <Button>
                                    <Plus className="w-5 h-5 mr-2" />
                                    쿠폰 받으러 가기
                                </Button>
                            </Link>
                        )}
                    </Card>
                )}
            </div>
        </div>
    );
}
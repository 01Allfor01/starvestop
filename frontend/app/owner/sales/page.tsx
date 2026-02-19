'use client';

import { useState } from 'react';
import Link from 'next/link';
import { ArrowLeft, TrendingUp, DollarSign, ShoppingCart, Calendar } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';

export default function OwnerSalesPage() {
    const [period, setPeriod] = useState<'day' | 'week' | 'month'>('day');

    // TODO: 실제 API 데이터로 교체
    const salesData = {
        today: {
            revenue: 450000,
            orders: 23,
            items: 67,
        },
        week: {
            revenue: 2800000,
            orders: 156,
            items: 432,
        },
        month: {
            revenue: 12500000,
            orders: 678,
            items: 1892,
        },
    };

    const topProducts = [
        { name: '프리미엄 크루아상', sales: 1200000, count: 240 },
        { name: '샐러드', sales: 980000, count: 136 },
        { name: '도시락', sales: 750000, count: 77 },
        { name: '과일 박스', sales: 620000, count: 52 },
    ];

    const dailySales = [
        { date: '02.03', revenue: 380000 },
        { date: '02.04', revenue: 420000 },
        { date: '02.05', revenue: 390000 },
        { date: '02.06', revenue: 510000 },
        { date: '02.07', revenue: 460000 },
        { date: '02.08', revenue: 490000 },
        { date: '02.09', revenue: 450000 },
    ];

    const currentData = salesData[period === 'day' ? 'today' : period === 'week' ? 'week' : 'month'];

    return (
        <div className="min-h-screen bg-gray-50">
            <div className="bg-white border-b border-gray-200">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
                    <Link href="/owner/dashboard" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>대시보드</span>
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900">매출 통계</h1>
                </div>
            </div>

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 기간 선택 */}
                <div className="flex space-x-2 mb-6">
                    <Button
                        variant={period === 'day' ? 'primary' : 'outline'}
                        onClick={() => setPeriod('day')}
                    >
                        오늘
                    </Button>
                    <Button
                        variant={period === 'week' ? 'primary' : 'outline'}
                        onClick={() => setPeriod('week')}
                    >
                        이번 주
                    </Button>
                    <Button
                        variant={period === 'month' ? 'primary' : 'outline'}
                        onClick={() => setPeriod('month')}
                    >
                        이번 달
                    </Button>
                </div>

                {/* 요약 통계 */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                    <Card>
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">총 매출</p>
                                <p className="text-3xl font-bold text-gray-900">
                                    {currentData.revenue.toLocaleString()}원
                                </p>
                            </div>
                            <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                                <DollarSign className="w-6 h-6 text-green-600" />
                            </div>
                        </div>
                    </Card>

                    <Card>
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">주문 건수</p>
                                <p className="text-3xl font-bold text-gray-900">{currentData.orders}건</p>
                            </div>
                            <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                                <ShoppingCart className="w-6 h-6 text-blue-600" />
                            </div>
                        </div>
                    </Card>

                    <Card>
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-600 mb-1">판매 상품</p>
                                <p className="text-3xl font-bold text-gray-900">{currentData.items}개</p>
                            </div>
                            <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center">
                                <TrendingUp className="w-6 h-6 text-purple-600" />
                            </div>
                        </div>
                    </Card>
                </div>

                {/* 일별 매출 */}
                <Card className="mb-8">
                    <h2 className="text-xl font-bold text-gray-900 mb-6">일별 매출 추이</h2>
                    <div className="space-y-2">
                        {dailySales.map((day, idx) => (
                            <div key={idx} className="flex items-center">
                                <span className="text-sm text-gray-600 w-16">{day.date}</span>
                                <div className="flex-1 mx-4">
                                    <div className="bg-gray-200 rounded-full h-8">
                                        <div
                                            className="bg-primary-500 h-8 rounded-full flex items-center justify-end pr-3"
                                            style={{ width: `${(day.revenue / 600000) * 100}%` }}
                                        >
                      <span className="text-sm font-medium text-white">
                        {day.revenue.toLocaleString()}원
                      </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </Card>

                {/* 인기 상품 */}
                <Card>
                    <h2 className="text-xl font-bold text-gray-900 mb-6">베스트 상품</h2>
                    <div className="space-y-4">
                        {topProducts.map((product, idx) => (
                            <div key={idx} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                                <div className="flex items-center">
                                    <span className="text-2xl font-bold text-primary-500 mr-4">#{idx + 1}</span>
                                    <div>
                                        <p className="font-medium text-gray-900">{product.name}</p>
                                        <p className="text-sm text-gray-600">{product.count}개 판매</p>
                                    </div>
                                </div>
                                <p className="text-lg font-bold text-gray-900">
                                    {product.sales.toLocaleString()}원
                                </p>
                            </div>
                        ))}
                    </div>
                </Card>
            </div>
        </div>
    );
}
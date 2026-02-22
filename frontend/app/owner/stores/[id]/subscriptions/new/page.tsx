'use client';

import { useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, CalendarCheck } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Input from '@/components/ui/Input';
import { ownerApi } from '@/lib/api/owner';
import { Day, MealTime } from '@/types/owner';
import { DAY_LABELS, MEAL_TIME_LABELS, daysToBitmask, mealTimesToBitmask } from '@/lib/helpers/bitmask';

export default function NewSubscriptionPage() {
    const params = useParams();
    const router = useRouter();
    const storeId = Number(params.id);

    const [loading, setLoading] = useState(false);

    const [formData, setFormData] = useState({
        name: '',
        description: '',
        price: '',
        stock: '',
    });

    const [selectedDays, setSelectedDays] = useState<Day[]>([]);
    const [selectedMealTimes, setSelectedMealTimes] = useState<MealTime[]>([]);

    const [errors, setErrors] = useState<Record<string, string>>({});

    const handleChange = (field: string) => (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData(prev => ({ ...prev, [field]: e.target.value }));
        setErrors(prev => ({ ...prev, [field]: '' }));
    };

    const handleDayToggle = (day: Day) => {
        setSelectedDays(prev => {
            if (prev.includes(day)) {
                return prev.filter(d => d !== day);
            } else {
                return [...prev, day];
            }
        });
        setErrors(prev => ({ ...prev, days: '' }));
    };

    const handleMealTimeToggle = (mealTime: MealTime) => {
        setSelectedMealTimes(prev => {
            if (prev.includes(mealTime)) {
                return prev.filter(m => m !== mealTime);
            } else {
                return [...prev, mealTime];
            }
        });
        setErrors(prev => ({ ...prev, mealTimes: '' }));
    };

    const validateForm = () => {
        const newErrors: Record<string, string> = {};

        if (!formData.name.trim()) newErrors.name = '구독 이름을 입력해주세요';
        if (formData.name.length > 100) newErrors.name = '구독 이름은 100자 이하로 입력해주세요';

        if (!formData.description.trim()) newErrors.description = '구독 설명을 입력해주세요';
        if (formData.description.length > 255) newErrors.description = '설명은 255자 이하로 입력해주세요';

        if (selectedDays.length === 0) newErrors.days = '요일을 최소 1개 이상 선택해주세요';

        if (selectedMealTimes.length === 0) newErrors.mealTimes = '식사 시간을 최소 1개 이상 선택해주세요';

        const price = parseFloat(formData.price);
        if (!formData.price || isNaN(price)) newErrors.price = '가격을 입력해주세요';
        else if (price < 0) newErrors.price = '가격은 0 이상이어야 합니다';

        const stock = parseInt(formData.stock);
        if (!formData.stock || isNaN(stock)) newErrors.stock = '재고를 입력해주세요';
        else if (stock < 0) newErrors.stock = '재고는 0 이상이어야 합니다';
        else if (stock > 20000) newErrors.stock = '재고는 20000 이하로 입력해주세요';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!validateForm()) return;

        try {
            setLoading(true);

            // 비트마스크 변환
            const dayBitmask = daysToBitmask(selectedDays);
            const mealTimeBitmask = mealTimesToBitmask(selectedMealTimes);

            await ownerApi.createSubscription(storeId, {
                name: formData.name,
                description: formData.description,
                day: dayBitmask,
                mealTime: mealTimeBitmask,
                price: parseFloat(formData.price),
                stock: parseInt(formData.stock),
            });

            alert('구독이 등록되었습니다!');
            router.push(`/owner/stores/${storeId}`);
        } catch (error: any) {
            console.error('구독 등록 실패:', error);
            alert(error.response?.data?.message || '구독 등록에 실패했습니다');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href={`/owner/stores/${storeId}`} className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        매장으로
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900 flex items-center">
                        <CalendarCheck className="w-8 h-8 text-secondary-600 mr-3" />
                        구독 등록
                    </h1>
                </div>

                {/* 폼 */}
                <form onSubmit={handleSubmit}>
                    <Card>
                        <div className="space-y-6">
                            {/* 기본 정보 */}
                            <div>
                                <h2 className="text-lg font-semibold text-gray-900 mb-4">기본 정보</h2>

                                <div className="space-y-4">
                                    <Input
                                        label="구독 이름"
                                        value={formData.name}
                                        onChange={handleChange('name')}
                                        placeholder="예: 평일 점심 구독"
                                        error={errors.name}
                                    />

                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                            구독 설명
                                        </label>
                                        <textarea
                                            value={formData.description}
                                            onChange={handleChange('description')}
                                            placeholder="예: 월~금 점심 픽업 구독"
                                            rows={3}
                                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                        />
                                        {errors.description && <p className="mt-1.5 text-sm text-red-600">{errors.description}</p>}
                                    </div>
                                </div>
                            </div>

                            {/* 요일 선택 */}
                            <div className="pt-6 border-t border-gray-200">
                                <h2 className="text-lg font-semibold text-gray-900 mb-4">픽업 요일</h2>

                                <div className="grid grid-cols-7 gap-2">
                                    {Object.entries(DAY_LABELS).map(([day, label]) => (
                                        <button
                                            key={day}
                                            type="button"
                                            onClick={() => handleDayToggle(day as Day)}
                                            className={`py-3 rounded-lg font-medium transition ${
                                                selectedDays.includes(day as Day)
                                                    ? 'bg-secondary-500 text-white'
                                                    : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                                            }`}
                                        >
                                            {label}
                                        </button>
                                    ))}
                                </div>
                                {errors.days && <p className="mt-2 text-sm text-red-600">{errors.days}</p>}
                                {selectedDays.length > 0 && (
                                    <p className="mt-2 text-sm text-gray-600">
                                        선택된 요일: {selectedDays.map(d => DAY_LABELS[d]).join(', ')}
                                    </p>
                                )}
                            </div>

                            {/* 식사 시간 선택 */}
                            <div className="pt-6 border-t border-gray-200">
                                <h2 className="text-lg font-semibold text-gray-900 mb-4">식사 시간</h2>

                                <div className="grid grid-cols-3 gap-4">
                                    {Object.entries(MEAL_TIME_LABELS).map(([mealTime, label]) => (
                                        <button
                                            key={mealTime}
                                            type="button"
                                            onClick={() => handleMealTimeToggle(mealTime as MealTime)}
                                            className={`py-4 rounded-lg font-medium transition ${
                                                selectedMealTimes.includes(mealTime as MealTime)
                                                    ? 'bg-secondary-500 text-white'
                                                    : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                                            }`}
                                        >
                                            {label}
                                        </button>
                                    ))}
                                </div>
                                {errors.mealTimes && <p className="mt-2 text-sm text-red-600">{errors.mealTimes}</p>}
                                {selectedMealTimes.length > 0 && (
                                    <p className="mt-2 text-sm text-gray-600">
                                        선택된 시간: {selectedMealTimes.map(m => MEAL_TIME_LABELS[m]).join(', ')}
                                    </p>
                                )}
                            </div>

                            {/* 가격 및 재고 */}
                            <div className="pt-6 border-t border-gray-200">
                                <h2 className="text-lg font-semibold text-gray-900 mb-4">가격 및 재고</h2>

                                <div className="space-y-4">
                                    <Input
                                        label="월 구독료"
                                        type="number"
                                        value={formData.price}
                                        onChange={handleChange('price')}
                                        placeholder="5900"
                                        helperText="원 단위로 입력"
                                        error={errors.price}
                                    />

                                    <Input
                                        label="구독 가능 인원"
                                        type="number"
                                        value={formData.stock}
                                        onChange={handleChange('stock')}
                                        placeholder="100"
                                        helperText="최대 20,000명까지 입력 가능"
                                        error={errors.stock}
                                    />
                                </div>
                            </div>

                            {/* 미리보기 */}
                            {selectedDays.length > 0 && selectedMealTimes.length > 0 && formData.price && (
                                <div className="pt-6 border-t border-gray-200">
                                    <h2 className="text-lg font-semibold text-gray-900 mb-4">미리보기</h2>
                                    <div className="p-4 bg-secondary-50 border border-secondary-200 rounded-lg">
                                        <p className="text-sm text-gray-700 mb-2">
                                            <span className="font-medium">픽업 일정:</span>{' '}
                                            {selectedDays.map(d => DAY_LABELS[d]).join(', ')}{' '}
                                            {selectedMealTimes.map(m => MEAL_TIME_LABELS[m]).join(', ')}
                                        </p>
                                        <p className="text-sm text-gray-700">
                                            <span className="font-medium">월 구독료:</span>{' '}
                                            <span className="text-secondary-600 font-bold">
                                                {parseFloat(formData.price).toLocaleString()}원
                                            </span>
                                        </p>
                                    </div>
                                </div>
                            )}
                        </div>

                        {/* 버튼 */}
                        <div className="flex space-x-3 mt-8">
                            <Button
                                type="button"
                                variant="outline"
                                fullWidth
                                onClick={() => router.back()}
                            >
                                취소
                            </Button>
                            <Button
                                type="submit"
                                variant="secondary"
                                fullWidth
                                loading={loading}
                            >
                                등록하기
                            </Button>
                        </div>
                    </Card>
                </form>
            </div>
        </div>
    );
}
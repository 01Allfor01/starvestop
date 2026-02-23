'use client';

import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, Loader2 } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Input from '@/components/ui/Input';
import { storesApi } from '@/lib/api/stores';
import { ownerApi } from '@/lib/api/owner';
import { StoreCategory, StoreStatus } from '@/types/owner';
import { STORE_CATEGORY_LABELS, STORE_STATUS_LABELS } from '@/lib/helpers/bitmask';

export default function EditStorePage() {
    const params = useParams();
    const router = useRouter();
    const storeId = Number(params.id);

    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);

    const [formData, setFormData] = useState({
        name: '',
        address: '',
        description: '',
        category: '' as StoreCategory | '',
        latitude: '',
        longitude: '',
        openTime: '',
        closeTime: '',
        status: 'OPENED' as StoreStatus,
    });

    const [errors, setErrors] = useState<Record<string, string>>({});

    useEffect(() => {
        fetchStore();
    }, [storeId]);

    const fetchStore = async () => {
        try {
            setLoading(true);
            const data = await storesApi.getStore(storeId);

            setFormData({
                name: data.name,
                address: data.address,
                description: data.description,
                category: data.category as any,
                latitude: data.latitude as any,
                longitude: data.longitude as any,
                openTime: data.openTime.slice(0, 5),
                closeTime: data.closeTime.slice(0, 5),
                status: data.status as any,
            });
        } catch (error: any) {
            console.error('매장 정보 로딩 실패:', error);
            alert('매장 정보를 불러올 수 없습니다');
            router.push('/owner/dashboard');
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (field: string) => (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        setFormData(prev => ({ ...prev, [field]: e.target.value }));
        setErrors(prev => ({ ...prev, [field]: '' }));
    };

    const validateForm = () => {
        const newErrors: Record<string, string> = {};

        if (!formData.name.trim()) newErrors.name = '매장 이름을 입력해주세요';
        if (formData.name.length > 255) newErrors.name = '매장 이름은 255자 이하로 입력해주세요';

        if (!formData.address.trim()) newErrors.address = '주소를 입력해주세요';
        if (formData.address.length > 255) newErrors.address = '주소는 255자 이하로 입력해주세요';

        if (!formData.description.trim()) newErrors.description = '매장 설명을 입력해주세요';
        if (formData.description.length > 255) newErrors.description = '설명은 255자 이하로 입력해주세요';

        if (!formData.category) newErrors.category = '카테고리를 선택해주세요';

        const lat = parseFloat(formData.latitude);
        if (!formData.latitude || isNaN(lat)) newErrors.latitude = '위도를 입력해주세요';
        else if (lat < -90 || lat > 90) newErrors.latitude = '위도는 -90 ~ 90 사이여야 합니다';

        const lng = parseFloat(formData.longitude);
        if (!formData.longitude || isNaN(lng)) newErrors.longitude = '경도를 입력해주세요';
        else if (lng < -180 || lng > 180) newErrors.longitude = '경도는 -180 ~ 180 사이여야 합니다';

        if (!formData.openTime) newErrors.openTime = '오픈 시간을 입력해주세요';
        if (!formData.closeTime) newErrors.closeTime = '마감 시간을 입력해주세요';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!validateForm()) return;

        try {
            setSubmitting(true);

            await ownerApi.updateStore(storeId, {
                name: formData.name,
                address: formData.address,
                description: formData.description,
                category: formData.category as StoreCategory,
                latitude: parseFloat(formData.latitude),
                longitude: parseFloat(formData.longitude),
                openTime: formData.openTime + ':00',
                closeTime: formData.closeTime + ':00',
                status: formData.status,
            });

            alert('매장 정보가 수정되었습니다!');
            router.push(`/owner/stores/${storeId}`);
        } catch (error: any) {
            console.error('매장 수정 실패:', error);
            alert(error.response?.data?.message || '매장 수정에 실패했습니다');
        } finally {
            setSubmitting(false);
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
            <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href={`/owner/stores/${storeId}`} className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        매장 상세로
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900">매장 정보 수정</h1>
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
                                        label="매장 이름"
                                        value={formData.name}
                                        onChange={handleChange('name')}
                                        error={errors.name}
                                    />

                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                            카테고리
                                        </label>
                                        <select
                                            value={formData.category}
                                            onChange={handleChange('category')}
                                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                        >
                                            {Object.entries(STORE_CATEGORY_LABELS).map(([value, label]) => (
                                                <option key={value} value={value}>{label}</option>
                                            ))}
                                        </select>
                                        {errors.category && <p className="mt-1.5 text-sm text-red-600">{errors.category}</p>}
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                            매장 설명
                                        </label>
                                        <textarea
                                            value={formData.description}
                                            onChange={handleChange('description')}
                                            rows={3}
                                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                        />
                                        {errors.description && <p className="mt-1.5 text-sm text-red-600">{errors.description}</p>}
                                    </div>
                                </div>
                            </div>

                            {/* 위치 정보 */}
                            <div className="pt-6 border-t border-gray-200">
                                <h2 className="text-lg font-semibold text-gray-900 mb-4">위치 정보</h2>

                                <div className="space-y-4">
                                    <Input
                                        label="주소"
                                        value={formData.address}
                                        onChange={handleChange('address')}
                                        error={errors.address}
                                    />

                                    <div className="grid grid-cols-2 gap-4">
                                        <Input
                                            label="위도"
                                            type="number"
                                            step="any"
                                            value={formData.latitude}
                                            onChange={handleChange('latitude')}
                                            error={errors.latitude}
                                        />
                                        <Input
                                            label="경도"
                                            type="number"
                                            step="any"
                                            value={formData.longitude}
                                            onChange={handleChange('longitude')}
                                            error={errors.longitude}
                                        />
                                    </div>
                                </div>
                            </div>

                            {/* 영업 정보 */}
                            <div className="pt-6 border-t border-gray-200">
                                <h2 className="text-lg font-semibold text-gray-900 mb-4">영업 정보</h2>

                                <div className="space-y-4">
                                    <div className="grid grid-cols-2 gap-4">
                                        <Input
                                            label="오픈 시간"
                                            type="time"
                                            value={formData.openTime}
                                            onChange={handleChange('openTime')}
                                            error={errors.openTime}
                                        />
                                        <Input
                                            label="마감 시간"
                                            type="time"
                                            value={formData.closeTime}
                                            onChange={handleChange('closeTime')}
                                            error={errors.closeTime}
                                        />
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                            영업 상태
                                        </label>
                                        <select
                                            value={formData.status}
                                            onChange={handleChange('status')}
                                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                        >
                                            {Object.entries(STORE_STATUS_LABELS).map(([value, label]) => (
                                                <option key={value} value={value}>{label}</option>
                                            ))}
                                        </select>
                                    </div>
                                </div>
                            </div>
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
                                fullWidth
                                loading={submitting}
                            >
                                수정하기
                            </Button>
                        </div>
                    </Card>
                </form>
            </div>
        </div>
    );
}
'use client';

import { useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, Package } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Input from '@/components/ui/Input';
import { ownerApi } from '@/lib/api/owner';
import { ProductStatus } from '@/types/owner';
import { PRODUCT_STATUS_LABELS } from '@/lib/helpers/bitmask';

export default function NewProductPage() {
    const params = useParams();
    const router = useRouter();
    const storeId = Number(params.id);

    const [loading, setLoading] = useState(false);

    const [formData, setFormData] = useState({
        name: '',
        description: '',
        stock: '',
        price: '',
        salePrice: '',
        status: 'GENERAL' as ProductStatus,
    });

    const [errors, setErrors] = useState<Record<string, string>>({});

    const handleChange = (field: string) => (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        setFormData(prev => ({ ...prev, [field]: e.target.value }));
        setErrors(prev => ({ ...prev, [field]: '' }));
    };

    const validateForm = () => {
        const newErrors: Record<string, string> = {};

        if (!formData.name.trim()) newErrors.name = '상품명을 입력해주세요';
        if (formData.name.length > 255) newErrors.name = '상품명은 255자 이하로 입력해주세요';

        if (!formData.description.trim()) newErrors.description = '상품 설명을 입력해주세요';
        if (formData.description.length > 255) newErrors.description = '설명은 255자 이하로 입력해주세요';

        const stock = parseInt(formData.stock);
        if (!formData.stock || isNaN(stock)) newErrors.stock = '재고를 입력해주세요';
        else if (stock < 0) newErrors.stock = '재고는 0 이상이어야 합니다';
        else if (stock > 10000) newErrors.stock = '재고는 10000 이하로 입력해주세요';

        const price = parseFloat(formData.price);
        if (!formData.price || isNaN(price)) newErrors.price = '정가를 입력해주세요';
        else if (price < 0) newErrors.price = '정가는 0 이상이어야 합니다';
        else if (price > 1000000) newErrors.price = '정가는 1000000원 이하여야 합니다';

        const salePrice = parseFloat(formData.salePrice);
        if (!formData.salePrice || isNaN(salePrice)) newErrors.salePrice = '세일 가격을 입력해주세요';
        else if (salePrice < 0) newErrors.salePrice = '세일 가격은 0 이상이어야 합니다';
        else if (salePrice > 1000000) newErrors.salePrice = '세일 가격은 1000000원 이하여야 합니다';
        else if (salePrice > price) newErrors.salePrice = '세일 가격은 정가보다 낮아야 합니다';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!validateForm()) return;

        try {
            setLoading(true);

            await ownerApi.createProduct({
                storeId,
                name: formData.name,
                description: formData.description,
                stock: parseInt(formData.stock),
                price: parseFloat(formData.price),
                salePrice: parseFloat(formData.salePrice),
                status: formData.status,
            });

            alert('상품이 등록되었습니다!');
            router.push(`/owner/stores/${storeId}`);
        } catch (error: any) {
            console.error('상품 등록 실패:', error);
            alert(error.response?.data?.message || '상품 등록에 실패했습니다');
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
                        <Package className="w-8 h-8 text-primary-600 mr-3" />
                        상품 등록
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
                                        label="상품명"
                                        value={formData.name}
                                        onChange={handleChange('name')}
                                        placeholder="예: 프리미엄 국밥"
                                        error={errors.name}
                                    />

                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                            상품 설명
                                        </label>
                                        <textarea
                                            value={formData.description}
                                            onChange={handleChange('description')}
                                            placeholder="예: 진하게 우려낸 사골육수"
                                            rows={3}
                                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                        />
                                        {errors.description && <p className="mt-1.5 text-sm text-red-600">{errors.description}</p>}
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                            상품 상태
                                        </label>
                                        <select
                                            value={formData.status}
                                            onChange={handleChange('status')}
                                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                        >
                                            {Object.entries(PRODUCT_STATUS_LABELS).map(([value, label]) => (
                                                <option key={value} value={value}>{label}</option>
                                            ))}
                                        </select>
                                    </div>
                                </div>
                            </div>

                            {/* 재고 및 가격 */}
                            <div className="pt-6 border-t border-gray-200">
                                <h2 className="text-lg font-semibold text-gray-900 mb-4">재고 및 가격</h2>

                                <div className="space-y-4">
                                    <Input
                                        label="재고"
                                        type="number"
                                        value={formData.stock}
                                        onChange={handleChange('stock')}
                                        placeholder="50"
                                        helperText="최대 10,000개까지 입력 가능"
                                        error={errors.stock}
                                    />

                                    <Input
                                        label="정가"
                                        type="number"
                                        value={formData.price}
                                        onChange={handleChange('price')}
                                        placeholder="6500"
                                        helperText="원 단위로 입력 (최대 1,000,000원)"
                                        error={errors.price}
                                    />

                                    <Input
                                        label="세일 가격"
                                        type="number"
                                        value={formData.salePrice}
                                        onChange={handleChange('salePrice')}
                                        placeholder="4900"
                                        helperText="정가보다 낮은 가격으로 입력"
                                        error={errors.salePrice}
                                    />

                                    {formData.price && formData.salePrice && parseFloat(formData.price) > parseFloat(formData.salePrice) && (
                                        <div className="p-3 bg-green-50 border border-green-200 rounded-lg">
                                            <p className="text-sm text-green-700">
                                                할인액: {(parseFloat(formData.price) - parseFloat(formData.salePrice)).toLocaleString()}원
                                            </p>
                                        </div>
                                    )}
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
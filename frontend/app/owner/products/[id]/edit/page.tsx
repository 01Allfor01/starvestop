'use client';

import { useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, Upload, Trash2 } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Input from '@/components/ui/Input';

export default function EditProductPage() {
    const params = useParams();
    const router = useRouter();
    const productId = params.id;
    const [loading, setLoading] = useState(false);

    // TODO: 실제 API 데이터로 교체
    const [formData, setFormData] = useState({
        name: '프리미엄 크루아상 3입',
        category: '베이커리',
        originalPrice: '6000',
        salePrice: '3000',
        stock: '12',
        description: '신선한 버터로 만든 프리미엄 크루아상입니다.',
        isSale: true,
        saleStartTime: '18:00',
        saleEndTime: '21:00',
        status: 'ACTIVE',
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value, type } = e.target;
        setFormData({
            ...formData,
            [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value,
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        // TODO: 실제 API 호출
        setTimeout(() => {
            console.log('상품 수정:', formData);
            alert('상품이 수정되었습니다!');
            router.push('/owner/products');
        }, 1000);
    };

    const handleDelete = () => {
        if (confirm('정말 이 상품을 삭제하시겠습니까?')) {
            // TODO: 실제 API 호출
            alert('상품이 삭제되었습니다.');
            router.push('/owner/products');
        }
    };

    const discount = formData.originalPrice && formData.salePrice
        ? Math.round((1 - Number(formData.salePrice) / Number(formData.originalPrice)) * 100)
        : 0;

    return (
        <div className="min-h-screen bg-gray-50">
            <div className="bg-white border-b border-gray-200">
                <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
                    <Link href="/owner/products" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>상품 관리로</span>
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900">상품 수정</h1>
                </div>
            </div>

            <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <form onSubmit={handleSubmit} className="space-y-6">
                    {/* 기본 정보 */}
                    <Card>
                        <h2 className="text-xl font-bold text-gray-900 mb-4">기본 정보</h2>
                        <div className="space-y-4">
                            <Input
                                label="상품명"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                required
                            />

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    카테고리
                                </label>
                                <select
                                    name="category"
                                    value={formData.category}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                    required
                                >
                                    <option value="베이커리">베이커리</option>
                                    <option value="샐러드">샐러드</option>
                                    <option value="도시락">도시락</option>
                                    <option value="과일">과일</option>
                                    <option value="채소">채소</option>
                                    <option value="음료">음료</option>
                                    <option value="기타">기타</option>
                                </select>
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    상품 설명
                                </label>
                                <textarea
                                    name="description"
                                    value={formData.description}
                                    onChange={handleChange}
                                    rows={4}
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    판매 상태
                                </label>
                                <select
                                    name="status"
                                    value={formData.status}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                >
                                    <option value="ACTIVE">판매중</option>
                                    <option value="INACTIVE">판매중지</option>
                                </select>
                            </div>
                        </div>
                    </Card>

                    {/* 가격 정보 */}
                    <Card>
                        <h2 className="text-xl font-bold text-gray-900 mb-4">가격 정보</h2>
                        <div className="space-y-4">
                            <Input
                                label="정상가"
                                name="originalPrice"
                                type="number"
                                value={formData.originalPrice}
                                onChange={handleChange}
                                required
                            />

                            <Input
                                label="판매가"
                                name="salePrice"
                                type="number"
                                value={formData.salePrice}
                                onChange={handleChange}
                                required
                                helperText={discount > 0 ? `할인율: ${discount}%` : ''}
                            />

                            <Input
                                label="재고 수량"
                                name="stock"
                                type="number"
                                value={formData.stock}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </Card>

                    {/* 마감 세일 */}
                    <Card>
                        <div className="flex items-center justify-between mb-4">
                            <h2 className="text-xl font-bold text-gray-900">마감 세일</h2>
                            <label className="flex items-center cursor-pointer">
                                <input
                                    type="checkbox"
                                    name="isSale"
                                    checked={formData.isSale}
                                    onChange={handleChange}
                                    className="w-4 h-4 text-primary-500 border-gray-300 rounded focus:ring-primary-500"
                                />
                                <span className="ml-2 text-sm text-gray-700">마감세일 상품으로 등록</span>
                            </label>
                        </div>

                        {formData.isSale && (
                            <div className="grid grid-cols-2 gap-4">
                                <Input
                                    label="세일 시작 시간"
                                    name="saleStartTime"
                                    type="time"
                                    value={formData.saleStartTime}
                                    onChange={handleChange}
                                />
                                <Input
                                    label="세일 종료 시간"
                                    name="saleEndTime"
                                    type="time"
                                    value={formData.saleEndTime}
                                    onChange={handleChange}
                                />
                            </div>
                        )}
                    </Card>

                    {/* 상품 이미지 */}
                    <Card>
                        <h2 className="text-xl font-bold text-gray-900 mb-4">상품 이미지</h2>

                        {/* 기존 이미지 */}
                        <div className="mb-4">
                            <p className="text-sm text-gray-600 mb-2">현재 이미지</p>
                            <div className="flex items-start space-x-2">
                                <img
                                    src="https://images.unsplash.com/photo-1555507036-ab1f4038808a"
                                    alt="상품 이미지"
                                    className="w-32 h-32 object-cover rounded-lg"
                                />
                                <button
                                    type="button"
                                    className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition"
                                >
                                    <Trash2 className="w-5 h-5" />
                                </button>
                            </div>
                        </div>

                        {/* 새 이미지 업로드 */}
                        <div className="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center">
                            <Upload className="w-12 h-12 text-gray-400 mx-auto mb-4" />
                            <p className="text-gray-600 mb-2">새 이미지를 드래그하거나 클릭하여 업로드</p>
                            <p className="text-sm text-gray-500">JPG, PNG (최대 5MB)</p>
                            <input type="file" className="hidden" accept="image/*" multiple />
                        </div>
                    </Card>

                    {/* 버튼 */}
                    <div className="space-y-3">
                        <div className="flex space-x-3">
                            <Button type="submit" fullWidth size="lg" loading={loading}>
                                수정 완료
                            </Button>
                            <Button
                                type="button"
                                variant="outline"
                                fullWidth
                                size="lg"
                                onClick={() => router.back()}
                            >
                                취소
                            </Button>
                        </div>
                        <Button
                            type="button"
                            variant="danger"
                            fullWidth
                            size="lg"
                            onClick={handleDelete}
                        >
                            <Trash2 className="w-5 h-5 mr-2" />
                            상품 삭제
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    );
}
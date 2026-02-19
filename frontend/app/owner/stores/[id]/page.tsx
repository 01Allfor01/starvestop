'use client';

import { useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, Upload } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Input from '@/components/ui/Input';

export default function EditStorePage() {
    const params = useParams();
    const router = useRouter();
    const storeId = params.id;
    const [loading, setLoading] = useState(false);

    // TODO: 실제 API 데이터로 교체
    const [formData, setFormData] = useState({
        name: '파리바게뜨 강남점',
        category: '베이커리',
        address: '서울특별시 강남구 테헤란로 123',
        phone: '02-1234-5678',
        openTime: '08:00',
        closeTime: '22:00',
        description: '신선한 빵을 매일 구워냅니다',
        image: '',
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        // TODO: 실제 API 호출
        setTimeout(() => {
            console.log('가게 수정:', formData);
            alert('가게 정보가 수정되었습니다!');
            router.push('/owner/stores');
        }, 1000);
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <div className="bg-white border-b border-gray-200">
                <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
                    <Link href="/owner/stores" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>가게 관리로</span>
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900">가게 수정</h1>
                </div>
            </div>

            <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <form onSubmit={handleSubmit} className="space-y-6">
                    {/* 기본 정보 */}
                    <Card>
                        <h2 className="text-xl font-bold text-gray-900 mb-4">기본 정보</h2>
                        <div className="space-y-4">
                            <Input
                                label="가게 이름"
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
                                    <option value="정육">정육</option>
                                </select>
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    가게 설명
                                </label>
                                <textarea
                                    name="description"
                                    value={formData.description}
                                    onChange={handleChange}
                                    rows={4}
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                />
                            </div>
                        </div>
                    </Card>

                    {/* 위치 정보 */}
                    <Card>
                        <h2 className="text-xl font-bold text-gray-900 mb-4">위치 정보</h2>
                        <div className="space-y-4">
                            <div>
                                <Input
                                    label="주소"
                                    name="address"
                                    value={formData.address}
                                    onChange={handleChange}
                                    required
                                />
                                <Button type="button" variant="outline" size="sm" className="mt-2">
                                    주소 검색
                                </Button>
                            </div>

                            <Input
                                label="전화번호"
                                name="phone"
                                type="tel"
                                value={formData.phone}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </Card>

                    {/* 영업 시간 */}
                    <Card>
                        <h2 className="text-xl font-bold text-gray-900 mb-4">영업 시간</h2>
                        <div className="grid grid-cols-2 gap-4">
                            <Input
                                label="오픈 시간"
                                name="openTime"
                                type="time"
                                value={formData.openTime}
                                onChange={handleChange}
                                required
                            />
                            <Input
                                label="마감 시간"
                                name="closeTime"
                                type="time"
                                value={formData.closeTime}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </Card>

                    {/* 가게 이미지 */}
                    <Card>
                        <h2 className="text-xl font-bold text-gray-900 mb-4">가게 이미지</h2>
                        <div className="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center">
                            <Upload className="w-12 h-12 text-gray-400 mx-auto mb-4" />
                            <p className="text-gray-600 mb-2">이미지를 드래그하거나 클릭하여 업로드</p>
                            <p className="text-sm text-gray-500">JPG, PNG (최대 5MB)</p>
                            <input type="file" className="hidden" accept="image/*" />
                        </div>
                    </Card>

                    {/* 버튼 */}
                    <div className="flex space-x-3">
                        <Button type="submit" fullWidth size="lg" loading={loading}>
                            저장하기
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
                </form>
            </div>
        </div>
    );
}
'use client';

import { useState } from 'react';
import Link from 'next/link';
import { ArrowLeft, Camera, User, Mail, Phone, MapPin } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Input from '@/components/ui/Input';

export default function EditProfilePage() {
    const [loading, setLoading] = useState(false);

    // TODO: 실제 사용자 데이터로 교체
    const [formData, setFormData] = useState({
        nickname: '홍길동',
        username: '홍길동',
        email: 'hong@example.com',
        phone: '010-1234-5678',
        address: '서울특별시 강남구 테헤란로 123',
        addressDetail: '456호',
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        // TODO: 실제 API 호출로 교체
        setTimeout(() => {
            console.log('회원정보 수정:', formData);
            alert('회원정보가 수정되었습니다.');
            setLoading(false);
        }, 1000);
    };

    const handlePasswordChange = () => {
        alert('비밀번호 변경 페이지로 이동합니다.');
    };

    const handleWithdraw = () => {
        if (confirm('정말 탈퇴하시겠습니까? 모든 데이터가 삭제됩니다.')) {
            alert('회원 탈퇴가 처리되었습니다.');
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8">
                {/* 헤더 */}
                <div className="mb-8">
                    <Link href="/mypage" className="inline-flex items-center text-gray-600 hover:text-gray-900 mb-4">
                        <ArrowLeft className="w-5 h-5 mr-2" />
                        <span>마이페이지로</span>
                    </Link>
                    <h1 className="text-3xl font-bold text-gray-900">회원정보 수정</h1>
                </div>

                <form onSubmit={handleSubmit} className="space-y-6">
                    {/* 프로필 사진 */}
                    <Card>
                        <h2 className="text-lg font-semibold text-gray-900 mb-4">프로필 사진</h2>
                        <div className="flex items-center">
                            <div className="relative">
                                <div className="w-24 h-24 bg-gradient-to-br from-primary-500 to-primary-600 rounded-full flex items-center justify-center">
                                    <User className="w-12 h-12 text-white" />
                                </div>
                                <button
                                    type="button"
                                    className="absolute bottom-0 right-0 w-8 h-8 bg-white border-2 border-gray-200 rounded-full flex items-center justify-center hover:bg-gray-50"
                                >
                                    <Camera className="w-4 h-4 text-gray-600" />
                                </button>
                            </div>
                            <div className="ml-6">
                                <Button type="button" variant="outline" size="sm">
                                    사진 변경
                                </Button>
                                <p className="text-sm text-gray-500 mt-2">
                                    JPG, PNG 파일 (최대 5MB)
                                </p>
                            </div>
                        </div>
                    </Card>

                    {/* 기본 정보 */}
                    <Card>
                        <h2 className="text-lg font-semibold text-gray-900 mb-4">기본 정보</h2>
                        <div className="space-y-4">
                            <Input
                                label="닉네임"
                                name="nickname"
                                value={formData.nickname}
                                onChange={handleChange}
                                required
                            />
                            <Input
                                label="이름"
                                name="username"
                                value={formData.username}
                                onChange={handleChange}
                                required
                            />
                            <Input
                                label="이메일"
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                disabled
                                helperText="이메일은 변경할 수 없습니다"
                            />
                            <Input
                                label="전화번호"
                                type="tel"
                                name="phone"
                                value={formData.phone}
                                onChange={handleChange}
                            />
                        </div>
                    </Card>

                    {/* 배송지 정보 */}
                    <Card>
                        <h2 className="text-lg font-semibold text-gray-900 mb-4">기본 배송지</h2>
                        <div className="space-y-4">
                            <div>
                                <Input
                                    label="주소"
                                    name="address"
                                    value={formData.address}
                                    onChange={handleChange}
                                />
                                <Button type="button" variant="outline" size="sm" className="mt-2">
                                    주소 검색
                                </Button>
                            </div>
                            <Input
                                label="상세주소"
                                name="addressDetail"
                                value={formData.addressDetail}
                                onChange={handleChange}
                                placeholder="동/호수를 입력하세요"
                            />
                        </div>
                    </Card>

                    {/* 보안 */}
                    <Card>
                        <h2 className="text-lg font-semibold text-gray-900 mb-4">보안</h2>
                        <Button type="button" variant="outline" fullWidth onClick={handlePasswordChange}>
                            비밀번호 변경
                        </Button>
                    </Card>

                    {/* 저장 버튼 */}
                    <div className="space-y-3">
                        <Button type="submit" fullWidth size="lg" loading={loading}>
                            저장하기
                        </Button>
                        <Button type="button" variant="outline" fullWidth onClick={() => window.history.back()}>
                            취소
                        </Button>
                    </div>

                    {/* 회원 탈퇴 */}
                    <div className="pt-6 border-t border-gray-200">
                        <button
                            type="button"
                            onClick={handleWithdraw}
                            className="text-sm text-red-500 hover:text-red-600 underline"
                        >
                            회원 탈퇴
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}
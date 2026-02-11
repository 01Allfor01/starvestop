'use client';

import { useState } from 'react';
import Link from 'next/link';
import { ArrowLeft, Camera, User, X, CheckCircle, XCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Input from '@/components/ui/Input';

export default function EditProfilePage() {
    const [loading, setLoading] = useState(false);
    const [showPasswordModal, setShowPasswordModal] = useState(false);

    // TODO: 실제 사용자 데이터로 교체
    const [formData, setFormData] = useState({
        name: '홍길동',
        nickname: '홍길동',
        email: 'hong@example.com',
    });

    // 비밀번호 변경 모달 상태
    const [passwordData, setPasswordData] = useState({
        newPassword: '',
        confirmPassword: '',
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPasswordData({
            ...passwordData,
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

    const handlePasswordSubmit = async () => {
        if (passwordData.newPassword !== passwordData.confirmPassword) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        if (passwordData.newPassword.length < 8) {
            alert('비밀번호는 8자 이상이어야 합니다.');
            return;
        }

        // TODO: 실제 API 호출로 교체
        setTimeout(() => {
            console.log('비밀번호 변경:', passwordData);
            alert('비밀번호가 변경되었습니다.');
            setShowPasswordModal(false);
            setPasswordData({ newPassword: '', confirmPassword: '' });
        }, 1000);
    };

    const handleWithdraw = () => {
        if (confirm('정말 탈퇴하시겠습니까? 모든 데이터가 삭제됩니다.')) {
            alert('회원 탈퇴가 처리되었습니다.');
        }
    };

    // 비밀번호 일치 여부
    const passwordsMatch = passwordData.newPassword && passwordData.confirmPassword &&
        passwordData.newPassword === passwordData.confirmPassword;
    const passwordsDontMatch = passwordData.confirmPassword &&
        passwordData.newPassword !== passwordData.confirmPassword;

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
                            {/* 이름 - 정보 표시 */}
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    이름
                                </label>
                                <div className="px-4 py-3 bg-gray-50 border border-gray-200 rounded-lg">
                                    <p className="text-gray-900">{formData.name}</p>
                                </div>
                            </div>

                            {/* 이메일 - 정보 표시 */}
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    이메일
                                </label>
                                <div className="px-4 py-3 bg-gray-50 border border-gray-200 rounded-lg">
                                    <p className="text-gray-900">{formData.email}</p>
                                </div>
                            </div>

                            {/* 닉네임 - 입력 가능 */}
                            <Input
                                label="닉네임"
                                name="nickname"
                                value={formData.nickname}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </Card>

                    {/* 보안 */}
                    <Card>
                        <h2 className="text-lg font-semibold text-gray-900 mb-4">보안</h2>
                        <Button
                            type="button"
                            variant="outline"
                            fullWidth
                            onClick={() => setShowPasswordModal(true)}
                        >
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

                {/* 비밀번호 변경 모달 */}
                {showPasswordModal && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                        <div className="bg-white rounded-2xl max-w-md w-full p-6">
                            <div className="flex items-center justify-between mb-6">
                                <h3 className="text-xl font-bold text-gray-900">비밀번호 변경</h3>
                                <button
                                    onClick={() => {
                                        setShowPasswordModal(false);
                                        setPasswordData({ newPassword: '', confirmPassword: '' });
                                    }}
                                    className="text-gray-400 hover:text-gray-600"
                                >
                                    <X className="w-6 h-6" />
                                </button>
                            </div>

                            <div className="space-y-4">
                                <Input
                                    label="새 비밀번호"
                                    type="password"
                                    name="newPassword"
                                    value={passwordData.newPassword}
                                    onChange={handlePasswordChange}
                                    placeholder="8자 이상 입력해주세요"
                                    required
                                />
                                <div>
                                    <Input
                                        label="새 비밀번호 확인"
                                        type="password"
                                        name="confirmPassword"
                                        value={passwordData.confirmPassword}
                                        onChange={handlePasswordChange}
                                        placeholder="비밀번호를 다시 입력해주세요"
                                        required
                                        className={
                                            passwordsDontMatch
                                                ? 'border-red-500 focus:ring-red-500'
                                                : passwordsMatch
                                                    ? 'border-green-500 focus:ring-green-500'
                                                    : ''
                                        }
                                    />
                                    {passwordsMatch && (
                                        <div className="flex items-center mt-2 text-sm text-green-600">
                                            <CheckCircle className="w-4 h-4 mr-1" />
                                            <span>비밀번호가 일치합니다</span>
                                        </div>
                                    )}
                                    {passwordsDontMatch && (
                                        <div className="flex items-center mt-2 text-sm text-red-600">
                                            <XCircle className="w-4 h-4 mr-1" />
                                            <span>비밀번호가 일치하지 않습니다</span>
                                        </div>
                                    )}
                                </div>
                            </div>

                            <div className="mt-6 space-y-3">
                                <Button
                                    fullWidth
                                    onClick={handlePasswordSubmit}
                                    disabled={passwordsDontMatch || !passwordData.newPassword || !passwordData.confirmPassword}
                                >
                                    변경하기
                                </Button>
                                <Button
                                    variant="outline"
                                    fullWidth
                                    onClick={() => {
                                        setShowPasswordModal(false);
                                        setPasswordData({ newPassword: '', confirmPassword: '' });
                                    }}
                                >
                                    취소
                                </Button>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
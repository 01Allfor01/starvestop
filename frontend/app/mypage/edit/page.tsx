'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { ArrowLeft, Camera, User, CheckCircle, XCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Input from '@/components/ui/Input';
import { usersApi } from '@/lib/api/users';

export default function EditProfilePage() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);

    // 사용자 데이터 state
    const [formData, setFormData] = useState({
        name: '',
        nickname: '',
        email: '',
        imageUrl: '',
        newPassword: '',
        confirmPassword: '',
    });

    // 초기 데이터 로드
    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const data = await usersApi.getMyInfo();
                setFormData(prev => ({
                    ...prev,
                    name: data.username || '',
                    nickname: data.nickname || '',
                    email: data.email || '',
                    imageUrl: data.imageUrl || '',
                }));
            } catch (error) {
                console.error("사용자 정보 로드 실패:", error);
            }
        };
        fetchUserData();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    // 프로필 수정
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        try {
            // 비밀번호 입력했으면 유효성 검사
            if (formData.newPassword) {
                if (formData.newPassword !== formData.confirmPassword) {
                    alert('비밀번호가 일치하지 않습니다.');
                    setLoading(false);
                    return;
                }

                if (formData.newPassword.length < 8) {
                    alert('비밀번호는 8자 이상이어야 합니다.');
                    setLoading(false);
                    return;
                }

                const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/;
                if (!passwordRegex.test(formData.newPassword)) {
                    alert('비밀번호는 8자 이상, 영문자, 숫자, 특수문자를 최소 1개씩 포함해야 합니다.');
                    setLoading(false);
                    return;
                }
            }

            // ✅ 하나의 API 요청으로 통합
            const updateData: any = {
                nickname: formData.nickname,
            };

            // 비밀번호 입력했으면 추가
            if (formData.newPassword) {
                updateData.password = formData.newPassword;
            }

            await usersApi.updateUser(updateData);

            alert(formData.newPassword
                ? '회원정보와 비밀번호가 변경되었습니다.'
                : '회원정보가 수정되었습니다.'
            );

            // 비밀번호 필드 초기화
            setFormData(prev => ({
                ...prev,
                newPassword: '',
                confirmPassword: '',
            }));

            // 데이터 갱신
            const updated = await usersApi.getMyInfo();
            setFormData(prev => ({
                ...prev,
                name: updated.username || '',
                nickname: updated.nickname || '',
                email: updated.email || '',
                imageUrl: updated.imageUrl || '',
            }));

        } catch (error: any) {
            console.error("회원정보 수정 실패:", error);
            const msg = error.response?.data?.message || '회원정보 수정에 실패했습니다.';
            alert(msg);
        } finally {
            setLoading(false);
        }
    };

    // 회원 탈퇴
    const handleWithdraw = async () => {
        if (confirm('정말 탈퇴하시겠습니까? 모든 데이터가 삭제됩니다.')) {
            try {
                await usersApi.withdraw();
                alert('회원 탈퇴가 처리되었습니다.');
                localStorage.removeItem('accessToken');
                router.push('/');
            } catch (error) {
                console.error("회원 탈퇴 실패:", error);
                alert("회원 탈퇴 처리에 실패했습니다.");
            }
        }
    };

    // 비밀번호 일치 여부
    const shouldCheck = formData.newPassword.length > 0 && formData.confirmPassword.length > 0;

    const passwordsMatch =
        shouldCheck && formData.newPassword === formData.confirmPassword;

    const passwordsDontMatch =
        shouldCheck && formData.newPassword !== formData.confirmPassword;

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
                                <div className="w-24 h-24 bg-gradient-to-br from-primary-500 to-primary-600 rounded-full flex items-center justify-center overflow-hidden">
                                    {formData.imageUrl ? (
                                        <img src={formData.imageUrl} alt="Profile" className="w-full h-full object-cover" />
                                    ) : (
                                        <User className="w-12 h-12 text-white" />
                                    )}
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
                            {/* 이름 - 읽기 전용 */}
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    이름
                                </label>
                                <div className="px-4 py-3 bg-gray-50 border border-gray-200 rounded-lg">
                                    <p className="text-gray-900">{formData.name}</p>
                                </div>
                            </div>

                            {/* 이메일 - 읽기 전용 */}
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    이메일
                                </label>
                                <div className="px-4 py-3 bg-gray-50 border border-gray-200 rounded-lg">
                                    <p className="text-gray-900">{formData.email}</p>
                                </div>
                            </div>

                            {/* 닉네임 */}
                            <Input
                                label="닉네임"
                                name="nickname"
                                value={formData.nickname}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </Card>

                    {/* 비밀번호 변경 */}
                    <Card>
                        <h2 className="text-lg font-semibold text-gray-900 mb-4">비밀번호 변경</h2>
                        <div className="space-y-4">
                            <Input
                                label="새 비밀번호"
                                type="password"
                                name="newPassword"
                                value={formData.newPassword}
                                onChange={handleChange}
                                placeholder="변경하지 않으려면 빈칸으로 두세요"
                                autoComplete="new-password"
                            />
                            <div>
                                <Input
                                    label="새 비밀번호 확인"
                                    type="password"
                                    name="confirmPassword"
                                    value={formData.confirmPassword}
                                    onChange={handleChange}
                                    placeholder="새 비밀번호를 다시 입력해주세요"
                                    disabled={!formData.newPassword}
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
                            <p className="text-sm text-gray-500">
                                💡 비밀번호는 8자 이상, 영문자, 숫자, 특수문자를 최소 1개씩 포함해야 합니다
                            </p>
                        </div>
                    </Card>

                    {/* 저장 버튼 */}
                    <div className="space-y-3">
                        <Button
                            type="submit"
                            fullWidth
                            size="lg"
                            loading={loading}
                            disabled={passwordsDontMatch}
                        >
                            저장하기
                        </Button>
                        <Button type="button" variant="outline" fullWidth onClick={() => router.back()}>
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
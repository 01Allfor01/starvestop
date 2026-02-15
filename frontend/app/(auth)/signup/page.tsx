'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Mail, Lock, User, Eye, EyeOff, CheckCircle, XCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import { authApi } from '@/lib/api/auth';

export default function SignupPage() {
    const router = useRouter();
    const [showPassword, setShowPassword] = useState(false);
    const [showPasswordConfirm, setShowPasswordConfirm] = useState(false);

    const [formData, setFormData] = useState({
        email: '',
        password: '',
        passwordConfirm: '',
        nickname: '',
        username: '',
    });
    const [loading, setLoading] = useState(false);

    // 페이지 진입 시 기존 토큰 정리
    useEffect(() => {
        localStorage.removeItem('accessToken');
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (formData.password !== formData.passwordConfirm) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        setLoading(true);

        try {
            // 회원가입 (토큰 바로 반환됨!)
            const response = await authApi.signUp({
                email: formData.email,
                password: formData.password,
                nickname: formData.nickname,
                username: formData.username,
            });

            console.log('✅ 회원가입 성공:', response);

            // 토큰 저장
            localStorage.setItem('accessToken', response.accessToken);

            // 메인으로 이동
            alert('회원가입 완료! 자동 로그인되었습니다.');
            router.push('/');

        } catch (error: any) {
            console.error('회원가입 에러:', error);
            const errorMessage = error.response?.data?.message || '오류가 발생했습니다.';
            alert(`[오류] ${errorMessage}`);
        } finally {
            setLoading(false);
        }
    };

    const passwordsMatch = formData.passwordConfirm && formData.password === formData.passwordConfirm;
    const passwordsDontMatch = formData.passwordConfirm && formData.password !== formData.passwordConfirm;

    return (
        <div className="min-h-screen bg-gradient-to-br from-orange-50 to-orange-100 flex items-center justify-center p-4">
            <div className="w-full max-w-md">
                <div className="text-center mb-8">
                    <Link href="/" className="inline-flex items-center justify-center space-x-2 mb-4">
                        <div className="w-16 h-16 bg-gradient-to-br from-primary-500 to-primary-600 rounded-3xl flex items-center justify-center shadow-lg">
                            <span className="text-white text-3xl font-bold">S</span>
                        </div>
                    </Link>
                    <h1 className="text-3xl font-bold bg-gradient-to-r from-primary-500 to-primary-600 bg-clip-text text-transparent mb-2">
                        Starve Stop
                    </h1>
                </div>

                <div className="bg-white rounded-2xl shadow-xl p-8">
                    <h2 className="text-2xl font-bold text-gray-900 mb-6">회원가입</h2>

                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div className="relative">
                            <Mail className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type="email"
                                name="email"
                                placeholder="이메일"
                                value={formData.email}
                                onChange={handleChange}
                                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                required
                            />
                        </div>

                        <div className="relative">
                            <Lock className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type={showPassword ? 'text' : 'password'}
                                name="password"
                                placeholder="비밀번호 (영문, 숫자, 특수문자 포함 8자 이상)"
                                value={formData.password}
                                onChange={handleChange}
                                className="w-full pl-10 pr-12 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                required
                                minLength={8}
                            />
                            <button
                                type="button"
                                onClick={() => setShowPassword(!showPassword)}
                                className="absolute right-3 top-3.5 text-gray-400 hover:text-gray-600"
                            >
                                {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
                            </button>
                        </div>

                        <div>
                            <div className="relative">
                                <Lock className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                                <input
                                    type={showPasswordConfirm ? 'text' : 'password'}
                                    name="passwordConfirm"
                                    placeholder="비밀번호 확인"
                                    value={formData.passwordConfirm}
                                    onChange={handleChange}
                                    className={`w-full pl-10 pr-12 py-3 border rounded-lg focus:outline-none focus:ring-2 ${
                                        passwordsMatch ? 'border-green-300 focus:ring-green-500' :
                                            passwordsDontMatch ? 'border-red-300 focus:ring-red-500' :
                                                'border-gray-300 focus:ring-primary-500'
                                    }`}
                                    required
                                />
                                <button
                                    type="button"
                                    onClick={() => setShowPasswordConfirm(!showPasswordConfirm)}
                                    className="absolute right-3 top-3.5 text-gray-400 hover:text-gray-600"
                                >
                                    {showPasswordConfirm ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
                                </button>
                            </div>
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

                        <div className="relative">
                            <User className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type="text"
                                name="nickname"
                                placeholder="닉네임"
                                value={formData.nickname}
                                onChange={handleChange}
                                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                required
                            />
                        </div>

                        <div className="relative">
                            <User className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type="text"
                                name="username"
                                placeholder="이름 (실명)"
                                value={formData.username}
                                onChange={handleChange}
                                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                required
                            />
                        </div>

                        <Button
                            type="submit"
                            fullWidth
                            size="lg"
                            loading={loading}
                            className="mt-6"
                            disabled={passwordsDontMatch}
                        >
                            회원가입
                        </Button>
                    </form>

                    <div className="relative my-6">
                        <div className="absolute inset-0 flex items-center">
                            <div className="w-full border-t border-gray-200"></div>
                        </div>
                        <div className="relative flex justify-center text-sm">
                            <span className="px-4 bg-white text-gray-500">또는</span>
                        </div>
                    </div>

                    <p className="text-center text-sm text-gray-600 mt-6">
                        이미 회원이신가요? <Link href="/login" className="text-primary-500 font-semibold">로그인</Link>
                    </p>
                </div>
            </div>
        </div>
    );
}
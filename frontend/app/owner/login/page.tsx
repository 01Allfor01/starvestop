'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { Mail, Lock, Eye, EyeOff, Store } from 'lucide-react';
import Button from '@/components/ui/Button';
import { authApi } from '@/lib/api/auth';

export default function OwnerLoginPage() {
    const router = useRouter();
    const [showPassword, setShowPassword] = useState(false);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (!email || !password) {
            setError('이메일과 비밀번호를 입력해주세요');
            return;
        }

        try {
            setLoading(true);

            const response = await authApi.signInOwner({
                email,
                password,
            });

            // 토큰 저장
            localStorage.setItem('accessToken', response.accessToken);

            // 대시보드로 이동
            router.push('/owner/dashboard');
        } catch (error: any) {
            console.error('로그인 실패:', error);
            setError(error.response?.data?.message || '이메일 또는 비밀번호가 올바르지 않습니다');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-primary-500 to-primary-700 flex items-center justify-center p-4">
            <div className="w-full max-w-md">
                {/* 로고 */}
                <div className="text-center mb-8">
                    <div className="inline-flex items-center justify-center w-20 h-20 bg-white rounded-3xl shadow-xl mb-4">
                        <Store className="w-10 h-10 text-primary-600" />
                    </div>
                    <h1 className="text-4xl font-bold text-white mb-2">
                        사장님 페이지
                    </h1>
                    <p className="text-primary-100">
                        Starve Stop 사장님 전용 관리 시스템
                    </p>
                </div>

                {/* 로그인 폼 */}
                <div className="bg-white rounded-2xl shadow-2xl p-8">
                    <h2 className="text-2xl font-bold text-gray-900 mb-6">로그인</h2>

                    {error && (
                        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
                            <p className="text-sm text-red-600">{error}</p>
                        </div>
                    )}

                    <form onSubmit={handleSubmit} className="space-y-4">
                        {/* 이메일 */}
                        <div className="relative">
                            <Mail className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type="email"
                                placeholder="사업자 이메일"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                                required
                            />
                        </div>

                        {/* 비밀번호 */}
                        <div className="relative">
                            <Lock className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type={showPassword ? 'text' : 'password'}
                                placeholder="비밀번호"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="w-full pl-10 pr-12 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                                required
                            />
                            <button
                                type="button"
                                onClick={() => setShowPassword(!showPassword)}
                                className="absolute right-3 top-3.5 text-gray-400 hover:text-gray-600"
                            >
                                {showPassword ? (
                                    <EyeOff className="w-5 h-5" />
                                ) : (
                                    <Eye className="w-5 h-5" />
                                )}
                            </button>
                        </div>

                        {/* 자동 로그인 */}
                        <div className="flex items-center justify-between text-sm">
                            <label className="flex items-center cursor-pointer">
                                <input
                                    type="checkbox"
                                    className="w-4 h-4 text-primary-500 border-gray-300 rounded focus:ring-primary-500"
                                />
                                <span className="ml-2 text-gray-600">자동 로그인</span>
                            </label>
                        </div>

                        {/* 로그인 버튼 */}
                        <Button
                            type="submit"
                            fullWidth
                            size="lg"
                            loading={loading}
                            className="mt-6"
                        >
                            로그인
                        </Button>
                    </form>

                    {/* 입점 문의 */}
                    <div className="mt-6 pt-6 border-t border-gray-200 text-center">
                        <p className="text-sm text-gray-600 mb-3">
                            아직 Starve Stop에 입점하지 않으셨나요?
                        </p>
                        <Link href="/owner/signup">
                            <Button variant="outline" fullWidth>
                                입점 신청하기
                            </Button>
                        </Link>
                    </div>

                    {/* 소비자 페이지 링크 */}
                    <div className="mt-4 text-center">
                        <Link href="/login" className="text-sm text-gray-500 hover:text-primary-500">
                            소비자 페이지로 이동 →
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}
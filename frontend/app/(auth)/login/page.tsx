'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation'; // 페이지 이동을 위한 훅 추가
import { Mail, Lock, Eye, EyeOff } from 'lucide-react';
import Button from '@/components/ui/Button';
import { authApi } from '@/lib/api/auth'; // API import (경로 확인 필요)

export default function LoginPage() {
    const router = useRouter(); // 라우터 초기화
    const [showPassword, setShowPassword] = useState(false);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        try {
            // 1. API 호출
            const data = await authApi.signIn({ email, password });

            // 2. 토큰 저장 (백엔드에서 AccessToken 대문자로 줌)
            if (data.AccessToken) {
                localStorage.setItem('accessToken', data.AccessToken);

                // 3. 성공 메시지 및 이동
                // alert('환영합니다!'); // 필요하다면 주석 해제
                router.push('/');
            } else {
                throw new Error("토큰을 받아오지 못했습니다.");
            }

        } catch (error: any) {
            console.error('로그인 실패:', error);
            // 백엔드에서 보내주는 에러 메시지가 있다면 그걸 보여주고, 없으면 기본 메시지
            const errorMessage = error.response?.data?.message || '이메일 또는 비밀번호를 확인해주세요.';
            alert(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-orange-50 to-orange-100 flex items-center justify-center p-4">
            <div className="w-full max-w-md">
                {/* 로고 */}
                <div className="text-center mb-8">
                    <Link href="/" className="inline-flex items-center justify-center space-x-2 mb-4">
                        <div className="w-16 h-16 bg-gradient-to-br from-primary-500 to-primary-600 rounded-3xl flex items-center justify-center shadow-lg">
                            <span className="text-white text-3xl font-bold">S</span>
                        </div>
                    </Link>
                    <h1 className="text-3xl font-bold bg-gradient-to-r from-primary-500 to-primary-600 bg-clip-text text-transparent mb-2">
                        Starve Stop
                    </h1>
                    <p className="text-gray-600">
                        신선한 음식을 더 저렴하게
                    </p>
                </div>

                {/* 로그인 폼 */}
                <div className="bg-white rounded-2xl shadow-xl p-8">
                    <h2 className="text-2xl font-bold text-gray-900 mb-6">로그인</h2>

                    <form onSubmit={handleSubmit} className="space-y-4">
                        {/* 이메일 */}
                        <div className="relative">
                            <Mail className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type="email"
                                placeholder="이메일"
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

                    {/* 구분선 */}
                    <div className="relative my-6">
                        <div className="absolute inset-0 flex items-center">
                            <div className="w-full border-t border-gray-200"></div>
                        </div>
                        <div className="relative flex justify-center text-sm">
                            <span className="px-4 bg-white text-gray-500">또는</span>
                        </div>
                    </div>

                    {/* 카카오 로그인 */}
                    <button className="w-full flex items-center justify-center space-x-2 bg-[#FEE500] text-[#000000] py-3 rounded-lg font-medium hover:bg-[#FDD835] transition-colors">
                        <svg className="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
                            <path d="M12 3c5.799 0 10.5 3.664 10.5 8.185 0 4.52-4.701 8.184-10.5 8.184a13.5 13.5 0 01-1.727-.11l-4.408 2.883c-.501.265-.678.236-.472-.413l.892-3.678c-2.88-1.46-4.785-3.99-4.785-6.866C1.5 6.665 6.201 3 12 3z" />
                        </svg>
                        <span>카카오로 시작하기</span>
                    </button>

                    {/* 회원가입 링크 */}
                    <p className="text-center text-sm text-gray-600 mt-6">
                        아직 회원이 아니신가요?{' '}
                        <Link href="/signup" className="text-primary-500 font-semibold hover:text-primary-600">
                            회원가입
                        </Link>
                    </p>

                    {/* 사장님 로그인 */}
                    <div className="mt-4 pt-4 border-t border-gray-100">
                        <Link
                            href="/owner/login"
                            className="block text-center text-sm text-gray-500 hover:text-primary-500"
                        >
                            사장님이신가요? 사장님 로그인으로 이동
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}
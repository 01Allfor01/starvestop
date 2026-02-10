'use client';

import { useState } from 'react';
import Link from 'next/link';
import { Mail, Lock, User, Eye, EyeOff, CheckCircle, XCircle } from 'lucide-react';
import Button from '@/components/ui/Button';

export default function SignupPage() {
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

        // TODO: 나중에 실제 API 호출로 교체
        setTimeout(() => {
            console.log('회원가입:', formData);
            setLoading(false);
        }, 1000);
    };

    // 비밀번호 일치 여부 체크
    const passwordsMatch = formData.passwordConfirm && formData.password === formData.passwordConfirm;
    const passwordsDontMatch = formData.passwordConfirm && formData.password !== formData.passwordConfirm;

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

                {/* 회원가입 폼 */}
                <div className="bg-white rounded-2xl shadow-xl p-8">
                    <h2 className="text-2xl font-bold text-gray-900 mb-6">회원가입</h2>

                    <form onSubmit={handleSubmit} className="space-y-4">
                        {/* 이메일 */}
                        <div className="relative">
                            <Mail className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type="email"
                                name="email"
                                placeholder="이메일"
                                value={formData.email}
                                onChange={handleChange}
                                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                                required
                            />
                        </div>

                        {/* 비밀번호 */}
                        <div className="relative">
                            <Lock className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type={showPassword ? 'text' : 'password'}
                                name="password"
                                placeholder="비밀번호 (8자 이상)"
                                value={formData.password}
                                onChange={handleChange}
                                className="w-full pl-10 pr-12 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                                required
                                minLength={8}
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

                        {/* 비밀번호 확인 */}
                        <div>
                            <div className="relative">
                                <Lock className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                                <input
                                    type={showPasswordConfirm ? 'text' : 'password'}
                                    name="passwordConfirm"
                                    placeholder="비밀번호 확인"
                                    value={formData.passwordConfirm}
                                    onChange={handleChange}
                                    className={`w-full pl-10 pr-12 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:border-transparent ${
                                        passwordsMatch
                                            ? 'border-green-300 focus:ring-green-500'
                                            : passwordsDontMatch
                                                ? 'border-red-300 focus:ring-red-500'
                                                : 'border-gray-300 focus:ring-primary-500'
                                    }`}
                                    required
                                />
                                <button
                                    type="button"
                                    onClick={() => setShowPasswordConfirm(!showPasswordConfirm)}
                                    className="absolute right-3 top-3.5 text-gray-400 hover:text-gray-600"
                                >
                                    {showPasswordConfirm ? (
                                        <EyeOff className="w-5 h-5" />
                                    ) : (
                                        <Eye className="w-5 h-5" />
                                    )}
                                </button>
                            </div>
                            {/* 실시간 피드백 메시지 */}
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

                        {/* 닉네임 */}
                        <div className="relative">
                            <User className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type="text"
                                name="nickname"
                                placeholder="닉네임"
                                value={formData.nickname}
                                onChange={handleChange}
                                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                                required
                            />
                        </div>

                        {/* 이름 */}
                        <div className="relative">
                            <User className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type="text"
                                name="username"
                                placeholder="이름"
                                value={formData.username}
                                onChange={handleChange}
                                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                                required
                            />
                        </div>

                        {/* 회원가입 버튼 */}
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

                    {/* 구분선 */}
                    <div className="relative my-6">
                        <div className="absolute inset-0 flex items-center">
                            <div className="w-full border-t border-gray-200"></div>
                        </div>
                        <div className="relative flex justify-center text-sm">
                            <span className="px-4 bg-white text-gray-500">또는</span>
                        </div>
                    </div>

                    {/* 카카오 회원가입 */}
                    <button className="w-full flex items-center justify-center space-x-2 bg-[#FEE500] text-[#000000] py-3 rounded-lg font-medium hover:bg-[#FDD835] transition-colors">
                        <svg className="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
                            <path d="M12 3c5.799 0 10.5 3.664 10.5 8.185 0 4.52-4.701 8.184-10.5 8.184a13.5 13.5 0 01-1.727-.11l-4.408 2.883c-.501.265-.678.236-.472-.413l.892-3.678c-2.88-1.46-4.785-3.99-4.785-6.866C1.5 6.665 6.201 3 12 3z" />
                        </svg>
                        <span>카카오로 시작하기</span>
                    </button>

                    {/* 로그인 링크 */}
                    <p className="text-center text-sm text-gray-600 mt-6">
                        이미 회원이신가요?{' '}
                        <Link href="/login" className="text-primary-500 font-semibold hover:text-primary-600">
                            로그인
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
}
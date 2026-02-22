'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Mail, Lock, User, Eye, EyeOff, Store, ArrowLeft } from 'lucide-react';
import Button from '@/components/ui/Button';
import Input from '@/components/ui/Input';
import { authApi } from '@/lib/api/auth';

export default function OwnerSignupPage() {
    const router = useRouter();
    const [showPassword, setShowPassword] = useState(false);
    const [loading, setLoading] = useState(false);

    const [formData, setFormData] = useState({
        email: '',
        password: '',
        passwordConfirm: '',
        username: '',
    });

    const [errors, setErrors] = useState({
        email: '',
        password: '',
        passwordConfirm: '',
        username: '',
    });

    const validateForm = () => {
        const newErrors = {
            email: '',
            password: '',
            passwordConfirm: '',
            username: '',
        };

        // 이메일 검증
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!formData.email) {
            newErrors.email = '이메일을 입력해주세요';
        } else if (!emailRegex.test(formData.email)) {
            newErrors.email = '올바른 이메일 형식이 아닙니다';
        } else if (formData.email.length > 100) {
            newErrors.email = '이메일은 100자 이하로 입력해주세요';
        }

        // 비밀번호 검증
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/;
        if (!formData.password) {
            newErrors.password = '비밀번호를 입력해주세요';
        } else if (!passwordRegex.test(formData.password)) {
            newErrors.password = '8자 이상, 영문/숫자/특수문자를 포함해야 합니다';
        } else if (formData.password.length > 100) {
            newErrors.password = '비밀번호는 100자 이하로 입력해주세요';
        }

        // 비밀번호 확인
        if (!formData.passwordConfirm) {
            newErrors.passwordConfirm = '비밀번호 확인을 입력해주세요';
        } else if (formData.password !== formData.passwordConfirm) {
            newErrors.passwordConfirm = '비밀번호가 일치하지 않습니다';
        }

        // 이름 검증
        if (!formData.username) {
            newErrors.username = '이름을 입력해주세요';
        } else if (formData.username.length < 2) {
            newErrors.username = '이름은 2자 이상 입력해주세요';
        } else if (formData.username.length > 30) {
            newErrors.username = '이름은 30자 이하로 입력해주세요';
        }

        setErrors(newErrors);
        return !Object.values(newErrors).some(error => error !== '');
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!validateForm()) return;

        try {
            setLoading(true);

            const response = await authApi.signUpOwner({
                email: formData.email,
                password: formData.password,
                username: formData.username,
            });

            // 토큰 저장
            localStorage.setItem('accessToken', response.accessToken);

            alert('회원가입이 완료되었습니다!');
            router.push('/owner/dashboard');
        } catch (error: any) {
            console.error('회원가입 실패:', error);
            alert(error.response?.data?.message || '회원가입에 실패했습니다.');
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (field: string) => (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData(prev => ({ ...prev, [field]: e.target.value }));
        setErrors(prev => ({ ...prev, [field]: '' }));
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-primary-500 to-primary-700 flex items-center justify-center p-4">
            <div className="w-full max-w-md">
                {/* 뒤로가기 */}
                <Link
                    href="/owner/login"
                    className="inline-flex items-center text-white hover:text-primary-100 mb-6 transition"
                >
                    <ArrowLeft className="w-5 h-5 mr-2" />
                    로그인으로 돌아가기
                </Link>

                {/* 로고 */}
                <div className="text-center mb-8">
                    <div className="inline-flex items-center justify-center w-20 h-20 bg-white rounded-3xl shadow-xl mb-4">
                        <Store className="w-10 h-10 text-primary-600" />
                    </div>
                    <h1 className="text-4xl font-bold text-white mb-2">
                        사장님 입점 신청
                    </h1>
                    <p className="text-primary-100">
                        Starve Stop과 함께 성장하세요
                    </p>
                </div>

                {/* 회원가입 폼 */}
                <div className="bg-white rounded-2xl shadow-2xl p-8">
                    <h2 className="text-2xl font-bold text-gray-900 mb-6">회원가입</h2>

                    <form onSubmit={handleSubmit} className="space-y-4">
                        {/* 이름 */}
                        <div className="relative">
                            <User className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type="text"
                                placeholder="사장님 성함"
                                value={formData.username}
                                onChange={handleChange('username')}
                                className={`w-full pl-10 pr-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent ${
                                    errors.username ? 'border-red-300' : 'border-gray-300'
                                }`}
                            />
                            {errors.username && (
                                <p className="mt-1 text-sm text-red-600">{errors.username}</p>
                            )}
                        </div>

                        {/* 이메일 */}
                        <div className="relative">
                            <Mail className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type="email"
                                placeholder="사업자 이메일"
                                value={formData.email}
                                onChange={handleChange('email')}
                                className={`w-full pl-10 pr-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent ${
                                    errors.email ? 'border-red-300' : 'border-gray-300'
                                }`}
                            />
                            {errors.email && (
                                <p className="mt-1 text-sm text-red-600">{errors.email}</p>
                            )}
                        </div>

                        {/* 비밀번호 */}
                        <div className="relative">
                            <Lock className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type={showPassword ? 'text' : 'password'}
                                placeholder="비밀번호 (8자 이상, 영문/숫자/특수문자 포함)"
                                value={formData.password}
                                onChange={handleChange('password')}
                                className={`w-full pl-10 pr-12 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent ${
                                    errors.password ? 'border-red-300' : 'border-gray-300'
                                }`}
                            />
                            <button
                                type="button"
                                onClick={() => setShowPassword(!showPassword)}
                                className="absolute right-3 top-3.5 text-gray-400 hover:text-gray-600"
                            >
                                {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
                            </button>
                            {errors.password && (
                                <p className="mt-1 text-sm text-red-600">{errors.password}</p>
                            )}
                        </div>

                        {/* 비밀번호 확인 */}
                        <div className="relative">
                            <Lock className="absolute left-3 top-3.5 w-5 h-5 text-gray-400" />
                            <input
                                type={showPassword ? 'text' : 'password'}
                                placeholder="비밀번호 확인"
                                value={formData.passwordConfirm}
                                onChange={handleChange('passwordConfirm')}
                                className={`w-full pl-10 pr-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent ${
                                    errors.passwordConfirm ? 'border-red-300' : 'border-gray-300'
                                }`}
                            />
                            {errors.passwordConfirm && (
                                <p className="mt-1 text-sm text-red-600">{errors.passwordConfirm}</p>
                            )}
                        </div>

                        {/* 가입 버튼 */}
                        <Button
                            type="submit"
                            fullWidth
                            size="lg"
                            loading={loading}
                            className="mt-6"
                        >
                            입점 신청하기
                        </Button>
                    </form>

                    {/* 로그인 링크 */}
                    <div className="mt-6 pt-6 border-t border-gray-200 text-center">
                        <p className="text-sm text-gray-600">
                            이미 계정이 있으신가요?{' '}
                            <Link href="/owner/login" className="text-primary-500 hover:text-primary-600 font-medium">
                                로그인
                            </Link>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
}
// ✅ API 연결된 로그인 페이지 예시
// app/login/page.tsx 파일에 적용하세요!

'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { authApi } from '@/lib/api/auth';
import Input from '@/components/ui/Input';
import Button from '@/components/ui/Button';

export default function LoginPage() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            // API 호출
            const data = await authApi.login({
                email: formData.email,
                password: formData.password,
            });

            // 토큰 저장
            localStorage.setItem('accessToken', data.accessToken);
            localStorage.setItem('refreshToken', data.refreshToken);

            // 사용자 정보 저장 (선택)
            localStorage.setItem('user', JSON.stringify(data.user));

            // 홈으로 이동
            router.push('/');
        } catch (err: any) {
            console.error('Login error:', err);
            setError(err.response?.data?.message || '로그인에 실패했습니다');
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            {error && (
                <div className="text-red-500 text-sm mb-4">{error}</div>
            )}
            <Input
                label="이메일"
                type="email"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                required
            />
            <Input
                label="비밀번호"
                type="password"
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                required
            />
            <Button type="submit" loading={loading} fullWidth>
                로그인
            </Button>
        </form>
    );
}

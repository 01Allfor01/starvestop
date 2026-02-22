'use client';

import { useEffect, useRef, Suspense } from 'react'; // Suspense 추가
import { useRouter, useSearchParams } from 'next/navigation';
import { Loader2 } from 'lucide-react';
import { authApi } from '@/lib/api/auth';

// 1. 기존 로직을 별도의 컨텐츠 컴포넌트로 분리
function KakaoCallbackContent() {
    const router = useRouter();
    const searchParams = useSearchParams();
    const hasProcessed = useRef(false); // ✅ 중복 실행 방지

    useEffect(() => {
        // ✅ 이미 처리했으면 리턴
        if (hasProcessed.current) return;
        hasProcessed.current = true;

        const handleCallback = async () => {
            const code = searchParams.get('code');

            console.log('🔍 받은 code:', code);

            if (!code) {
                alert('카카오 로그인에 실패했습니다.');
                router.push('/login');
                return;
            }

            try {
                console.log('📡 백엔드 API 호출 시작...');
                const response = await authApi.kakaoCallback(code);
                console.log('✅ 백엔드 응답:', response);

                const token = response.accessToken;

                if (token) {
                    localStorage.setItem('accessToken', token);
                    window.dispatchEvent(new Event('login'));
                    router.push('/');
                } else {
                    throw new Error('토큰이 없습니다.');
                }
            } catch (error: any) {
                console.error('❌ 카카오 로그인 처리 실패:', error);
                alert('로그인 처리 중 오류가 발생했습니다.');
                router.push('/login');
            }
        };

        handleCallback();
    }, []); // ✅ 의존성 배열 비우기

    return (
        <div className="min-h-screen bg-gradient-to-br from-orange-50 to-orange-100 flex items-center justify-center">
            <div className="text-center">
                <Loader2 className="w-12 h-12 text-primary-500 animate-spin mx-auto mb-4" />
                <p className="text-gray-600">카카오 로그인 처리 중...</p>
            </div>
        </div>
    );
}

// 2. 기본 export 페이지에서 Suspense로 감싸기
export default function KakaoCallbackPage() {
    return (
        <Suspense fallback={
            <div className="min-h-screen bg-gradient-to-br from-orange-50 to-orange-100 flex items-center justify-center">
                <Loader2 className="w-12 h-12 text-primary-500 animate-spin mx-auto mb-4" />
            </div>
        }>
            <KakaoCallbackContent />
        </Suspense>
    );
}
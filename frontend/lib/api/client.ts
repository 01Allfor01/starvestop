import axios from 'axios';

// 백엔드 공통 응답 형식
export interface CommonResponse<T> {
    status: number;
    message: string;
    data: T;
}

// API 기본 클라이언트
export const apiClient = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
});

// 요청 인터셉터 (토큰 자동 추가)
apiClient.interceptors.request.use(
    (config) => {
        // localStorage에서 토큰 가져오기
        if (typeof window !== 'undefined') {
            const token = localStorage.getItem('accessToken');
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터 (CommonResponse에서 data 추출)
apiClient.interceptors.response.use(
    (response) => {
        // CommonResponse 구조에서 data만 추출
        if (response.data && 'data' in response.data) {
            return { ...response, data: response.data.data };
        }
        return response;
    },
    async (error) => {
        // 401 에러 (토큰 만료)
        if (error.response?.status === 401) {
            if (typeof window !== 'undefined') {
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                window.location.href = '/login';
            }
        }
        return Promise.reject(error);
    }
);

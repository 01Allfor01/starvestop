import axios from 'axios';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터: JWT 토큰 자동 추가
api.interceptors.request.use(
  (config) => {
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

// 응답 인터셉터: 공통 응답 처리 및 에러 핸들링
api.interceptors.response.use(
  (response) => {
    // 백엔드 응답 형식: { status: "SUCCESS", message: "...", data: ... }
    return response.data.data; // 실제 데이터만 반환
  },
  (error) => {
    // 401 Unauthorized: 토큰 만료 또는 인증 실패
    if (error.response?.status === 401) {
      if (typeof window !== 'undefined') {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('user');
        window.location.href = '/login';
      }
    }

    // 403 Forbidden: 권한 없음
    if (error.response?.status === 403) {
      console.error('접근 권한이 없습니다');
    }

    // 에러 메시지 추출
    const errorMessage =
      error.response?.data?.message || 
      error.message || 
      '알 수 없는 오류가 발생했습니다';

    return Promise.reject(new Error(errorMessage));
  }
);

export default api;

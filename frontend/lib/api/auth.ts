import {apiClient} from './client';

// 인증 관련 타입 (백엔드 DTO에 맞춤)
export interface SignInRequest {
    email: string;
    password: string;
}

export interface SignUpRequest {
    email: string;
    password: string;
    nickname: string;
    username: string;
}

export interface SignUpOwnerRequest {
    email: string;
    password: string;
    name: string;
    nickname: string;
    businessNumber: string;
}

export interface SignInResponse {
    AccessToken: string;
}

export interface SignUpResponse {
    userId: number;
    email: string;
    name: string;
    AccessToken: string;
}

export const authApi = {
    // 소비자 로그인
    signIn: async (data: SignInRequest) => {
        return await apiClient.post<SignInResponse>('/auth/signin', data);
    },

    // 소비자 회원가입
    signUp: async (data: SignUpRequest) => {
        return await apiClient.post<SignUpResponse>('/auth/signup', data);
    },

    // 판매자 로그인
    signInOwner: async (data: SignInRequest) => {
        return await apiClient.post<SignInResponse>('/auth/signin/owner', data);
    },

    // 판매자 회원가입
    signUpOwner: async (data: SignUpOwnerRequest) => {
        return await apiClient.post<SignUpResponse>('/auth/signup/owner', data);
    },
};

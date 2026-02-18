import { apiClient } from './client';

// 사용자 타입
export interface User {
    id: number;
    email: string;
    name: string;
    nickname: string;
    role: string;
    createdAt: string;
}

export interface GetUserResponse {
    id: number;
    email: string;
    nickname: string;
    username: string;
    imageUrl: string;
}

export interface UpdateProfileRequest {
    nickname: string;
    // 백엔드 DTO에 따라 추가 필드 가능
}

export const usersApi = {
    // 내 정보 조회
    getMyInfo: async () => {
        const response = await apiClient.get<GetUserResponse>('/users');
        return response.data;
    },

    // 프로필 수정
    updateProfile: async (data: UpdateProfileRequest) => {
        const response = await apiClient.patch('/users', data);
        return response.data;
    },

    // 회원 탈퇴
    withdraw: async () => {
        const response = await apiClient.delete('/users');
        return response.data;
    },
};

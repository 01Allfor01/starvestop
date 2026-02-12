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

export interface UpdateProfileRequest {
    nickname: string;
}

export interface ChangePasswordRequest {
    newPassword: string;
}

export const usersApi = {
    // 내 정보 조회
    getMe: async () => {
        const response = await apiClient.get<User>('/users/me');
        return response.data;
    },

    // 프로필 수정
    updateProfile: async (data: UpdateProfileRequest) => {
        const response = await apiClient.put('/users/me', data);
        return response.data;
    },

    // 비밀번호 변경
    changePassword: async (data: ChangePasswordRequest) => {
        const response = await apiClient.put('/users/me/password', data);
        return response.data;
    },

    // 회원 탈퇴
    withdraw: async () => {
        const response = await apiClient.delete('/users/me');
        return response.data;
    },
};

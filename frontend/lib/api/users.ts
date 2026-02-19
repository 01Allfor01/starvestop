import { apiClient } from './client';

export interface GetUserResponse {
    id: number;
    username: string;
    nickname: string;
    email: string;
    role: string;
    userKey: string;
    imageUrl?: string;
}

// ✅ 통합된 업데이트 요청 타입
export interface UpdateUserRequest {
    nickname: string;
    password?: string; // 선택사항
}

export const usersApi = {
    // 내 정보 조회
    getMyInfo: async (): Promise<GetUserResponse> => {
        const response = await apiClient.get<GetUserResponse>('/users');
        return response.data;
    },

    // ✅ 프로필 수정 (닉네임 + 비밀번호 통합)
    updateUser: async (data: UpdateUserRequest) => {
        const response = await apiClient.patch('/users', data);
        return response.data;
    },

    // 회원 탈퇴
    withdraw: async () => {
        const response = await apiClient.delete('/users/');
        return response.data;
    },
};
import { chatApi } from '@/lib/api/chat';

/**
 * 가게와의 채팅방을 열거나 생성
 * @param storeId 가게 ID
 * @param router Next.js router
 */
export const openOrCreateChatRoom = async (storeId: number, router: any) => {
    try {
        // 1. 기존 채팅방 목록 조회
        const chatRooms = await chatApi.getChatRooms();

        // 2. 해당 가게와의 채팅방 찾기
        const existingRoom = chatRooms.find(room => room.storeId === storeId);

        if (existingRoom) {
            // 3-1. 기존 채팅방이 있으면 해당 채팅방으로 이동
            console.log('✅ 기존 채팅방 발견:', existingRoom.id);
            router.push(`/chat/${existingRoom.id}`);
        } else {
            // 3-2. 기존 채팅방이 없으면 새로 생성
            console.log('📝 새 채팅방 생성 중...');
            const newRoom = await chatApi.createChatRoom(storeId);
            console.log('✅ 채팅방 생성 완료:', newRoom.id);
            router.push(`/chat/${newRoom.id}`);
        }
    } catch (error) {
        console.error('❌ 채팅방 열기 실패:', error);

        // 로그인 필요 에러 처리
        if ((error as any)?.response?.status === 401) {
            alert('로그인이 필요합니다');
            router.push('/login');
        } else {
            alert('채팅방을 열 수 없습니다. 다시 시도해주세요.');
        }
    }
};
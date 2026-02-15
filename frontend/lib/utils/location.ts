// 지구 반지름 (km)
const EARTH_RADIUS = 6371;

// 라디안 변환 함수
const toRad = (value: number) => (value * Math.PI) / 180;

// 두 좌표 사이의 거리 계산 (Haversine 공식)
export const calculateDistance = (lat1: number, lon1: number, lat2: number, lon2: number): string => {
    if (!lat1 || !lon1 || !lat2 || !lon2) return '0.0';

    const dLat = toRad(lat2 - lat1);
    const dLon = toRad(lon2 - lon1);

    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const distance = EARTH_RADIUS * c;

    return distance.toFixed(1); // 소수점 첫째 자리까지 반환 (예: 1.2)
};

// 현재 내 위치 가져오기 (Promise로 감싸서 사용하기 편하게)
export const getCurrentLocation = (): Promise<{ lat: number; lng: number } | null> => {
    return new Promise((resolve) => {
        if (!navigator.geolocation) {
            console.error('이 브라우저는 위치 정보를 지원하지 않습니다.');
            resolve(null);
            return;
        }

        navigator.geolocation.getCurrentPosition(
            (position) => {
                resolve({
                    lat: position.coords.latitude,
                    lng: position.coords.longitude,
                });
            },
            (error) => {
                console.error('위치 정보를 가져올 수 없습니다:', error);
                resolve(null);
            }
        );
    });
};
import { Day, MealTime } from '@/types/owner';

// ─────────────────────────────────────────────────────────
// 요일 비트마스크
// ─────────────────────────────────────────────────────────
export const DAY_BITS: Record<Day, number> = {
    [Day.MONDAY]: 1,      // 2^0
    [Day.TUESDAY]: 2,     // 2^1
    [Day.WEDNESDAY]: 4,   // 2^2
    [Day.THURSDAY]: 8,    // 2^3
    [Day.FRIDAY]: 16,     // 2^4
    [Day.SATURDAY]: 32,   // 2^5
    [Day.SUNDAY]: 64,     // 2^6
};

// Day[] → 비트마스크 숫자
export function daysToBitmask(days: Day[]): number {
    return days.reduce((bitmask, day) => bitmask | DAY_BITS[day], 0);
}

// 비트마스크 숫자 → Day[]
export function bitmaskToDays(bitmask: number): Day[] {
    const result: Day[] = [];
    Object.entries(DAY_BITS).forEach(([day, bit]) => {
        if ((bitmask & bit) !== 0) {
            result.push(day as Day);
        }
    });
    return result;
}

// ─────────────────────────────────────────────────────────
// 식사시간 비트마스크
// ─────────────────────────────────────────────────────────
export const MEAL_TIME_BITS: Record<MealTime, number> = {
    [MealTime.BREAKFAST]: 1,  // 2^0
    [MealTime.LUNCH]: 2,      // 2^1
    [MealTime.DINNER]: 4,     // 2^2
};

// MealTime[] → 비트마스크 숫자
export function mealTimesToBitmask(mealTimes: MealTime[]): number {
    return mealTimes.reduce((bitmask, time) => bitmask | MEAL_TIME_BITS[time], 0);
}

// 비트마스크 숫자 → MealTime[]
export function bitmaskToMealTimes(bitmask: number): MealTime[] {
    const result: MealTime[] = [];
    Object.entries(MEAL_TIME_BITS).forEach(([time, bit]) => {
        if ((bitmask & bit) !== 0) {
            result.push(time as MealTime);
        }
    });
    return result;
}

// ─────────────────────────────────────────────────────────
// UI용 한글 매핑
// ─────────────────────────────────────────────────────────
export const DAY_LABELS: Record<Day, string> = {
    [Day.MONDAY]: '월',
    [Day.TUESDAY]: '화',
    [Day.WEDNESDAY]: '수',
    [Day.THURSDAY]: '목',
    [Day.FRIDAY]: '금',
    [Day.SATURDAY]: '토',
    [Day.SUNDAY]: '일',
};

export const MEAL_TIME_LABELS: Record<MealTime, string> = {
    [MealTime.BREAKFAST]: '아침',
    [MealTime.LUNCH]: '점심',
    [MealTime.DINNER]: '저녁',
};

export const STORE_CATEGORY_LABELS = {
    KOREAN_FOOD: '한식',
    JAPANESE_FOOD: '일식',
    CHINESE_FOOD: '중식',
    WESTERN_FOOD: '양식',
    ASIAN_FOOD: '아시안',
    FAST_FOOD: '패스트푸드',
    CAFE: '카페',
    DESSERT: '디저트',
};

export const STORE_STATUS_LABELS = {
    OPENED: '영업중',
    CLOSED: '영업종료',
};

export const PRODUCT_STATUS_LABELS = {
    GENERAL: '일반',
    SALE: '세일',
};
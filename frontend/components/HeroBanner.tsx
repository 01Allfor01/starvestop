'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { ChevronLeft, ChevronRight } from 'lucide-react';

// 배너 데이터
const banners = [
    {
        id: 1,
        title: '오늘 마감! 한정수량!',
        subtitle: '신선한 음식을 지금 바로 픽업하세요',
        link: '/products/sale',
        bgColor: 'from-orange-500 to-red-300',
        image: 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800',
        emoji: '',
    },
    {
        id: 2,
        title: '합리적인 가격으로 규칙적인 식사',
        subtitle: '매주 신선한 한 끼를 정기 배송으로',
        link: '/subscriptions',
        bgColor: 'from-emerald-500 to-teal-200',
        image: 'https://images.unsplash.com/photo-1616645297079-dfaf44a6f977?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
        emoji: '',
    },
    {
        id: 3,
        title: '다양한 쿠폰 확인하러 가기',
        subtitle: '지금 바로 사용 가능한 할인 쿠폰',
        link: '/coupons',
        bgColor: 'from-yellow-400 to-orange-400',
        image: 'https://images.unsplash.com/photo-1607083206869-4c7672e72a8a?w=800',
        emoji: '🎁',
    },
    {
        id: 4,
        title: '디저트가 떙길 땐?',
        subtitle: '내 주변 디저트 매장 확인하기',
        link: '/stores?category=DESSERT',
        bgColor: 'from-pink-400 to-purple-300',
        image: 'https://images.unsplash.com/photo-1488477181946-6428a0291777?w=800',
        emoji: '🍰',
    },
    {
        id: 5,
        title: '한식이 떙길 땐?',
        subtitle: '내 주변 한식당 둘러보기',
        link: '/stores?category=KOREAN_FOOD',
        bgColor: 'from-amber-600 to-orange-300',
        image: 'data:image/jpeg;base64,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',
        emoji: '🍚',
    },
];

export default function HeroBanner() {
    const [currentIndex, setCurrentIndex] = useState(0);
    const [isHovered, setIsHovered] = useState(false);

    // 자동 슬라이드 (4초마다)
    useEffect(() => {
        if (isHovered) return;

        const timer = setInterval(() => {
            setCurrentIndex((prev) => (prev + 1) % banners.length);
        }, 3000);

        return () => clearInterval(timer);
    }, [isHovered]);

    const goToPrevious = () => {
        setCurrentIndex((prev) => (prev - 1 + banners.length) % banners.length);
    };

    const goToNext = () => {
        setCurrentIndex((prev) => (prev + 1) % banners.length);
    };

    const goToSlide = (index: number) => {
        setCurrentIndex(index);
    };

    return (
        <div
            className="relative h-[400px] md:h-[500px] overflow-hidden"
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            {/* 배너 슬라이드 */}
            <div className="relative h-full">
                {banners.map((banner, index) => (
                    <Link
                        key={banner.id}
                        href={banner.link}
                        className={`absolute inset-0 transition-opacity duration-700 ${
                            index === currentIndex ? 'opacity-100 z-10' : 'opacity-0 z-0'
                        }`}
                    >
                        {/* 배경 그라데이션 */}
                        <div className={`absolute inset-0 bg-gradient-to-r ${banner.bgColor}`} />

                        {/* 배경 이미지 (오버레이) */}
                        <div className="absolute inset-0">
                            <img
                                src={banner.image}
                                alt={banner.title}
                                className="w-full h-full object-cover opacity-20"
                            />
                        </div>

                        {/* 컨텐츠 */}
                        <div className="relative h-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex items-center">
                            <div className="max-w-2xl text-white">
                                {/* 이모지 */}
                                <div className="text-6xl mb-4 animate-bounce">{banner.emoji}</div>

                                {/* 제목 */}
                                <h2 className="text-3xl md:text-5xl font-bold mb-4 drop-shadow-lg">
                                    {banner.title}
                                </h2>

                                {/* 부제목 */}
                                <p className="text-lg md:text-2xl mb-6 drop-shadow-md opacity-95">
                                    {banner.subtitle}
                                </p>

                                {/* 버튼 */}
                                <div className="inline-block bg-white text-gray-900 px-8 py-3 rounded-full font-semibold hover:bg-opacity-90 transition-all hover:scale-105 shadow-xl">
                                    자세히 보기 →
                                </div>
                            </div>
                        </div>
                    </Link>
                ))}
            </div>

            {/* 좌우 화살표 */}
            <button
                onClick={goToPrevious}
                className="absolute left-4 top-1/2 -translate-y-1/2 z-20 bg-white/30 backdrop-blur-sm hover:bg-white/50 text-white p-3 rounded-full transition-all hover:scale-110"
                aria-label="이전"
            >
                <ChevronLeft className="w-6 h-6" />
            </button>
            <button
                onClick={goToNext}
                className="absolute right-4 top-1/2 -translate-y-1/2 z-20 bg-white/30 backdrop-blur-sm hover:bg-white/50 text-white p-3 rounded-full transition-all hover:scale-110"
                aria-label="다음"
            >
                <ChevronRight className="w-6 h-6" />
            </button>

            {/* 하단 인디케이터 */}
            <div className="absolute bottom-6 left-1/2 -translate-x-1/2 z-20 flex space-x-2">
                {banners.map((_, index) => (
                    <button
                        key={index}
                        onClick={() => goToSlide(index)}
                        className={`transition-all ${
                            index === currentIndex
                                ? 'w-8 bg-white'
                                : 'w-2 bg-white/50 hover:bg-white/75'
                        } h-2 rounded-full`}
                        aria-label={`${index + 1}번째 슬라이드로 이동`}
                    />
                ))}
            </div>

            {/* 진행 바 */}
            {!isHovered && (
                <div className="absolute bottom-0 left-0 right-0 h-1 bg-white/20 z-20">
                    <div
                        className="h-full bg-white transition-all duration-[4000ms] ease-linear"
                        style={{ width: '100%' }}
                        key={currentIndex}
                    />
                </div>
            )}
        </div>
    );
}
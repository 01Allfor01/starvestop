'use client';

import { HTMLAttributes } from 'react';
import { cva, type VariantProps } from 'class-variance-authority';
import { cn } from '@/lib/utils';

const badgeVariants = cva(
    'inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold transition-all',
    {
        variants: {
            variant: {
                // 마감 세일 배지 (빨강)
                sale: 'bg-red-500 text-white animate-pulse',

                // 구독 배지 (민트 그린)
                subscription: 'bg-secondary-500 text-white',

                // 새 상품 (오렌지)
                new: 'bg-primary-500 text-white',

                // 품절 (회색)
                soldout: 'bg-gray-400 text-white',

                // 일반 (회색 아웃라인)
                default: 'bg-gray-100 text-gray-700',

                // 성공 (녹색)
                success: 'bg-green-500 text-white',

                // 경고 (노랑)
                warning: 'bg-yellow-500 text-white',
            },
        },
        defaultVariants: {
            variant: 'default',
        },
    }
);

export interface BadgeProps
    extends HTMLAttributes<HTMLSpanElement>,
        VariantProps<typeof badgeVariants> {}

export default function Badge({ className, variant, ...props }: BadgeProps) {
    return (
        <span className={cn(badgeVariants({ variant }), className)} {...props} />
    );
}
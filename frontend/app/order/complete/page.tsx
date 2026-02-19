// app/order/complete/page.tsx
import { Suspense } from 'react';
import OrderCompleteClient from './OrderCompleteClient';

export default function OrderCompletePage() {
    return (
        <Suspense fallback={null}>
            <OrderCompleteClient />
        </Suspense>
    );
}

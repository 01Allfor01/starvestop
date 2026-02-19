// app/payment/fail/page.tsx
import { Suspense } from 'react';
import PaymentFailClient from './PaymentFailClient';

export default function PaymentFailPage() {
    return (
        <Suspense fallback={null}>
            <PaymentFailClient />
        </Suspense>
    );
}

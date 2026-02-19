// app/payment/success/page.tsx
import { Suspense } from 'react';
import PaymentSuccessClient from './PaymentSuccessClient';

export default function PaymentSuccessPage() {
    return (
        <Suspense fallback={null}>
            <PaymentSuccessClient />
        </Suspense>
    );
}

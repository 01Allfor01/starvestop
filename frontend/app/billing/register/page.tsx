// app/billing/register/page.tsx
import { Suspense } from 'react';
import BillingRegisterClient from './BillingRegisterClient';

export default function BillingRegisterPage() {
    return (
        <Suspense fallback={null}>
            <BillingRegisterClient />
        </Suspense>
    );
}

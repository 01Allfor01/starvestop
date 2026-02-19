// app/billing/success/page.tsx
import { Suspense } from 'react';
import BillingSuccessClient from './BillingSuccessClient';

export default function BillingSuccessPage() {
    return (
        <Suspense fallback={null}>
            <BillingSuccessClient />
        </Suspense>
    );
}

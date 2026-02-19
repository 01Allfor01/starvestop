import { Suspense } from 'react';
import BillingFailClient from './BillingFailClient';

export default function BillingFailPage() {
    return (
        <Suspense fallback={null}>
            <BillingFailClient />
        </Suspense>
    );
}

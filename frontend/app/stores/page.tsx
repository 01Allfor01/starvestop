// app/stores/page.tsx
import { Suspense } from 'react';
import StoresClient from './StoresClient';

export default function StoresPage() {
    return (
        <Suspense fallback={null}>
            <StoresClient />
        </Suspense>
    );
}

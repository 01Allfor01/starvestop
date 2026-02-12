// ✅ API 연결된 마감세일 상품 페이지 예시
// app/products/sale/page.tsx 파일에 적용하세요!

'use client';

import { useState, useEffect } from 'react';
import { productsApi, Product } from '@/lib/api/products';
import ProductCard from '@/components/ProductCard';

export default function SaleProductsPage() {
    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const data = await productsApi.getSaleProducts();
                setProducts(data);
            } catch (error: any) {
                console.error('Failed to fetch products:', error);
                setError('상품을 불러오는데 실패했습니다');
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, []);

    if (loading) {
        return (
            <div className="flex justify-center items-center min-h-screen">
                <div className="text-gray-600">로딩 중...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="flex justify-center items-center min-h-screen">
                <div className="text-red-500">{error}</div>
            </div>
        );
    }

    return (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {products.map((product) => (
                <ProductCard key={product.id} product={product} />
            ))}
        </div>
    );
}

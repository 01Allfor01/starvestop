'use client';

import { useState } from 'react';
import Link from 'next/link';
import { Plus, Edit, Trash2, Search, Package, AlertCircle } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';
import Input from '@/components/ui/Input';

export default function OwnerProductsPage() {
    const [searchQuery, setSearchQuery] = useState('');
    const [filterStatus, setFilterStatus] = useState<'all' | 'active' | 'inactive'>('all');

    // TODO: 실제 API 데이터로 교체
    const products = [
        {
            id: 1,
            name: '프리미엄 크루아상 3입',
            originalPrice: 6000,
            salePrice: 3000,
            discount: 50,
            stock: 12,
            status: 'ACTIVE',
            image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a',
            category: '베이커리',
            isSale: true,
        },
        {
            id: 2,
            name: '프리미엄 닭가슴살 샐러드',
            originalPrice: 12000,
            salePrice: 7200,
            discount: 40,
            stock: 8,
            status: 'ACTIVE',
            image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c',
            category: '샐러드',
            isSale: true,
        },
        {
            id: 3,
            name: '스페셜 연어 도시락',
            originalPrice: 13000,
            salePrice: 9750,
            discount: 25,
            stock: 0,
            status: 'INACTIVE',
            image: 'https://images.unsplash.com/photo-1608198399988-841b3c6f76d2',
            category: '도시락',
            isSale: false,
        },
        {
            id: 4,
            name: '신선 과일 박스',
            originalPrice: 15000,
            salePrice: 12000,
            discount: 20,
            stock: 5,
            status: 'ACTIVE',
            image: 'https://images.unsplash.com/photo-1488459716781-31db52582fe9',
            category: '과일',
            isSale: true,
        },
    ];

    const filteredProducts = products
        .filter(p => {
            if (filterStatus === 'active') return p.status === 'ACTIVE';
            if (filterStatus === 'inactive') return p.status === 'INACTIVE';
            return true;
        })
        .filter(p => p.name.toLowerCase().includes(searchQuery.toLowerCase()));

    const handleDelete = (id: number, name: string) => {
        if (confirm(`"${name}"을(를) 삭제하시겠습니까?`)) {
            // TODO: 실제 API 호출
            alert('상품이 삭제되었습니다.');
        }
    };

    const handleToggleStatus = (id: number, currentStatus: string) => {
        // TODO: 실제 API 호출
        const newStatus = currentStatus === 'ACTIVE' ? '판매중지' : '판매중';
        alert(`상품이 ${newStatus} 되었습니다.`);
    };

    const stats = {
        total: products.length,
        active: products.filter(p => p.status === 'ACTIVE').length,
        lowStock: products.filter(p => p.stock <= 5 && p.stock > 0).length,
        outOfStock: products.filter(p => p.stock === 0).length,
    };

    return (
        <div className="min-h-screen bg-gray-50">
            {/* 헤더 */}
            <div className="bg-white border-b border-gray-200">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <Link href="/owner/dashboard" className="text-sm text-gray-600 hover:text-primary-500 mb-2 inline-block">
                                ← 대시보드
                            </Link>
                            <h1 className="text-3xl font-bold text-gray-900">상품 관리</h1>
                            <p className="text-gray-600 mt-1">총 {stats.total}개의 상품</p>
                        </div>
                        <Link href="/owner/products/new">
                            <Button>
                                <Plus className="w-5 h-5 mr-2" />
                                상품 등록
                            </Button>
                        </Link>
                    </div>
                </div>
            </div>

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 통계 */}
                <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
                    <Card>
                        <div className="text-center">
                            <p className="text-sm text-gray-600 mb-1">전체 상품</p>
                            <p className="text-2xl font-bold text-gray-900">{stats.total}개</p>
                        </div>
                    </Card>
                    <Card>
                        <div className="text-center">
                            <p className="text-sm text-gray-600 mb-1">판매 중</p>
                            <p className="text-2xl font-bold text-green-600">{stats.active}개</p>
                        </div>
                    </Card>
                    <Card>
                        <div className="text-center">
                            <p className="text-sm text-gray-600 mb-1">재고 부족</p>
                            <p className="text-2xl font-bold text-yellow-600">{stats.lowStock}개</p>
                        </div>
                    </Card>
                    <Card>
                        <div className="text-center">
                            <p className="text-sm text-gray-600 mb-1">품절</p>
                            <p className="text-2xl font-bold text-red-600">{stats.outOfStock}개</p>
                        </div>
                    </Card>
                </div>

                {/* 검색 & 필터 */}
                <Card className="mb-6">
                    <div className="flex flex-col md:flex-row gap-4">
                        <div className="flex-1">
                            <div className="relative">
                                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                                <input
                                    type="text"
                                    placeholder="상품명 검색"
                                    value={searchQuery}
                                    onChange={(e) => setSearchQuery(e.target.value)}
                                    className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                                />
                            </div>
                        </div>
                        <div className="flex space-x-2">
                            <button
                                onClick={() => setFilterStatus('all')}
                                className={`px-4 py-2 rounded-lg transition ${
                                    filterStatus === 'all'
                                        ? 'bg-primary-500 text-white'
                                        : 'bg-white text-gray-700 hover:bg-gray-100'
                                }`}
                            >
                                전체
                            </button>
                            <button
                                onClick={() => setFilterStatus('active')}
                                className={`px-4 py-2 rounded-lg transition ${
                                    filterStatus === 'active'
                                        ? 'bg-primary-500 text-white'
                                        : 'bg-white text-gray-700 hover:bg-gray-100'
                                }`}
                            >
                                판매중
                            </button>
                            <button
                                onClick={() => setFilterStatus('inactive')}
                                className={`px-4 py-2 rounded-lg transition ${
                                    filterStatus === 'inactive'
                                        ? 'bg-primary-500 text-white'
                                        : 'bg-white text-gray-700 hover:bg-gray-100'
                                }`}
                            >
                                판매중지
                            </button>
                        </div>
                    </div>
                </Card>

                {/* 상품 리스트 */}
                {filteredProducts.length > 0 ? (
                    <div className="space-y-4">
                        {filteredProducts.map((product) => (
                            <Card key={product.id} padding="none">
                                <div className="flex flex-col md:flex-row">
                                    <img
                                        src={product.image}
                                        alt={product.name}
                                        className="w-full md:w-48 h-48 object-cover"
                                    />
                                    <div className="p-6 flex-1">
                                        <div className="flex items-start justify-between mb-4">
                                            <div className="flex-1">
                                                <div className="flex items-center space-x-2 mb-2">
                                                    <h3 className="text-xl font-bold text-gray-900">{product.name}</h3>
                                                    {product.isSale && (
                                                        <Badge variant="sale">{product.discount}% OFF</Badge>
                                                    )}
                                                    {product.status === 'ACTIVE' ? (
                                                        <Badge variant="success">판매중</Badge>
                                                    ) : (
                                                        <Badge variant="default">판매중지</Badge>
                                                    )}
                                                </div>
                                                <p className="text-sm text-gray-500 mb-3">{product.category}</p>

                                                <div className="flex items-baseline space-x-2 mb-3">
                                                    {product.isSale && (
                                                        <span className="text-sm text-gray-400 line-through">
                              {product.originalPrice.toLocaleString()}원
                            </span>
                                                    )}
                                                    <span className="text-2xl font-bold text-primary-600">
                            {product.salePrice.toLocaleString()}원
                          </span>
                                                </div>

                                                <div className="flex items-center space-x-4">
                                                    <div className={`flex items-center ${
                                                        product.stock === 0 ? 'text-red-600' :
                                                            product.stock <= 5 ? 'text-yellow-600' :
                                                                'text-gray-700'
                                                    }`}>
                                                        <Package className="w-4 h-4 mr-1" />
                                                        <span className="text-sm font-medium">
                              재고: {product.stock}개
                            </span>
                                                    </div>
                                                    {product.stock <= 5 && product.stock > 0 && (
                                                        <div className="flex items-center text-yellow-600">
                                                            <AlertCircle className="w-4 h-4 mr-1" />
                                                            <span className="text-sm">재고 부족</span>
                                                        </div>
                                                    )}
                                                    {product.stock === 0 && (
                                                        <div className="flex items-center text-red-600">
                                                            <AlertCircle className="w-4 h-4 mr-1" />
                                                            <span className="text-sm font-semibold">품절</span>
                                                        </div>
                                                    )}
                                                </div>
                                            </div>
                                        </div>

                                        <div className="flex space-x-2">
                                            <Link href={`/owner/products/${product.id}/edit`} className="flex-1">
                                                <Button variant="outline" fullWidth size="sm">
                                                    <Edit className="w-4 h-4 mr-2" />
                                                    수정
                                                </Button>
                                            </Link>
                                            <Button
                                                variant="outline"
                                                size="sm"
                                                onClick={() => handleToggleStatus(product.id, product.status)}
                                            >
                                                {product.status === 'ACTIVE' ? '판매중지' : '판매시작'}
                                            </Button>
                                            <Button
                                                variant="outline"
                                                size="sm"
                                                onClick={() => handleDelete(product.id, product.name)}
                                                className="text-red-600 hover:text-red-700 hover:border-red-300"
                                            >
                                                <Trash2 className="w-4 h-4" />
                                            </Button>
                                        </div>
                                    </div>
                                </div>
                            </Card>
                        ))}
                    </div>
                ) : (
                    <Card className="text-center py-16">
                        <Package className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600 mb-4">
                            {searchQuery ? '검색 결과가 없습니다' : '등록된 상품이 없습니다'}
                        </p>
                        <Link href="/owner/products/new">
                            <Button>
                                <Plus className="w-5 h-5 mr-2" />
                                첫 상품 등록하기
                            </Button>
                        </Link>
                    </Card>
                )}
            </div>
        </div>
    );
}
'use client';

import { useState } from 'react';
import Link from 'next/link';
import { Store, MapPin, Clock, Phone, Edit, Trash2, Plus } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function OwnerStoresPage() {
    // TODO: 실제 API 데이터로 교체
    const [stores, setStores] = useState([
        {
            id: 1,
            name: '파리바게뜨 강남점',
            category: '베이커리',
            address: '서울특별시 강남구 테헤란로 123',
            phone: '02-1234-5678',
            openTime: '08:00',
            closeTime: '22:00',
            isOpen: true,
            productCount: 45,
            image: 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4',
        },
    ]);

    const handleDelete = (id: number) => {
        if (confirm('정말 이 가게를 삭제하시겠습니까?')) {
            setStores(stores.filter(s => s.id !== id));
            alert('가게가 삭제되었습니다.');
        }
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
                            <h1 className="text-3xl font-bold text-gray-900">가게 관리</h1>
                            <p className="text-gray-600 mt-1">총 {stores.length}개의 가게</p>
                        </div>
                        <Link href="/owner/stores/new">
                            <Button>
                                <Plus className="w-5 h-5 mr-2" />
                                가게 추가
                            </Button>
                        </Link>
                    </div>
                </div>
            </div>

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {stores.length > 0 ? (
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                        {stores.map((store) => (
                            <Card key={store.id} padding="none" className="overflow-hidden">
                                <div className="flex flex-col md:flex-row">
                                    <img
                                        src={store.image}
                                        alt={store.name}
                                        className="w-full md:w-48 h-48 object-cover"
                                    />
                                    <div className="p-6 flex-1">
                                        <div className="flex items-start justify-between mb-4">
                                            <div>
                                                <div className="flex items-center space-x-2 mb-2">
                                                    <h3 className="text-xl font-bold text-gray-900">{store.name}</h3>
                                                    <Badge variant={store.isOpen ? 'success' : 'default'}>
                                                        {store.isOpen ? '영업중' : '영업종료'}
                                                    </Badge>
                                                </div>
                                                <p className="text-sm text-gray-500">{store.category}</p>
                                            </div>
                                        </div>

                                        <div className="space-y-2 text-sm text-gray-700 mb-4">
                                            <div className="flex items-start">
                                                <MapPin className="w-4 h-4 text-gray-400 mr-2 mt-0.5 flex-shrink-0" />
                                                <span>{store.address}</span>
                                            </div>
                                            <div className="flex items-center">
                                                <Phone className="w-4 h-4 text-gray-400 mr-2" />
                                                <span>{store.phone}</span>
                                            </div>
                                            <div className="flex items-center">
                                                <Clock className="w-4 h-4 text-gray-400 mr-2" />
                                                <span>{store.openTime} - {store.closeTime}</span>
                                            </div>
                                            <div className="flex items-center">
                                                <Store className="w-4 h-4 text-gray-400 mr-2" />
                                                <span>등록 상품 {store.productCount}개</span>
                                            </div>
                                        </div>

                                        <div className="flex space-x-2">
                                            <Link href={`/owner/stores/${store.id}/edit`} className="flex-1">
                                                <Button variant="outline" fullWidth size="sm">
                                                    <Edit className="w-4 h-4 mr-2" />
                                                    수정
                                                </Button>
                                            </Link>
                                            <Button
                                                variant="outline"
                                                size="sm"
                                                onClick={() => handleDelete(store.id)}
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
                        <Store className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-600 mb-4">등록된 가게가 없습니다</p>
                        <Link href="/owner/stores/new">
                            <Button>
                                <Plus className="w-5 h-5 mr-2" />
                                첫 가게 등록하기
                            </Button>
                        </Link>
                    </Card>
                )}
            </div>
        </div>
    );
}
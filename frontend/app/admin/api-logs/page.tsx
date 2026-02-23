'use client';

import { useState } from 'react';
import Link from 'next/link';
import { ArrowLeft, Search, Filter, Download } from 'lucide-react';
import Button from '@/components/ui/Button';
import Card from '@/components/ui/Card';
import Badge from '@/components/ui/Badge';

export default function ApiLogsPage() {
    const [searchQuery, setSearchQuery] = useState('');
    const [filterMethod, setFilterMethod] = useState<'all' | 'GET' | 'POST' | 'PUT' | 'DELETE'>('all');
    const [filterStatus, setFilterStatus] = useState<'all' | '2xx' | '4xx' | '5xx'>('all');

    // TODO: 실제 API 데이터로 교체
    const logs = [
        {
            id: 1,
            method: 'POST',
            path: '/api/orders',
            status: 201,
            duration: 245,
            timestamp: '2026-02-09 14:35:21',
            userId: 'user_123',
        },
        {
            id: 2,
            method: 'GET',
            path: '/api/products',
            status: 200,
            duration: 89,
            timestamp: '2026-02-09 14:35:18',
            userId: 'user_456',
        },
        {
            id: 3,
            method: 'PUT',
            path: '/api/stores/1',
            status: 200,
            duration: 156,
            timestamp: '2026-02-09 14:35:15',
            userId: 'owner_789',
        },
        {
            id: 4,
            method: 'GET',
            path: '/api/users/profile',
            status: 404,
            duration: 12,
            timestamp: '2026-02-09 14:35:10',
            userId: 'user_999',
        },
        {
            id: 5,
            method: 'POST',
            path: '/api/payments',
            status: 500,
            duration: 3421,
            timestamp: '2026-02-09 14:35:05',
            userId: 'user_111',
        },
        {
            id: 6,
            method: 'DELETE',
            path: '/api/cart/items/5',
            status: 204,
            duration: 45,
            timestamp: '2026-02-09 14:35:00',
            userId: 'user_222',
        },
    ];

    const filteredLogs = logs.filter(log => {
        const matchMethod = filterMethod === 'all' || log.method === filterMethod;
        const matchStatus =
            filterStatus === 'all' ||
            (filterStatus === '2xx' && log.status >= 200 && log.status < 300) ||
            (filterStatus === '4xx' && log.status >= 400 && log.status < 500) ||
            (filterStatus === '5xx' && log.status >= 500);
        const matchSearch = searchQuery === '' || log.path.toLowerCase().includes(searchQuery.toLowerCase());

        return matchMethod && matchStatus && matchSearch;
    });

    const getMethodColor = (method: string) => {
        switch (method) {
            case 'GET': return 'text-blue-600 bg-blue-50';
            case 'POST': return 'text-green-600 bg-green-50';
            case 'PUT': return 'text-yellow-600 bg-yellow-50';
            case 'DELETE': return 'text-red-600 bg-red-50';
            default: return 'text-gray-600 bg-gray-50';
        }
    };

    const getStatusVariant = (status: number) => {
        if (status >= 200 && status < 300) return 'success';
        if (status >= 400 && status < 500) return 'warning';
        if (status >= 500) return 'sale';
        return 'default';
    };

    const stats = {
        total: logs.length,
        success: logs.filter(l => l.status >= 200 && l.status < 300).length,
        clientError: logs.filter(l => l.status >= 400 && l.status < 500).length,
        serverError: logs.filter(l => l.status >= 500).length,
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <div className="bg-white border-b border-gray-200">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <Link href="/admin/dashboard" className="text-sm text-gray-600 hover:text-primary-500 mb-2 inline-block">
                                ← 대시보드
                            </Link>
                            <h1 className="text-3xl font-bold text-gray-900">API 로그</h1>
                            <p className="text-gray-600 mt-1">전체 API 요청 내역</p>
                        </div>
                        <Button variant="outline">
                            <Download className="w-5 h-5 mr-2" />
                            다운로드
                        </Button>
                    </div>
                </div>
            </div>

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 통계 */}
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
                    <Card>
                        <div className="text-center">
                            <p className="text-sm text-gray-600 mb-1">전체</p>
                            <p className="text-2xl font-bold text-gray-900">{stats.total}</p>
                        </div>
                    </Card>
                    <Card>
                        <div className="text-center">
                            <p className="text-sm text-gray-600 mb-1">성공 (2xx)</p>
                            <p className="text-2xl font-bold text-green-600">{stats.success}</p>
                        </div>
                    </Card>
                    <Card>
                        <div className="text-center">
                            <p className="text-sm text-gray-600 mb-1">클라이언트 에러 (4xx)</p>
                            <p className="text-2xl font-bold text-yellow-600">{stats.clientError}</p>
                        </div>
                    </Card>
                    <Card>
                        <div className="text-center">
                            <p className="text-sm text-gray-600 mb-1">서버 에러 (5xx)</p>
                            <p className="text-2xl font-bold text-red-600">{stats.serverError}</p>
                        </div>
                    </Card>
                </div>

                {/* 검색 & 필터 */}
                <Card className="mb-6">
                    <div className="space-y-4">
                        <div className="relative">
                            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                            <input
                                type="text"
                                placeholder="API 경로 검색"
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                            />
                        </div>

                        <div className="flex flex-wrap gap-2">
                            <p className="text-sm font-medium text-gray-700 w-full mb-1">메서드:</p>
                            {(['all', 'GET', 'POST', 'PUT', 'DELETE'] as const).map((method) => (
                                <button
                                    key={method}
                                    onClick={() => setFilterMethod(method)}
                                    className={`px-3 py-1 rounded-lg text-sm transition ${
                                        filterMethod === method
                                            ? 'bg-primary-500 text-white'
                                            : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                                    }`}
                                >
                                    {method === 'all' ? '전체' : method}
                                </button>
                            ))}

                            <p className="text-sm font-medium text-gray-700 w-full mt-2 mb-1">상태코드:</p>
                            {(['all', '2xx', '4xx', '5xx'] as const).map((status) => (
                                <button
                                    key={status}
                                    onClick={() => setFilterStatus(status)}
                                    className={`px-3 py-1 rounded-lg text-sm transition ${
                                        filterStatus === status
                                            ? 'bg-primary-500 text-white'
                                            : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                                    }`}
                                >
                                    {status === 'all' ? '전체' : status}
                                </button>
                            ))}
                        </div>
                    </div>
                </Card>

                {/* 로그 목록 */}
                <Card padding="none">
                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead className="bg-gray-50 border-b border-gray-200">
                            <tr>
                                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">시간</th>
                                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">메서드</th>
                                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">경로</th>
                                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">상태</th>
                                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">응답시간</th>
                                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">사용자</th>
                            </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-200">
                            {filteredLogs.map((log) => (
                                <tr key={log.id} className="hover:bg-gray-50">
                                    <td className="px-4 py-3 text-sm text-gray-600">{log.timestamp}</td>
                                    <td className="px-4 py-3">
                      <span className={`px-2 py-1 text-xs font-medium rounded ${getMethodColor(log.method)}`}>
                        {log.method}
                      </span>
                                    </td>
                                    <td className="px-4 py-3 text-sm font-mono text-gray-900">{log.path}</td>
                                    <td className="px-4 py-3">
                                        <Badge variant={getStatusVariant(log.status) as any}>{log.status}</Badge>
                                    </td>
                                    <td className="px-4 py-3 text-sm text-gray-600">{log.duration}ms</td>
                                    <td className="px-4 py-3 text-sm text-gray-600">{log.userId}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </Card>
            </div>
        </div>
    );
}
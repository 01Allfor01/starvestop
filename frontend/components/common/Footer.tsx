import Link from 'next/link';

export default function Footer() {
    return (
        <footer className="bg-gray-900 text-gray-300">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
                {/* 메인 섹션 */}
                <div className="text-center mb-8">
                    <h2 className="text-2xl font-bold text-white mb-2">Starve Stop</h2>
                    <p className="text-gray-400">합리적인 가격으로 신선한 음식을</p>
                </div>

                {/* 저작권 */}
                <div className="border-t border-gray-800 pt-8 text-center">
                    <p className="text-sm text-gray-500">
                        © {new Date().getFullYear()} Starve Stop. All rights reserved.
                    </p>
                </div>
            </div>
        </footer>
    );
}
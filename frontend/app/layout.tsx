import type { Metadata } from "next";
import "./globals.css";
import { Providers } from "./providers";
import ConditionalHeader from "@/components/common/ConditionalHeader";
import Footer from "@/components/common/Footer";

export const metadata: Metadata = {
    title: "Starve Stop - 합리적인 가격으로 신선한 음식을",
    description: "마감 세일과 정기 구독으로 신선한 음식을 더 저렴하게",
    keywords: ["마감세일", "식품", "구독", "배달", "신선식품"],
};

export default function RootLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="ko">
        <head>
            <link rel="icon" href="/favicon.ico" />
        </head>
        <body className="antialiased">
        <Providers>
            <ConditionalHeader />
            <main className="min-h-screen">
                {children}
            </main>
            <Footer />
        </Providers>
        </body>
        </html>
    );
}
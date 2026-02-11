# 🍴 Starve Stop - Frontend

> 마감 세일과 정기 구독으로 신선한 음식을 더 저렴하게

Next.js 14 기반의 푸드테크 플랫폼 프론트엔드 프로젝트입니다.

## 📚 기술 스택

- **Framework**: Next.js 14 (App Router)
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **State Management**: 
  - Zustand (전역 상태)
  - TanStack Query (서버 상태)
- **Form**: React Hook Form + Zod
- **HTTP Client**: Axios
- **Real-time**: STOMP.js (WebSocket)
- **Date**: date-fns
- **Icons**: Lucide React
- **Animation**: Framer Motion
- **Toast**: Sonner

## 🚀 시작하기

### 1. 환경 변수 설정

`.env.local` 파일을 생성하고 다음 내용을 입력하세요:

```bash
# 백엔드 API URL
NEXT_PUBLIC_API_URL=http://localhost:8080

# WebSocket URL
NEXT_PUBLIC_WS_URL=ws://localhost:8080/ws

# 카카오 로그인
NEXT_PUBLIC_KAKAO_CLIENT_ID=여기에_카카오_클라이언트_ID_입력
NEXT_PUBLIC_KAKAO_REDIRECT_URI=http://localhost:3000

# 토스페이먼츠
NEXT_PUBLIC_TOSS_CLIENT_KEY=여기에_토스_클라이언트_키_입력
```

### 2. 의존성 설치

```bash
npm install
```

### 3. 개발 서버 실행

```bash
npm run dev
```

브라우저에서 [http://localhost:3000](http://localhost:3000)을 열어 확인하세요.

## 📁 프로젝트 구조

```
starvestop-frontend/
├── app/                    # Next.js App Router
│   ├── (auth)/            # 인증 레이아웃 그룹
│   │   ├── login/
│   │   └── signup/
│   ├── (consumer)/        # 소비자 레이아웃 그룹
│   │   ├── page.tsx       # 홈
│   │   ├── products/
│   │   ├── stores/
│   │   ├── cart/
│   │   └── mypage/
│   ├── layout.tsx         # 루트 레이아웃
│   ├── providers.tsx      # React Query Provider
│   └── globals.css        # 전역 스타일
├── components/            # 재사용 컴포넌트
│   ├── common/           # 공통 컴포넌트
│   ├── product/          # 상품 관련
│   ├── store/            # 가게 관련
│   └── ui/               # UI 컴포넌트
├── lib/                   # 유틸리티
│   ├── axios.ts          # API 클라이언트
│   └── utils.ts          # 헬퍼 함수
├── hooks/                 # 커스텀 훅
├── stores/                # Zustand 스토어
│   ├── authStore.ts      # 인증 상태
│   └── cartStore.ts      # 장바구니 상태
├── types/                 # TypeScript 타입
│   └── index.ts
├── constants/             # 상수
└── public/                # 정적 파일
```

## 🎨 디자인 시스템

### 컬러 팔레트

- **Primary (오렌지)**: 따뜻함, 식욕, 에너지
  - Main: `#FF9800`
- **Secondary (민트 그린)**: 신선함, 건강
  - Main: `#00AA84`

### 타이포그래피

- **폰트**: Pretendard (한글), Inter (영문)
- **기본 크기**: 16px

### 컴포넌트

- 둥근 모서리: 8-16px
- 그림자: soft, medium, strong
- 애니메이션: fade-in, slide-up

## 🔧 개발 가이드

### API 호출

```typescript
import api from '@/lib/axios';
import { Product } from '@/types';

// GET 요청
const products = await api.get<Product[]>('/products');

// POST 요청
const newProduct = await api.post('/products', {
  name: '크루아상',
  price: 3000,
});
```

### 전역 상태 사용

```typescript
import { useAuthStore } from '@/stores/authStore';
import { useCartStore } from '@/stores/cartStore';

function MyComponent() {
  const { user, isAuthenticated } = useAuthStore();
  const { items, totalPrice } = useCartStore();
  
  // ...
}
```

### React Query 사용

```typescript
import { useQuery } from '@tanstack/react-query';
import api from '@/lib/axios';

function ProductList() {
  const { data, isLoading, error } = useQuery({
    queryKey: ['products'],
    queryFn: () => api.get('/products'),
  });
  
  // ...
}
```

## 📝 개발 우선순위

1. ✅ **프로젝트 세팅**
2. 🔄 **로그인/회원가입** (진행 중)
3. ⏳ **메인 페이지** (마감세일, 구독)
4. ⏳ **상품 상세/장바구니**
5. ⏳ **마이페이지**

## 🌐 환경별 설정

### 로컬 개발
```
API_URL: http://localhost:8080
```

### 프로덕션
```
API_URL: https://api.starvestop.com (배포 시 변경)
```

## 📚 참고 자료

- [Next.js 문서](https://nextjs.org/docs)
- [Tailwind CSS 문서](https://tailwindcss.com/docs)
- [TanStack Query 문서](https://tanstack.com/query/latest)
- [Zustand 문서](https://docs.pmnd.rs/zustand)

## 👥 팀

- **프론트엔드 개발**: Claude (AI Assistant)
- **백엔드 개발**: [팀원 이름]

## 📄 라이선스

© 2026 Starve Stop. All rights reserved.

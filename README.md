<img width="1902" height="1063" alt="image" src="https://github.com/user-attachments/assets/1a2b574d-d778-4bfe-b226-851282cf2092" />


<div style="text-align: center;">
저렴하고 든든한 한 끼를 제공하자

</div>

[자세한 내용은 브로셔에서](https://www.notion.so/teamsparta/1-Starve-stop-2fb2dc3ef51480cebd50f1a2509e7311?source=copy_link) 

---

## 📄목차

- 🍱 [프로젝트 소개](#-프로젝트-소개)
- 🗃️ [주요 기능](#-주요-기능)
- 🖥️ [설계 문서](#-설계-문서)
- ✨ [기술적 의사 결정](#-기술적-의사-결정)
- 🧨 [트러블 슈팅 및 성능개선](#-트러블-슈팅-및-성능-개선)
- 🖥️ [기술 스택](#-기술-스택)

---
## 🍱 프로젝트 소개


### 🍽️ 1인 가구를 위한 식사 구독·결제 통합 플랫폼
본 프로젝트는 1인 가구를 위한 식사 구독 결제 통합 플랫폼입니다.

식사 구독을 제공하는 매장과 사용자를 연결하고, 마감 할인 식사 정보까지 한 번에 제공하여 1인 가구와 음식점 모두에게 지속 가능한 식사 생태계를 구축하는 것을 목표로 합니다.

### 👤 사용자
- **합리적인 식사 루틴 형성** : 내 주변 매장을 탐색 및 비교하고, 플랫폼 내에서 구독 신청부터 결제까지 한 번에 진행합니다.
- **가성비 넘치는 한 끼** : 마감 할인 정보를 통해 동일한 퀄리티의 음식을 더 저렴하게 즐길 수 있습니다.

### 🏪 매장
- **지속 가능한 매출 구조 설계** : 구독 기반의 자동 결제를 통해 고정 고객을 확보하고 예측 가능한 매출 흐름을 만듭니다.
- **재고 부담 완화** : 판매가 어려운 마감 시간대 상품을 할인 메뉴로 등록하여 음식 폐기를 줄이고 추가 수익을 창출합니다.

--- 

## 🗃️ 주요 기능

### 🛍 마감 상품 세일 제공
- 판매자가 직접 마감 임박 상품이나 재고를 할인 등록하여 재고 부담을 완화
- 소비자에게 가성비 높은 식사 제공
- 다수 사용자가 동시에 마감 상품을 구매할 때 발생하는 초과 구매(Lost Update) 문제를 방지하기 위해 조건부 UPDATE 쿼리를 적용
- DB 부하와 네트워크 왕복을 최소화하면서도 동시성 제어를 구현

### 📍 실시간 위치 기반 거리 순 상품 조회
- 사용자의 현재 위치를 기준으로 일정 반경 내 가까운 매장의 상품을 빠르게 정렬 및 조회
- 대용량 데이터 환경에서의 병목을 해결하기 위해 Redis GEO를 도입하여 반경 탐색 및 거리 정렬을 위임
- DB 조회 시 MBRContains를 활용한 Bounding Box 1차 필터링을 적용하여 응답 속도를 대폭 단축

### 🔁 구독 기능 및 자동 결제
- 월 단위 식사 구독 제공 및 매장 운영 일정에 맞춘 자동 결제 갱신
- 도메인 이벤트 기반 설계를 도입하여 강결합 문제를 해결하고 부가 기능 실패가 결제 실패로 이어지는 구조적 문제를 해결

### 🧾 판매자 정산 시스템
- 판매자의 결제 내역 기반 월 단위 자동 집계 및 수수료·정산금 분리 관리
- 총 매출, 실제 정산 금액을 명확히 분리하여 데이터를 관리
- PG사의 정산 내역과 서비스 정산 데이터를 비교·검증할 수 있는 구조로 설계

### 🔔 결제·구독 이벤트 알림
- 결제 성공/실패, 구독 갱신 등 중요 상태 변화 발생 시 즉시 알림 및 영수증 자동 처리
- 프론트엔드 의존도를 낮추고 향후 모바일 앱 확장을 고려해 FCM 인프라를 구축
- 스케줄러 기반으로 알림을 대량 발송

  
### 🔐 OAuth 2.0 기반 인증·인가
- 카카오 로그인으로 간편 가입 제공. 사용자와 판매자의 역할을 분리하여 보안과 책임에 맞는 인증 정책 적용


### 🎟️ 쿠폰 할인 기능
- 이벤트 프로모션 쿠폰 발급 및 주문 시 간편 적용

### 💬 실시간 1:1 CS 채팅
- 구매자와 판매자 간 상품 수령, 일정 변경 등에 대한 매장 단위 1:1 채팅 지원

---
## 🖥️ 기술 스택
<details>
   <summary>제목</summary>
   <div markdown="1">

### Language
<img alt="Static Badge" src="https://img.shields.io/badge/Java-007396?style=flat&logo=data:image/svg%2bxml;base64,PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj4KDTwhLS0gVXBsb2FkZWQgdG86IFNWRyBSZXBvLCB3d3cuc3ZncmVwby5jb20sIFRyYW5zZm9ybWVkIGJ5OiBTVkcgUmVwbyBNaXhlciBUb29scyAtLT4KPHN2ZyB3aWR0aD0iMTUwcHgiIGhlaWdodD0iMTUwcHgiIHZpZXdCb3g9IjAgMCAzMi4wMCAzMi4wMCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiBmaWxsPSIjZmZmZmZmIiBzdHJva2U9IiNmZmZmZmYiIHN0cm9rZS13aWR0aD0iMC4yNTYiPgoNPGcgaWQ9IlNWR1JlcG9fYmdDYXJyaWVyIiBzdHJva2Utd2lkdGg9IjAiLz4KDTxnIGlkPSJTVkdSZXBvX3RyYWNlckNhcnJpZXIiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIvPgoNPGcgaWQ9IlNWR1JlcG9faWNvbkNhcnJpZXIiPiA8cGF0aCBmaWxsPSIjZmZmZmZmIiBkPSJNMTIuNTU3IDIzLjIyYzAgMC0wLjk4MiAwLjU3MSAwLjY5OSAwLjc2NSAyLjAzNyAwLjIzMiAzLjA3OSAwLjE5OSA1LjMyNC0wLjIyNiAwIDAgMC41OSAwLjM3IDEuNDE1IDAuNjkxLTUuMDMzIDIuMTU3LTExLjM5LTAuMTI1LTcuNDM3LTEuMjN6TTExLjk0MiAyMC40MDVjMCAwLTEuMTAyIDAuODE2IDAuNTgxIDAuOTkgMi4xNzYgMC4yMjQgMy44OTUgMC4yNDMgNi44NjktMC4zMyAwIDAgMC40MTEgMC40MTcgMS4wNTggMC42NDUtNi4wODUgMS43NzktMTIuODYzIDAuMTQtOC41MDgtMS4zMDV6TTE3LjEyNyAxNS42M2MxLjI0IDEuNDI4LTAuMzI2IDIuNzEzLTAuMzI2IDIuNzEzczMuMTQ5LTEuNjI1IDEuNzAzLTMuNjYxYy0xLjM1MS0xLjg5OC0yLjM4Ni0yLjg0MSAzLjIyMS02LjA5MyAwIDAtOC44MDEgMi4xOTgtNC41OTggNy4wNDJ6TTIzLjc4MyAyNS4zMDJjMCAwIDAuNzI3IDAuNTk5LTAuODAxIDEuMDYyLTIuOTA1IDAuODgtMTIuMDkxIDEuMTQ2LTE0LjY0MyAwLjAzNS0wLjkxNy0wLjM5OSAwLjgwMy0wLjk1MyAxLjM0NC0xLjA2OSAwLjU2NC0wLjEyMiAwLjg4Ny0wLjEgMC44ODctMC4xLTEuMDIwLTAuNzE5LTYuNTk0IDEuNDExLTIuODMxIDIuMDIxIDEwLjI2MiAxLjY2NCAxOC43MDYtMC43NDkgMTYuMDQ0LTEuOTV6TTEzLjAyOSAxNy40ODljMCAwLTQuNjczIDEuMTEtMS42NTUgMS41MTMgMS4yNzQgMC4xNzEgMy44MTQgMC4xMzIgNi4xODEtMC4wNjYgMS45MzQtMC4xNjMgMy44NzYtMC41MSAzLjg3Ni0wLjUxcy0wLjY4MiAwLjI5Mi0xLjE3NSAwLjYyOWMtNC43NDUgMS4yNDgtMTMuOTExIDAuNjY3LTExLjI3Mi0wLjYwOSAyLjIzMi0xLjA3OSA0LjA0Ni0wLjk1NiA0LjA0Ni0wLjk1NnpNMjEuNDEyIDIyLjE3NGM0LjgyNC0yLjUwNiAyLjU5My00LjkxNSAxLjAzNy00LjU5MS0wLjM4MiAwLjA3OS0wLjU1MiAwLjE0OC0wLjU1MiAwLjE0OHMwLjE0Mi0wLjIyMiAwLjQxMi0wLjMxOGMzLjA3OS0xLjA4MyA1LjQ0OCAzLjE5My0wLjk5NCA0Ljg4Ny0wIDAgMC4wNzUtMC4wNjcgMC4wOTctMC4xMjZ6TTE4LjUwMyAzLjMzN2MwIDAgMi42NzEgMi42NzItMi41MzQgNi43ODEtNC4xNzQgMy4yOTYtMC45NTIgNS4xNzYtMC4wMDIgNy4zMjMtMi40MzYtMi4xOTgtNC4yMjQtNC4xMzMtMy4wMjUtNS45MzQgMS43NjEtMi42NDQgNi42MzgtMy45MjUgNS41Ni04LjE3ek0xMy41MDMgMjguOTY2YzQuNjMgMC4yOTYgMTEuNzQtMC4xNjQgMTEuOTA4LTIuMzU1IDAgMC0wLjMyNCAwLjgzMS0zLjgyNiAxLjQ5LTMuOTUyIDAuNzQ0LTguODI2IDAuNjU3LTExLjcxNiAwLjE4IDAgMCAwLjU5MiAwLjQ5IDMuNjM1IDAuNjg1eiIvPiA8L2c+Cg08L3N2Zz4="/>

### IDE
<img alt="Static Badge" src="https://img.shields.io/badge/intellijidea-black?style=flat&logo=intellijidea&logoColor=white"/>

### BackEnd
<img alt="Static Badge" src="https://img.shields.io/badge/Spring-6DB33F?style=flat&logo=Spring&logoColor=white"/>
<img alt="Static Badge" src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=SpringBoot&logoColor=white"/>

> Spring Data JPA, QueryDSL, Spring WebClient, Spring Batch

### Security
<img alt="Static Badge" src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=SpringSecurity&logoColor=white"/>
<img alt="Static Badge" src="https://img.shields.io/badge/JWT-6DB33F?style=flat&logo=jsonwebtokens&logoColor=white"/>

### Batch
> Spring Batch


### DataBase 
<img alt="Static Badge" src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white"/>
<img alt="Static Badge" src="https://img.shields.io/badge/Redis-FF4438?style=flat&logo=Redis&logoColor=white">

### AI
> OpenAIEmbeddings


### Monitoring
> Spring Actuator, Prometheus, Grafana, AWS CloudWatch

### OpenAPI
> toss Payments
> kakao OAuth 2.0

###  Performance Monitoring
> K6
> Grafana
> nGrinder

### CI/CD
<img alt="Static Badge" src="https://img.shields.io/badge/githubactions-2088FF?style=flat&logo=GitHub%20Actions&logoColor=white">
<img alt="Static Badge" src="https://img.shields.io/badge/docker-2496ED?style=flat&logo=Docker&logoColor=white">

### Log Visualization and Analysis
> ElasticSearch
> Logstash
> Kibana

### Infra
> AWS RDS
> AWS ECR
> AWS EC2
> AWS ALB
> AWS Internet Gateway
> AWS IAM
> AWS S3

### Collaboration
> GitHub
> Slack
> Notion
> ERD Colud
> Figma
> Jira

### Test
> Swagger
> Postman


   </div>
</details>

---

## 📄 설계 문서

> [API 명세서](https://www.notion.so/teamsparta/2e62dc3ef5148018b07cea1a267abb7f?v=2e62dc3ef51480798617000c18d770a8&source=copy_link)

> [와이어프레임](https://www.figma.com/proto/QdWQmOFKB8TlomTfi8sJQ2/Starve-Stop_AllForOne?node-id=0-1&t=u7U0nbZuFtvCGO79-1)     

> <details>
>   <summary>ERD</summary>
>   <div markdown="1">
>       <img width="1635" height="1014" alt="image" src="https://github.com/user-attachments/assets/25460d96-c774-423c-8e52-117db97c88dd" />

>   </div>
> </details>

> <details>
> <summary>시스템 아키텍처</summary>
> <div markdown="1">
> <img width="1562" height="1091" alt="image" src="https://github.com/user-attachments/assets/df09f513-4c49-4070-988c-b8694b78a447" />

> </div>
> </details>






---

## ✨ 기술적 의사 결정

<details>
   <summary>결제 도메인 이벤트 적용 (결합도 완화 및 안정성 확보)</summary>
   <div markdown="1">

### 배경
- 기존 결제 유스케이스는 아래의 세 가지 로직이 하나의 트랜잭션으로 강하게 결합
  - 결제처리
  - 결제 상태 전이 로깅
  - 영수증 발급
  
결제는 매출과 직결되는 가장 핵심적인 비즈니스 로직입니다. 하지만 위의 구조는 영수증 발급이나 로깅 같은 부가 로직에서 예외가 발생할 경우, 정상적으로 처리된 결제까지 모두 롤백되는 구조적 문제가 존재했고 
  비즈니스적으로 치명적인 '결제 성공률 저하'를 막기 위해, 핵심 로직이 부가 기능에 종속되지 않도록 책임을 분리하고 트랜잭션 경계를 재정의해야 했습니다.

### 고민
결제 흐름에서 부가 로직을 분리하기 위해 다음과 같은 접근 방식을 고려했습니다.

| 대안         | 장점                      | 단점                             |
|------------|-------------------------|--------------------------------|
| 서비스 내 직접 호출 | 하나의 메서드에서 전체 흐름 파악이 용이함 | 단일 트랜잭션 유지로 롤백 위험, 유스케이스 책임 증가 |
| AOP 도입     | 공통 관심사 분리 가능 | 도메인 흐름 표현 한계, 전이에 따른 조건적 실행에 부적합|

### 결정
도메인 이벤트(Domain Event) 기반 설계를 도입하기로 결정

- **선택 이유**
  
  결제라는 핵심 비즈니스 로직은 무조건 우선 완료되어야 합니다. 도메인 이벤트를 활용하면 로직 간 결합도를 낮추고 트랜잭션 커밋 시점을 제어하여, 부가 로직의 실패가 핵심 로직에 영향을 주지 않도록 완벽히 격리할 수 있습니다.

**구현 방향 및 적용 범위**
- 결제 유스케이스는 결제 처리에만 집중
- 결제 상태 변경 시 도메인 이벤트 발행
- 메인 결제 트랜잭션이 성공적으로 커밋된 이후 이벤트 리스너가 동작하도록 구성
- 영수증 발급과 결제 상태 전이 로깅은 리스너 측으로 책임을 위임

   </div>
</details>

<details>
   <summary>위치 기반 거리 계산 정렬 병목 해소 (Redis GEO 도입)</summary>
   <div markdown="1">
 
### 배경
- 100만 매장, 500만 상품 환경에서 '내 주변 매장 조회(거리순 정렬)' 기능의 응답 시간이 평균 20~40초까지 지연되는 심각한 성능 저하가 발생

원인을 분석한 결과, DB가 매 요청마다 모든 매장에 대해 공간 함수(ST_Distance_Sphere)로 거리 계산을 수행한 뒤 이를 다시 정렬하는 구조적 한계에 직면해 있었습니다. 데이터와 트래픽이 증가할수록 DB가 감당해야 할 연산량이 기하급수적으로 늘어나는 치명적인 리스크였습니다.

### 고민
DB의 연산 부하를 줄이고 응답 시간을 1초 이하로 단축하기 위해 다음 세 가지 방안을 고려했습니다.


| 대안                      | 장점            | 단점                                            |
|-------------------------|---------------|-----------------------------------------------|
| DB 쿼리 튜닝 (Bounding Box) | 기존 아키텍처 유지 가능 | 후보군은 줄어들지만, 여전히 DB에서 연산과 정렬이 발생하여 근본적 해결 불가   |
| 위치별 결과 캐싱               | DB 접근 최소화     | 사용자의 좌표 조합과 검색 조건(키워드/카테고리)이 방대하여 캐시 적중률이 극히 낮음 |
| Redis GEO 도입 | 메모리 기반으로 고속 반경 탐색 및 정렬 가능 | Redis 데이터 동기화 관리 필요, 장애 시 기능 영향               |

### 결정

Redis GEO 도입 및 인프라 간 역할 분리

- **선택 이유**
  
  거리 연산과 정렬을 DB가 처리하는 구조 자체가 문제의 핵심으로 연산에 최적화된 메모리 DB인 Redis GEO에 반경 탐색과 정렬을 위임하고, RDBMS는 데이터의 상세 조회 및 필터링에만 집중하도록 시스템의 역할을 완전히 분리하는 것이 가장 확실한 해결책이라고 판단

**구현 방향 및 적용**

범위기존에 DB가 모두 처리하던 작업을 아래와 같이 나누어 파이프라인을 재설계했습니다.
- Redis의 역할: 사용자의 현재 좌표를 기준으로 GEO 기능을 활용해 '반경 탐색, 거리 계산, 오름차순 정렬'을 수행하여 정렬된 매장 ID 리스트를 반환
- DB의 역할: Redis로부터 전달받은 ID 리스트를 IN 절로 조회하며, 키워드 검색 및 카테고리 필터링 등 상세 데이터 조회만 수행
       
  </div>
</details>

<details>
   <summary>Blue-Green 기반 무중단 배포 도입</summary>
   <div markdown="1">

### 배경
- 기존 Github Actions 기반 배포는 EC2 서버에 접속해 기존 컨테이너를 직접 종료(docker stop)하고 새 컨테이너를 실행(docker run)하는 방식
- 컨테이너가 교체되는 동안 서버가 잠시 멈춰, 해당 시간 동안 유입되는 사용자 요청이 실패하는 현상이 발생
- 새 버전의 정상 동작 여부를 사전에 검증하지 않고 실서비스에 투입했으며, 장애가 발생하면 직접 수동으로 이전 버전을 재실행해야 해 대응 속도가 떨어짐

### 고민
배포 중 사용자 요청 실패를 방지하고, 문제가 생겼을 때 즉각적인 롤백이 가능하도록 다음 세 가지 배포 전략을 비교했습니다.

| 배포 전략 | 전환 기준 | 버전 혼재 여부 | 롤백 속도             | 장점                            | 단점           |
| ------- | -------- |:--------:|-------------------|-------------------------------|--------------|
| Rolling Update | 서버 단위 (순차적) |    O     | 느림 (물리적 서버 교체 필요) | 추가 서버 불필요 |  DB 호환성 문제 발생 가능              |
| Canary | 트래픽 비율 |    O     | 빠름                | 실제 트래픽 기반 검증 | 비율 제어 로직 복잡  | 
| Blue-Green | 환경 전체 |    X     | 즉시 (ALB 스위칭)      | 버전 격리 및 사전 검증 가능 |  서버 자원 추가 필요 |

### 결정
Blue-Green 배포 전략 채택

- 선택 이유

  Rolling과 Canary는 운영 환경에 구버전과 신버전이 혼재되는 반면, Blue-Green 배포는 환경 전체를 분리하여 교체하므로 버전이 섞이지 않습니다. 무엇보다 AWS ALB의 Target Group 스위칭 한 번으로 수 초 내에 즉시 전체 롤백이 가능하여 현재 환경에서 시스템 안정성을 가장 높일 수 있다고 판단했습니다.

**구현 방향 및 적용 범위**

- dev 브랜치 merge
- Docker 이미지 빌드 후 ECR push
- ALB가 현재 바라보는 Target Group 확인
- 사용 중이 아닌 Target Group에 새 버전 배포
- `/actuator/health`로 정상 여부 확인
- 정상일 경우 ALB Listener를 새 Target Group으로 변경
- 실패 시 기존 Target Group 유지 (자동 롤백)

  </div>
</details>

<details>
   <summary>사용자 관점의 결제 API 응답 UX 개선</summary>
   <div markdown="1">

### 배경
- 기존 결제 API는 PG사 결제 실패 시 사유와 무관하게 일괄적으로 HTTP 에러를 반환.
- 프론트엔드는 HTTP 에러 여부만으로 결과를 판단했기에, '잔액 부족'이나 '한도 초과' 같은 정상적인 비즈니스 거절 상황도 단순 시스템 장애처럼 노출

시스템 장애와 비즈니스 실패를 명확히 구분하여, 프론트엔드에서 결제 상태를 명시적으로 처리하고 사용자에게 직관적인 UX를 제공해야 했습니다.

### 고민

결제 실패 응답을 어떻게 처리할지 아래 두 가지 극단적인 대안을 두고 고민했습니다.

| 대안                 | 장점                   | 단점                                             |
|--------------------|----------------------|------------------------------------------------|
| 모든 실패를 HTTP 에러 반환  | 서버 로직 일관성 유지 및 구현 단순 | 정상적인 비즈니스 실패와 시스템 오류 구분 불가, 세밀한 UX 처리 불가능      |
| 모든 결과를 HTTP 200 반환 | 프론트엔드 응답 처리 구조 단순화   | 실제 서버 장애나 네트워크 오류까지 정상 응답으로 처리되어 대응이 어려워질 수 있음 |

### 결정
HTTP 레벨 에러와 비즈니스 레벨 실패의 명확한 분리

- 선택 이유

  잔액 부족이나 한도 초과는 비즈니스 규칙에 의한 '정상적인 실패 결과'로 HTTP 에러 코드는 시스템 장애나 요청 실패 시 사용하고,
  비즈니스 로직의 성공 여부는 응답 데이터로 분리하는 것이 사용자 신뢰와 UX 개선 모두를 만족하는 가장 합리적인 설계라고 판단했습니다.

**구현 방향 및 적용 범위**

- 결제 요청은 정상적으로 처리
- 결제 결과가 비즈니스 실패인 경우
  - HTTP 200 응답
  - 명시적인 결제 상태값과 실패 사유 메시지 포함
- 시스템 오류 발생 시
  - HTTP 에러 응답 유지
- 프론트엔드는 HTTP 에러 여부가 아닌 결제 상태값 기준으로 화면 처리
   </div>
</details>

<details>
   <summary>알림 기능 확장을 위한 FCM 도입</summary>
   <div markdown="1">

### 배경


구독 상품으로 다루고 있는 것이 식품인 만큼 수령 시간이 늦어질수록 서비스 만족도가 떨어질 수 있어 상품 수령 시간에 대한 알림의 필요성을 느껴졌고,
또 판매자 측에서도 소비자의 결제 건을 수시로 확인 해줘야 하는 불편함이 존재해 결제 건에 대한 알림의 필요성도 느껴졌습니다.

그러나 프로젝트는 다음과 같은 제약 조건이 존재했다.

- 모바일 앱 개발을 진행할 시간이 부족
- 프론트엔드 전담 인력이 없음 (AI 기반으로 기능 구현 중)
- 포트폴리오 목적상 백엔드 설계 및 인프라 의사결정이 핵심
- MySQL 기반으로 사용자 토큰을 직접 관리하는 구조

즉, 웹 환경에서 빠르게 안정적인 알림 기능을 구현해야 하는 상황이었다.

## 2. 고민

웹에서 푸시 알림을 구현하기 위해 다음 두 가지 대안을 고려했다.


| 대안              | 장점                                  | 단점 |
|-----------------|-------------------------------------|---|
| 자체 웹 푸시 프로토콜 구현 | Firebase 등 외부 플랫폼 의존성이 없고, 직접 통제 가능 | 브라우저별 대응, 키 관리 등 프론트 작업량 증가 |
|(FCM) 기반 웹 푸시| 공통 인프라 사용 가능, 실패 토큰 식별 가능, 확장 시 구조 변경이 적음 | 외부 서비스 의존성 발생, Firebase 콘솔 기반 관리 필요 |

**의사 결정 기준**

- **확장성** (웹 → 모바일 확장 가능성)
- **운영 안정성** (토큰 만료/실패 처리 전략)
- **개발 리소스 현실성**


### 3. 결정

- FCM 기반 웹 푸시 구조를 선택

선택 이유:

1. 모바일 확장 시 기존 인프라 재사용 가능
2. 토큰 실패 응답 기반 정리 로직 구현 가능
3. MulticastMessage 기반 대량 발송 구조 설계 가능
4. 프론트 의존도를 최소화하면서 서버 중심 설계 가능

---

## 구현 방향 및 적용 범위

- 사용자 n개의 디바이스 대응 구조
- 스케줄러(7시/12시/18시) 기반 시간대 비트마스크 조회
- 결제 완료된 시점에서 판매자와 소비자 측에 결제 알림 발송.
- 실패 토큰 REQUIRES_NEW 트랜잭션으로 정리
- 발송과 토큰 정리 트랜잭션 분리

### 적용 범위

- 웹 기반 푸시 알림
- 향후 안드로이드 앱 도입 시 동일 FCM 서버 구조 활용

   </div>
</details>

---

## 🧨 트러블 슈팅 및 성능 개선

<details>
   <summary>QueryDSL 비트 연산 미지원 이슈</summary>
   <div markdown="1">

### 문제 상황
- 소비자에게 주기적으로 보내주는 구독 상품 수령을 위한 알림이 동작하지 않음.

### 문제 식별

**관찰한 문제점**

- 스케줄링 자체는 정상작동 하지만 발신되어야하는 알림이 발신되지 않음.
- 기존 비트 마스크로 설계된 필드의 조건 검사를 위한 `&` 연산이 동작하지 않음.

**핵심 문제 원인**

- JPQL에서 비트연산자를 지원하지 않는 것.

**문제 범위 및 영향 영역**

- 비트연산이 들어가야하는 모든 쿼리문


### 해결 방안
- 각 비트가 의미했던 것들을 column으로 나누기
- NativeQuery 이용
- Dialect를 이용한 커스텀 메서드 활용

### 최종 선택한 해결책과 선택 이유

- NativeQuery 이용

**선택 이유**

- 엔티티를 건들게 될 경우 나머지 로직들도 모두 수정이 들어가야 하기 때문에 교체비용이 발생
- 제한적인 nativequery 활용으로 querydsl의 장점인 유지보수가 용이하다는 점이 상쇄 가능해보임.

### 구현 방법 및 적용 범위

- queryDSL에서 NativeQuery로 교체

   </div>
</details>

<details>
   <summary>위치 기반 조회 쿼리 성능 튜닝 (Bounding Box 적용)</summary>
   <div markdown="1">

### 문제 상황

**문제가 발생한 기능**

사용자의 현재 위치를 기준으로 반경 5km 이내 매장을 거리 오름차순으로 정렬하여 조회하는 API

조회 조건

- 반경 5km
- 매장 카테고리 필터
- 키워드 검색 (매장명 / 상품명)
- 커서 기반 페이징
- 정렬 기준: 거리 오름차순

데이터 규모

- 매장 100만 건, 상품 500만 건

**사용자 영향**

- API 응답 시간 20초 ~ 최대 1분
- 실제 서비스에서 사용 불가능한 수준

### 2. 문제 식별
**관찰한 문제점**

**1. 거리 정렬이 붙는 순간 급격한 성능 저하**

- 위 쿼리에서 거리 정렬(`st_distance_sphere`)이 포함되면 응답 시간이 급격히 증가

**2. EXPLAIN 결과**

- 약 91만 건에 대해 거리 계산 후 정렬 수행
- LIMIT 10이지만 정렬은 전체 매장에 대해 수행됨
- 공간 인덱스 사용 흔적 없음

**3. 조건이 추가될수록 느려짐**

- 처음에는 키워드나 카테고리 조건이 추가하면
연산을 하게 되어서 느려진다고 생각했으나 실제로는 반대였다

- 이유는 거리 정렬이 병목이었고
거리 계산은 필터 이후 row에 대해서 만 수행되기 때문

| 조건 | 거리 계산 대상 row 수 |
| --- | --- |
| 조건 없음 | 거의 전체 매장 |
| 카테고리 있음 | 일부 매장 |
| 카테고리와 키워드 있음 | 더 적은 매장 |

> 조건이 추가되면 거리 계산 대상 매장 수가 줄어들어 오히려 빨라질 수 있음
>

## 핵심 문제 원인

> 거리 계산 함수 자체가 느린 것이 아님
거리 계산을 모든 매장에 수행한 것이 문제
>

```sql
  [100만 매장]
       ↓
모든 매장 거리 계산
       ↓
      정렬
       ↓
     페이징
```

## 문제 범위 및 영향 영역

- 위치 기반 반경 조회 API
- 거리 정렬이 포함된 모든 기능
- 데이터 증가에 따라 악화

### 3. 해결 방안

## 목표

- 정확한 거리 계산을 수행하기 전에 계산 대상 매장 수를 줄인다
- DB가 모든 매장에 대해 거리 계산하지 않도록 한다

## 고려했던 해결 방안

| 방법 | 장점 | 단점 |
| --- | --- | --- |
| 거리 조건만 사용 | 구현 단순 | 인덱스 미사용 |
| 공간 인덱스 + 거리 계산 | 일부 개선 | 계산 대상 row 많음 |
| BoundingBox + 거리 계산 | 계산 대상 대폭 감소 | 사각형 오차 존재 |
| 상관 서브 쿼리 사용 → JOIN 변경 | 반복 비용 제거 | 중복 제거 필요 |

## 최종 선택한 해결책과 그 이유

### 1. Bounding Box + 거리 계산

선택 이유

> 문제의 핵심은 거리 계산 함수가 아닌 거리 계산 대상 매장 수
- Bounding Box를 활용하면

  - 공간 인덱스 사용 가능
  - 사각형 범위로 후보 매장 대폭 감소
  - 그 이후에만 정확한 거리 계산 수행

Bounding Box를 활용하면

- 공간 인덱스 사용 가능
- 사각형 범위로 후보 매장 대폭 감소
- 그 이후에만 정확한 거리 계산 수행

## 구현 방법 및 적용 범위

```sql
	전체 매장 (100만)
	      ↓
Bounding Box 필터
	      ↓
		거리 계산 수행
	      ↓
	     정렬
	      ↓
			페이징
```

### 4. 해결 결과

## 문제 해결 여부 및 재발 가능성

- 현재 트래픽 기준에서는 조건 있을 때 7s이하로 줄임
- 데이터 증가 시 Bounding Box 후보 수 증가
- 거리 계산 함수는 후보 매장 수가 증가하면 비례해서 계산

   </div>
</details>

<details>
   <summary>텍스트 검색 최적화 (Redis 필터링 + Like vs Fulltext Index)</summary>
   <div markdown="1">

### 1. 문제 상황

**문제가 발생한 기능**

- Redis GEO로 5km 반경 내 매장 id List 추출
- 해당 id List를 기준으로 DB에서 상세 조회
- 매장명/상품명 키워드 검색 포함

**데이터 규모**

- 상품 500만, 매장 100만

**사용자 또는 시스템 영향**

- 키워드 조회 시 응답 속도가 불안정
- fulltext index를 적용한 이후 응답 시간이 증가

**문제를 방치했을 때 리스크**

- 검색이 느림 → 서비스 품질 저하
- 데이터 증가 시 성능 저하가 더 심해짐

**해결이 필요했던 배경**

- like ‘%keyword%’는 full scan이 발생
- fulltext index가 더 빠를 것이라 기대
- 하지만 테스트 시 기대와 전혀 다른 결과 발생
- `LIKE '%keyword%'` → 500ms 이하
- `FULLTEXT MATCH AGAINST` → 최대 10초

### 문제 식별

**관찰한 증상과 데이터**

- like 검색: postman 기준 약 500ms 이하
- fulltext index 검색: 동일 조건 검색 시 10s
- 인덱스를 사용했지만 왜 더 느린지 의문 발생

**핵심 원인 정리**

- 옵티마이저가 fulltext index를 타지 않음
- fulltext explain

### 3. 해결 방안

| 대안                | 장점                                | 단점                       |
|-------------------|-----------------------------------|--------------------------|
| like 적용           | Redis GEO로 후보가 충분히 줄어든 상황에서 매우 빠름 | 검색 범위가 넓어지면 full scan 위험 |
| fulltext index 적용 | 대규모 텍스트 검색에 적합                    | 현재 구조인 in (:ids)는 비효율    |

### 최종 선택: fulltext index를 적용하지 않고 like 적용

- 이미 Redis GEO로 row가 많이 적은 상태
- fulltext index의 장점이 사라지고 오히려 역효과

### 구현 방향 및 적용 범위

- 기존:
  - 거리 기반 List → DB 상세 조회(fulltext)

    ```
    Redis → DB 상세 조회(FULLTEXT)
    ```

- 변경:
  - 거리 기반 List → DB 상세 조회(like)

    ```
    Redis → DB 상세 조회(LIKE)
    ```

   </div>
</details>

<details>
   <summary>WebSocket 채팅 기능 이슈 해결</summary>
   <div markdown="1">

### WebSocket 중복 연결 문제

**문제 상황**

사용자가 ‘연결’ 버튼을 연속 클릭할 경우 WebSocket 연결이 중복 생성되어

동일한 메시지가 2~3회 중복 수신 되는 현상이 발생함.

**원인**

클라이언트에서 기존 연결 상태를 확인하지 않고 새로운 `SockJS` 인스턴스를 생성하여 다중 세션이 유지됨.

**해결**

- `connect()` 진입 시 기존 연결 여부를 검증하여 중복 실행 차단
- 연결 성공 시 버튼을 비활성화하여 UX 차원의 재발 방지

**개선 결과**

- 단일 세션 만 유지되도록 보장
- 메시지 중복 수신 문제 완전 제거


### 새로고침 시 상태 소실 문제

**문제 상황**

채팅방에서 새로고침(F5) 시 WebSocket 연결이 종료되고,

기존 메시지와 인증 정보가 초기화되어 재로그인이 필요했음.

**원인**

- WebSocket은 실시간 전송 프로토콜로, 데이터 저장 기능이 없음
- 브라우저 새로고침 시 메모리(DOM, JS 변수) 초기화

**해결**

1. **인증 유지**
  - 로그인 성공 시 JWT를 `localStorage`에 저장
  - 페이지 로드 시 토큰을 복원하여 자동 재 연결 구현
2. **이전 메시지 복원**
  - 채팅방 입장 시 REST API 호출
  - DB에 저장된 메시지를 조회 후 화면 렌더링

**개선 결과**

- 새로고침 후 자동 재연결
- 과거 대화 내역 유지
- 사용자 경험 개선


###CORS 정책 위반 문제

**문제 상황**

로컬 프론트엔드(`localhost:63342`)에서 백엔드(`localhost:8080`)로 요청 시

`Access-Control-Allow-Origin` 에러 발생

**원인**

서로 다른 Origin 간 자원 공유가 브라우저 보안 정책에 의해 차단됨.

**해결**

- `SecurityConfig`에 `CorsConfigurationSource` 등록
- 필요한 컨트롤러에 `@CrossOrigin` 설정 추가

**개선 결과**

- 프론트/백엔드 분리 환경에서 정상 통신 가능
- WebSocket 및 REST API 안정화


### WebSocket 환경에서 Custom AuthenticationPrincipal 바인딩 실패

**문제 상황**

`@MessageMapping` 메서드에서

`@AuthenticationPrincipal AuthUser authUser` 사용 시

`authUser`가 null로 주입 됨.

REST API에서는 정상 동작했으나 WebSocket 환경에서만 실패.


**원인 분석 과정**

1. **Principal 존재 여부 검증**
  - `Principal` 타입으로 변경 후 로그 확인
  - → 객체는 정상 전달됨
2. **타입 검증**
  - `Authentication`으로 캐스팅 후 `getPrincipal()` 호출
  - → 실제 객체 타입은 `AuthUser`가 맞음을 확인

**원인 결론**

Spring MVC와 Spring WebSocket은 서로 다른 설정 컨텍스트를 사용함.

WebSocket 요청 처리 과정에서

`HandlerMethodArgumentResolver`가 등록되지 않아

`@AuthenticationPrincipal`을 인식하지 못함.


**해결 전략**

정공법(Resolver 등록) 대신

현재 단계에서는 안정성과 단순성을 우선하여 우회 방식을 선택.

- `Principal`을 직접 주입받은 뒤
- 내부에서 `Authentication`으로 캐스팅하여 사용자 정보 추출

```java
// 변경 전
publicvoidsendMessage(@AuthenticationPrincipal AuthUser authUser, ...)
// 변경 후
publicvoidsendMessage(Principal principal, ...) {
UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
AuthUserauthUser= (AuthUser) token.getPrincipal();
}
```

**개선 결과**

- 설정 복잡도 증가 없이 문제 해결
- SecurityContext 기반 인증 정보 정상 활용
- WebSocket 메시지 처리 안정화
   </div>
</details>

<details>
   <summary>상품 재고 관련 동시성 문제</summary>
   <div markdown="1">

### 1. 문제 상황


**문제가 발생한 기능**

- 동시에 여러 사용자가 동일 상품을 구매하는 상황에서 동시성 제어가 적용되지 않아 상품 수량보다 많은 수량의 주문이 생기는 문제가 발생하였다.

**문제가 발생할 수 있는 시나리오**

- 동시 요청된 총 주문 수량이 상품 재고량을 넘어설 경우


**문제를 방치할 경우 리스크**

- 주문 데이터 신뢰도 하락
- 정산 및 재고 관리 오류
- 시스템 신뢰성 저하

### 2. 문제 식별

문제 상황 재현을 위한 테스트를 진행하였다.

**테스트 설계**

다음 조건으로 동시성 테스트를 설계하였다.

| 항목 | 값 |
| --- | --- |
| 초기 재고 | 10 |
| 동시 요청 수 | 100 |
| 요청 당 차감 수량 | 2 |
| 기대 성공 횟수 | 5 |
| 기대 실패 횟수 | 95 |

CountDownLatch를 활용해 100개의 쓰레드가 동시에 접근하도록 구성하였고,
테스트를 위해 기존 동시성 문제가 발생하던 코드에 트랜잭션을 적용 시킨 후 낙관적 락, 비관적  락, 분산 락 등을 도입하여 결과 값과 장단점을 비교하였다.

**실제 결과**

- 성공 : 약 50
- 실패 : 약 50
- 최종 재고 : 매번 다름

**핵심 원인**

동일한 재고 값을 조회한 여러 트랜잭션이

각각 수정 후 커밋하면서 Lost Update가 발생했다.


### 문제의 본질

조회 후 수정 방식 자체가 동시성 환경에 취약하다는 점

애플리케이션 레벨에서 값을 읽고 검증하는 순간,

이미 동시성 문제가 발생한다.


### 해결 방안

동시성 제어 방식 4가지를 비교 실험하였다.


| 대안                       | 장점                              | 단점                                |
|--------------------------|---------------------------------|-----------------------------------|
| 조건부 UPDATE (조회+수정 동시 처리) | 가장 빠름, X-Lock 자동 적용, DB 레벨에서 처리 |                                   |
| 낙관적 락                    | 충돌 감지 방식으로충돌이 적으면 효율적           | 충돌 발생 시 예외 및 재시도 필요, 실패가 잦을 시 성능 저하 발생 |
| 비관적 락                    | 충돌이 많이 발생할 수 있는 상황에서 성능 방어      | DB 대기 발생, 조회 순서가 꼬일 경우 데드락 가능성 있음 |
| 분산 락 | 다중 서버 환경 대응, DB 외부에서 직렬화 보장     | Redis 네트워크 비용, 가장 느린 처리 속도|


**비교 결과**

| 방식 | 성공 여부 | 평균 시간 |
| --- | --- | --- |
| 기존 방식 | 실패 | ~450ms |
| 조건부 UPDATE | 성공 | ~350ms |
| 낙관적 락 | 성공 | ~600ms |
| 비관적 락 | 성공 | ~500ms |
| 분산 락 | 성공 | ~1100ms |


### 최종 선택

**조건부 UPDATE 방식 채택**

선택 이유:

- 가장 빠른 실행 속도
- 가장 단순한 구조
- 별도의 동시성 제어 로직 불필요

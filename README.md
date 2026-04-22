# 🚀 Alien LMS

<p align="center">
  <img src="docs/images/00-project-logo.png" alt="Alien LMS Logo" width="220" />
</p>

<p align="center">
  우주 세계관 기반 학습 여정 플랫폼<br/>
  <b>대륙 탐색 → 강좌 수강 → 학습 진도 → 커뮤니티 참여 → 관리자 운영 인사이트</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?logo=openjdk" alt="Java" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Spring%20Security-Authentication-6DB33F?logo=springsecurity" alt="Spring Security" />
  <img src="https://img.shields.io/badge/JPA-Hibernate-blue" alt="JPA" />
  <img src="https://img.shields.io/badge/Thymeleaf-View-005F0F?logo=thymeleaf" alt="Thymeleaf" />
  <img src="https://img.shields.io/badge/MySQL-Database-4479A1?logo=mysql" alt="MySQL" />
  <img src="https://img.shields.io/badge/GCS-Cloud%20Storage-4285F4?logo=googlecloud" alt="GCS" />
</p>

---

## 1. 프로젝트 소개

**Alien LMS**는 기존의 단순 강의 목록형 LMS가 아니라,  
학생이 **대륙을 탐색하고 침공하며 성장하는 스토리형 학습 플랫폼**입니다.

기존 LMS의 문제는 강의 중심 구조로 인해 학습 지속성이 낮고, 사용자 간 상호작용이 부족하다는 점이었습니다.  
이를 해결하기 위해 본 프로젝트는 **세계관 기반 UX**, **대륙별 커뮤니티**, **강좌 결제 및 수강 흐름**, **학습 진도 관리**, **관리자 운영 인사이트**를 하나의 서비스 흐름으로 연결했습니다.

---

## 2. 대표 화면

> 아래 이미지는 README에서 가장 먼저 보이는 구간입니다.  
> **메인 화면 / 대륙 목록 / 강좌 상세**처럼 서비스의 분위기가 가장 잘 드러나는 화면을 배치하는 것을 추천합니다.

<p align="center">
  <img src="docs/images/01-main-home.png" alt="메인 홈 화면" width="80%" />
</p>

<p align="center">
  <img src="docs/images/02-continents.png" alt="대륙 목록 화면" width="31%" />
  <img src="docs/images/03-course-detail.png" alt="강좌 상세 화면" width="31%" />
  <img src="docs/images/04-learning-view.png" alt="강의 시청 화면" width="31%" />
</p>

**배치 의도**
- **첫 번째 이미지**: 서비스 정체성을 보여주는 메인 화면
- **두 번째 줄 3장**: 학생 여정의 핵심 화면을 한 번에 보여주는 구간
- 발표 자료가 아니라 GitHub README이므로, 너무 많은 이미지를 초반에 몰기보다 **대표 4장** 정도만 먼저 배치하는 것이 좋습니다.

---

## 3. 기획 배경

### 문제 정의
- 강의 중심 구조만으로는 학습 지속성이 낮고 재방문 동기가 약함
- 온라인 학습 환경에서는 공동체 경험이 부족해 중도 이탈이 발생하기 쉬움
- 관리자 입장에서도 운영 우선순위를 판단할 수 있는 통합 인사이트가 부족함

### 해결 방향
- 학생을 단순 수강생이 아닌 **Student Alien**으로 정의
- 카테고리를 일반 분류가 아닌 **대륙(Continent)** 개념으로 설계
- **대륙 탐색 → 강좌 선택 → 결제 → 수강 → 진도 확인 → 커뮤니티 활동**의 흐름을 하나로 연결
- 관리자에게는 **대륙 활성도와 강좌 성과를 기반으로 한 운영 판단 근거** 제공

---

## 4. 핵심 컨셉

### 🌌 세계관 기반 LMS
- 학생: **Student Alien**
- 강사: **Instructor Alien**
- 카테고리: **Continent**
- 학습 흐름: **탐색 → 침공 → 수강 → 성장**

### 🎯 지속 가능한 학습 경험 설계
- 단순 강의 나열이 아닌 **탐험형 UX**
- 대륙별 게시판과 댓글을 통한 **관심사 공동체 형성**
- 등급 시스템을 통한 **성취감과 재방문 유도**

---

## 5. 주요 사용자 흐름

### 학생(Student Alien)
1. 전체 대륙 조회 및 탐색
2. 관심 대륙 진입 후 강좌 목록 확인
3. 강좌 상세 조회 및 결제
4. 수강 등록 후 강의 시청
5. 진도 저장 및 이어보기
6. Q&A / 게시판 활동
7. 마이페이지에서 수강 현황, 결제 내역, 환불 상태 확인

### 강사(Instructor Alien)
1. 관리 중인 강좌 및 대륙 확인
2. 강좌/강의 등록 및 수정
3. 강좌 공지 작성
4. 수강생 학습 현황 및 Q&A 확인

### 관리자(Admin)
1. 회원 및 강사 계정 관리
2. 신고 콘텐츠 검토 및 중재
3. 환불 승인/거절 처리
4. 대륙별 통계와 인기 강좌 분석
5. 운영 우선순위 및 강사 채용 판단 지원

---

## 6. 주요 기능

### 학생 기능
- 회원가입 / 로그인 / 로그아웃
- 대륙 목록 조회 / 검색 / 상세 조회
- 강좌 상세 조회 및 결제
- 강의 영상 시청, 진도율 확인, 이어보기
- Q&A 작성 / 수정 / 삭제
- 대륙 게시판 및 댓글 작성
- 결제 내역 조회 및 환불 요청
- 마이페이지 조회

### 강사 기능
- 강사 프로필 관리
- 강좌 등록 / 수정 / 삭제
- 강의 영상 업로드
- 강좌 공지사항 작성
- 수강생 목록 및 학습 현황 조회
- 강의 Q&A 답변

### 관리자 기능
- 학생 / 강사 계정 조회 및 상태 변경
- 신고 게시글 / 댓글 / Q&A 관리
- 환불 요청 관리
- 대륙별 활성도 통계
- 인기 강좌 및 운영 인사이트 제공

---

## 7. 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Backend | Spring Boot, Spring Web MVC |
| Security | Spring Security, Session |
| ORM | Spring Data JPA, Hibernate |
| View | Thymeleaf |
| Database | MySQL |
| Storage | Google Cloud Storage |
| Build Tool | Gradle |
| Collaboration | GitHub, Notion, Slack |

---

## 8. 시스템 구조

> 이 섹션에는 **아키텍처 이미지 1장 + 학생 메인 흐름 이미지 1장** 정도를 넣는 것이 가장 깔끔합니다.

### 전체 요청 흐름
`Client → Controller → Service → Repository → DB → View`

### 학습 흐름
`로그인 → 강좌 조회 → 결제/수강 → 강의 시청 → 진도 저장 → Q&A`

<p align="center">
  <img src="docs/images/10-system-architecture.png" alt="시스템 구조도" width="85%" />
</p>

<p align="center">
  <img src="docs/images/11-student-flow.png" alt="학생 학습 흐름도" width="85%" />
</p>

---

## 9. ERD

> ERD는 README 중반부에 배치하는 것이 좋습니다.  
> 초반에 두면 가독성이 떨어지고, 너무 뒤에 두면 구조 설명과 연결이 끊깁니다.

<p align="center">
  <img src="docs/images/12-erd.png" alt="Alien LMS ERD" width="85%" />
</p>

### 핵심 도메인
- MEMBER
- CONTINENT
- COURSE
- LECTURE
- COURSE_NOTICE
- ENROLLMENT
- LECTURE_PROGRESS
- QNA
- CONTINENT_POST
- CONTINENT_COMMENT
- REPORT
- CART
- PAYMENT
- PAYMENT_ITEM
- REFUND

---

## 10. 핵심 기능 화면

### 10-1. 학생 학습 흐름
<p align="center">
  <img src="docs/images/20-course-detail.png" alt="강좌 상세" width="31%" />
  <img src="docs/images/21-payment.png" alt="결제 화면" width="31%" />
  <img src="docs/images/22-learning-progress.png" alt="진도 저장 화면" width="31%" />
</p>

**추천 설명 문구**
- 강좌 상세에서 가격과 학습 정보를 확인한 뒤 결제를 진행합니다.
- 결제 완료 후 수강 상태가 반영되고, 강의 시청 및 진도 저장이 가능합니다.
- 다음 접속 시 이어보기를 통해 학습 흐름이 유지됩니다.

### 10-2. 커뮤니티 / Q&A
<p align="center">
  <img src="docs/images/23-qna-list.png" alt="QnA 목록" width="31%" />
  <img src="docs/images/24-post-list.png" alt="대륙 게시판" width="31%" />
  <img src="docs/images/25-report-detail.png" alt="신고 상세" width="31%" />
</p>

**추천 설명 문구**
- 학습 중 발생한 질문은 강의 단위 Q&A를 통해 해결할 수 있습니다.
- 대륙 게시판은 관심사 기반 커뮤니티 역할을 수행합니다.
- 신고 기능을 통해 부적절한 콘텐츠를 운영 레벨에서 관리할 수 있습니다.

### 10-3. 관리자 인사이트 화면
<p align="center">
  <img src="docs/images/26-admin-statistics.png" alt="관리자 통계" width="80%" />
</p>

**추천 설명 문구**
- 관리자 화면에서는 대륙별 활성도와 강좌 성과를 시각화하여 운영 판단 근거를 제공합니다.
- 강사 채용 우선순위, 신고 집중 구간, 수요 대비 강좌 편차 등을 한눈에 파악할 수 있습니다.

---

## 11. 기술적 문제 해결

### 1) AOP 기반 유해어 선제 차단
- 게시글, 댓글, 공지사항 작성 시 유해어를 사전에 탐지
- 비즈니스 로직 진입 전에 차단하여 관리자 신고 처리 이전 노출 시간을 제거
- GlobalExceptionHandler와 연결하여 화면단 오류 메시지까지 일관되게 처리

### 2) GCS 기반 비동기 강의 업로드
- 업로드 요청과 HTTP 응답 흐름을 분리
- `@Async` 기반 처리로 업로드 블로킹 문제 완화
- DB에는 업로드 상태와 URL을 반영하고, 파일은 클라우드에 저장

### 3) 전역 예외 처리 구조 통합
- 예외 종류가 분산되어 응답 기준이 흔들리던 문제를 개선
- `BusinessException`, `ErrorCode`, `GlobalExceptionHandler` 구조로 통합
- 팀원별 구현 차이로 인해 발생할 수 있는 예외 처리 불일치를 줄임

### 4) JPA 로딩 전략 최적화
- 기본 전략은 `LAZY`로 유지
- 목록 조회에서는 `Fetch Join`을 적용해 N+1 문제를 완화
- 단건 조회와 목록 조회를 구분하여 성능과 설계 균형을 맞춤

### 5) 결제 금액 위변조 방지
- 클라이언트 금액을 신뢰하지 않고 서버에서 DB 기준으로 재계산
- 실제 결제 승인 시 조작 가능성을 줄여 결제 신뢰성을 확보

### 6) 신고 데이터 스냅샷 저장
- 신고 시점의 제목/본문을 별도로 저장
- 원본이 수정되거나 삭제되어도 운영 검토 근거를 유지

### 7) 환불 후 수강 상태 재정비
- 환불 완료 후에도 수강 버튼 상태가 비정상적으로 남는 문제를 개선
- `REFUNDED` 상태를 고려한 조회 및 업데이트 로직으로 리팩토링

---

## 12. 트러블슈팅 화면 배치 추천

> 이 구간은 README 후반부에 두는 것이 좋습니다.  
> “무엇을 만들었는가” 다음에 “어떤 문제를 해결했는가”를 보여주면 설득력이 좋아집니다.

<p align="center">
  <img src="docs/images/30-badword-aop.png" alt="AOP 유해어 차단" width="48%" />
  <img src="docs/images/31-gcs-upload.png" alt="GCS 비동기 업로드" width="48%" />
</p>

<p align="center">
  <img src="docs/images/32-loading-strategy.png" alt="JPA 로딩 전략" width="48%" />
  <img src="docs/images/33-admin-insight-problem-solving.png" alt="관리자 인사이트" width="48%" />
</p>

---

## 13. 협업 방식

### 협업 흐름
`Issue 생성 → Branch 생성 → 개발 → PR → 코드리뷰 → Merge`

### 운영 방식
- GitHub Issue로 작업 단위를 관리
- Milestone으로 개발 단계 구분
- Discussion / Slack / Notion으로 논의와 기록을 병행
- SQL 변경, 공통 규칙, 예외 처리 기준 등은 문서로 남겨 협업 충돌을 줄임

### 브랜치 예시
- `feature/auth-login`
- `feature/admin-statistics`
- `feature/payment-refund`
- `fix/refund-enrollment-status`
- `refactor/global-exception-handler`

---

## 14. 팀원 역할

| 이름 | 담당 영역 |
|---|---|
| 김용준 | 인증 / 관리자 / 통계 / 협업 관리 |
| 김재원 | 수강 / 진도 / 강의 업로드 |
| 박종준 | 결제 / 환불 |
| 이태연 | 콘텐츠 / Q&A / 강사 기능 |
| 정유지 | 게시판 / 신고 |

---

## 15. 프로젝트 구조 예시

> 아래는 README 설명용 예시입니다.  
> 실제 패키지 구조와 다르면 리포지토리에 맞게 수정해서 반영하세요.

```text
src/main/java/com/wanted/ailienlmsprogram
├── admin
├── auth
├── community
├── continent
├── course
├── lecture
├── member
├── payment
├── refund
└── global

src/main/resources
├── templates
├── static
└── application.yml
```

---

## 16. 실행 방법

> 아래 명령어는 **Spring Boot + Gradle 프로젝트 기준 예시**입니다.  
> 실제 저장소 설정과 다르면 리포지토리 환경에 맞게 수정하세요.

### 1) 저장소 클론
```bash
git clone <REPOSITORY_URL>
cd <PROJECT_DIRECTORY>
```

### 2) DB 설정
- MySQL 데이터베이스 생성
- `application.yml` 또는 `application-local.yml`에 DB 계정 정보 입력
- 필요 시 더미데이터 SQL 실행

### 3) 애플리케이션 실행
```bash
./gradlew bootRun
```

### 4) 접속
```text
http://localhost:8080
```

---

## 17. 향후 개선 방향

- 프로필 이미지 / 강좌 썸네일 / 강의 영상을 공통 스토리지 서비스로 통합
- 자동 로그인 및 아이디 기억하기 기능 추가
- member / user 등 분산된 관심사를 재정리하고 공통 로직 재사용 구조 강화
- 비동기 강의 업로드 실패 상태(`FAILED`) 및 재시도 기능 도입

---

## 18. README 이미지 폴더 규칙

```text
docs/
└── images/
    ├── 00-project-logo.png
    ├── 01-main-home.png
    ├── 02-continents.png
    ├── 03-course-detail.png
    ├── 04-learning-view.png
    ├── 10-system-architecture.png
    ├── 11-student-flow.png
    ├── 12-erd.png
    ├── 20-course-detail.png
    ├── 21-payment.png
    ├── 22-learning-progress.png
    ├── 23-qna-list.png
    ├── 24-post-list.png
    ├── 25-report-detail.png
    ├── 26-admin-statistics.png
    ├── 30-badword-aop.png
    ├── 31-gcs-upload.png
    ├── 32-loading-strategy.png
    └── 33-admin-insight-problem-solving.png
```

---

## 19. 이미지 캡처 우선순위

README용 이미지는 아래 순서대로 준비하면 됩니다.

1. **메인 화면**
2. **대륙 목록 화면**
3. **강좌 상세 화면**
4. **강의 시청 / 진도 저장 화면**
5. **Q&A 또는 게시판 화면**
6. **관리자 통계 화면**
7. **ERD 이미지**
8. **시스템 구조도**
9. **트러블슈팅 핵심 이미지 2~4장**

---

## 20. 회고 포인트

- 단순 CRUD를 나열하기보다, **학생 여정과 운영 흐름을 연결한 서비스 설계**가 중요했다.
- Spring Security, JPA, 예외 처리, 비동기 업로드, 외부 결제 연동처럼 서로 다른 기술을 실제 사용자 흐름 안에서 통합하는 경험을 얻었다.
- 기능 구현만이 아니라, **협업 구조와 문서화 방식도 프로젝트 품질에 직접적인 영향을 준다**는 점을 학습했다.

---

## 21. License

This project was created for educational purposes.

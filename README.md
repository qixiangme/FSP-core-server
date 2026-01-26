

# FSP-core-server

🔗 **Server Address:** http://ec2-15-134-227-197.ap-southeast-2.compute.amazonaws.com
ec2-15-134-227-197.ap-southeast-2.compute.amazonaws.com

## 📖 개요 (Overview)

**FSP-core-server**는 AI와 함께하는 현대적인 **시집 서비스**입니다.

사용자는 단순히 시를 읽는 것에 그치지 않고, 직접 시를 쓰고 AI의 도움을 받아 시 속에 담긴 은유와 감정을 깊이 있게 해석할 수 있습니다.

* **시집 감상 및 창작:** 다양한 시를 탐색하고 본인만의 시를 기록합니다.
* **AI 시 해석:** Spring AI를 활용하여 시의 의미를 분석하고 감상평을 공유합니다.
* **보안 및 안정성:** Spring Security와 JWT를 통한 안전한 사용자 인증을 제공합니다.

---

## 🛠 기술 스택 (Tech Stack)

| 구분 | 기술 (Technology) | 상세 내용 |
| --- | --- | --- |
| **Language** | **Kotlin** | 간결하고 안전한 서버 사이드 로직 구현 |
| **Framework** | **Spring Boot** | 애플리케이션 프레임워크 및 REST API 제공 |
| **AI Integration** | **Spring AI** | 외부 AI 모델과의 연동 및 프롬프트 관리 |
| **Security** | **Spring Security** | JWT 기반의 인증 및 권한 관리 |
| **Database/Cache** | **Redis** | 토큰 관리 및 데이터 캐싱을 통한 성능 최적화 |
| **Deployment** | **Docker** | 컨테이너 기반 배포 및 환경 격리 |

---

## 🚀 실행 방법

### 1. Docker를 이용한 실행

```bash
# 이미지 빌드
docker build -t fsp-core-server .

# 컨테이너 실행
docker run -p 8080:8080 fsp-core-server

```

### 2. 로컬 개발 환경 빌드

**필수 사항:** Java 17 이상, Redis 서버 실행 중

```bash
# 빌드 및 실행
./gradlew build
java -jar build/libs/*.jar

```

---

## ⚙️ 환경 변수 (Environment Variables)

애플리케이션 실행을 위해 다음 환경 변수 설정이 필요합니다.

* `JWT_SECRET`: JWT 서명 및 검증을 위한 비밀키
* `AI_SERVER_URL`: 통신 대상 AI 서버의 엔드포인트
* `SPRING_AI_API_KEY`: AI 서비스 이용을 위한 API Key (필요 시)

---

## 📜 라이선스

This project is licensed under the **MIT License**.

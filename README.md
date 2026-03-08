

# FSP-core-server

🔗 **Server Address:**  http://ec2-15-134-227-197.ap-southeast-2.compute.amazonaws.com

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
| **Security** | **Spring Security** | JWT 기반 인증 및 권한 관리 |
| **Database** | **PostgreSQL** | 운영 데이터 저장 및 관계형 스키마 설계 |
| **ORM** | **Spring Data JPA** | 도메인 기반 데이터 접근 계층 구현 |
| **Cache** | **Redis** | 토큰 관리 및 조회 성능 최적화를 위한 캐싱 |
| **Deployment** | **Docker** | 컨테이너 기반 배포 및 환경 격리 ||

---
## 아키텍쳐
<img width="1998" height="589" alt="image" src="https://github.com/user-attachments/assets/97d76e15-3420-468b-8df2-066ecbc25548" />


제공해주신 `docker-compose.yml` 파일을 보니 Redis, Spring Boot 서버, Nginx(Web)가 서로 유기적으로 연결되어 있네요. 기존의 단순 `docker run` 방식보다는 **Docker Compose**를 이용해 전체 스택을 한 번에 올리는 방식이 훨씬 정확하고 편리합니다.

수정된 README의 실행 방법 섹션입니다.

---

## 🚀 실행 방법 (Installation & Setup)

제공되는 `docker-compose.yml`을 사용하여 데이터베이스(Redis), 백엔드 서버, 프론트엔드(Nginx)를 한 번에 실행할 수 있습니다.

### 1. Docker Compose를 이용한 실행 (권장)

모든 서비스 간의 네트워크 설정과 의존성(`depends_on`)이 구성되어 있어 가장 안전한 실행 방법입니다.

```bash
# 1. 프로젝트 루트 디렉토리에서 빌드 및 컨테이너 실행
docker-compose up -d --build

# 2. 실행 상태 확인
docker-compose ps

# 3. 로그 확인 (필요 시)
docker-compose logs -f server

```

**접근 정보:**

* **Web (Frontend):** [http://localhost:80](https://www.google.com/search?q=http://localhost:80)
* **API Server (Backend):** [http://localhost:8084](https://www.google.com/search?q=http://localhost:8084)
* **Redis:** `localhost:6379` (내부망 이름: `redis`)

---

### 2. 개별 서비스 수동 실행

특정 서비스만 별도로 빌드하거나 실행해야 할 경우 다음 명령어를 사용합니다.

```bash
# 서버 이미지만 빌드
docker build -t fsp-core-server .

# 서버 컨테이너만 수동 실행 (Redis가 먼저 실행 중이어야 합니다)
docker run -d \
  --name fsp-core-server \
  -p 8084:8084 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e SPRING_DATA_REDIS_HOST=host.docker.internal \
  fsp-core-server

```

---

### 3. 로컬 개발 환경 빌드 (No Docker)

**필수 사항:** Java 17 이상, Redis 서버 로컬 실행 중

```bash
# 프로젝트 빌드
./gradlew build

# 서버 실행 (8084 포트)
java -jar build/libs/*.jar --server.port=8084

```

---

**💡 참고 사항**

* `web` 서비스는 `./build/web` 경로의 정적 파일과 `./nginx.conf` 설정을 참조합니다. 실행 전 해당 경로에 웹 빌드 파일이 있는지 확인해 주세요.
* 서버는 `8084` 포트로 매핑되어 있으니 API 테스트 시 포트 번호에 유의하시기 바랍니다.

---


## 🛣 API 엔드포인트 (API Endpoints)

### 👤 사용자 (User)

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/users/signup` | 회원가입 및 토큰 발급 |
| `POST` | `/users/login` | 로그인 및 토큰 발급 |

### 📜 시 (Poem)

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/poem` | 새로운 시 생성 및 저장 |
| `GET` | `/poem` | 전체 시 목록 조회 |
| `GET` | `/poem/private` | 비공개 시 목록 조회 |
| `GET` | `/poem/public` | 공개 시 목록 조회 |
| `GET` | `/poem/{id}` | 특정 시의 상세 정보 조회 |

### 💬 대화 및 해석 (Conversation)

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/conversations` | 새로운 대화(시 해석 세션) 시작 |
| `GET` | `/conversations/{conversationId}` | 기존 대화 내역 조회 |
| `POST` | `/conversations/elaborate/{conversationId}` | AI와 심층 대화 (Server-Sent Events 스트리밍) |

---

## 🛠 상세 연동 가이드

### AI 상세 대화 (Elaborate)

`/conversations/elaborate/{conversationId}` 엔드포인트는 **SSE(Server-Sent Events)**를 사용하여 AI의 응답을 실시간으로 스트리밍합니다.

* **Content-Type:** `text/event-stream`
* **Response Type:** `Flux<ElaborateResponse>`

---

## ⚙️ 환경 변수 (Environment Variables)

애플리케이션 실행을 위해 다음 환경 변수 설정이 필요합니다.

* `JWT_SECRET`: JWT 서명 및 검증을 위한 서버 비밀키 (HS256 이상 권장)
* `JWT_ACCESS_TOKEN_VALIDTY_MS`: Access Token의 유효 기간 (단위: $ms$)
* `JWT_REFRESH_TOKEN_VALIDTY_MS`: Refresh Token의 유효 기간 (단위: $ms$)


---

## 📜 라이선스

This project is licensed under the **MIT License**.

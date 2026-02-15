

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
| **Security** | **Spring Security** | JWT 기반의 인증 및 권한 관리 |
| **Database/Cache** | **Redis** | 토큰 관리 및 데이터 캐싱을 통한 성능 최적화 |
| **Deployment** | **Docker** | 컨테이너 기반 배포 및 환경 격리 |

---

## 📈 성능 측정/모니터링

현재 구성은 **Spring Boot Actuator + Micrometer + Prometheus + Grafana** 기준입니다.

### 1) 메트릭 수집 확인 (서버 단독)
아래 엔드포인트로 앱이 메트릭을 정상 노출하는지 먼저 확인합니다.

- `GET /actuator/health`
- `GET /actuator/metrics`
- `GET /actuator/metrics/http.server.requests`
- `GET /actuator/prometheus`

```bash
curl http://localhost:8084/actuator/health
curl http://localhost:8084/actuator/metrics/http.server.requests
curl http://localhost:8084/actuator/prometheus
```

### 2) Prometheus + Grafana 실행
`docker-compose.yml`에 Prometheus(9090), Grafana(3000)가 포함되어 있습니다.

```bash
docker compose up -d --build
```

- Prometheus UI: `http://localhost:9090`
- Grafana UI: `http://localhost:3000` (기본 계정 `admin/admin`)

### 3) Prometheus에서 바로 보는 핵심 쿼리
- 최근 1분 RPS
  - `sum(rate(http_server_requests_seconds_count[1m]))`
- URI별 평균 응답시간
  - `sum(rate(http_server_requests_seconds_sum[1m])) by (uri) / sum(rate(http_server_requests_seconds_count[1m])) by (uri)`
- 95퍼센타일 응답시간
  - `histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket[5m])) by (le, uri))`
- 5xx 비율
  - `sum(rate(http_server_requests_seconds_count{status=~"5.."}[5m])) / sum(rate(http_server_requests_seconds_count[5m]))`

### 4) 개선 전/후 비교 방법
1. 동일 부하 조건(예: nGrinder/k6/JMeter)으로 API를 호출
2. 위 쿼리의 값(RPS, p95, 5xx)을 기록
3. 코드/쿼리 튜닝 후 동일 시나리오 재측정
4. `p95 감소`, `RPS 증가`, `5xx 감소`를 기준으로 개선 여부 판단

`http.server.requests`는 SLO 버킷(`100ms`, `300ms`, `500ms`, `1s`, `3s`)으로 집계되므로,
특정 API가 어느 구간에 몰리는지 쉽게 확인할 수 있습니다.

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

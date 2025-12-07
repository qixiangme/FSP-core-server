# FSP-core-server

이 프로젝트는 Spring Boot로 작성된 서버 애플리케이션입니다.  
 Spring Security 기반의 JWT 인증을 지원하고, 외부 AI 서버와의 통신 기능을 포함합니다. Docker 기반으로 손쉽게 배포 및 실행할 수 있습니다.

## 기술 스택

- **Spring Boot**: 애플리케이션 프레임워크 및 서버
- **Spring Security + JWT**: 인증 및 권한 관리
- **외부 AI 서버 연동:** HTTP 기반 통신
- **Docker**: 컨테이너 기반 배포 및 실행

## 실행 방법

### 1. Docker로 실행

```bash
docker build -t fsp-core-server .
docker run -p 8080:8080 fsp-core-server
```

### 2. 로컬에서 직접 빌드 및 실행

필수: Java 17 이상, Redis 서버 실행 중

```bash
./gradlew build
java -jar build/libs/*.jar
```

## 환경 변수

- `JWT_SECRET` : JWT 암호화 비밀키
- `AI_SERVER_URL` : 통신 대상 AI 서버의 엔드포인트 URL

## 주요 특징

- Spring Boot 기반 REST API 제공  
- Spring Security/JWT 기반 인증 및 권한 관리  
- 외부 AI 서버와의 통신 기능(API 호출 등)  
- Docker로 손쉬운 배포 및 확장

## 라이선스

MIT License

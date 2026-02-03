---
name: "문서 불일치 수정: 포트 안내"
about: "README의 Docker 실행 포트와 실제 설정 포트 불일치 수정"
title: "[DOCS] README 포트 안내 8084로 정합성 맞춤"
labels: ["documentation"]
---

## 요약
README의 Docker 실행 예시 포트와 애플리케이션 실제 포트 설정의 불일치를 수정합니다.

## 문제
- README는 `-p 8080:8080`으로 안내하지만, 앱 설정은 `8084`입니다.
- 현재 안내대로 실행 시 접속 실패 가능성이 있습니다.

## 대상 파일
- `README.md`
- `src/main/resources/application.properties`

## 제안 작업
- README 실행 예시 포트를 `8084`로 변경하거나, 앱 설정을 `8080`으로 정합성 있게 변경합니다.

## 비고
- 문서/설정 간 일관성을 확보합니다.

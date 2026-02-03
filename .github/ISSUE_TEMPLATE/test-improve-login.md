---
name: "테스트 개선: 로그인 테스트 안정화"
about: "로그인 테스트에서 사전 사용자 생성 누락으로 인한 불안정성 개선"
title: "[TEST] 로그인 테스트에서 사용자 사전 생성 추가"
labels: ["test"]
---

## 요약
로그인 테스트가 사용자 생성 없이 실행되어 실패/불안정해지는 문제를 개선합니다.

## 문제
- `UserService.login`은 존재하지 않는 이메일에 대해 예외를 던집니다.
- 현재 테스트는 사용자 생성 없이 로그인 요청을 수행합니다.

## 대상 파일
- `src/test/kotlin/com/fsp/coreserver/controller/UserController.kt`
- `src/main/kotlin/com/fsp/coreserver/user/UserService.kt`

## 제안 작업
- `@BeforeEach`에서 테스트 유저를 저장하거나, `/users/signup`을 먼저 호출해 유저를 준비합니다.
- 이메일/비밀번호가 실제 저장된 값과 일치하도록 테스트 데이터 정비.

## 수용 기준
- 로그인 테스트가 항상 통과하며, 원인 없이 실패하지 않습니다.

---
name: "오탈자 수정: AI 프롬프트 띄어쓰기"
about: "AI 프롬프트 문구의 띄어쓰기(오탈자) 수정"
title: "[TYPO] AI 프롬프트 문구 띄어쓰기 수정"
labels: ["typo", "documentation"]
---

## 요약
AI 프롬프트 안내 문구에서 띄어쓰기 오탈자를 수정합니다.

## 문제
`응답할수` → `응답할 수`로 띄어쓰기가 필요합니다.

## 대상 파일
- `src/main/kotlin/com/fsp/coreserver/ai/ollama/AiService.kt`

## 제안 작업
- 안내 문구의 띄어쓰기 수정

## 비고
- 사용자에게 노출되는 문구의 가독성을 개선합니다.

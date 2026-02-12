# Spring AI 기반 RAG 적용 가이드 (FSP-core-server)

현재 구조는 `ConversationService -> AiService`로 넘어오면서 **시 본문 + 사용자 질문**을 그대로 LLM에 전달하는 방식입니다.
이 방식도 동작하지만, 아래 한계가 있습니다.

- 시 외부의 참고 지식(문학 사조, 표현 기법, 작가 배경)을 반영하기 어려움
- 답변의 일관성과 근거 제시가 약함
- 비슷한 질문이 반복되어도 컨텍스트 재사용이 어려움

이 문서는 기존 구조를 유지하면서 Spring AI로 RAG를 단계적으로 붙이는 실전 설계를 설명합니다.

---

## 1) 목표 아키텍처

1. **지식 소스 구성**
   - 시 본문
   - 작품 해설/비평 데이터
   - 작가/시대 배경 자료
2. **임베딩/색인(Ingestion)**
   - 문서를 청크 단위로 분할
   - 임베딩 생성 후 Vector Store에 저장
3. **질문 시점(Retrieval + Generation)**
   - 사용자 질문 임베딩
   - 관련 청크 Top-K 검색
   - 검색 결과를 프롬프트 컨텍스트로 넣어 생성
4. **출력 품질 제어**
   - 근거 기반 답변 강제
   - 검색 결과 없을 때 fallback 규칙

---

## 2) 의존성 추가

`build.gradle`에 Vector Store starter를 추가하세요.

```gradle
implementation("org.springframework.ai:spring-ai-starter-model-ollama")
implementation("org.springframework.ai:spring-ai-starter-vector-store-pgvector")
```

> 개발 초기에는 `SimpleVectorStore`(메모리)로 시작하고,
> 운영에서는 pgvector(또는 Milvus/Qdrant/Elasticsearch)로 전환하는 방식을 권장합니다.

---

## 3) 설정 예시 (`application-dev.properties`)

```properties
# Embedding 모델
spring.ai.ollama.embedding.model=nomic-embed-text

# PgVector 예시 (PostgreSQL 확장 필요)
spring.ai.vectorstore.pgvector.initialize-schema=true
spring.ai.vectorstore.pgvector.dimensions=768
spring.ai.vectorstore.pgvector.table-name=poem_knowledge
```

> `dimensions` 값은 사용 임베딩 모델 차원과 일치해야 합니다.

---

## 4) 핵심 구현 흐름

### 4-1. 문서 수집/청크/색인 서비스

`Poem`, 해설 텍스트를 `Document`로 변환하고 Vector Store에 넣습니다.

```kotlin
@Service
class RagIngestionService(
    private val poemRepository: PoemRepository,
    private val vectorStore: VectorStore
) {
    @Transactional(readOnly = true)
    fun reindexAll() {
        val docs = poemRepository.findAll().map { poem ->
            Document(
                "제목: ${poem.title}\n작가: ${poem.author}\n본문: ${poem.content}",
                mapOf("poemId" to poem.id, "title" to poem.title, "author" to poem.author)
            )
        }

        // 필요 시 기존 인덱스 삭제 후 재적재
        vectorStore.add(docs)
    }
}
```

실무에서는 `TokenTextSplitter`로 문서 분할 후 넣는 것을 권장합니다.

---

### 4-2. 검색(Retrieval) 서비스

```kotlin
@Service
class RagRetriever(
    private val vectorStore: VectorStore
) {
    fun retrieve(question: String, topK: Int = 4): List<Document> {
        val request = SearchRequest.builder()
            .query(question)
            .topK(topK)
            .similarityThreshold(0.65)
            .build()

        return vectorStore.similaritySearch(request)
    }
}
```

---

### 4-3. 생성 로직에 RAG 컨텍스트 주입

기존 `AiService.elaborate(text)` 구조를 아래와 같이 확장합니다.

```kotlin
fun elaborateWithRag(question: String, poemTitle: String): Flux<String> {
    val retrieved = ragRetriever.retrieve("$poemTitle $question", topK = 4)
    val context = retrieved.joinToString("\n\n---\n\n") { it.text }

    val prompt = Prompt(
        listOf(
            SystemMessage(
                """
                너는 시 해설 도우미다.
                아래 [검색 컨텍스트]에 근거해서 답하라.
                근거가 부족하면 추측하지 말고 부족하다고 말하라.
                """.trimIndent()
            ),
            UserMessage(
                """
                [검색 컨텍스트]
                $context

                [질문]
                $question
                """.trimIndent()
            )
        ),
        OllamaOptions.builder().model("gemma3:4b").temperature(0.3).build()
    )

    return ollamaChatModel.stream(prompt)
        .mapNotNull { it.result?.output?.text }
}
```

---

## 5) 현재 코드 기준 적용 포인트

- `ConversationService.generateChat()`에서 프롬프트 문자열을 직접 만들고 있음
  - 이 위치를 `AiService.elaborateWithRag(...)` 호출로 변경
- `PoemRepository`를 활용해 시 데이터를 주기적으로 재색인
  - 서버 기동 시 1회 + 시 등록/수정 시 증분 색인
- 스트리밍 응답(`Flux`)은 그대로 유지 가능

---

## 6) 품질 높이는 팁 (중요)

1. **청크 전략**
   - 시는 짧아도 해설 데이터는 길기 때문에 300~600 토큰 청크 + 10~15% overlap 권장
2. **Hybrid 검색**
   - 제목/작가 exact match 필터 + 벡터 유사도 검색 함께 사용
3. **근거 출력 포맷**
   - 답변 끝에 `참고 근거: [title, author]` 형태로 metadata 노출
4. **질문 재작성(Query Rewrite)**
   - 사용자의 짧은 질문을 검색 친화 문장으로 1차 변환 후 retrieval
5. **평가 루프 구축**
   - 정답셋(QA set)으로 retrieval hit-rate와 answer faithfulness를 주기 측정

---

## 7) 단계별 도입 로드맵

- **1단계 (1~2일)**: SimpleVectorStore + 시 본문만 색인 + RAG 프롬프트 연결
- **2단계 (3~5일)**: pgvector 전환 + 해설/배경 데이터 확장 + metadata 필터
- **3단계 (지속)**: 오프라인 평가 + 프롬프트/청크/threshold 튜닝 + fallback 고도화

---

## 8) 예상되는 함정

- 임베딩 모델 차원(dimension)과 DB 스키마 불일치
- 색인 데이터 품질이 낮아 검색이 엉뚱해지는 문제
- threshold를 너무 높여서 검색 결과가 비는 문제
- temperature가 높아져 근거 이탈(hallucination) 증가

---

## 9) 바로 실행할 최소 TODO

1. `spring-ai-starter-vector-store-pgvector` 의존성 추가
2. 임베딩 모델 설정 (`nomic-embed-text` 등)
3. `RagIngestionService`, `RagRetriever` 생성
4. `AiService`에 `elaborateWithRag` 추가
5. `ConversationService.generateChat()`에서 RAG 메서드 호출
6. 시 등록/수정 시 증분 색인 트리거 연결

이 순서로 가면 지금 구조를 크게 깨지 않고 RAG로 품질을 끌어올릴 수 있습니다.

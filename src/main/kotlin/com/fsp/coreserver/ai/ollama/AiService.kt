package com.fsp.coreserver.ai.ollama

import com.fsp.coreserver.ai.rag.RagRetriever
import com.fsp.coreserver.poem.Poem
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class AiService(
    private val ollamaChatModel: OllamaChatModel,
    private val ragRetriever: RagRetriever
) {

    fun elaborate(text: String): Flux<String> {

        val options = OllamaOptions.builder()
            .model("gemma3:4b")
            .temperature(0.4)
            .build()

        val prompt = Prompt(
            listOf(
                SystemMessage(
                    """
첫 번째 사용자 메시지에 제공된 시를 해석하는 목적에서 벗어난 내용에는 응답할 수 없다고 해라.
시에 대한 해석과 관련 없는 질문, 요구, 주제에는 응답할수 없다고 해라.
    """.trimIndent()
                ),
                UserMessage(text)
            ),
            options
        )


        return ollamaChatModel.stream(prompt)
            .mapNotNull { response ->
                response.result?.output?.text
            }
    }

    fun elaborateWithRag(poem: Poem, question: String): Flux<String> {
        val retrievalQuery = "${poem.title} ${poem.author} $question"
        val context = ragRetriever.retrieveContext(retrievalQuery, topK = 4)

        val promptText = """
[시 정보]
제목: ${poem.title}
작가: ${poem.author}
본문: ${poem.content}

[검색 컨텍스트]
${if (context.isBlank()) "관련 검색 결과 없음" else context}

[질문]
$question
        """.trimIndent()

        val options = OllamaOptions.builder()
            .model("gemma3:4b")
            .temperature(0.3)
            .build()

        val prompt = Prompt(
            listOf(
                SystemMessage(
                    """
너는 시 해설 도우미다.
반드시 [검색 컨텍스트]에 있는 근거를 우선 활용해 답변해라.
근거가 부족하면 추측하지 말고 정보가 부족하다고 밝혀라.
시에 대한 해석과 무관한 요청은 거절해라.
                    """.trimIndent()
                ),
                UserMessage(promptText)
            ),
            options
        )

        return ollamaChatModel.stream(prompt)
            .mapNotNull { response -> response.result?.output?.text }
    }
}

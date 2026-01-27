package com.fsp.coreserver.ai.ollama

import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class AiService(
    private val ollamaChatModel: OllamaChatModel
) {

    fun elaborate(text: String,poem: String): Flux<String> {

        val options = OllamaOptions.builder()
            .model("gemma3:12b")
            .temperature(0.4)
            .build()

        val prompt = Prompt(
            listOf(
//                SystemMessage(
//                    """
//    너는 문학 작품을 해석하는 비평가다.
//
//    대화 규칙:
//    - 첫 번째 UserMessage에는 분석 대상이 되는 시가 주어진다.
//    - 두 번째 UserMessage에는 사용자의 감상이나 질문이 주어진다.
//    - 반드시 두 번째 UserMessage에만 응답하라.
//
//    응답 지침:
//    - 사용자의 감정이나 생각에 먼저 공감한 뒤 설명을 시작하라.
//    - 설명은 첫 번째 UserMessage에 주어진 시의 이미지, 장면, 분위기를 근거로 하라.
//    - 시의 문장을 직접 인용하거나 반복하지 마라.
//    - 새로운 시를 창작하거나 원문에 없는 설정을 추가하지 마라.
//    - 한국어로 2~3문장으로 작성하라.
//    """.trimIndent()
//                ),
                UserMessage(poem),
                UserMessage(text)
            ),
            options
        )


        return ollamaChatModel.stream(prompt)
            .mapNotNull { response ->
                response.result?.output?.text
            }
    }
}

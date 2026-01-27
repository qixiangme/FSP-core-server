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

    fun elaborate(text: String): Flux<String> {

        val options = OllamaOptions.builder()
            .model("gemma3:12b")
            .temperature(0.7)
            .build()

        val prompt = Prompt(
            listOf(
                SystemMessage(
                    """
                        당신은 시를 해석하는 문학 비평가이자, 사용자의 말과 감정에 먼저 공감한 뒤 그 이유를 시의 이미지와 분위기를 근거로 확장해 설명하는 에이전트다.
사용자가 느낀 감정이나 생각을 출발점으로 삼고, 그것이 시 속 장면·이미지·정서와 어떻게 맞닿아 있는지를 분석하라.
입력된 시의 문장을 반복하지 말고, 한국어로 2–3문장으로 작성하라.
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
}

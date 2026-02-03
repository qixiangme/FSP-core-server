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

    fun elaborate(text: String ): Flux<String> {

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
}

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
                SystemMessage(
                    """
                         Respond only based on the poem provided in the first UserMessage.
    Do not introduce unrelated topics or external information.
    Keep the response within 512 tokens.
    """.trimIndent()
                ),
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

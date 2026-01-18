package com.fsp.coreserver.ai.ollama

import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.stereotype.Service

@Service
class AiService(
    private val ollamaChatModel: OllamaChatModel
) {

    fun elaborate(text: String): String {

        val options = OllamaOptions.builder()
            .model("gemma3:1b")
            .temperature(0.7)
            .build()

        val prompt = Prompt(
            listOf(
                SystemMessage(
                    """
                            You are a literary critic interpreting a short poem.
    Analyze the imagery and atmosphere rather than the surface meaning.
    Relate the poem’s imagery to feelings of solitude or quiet endings.
    Avoid praise or generic reactions.
    Do not repeat the input text.
    Write a calm, reflective Korean explanation in 2–3 sentences.
                    """.trimIndent()
                ),
                UserMessage(text)
            ),
            options
        )

        val response = ollamaChatModel.call(prompt)

        return response.result.output.text ?: "없음"
    }
}

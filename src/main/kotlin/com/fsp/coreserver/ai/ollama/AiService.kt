package com.fsp.coreserver.ai.ollama

import com.fsp.coreserver.conversation.Conversation
import com.fsp.coreserver.conversation.chat.SessionConversation
import com.fsp.coreserver.conversation.enum.Role
import com.fsp.coreserver.poem.PoemRepository
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.Message
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
    private val poemRepository: PoemRepository
) {

    fun elaborate(text: String ,session: SessionConversation,conversation: Conversation): Flux<String> {

        val options = OllamaOptions.builder()
            .model("gemma3:4b")
            .temperature(0.4)
            .build()
        val poem = poemRepository.findById(conversation.poemId)
            .orElseThrow { IllegalArgumentException("Poem not found") }

        val messages : MutableList<Message> = mutableListOf(
            SystemMessage(
                """
첫 번째 사용자 메시지에 제공된 시를 해석하는 목적에서 벗어난 내용에는 응답할 수 없다고 해라.
시에 대한 해석과 관련 없는 질문, 요구, 주제에는 응답할수 없다고 해라.
   제목: ${poem.title}
        작가: ${poem.author}
        본문: ${poem.content}
            """.trimIndent()
            )
        )

        session.chats.forEach { chat ->
            when (chat.role) {
                Role.USER -> messages.add(UserMessage(chat.content))
                Role.ASSISTANT -> messages.add(AssistantMessage(chat.content))
                Role.SYSTEM -> messages.add(SystemMessage(chat.content))
            }
        }
        // 이번 메시지 추가
        messages.add(UserMessage(text))

        val prompt = Prompt(messages, options)

        return ollamaChatModel.stream(prompt)
            .mapNotNull { response ->
                response.result?.output?.text
            }
    }
}

package com.fsp.coreserver.conversation

import com.fsp.coreserver.ai.ollama.AiService
import com.fsp.coreserver.conversation.chat.Chat
import com.fsp.coreserver.conversation.chat.ChatResponse
import com.fsp.coreserver.conversation.elaborate.ElaborateRequest
import com.fsp.coreserver.conversation.elaborate.ElaborateResponse
import com.fsp.coreserver.conversation.enum.Role
import com.fsp.coreserver.poem.PoemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux

//AI <-> USER 채팅 저장 로직 담당
@Service
class ConversationService(
    private val aiService: AiService,
    private val conversationRepository: ConversationRepository,
    private val poemRepository: PoemRepository
) {


    @Transactional
    fun generateConversation(request: ConversationRequest) : ConversationResponse{
        val conversation = conversationRepository.save(Conversation(request.userId,request.poemId))
        val res = ConversationResponse(
            userId = conversation.userId,
            poemId = conversation.poemId,
            conversationId = conversation.id,
            chats = emptyList())
        return res
    }
    @Transactional
    fun generateChat(request: ElaborateRequest): Flux<ElaborateResponse> {

        val conversation = conversationRepository.findById(request.conversationId)
            .orElseThrow { IllegalArgumentException("Conversation not found") }

        val poem = poemRepository.findById(conversation.poemId)
            .orElseThrow { IllegalArgumentException("Poem not found") }

        val prompt = """
        [시]
        제목: ${poem.title}
        작가: ${poem.author}
        본문: ${poem.content}
        [유저의 발화]
        ${request.content}
    """.trimIndent()

        return aiService.elaborate(prompt)
            .map { token ->
                ElaborateResponse(
                    conversationId = conversation.id,
                    delta = token
                )
            }
            .onErrorResume { e ->
                Flux.just(
                    ElaborateResponse(
                        conversationId = conversation.id,
                        delta = "",
                        done = true
                    )
                )
            }
            .concatWith(
                Flux.just(
                    ElaborateResponse(
                        conversationId = conversation.id,
                        delta = "",
                        done = true
                    )
                )
            )
    }
    @Transactional(readOnly = true)
    fun getConversation(conversationId: Long): ConversationResponse {
        val conversation = conversationRepository.findById(conversationId)
            .orElseThrow { IllegalArgumentException("Conversation not found") }
        val chats = conversation.chats.map {
            ChatResponse(it.role, it.content) }
        return ConversationResponse(
            conversation.userId,
            conversation.poemId,
            conversation.id,
            chats
        )
    }


}
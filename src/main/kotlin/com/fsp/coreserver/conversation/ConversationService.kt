package com.fsp.coreserver.conversation

import com.fsp.coreserver.ai.ollama.AiService
import com.fsp.coreserver.conversation.chat.Chat
import com.fsp.coreserver.conversation.chat.ChatResponse
import com.fsp.coreserver.conversation.chat.SessionChat
import com.fsp.coreserver.conversation.chat.SessionConversation
import com.fsp.coreserver.conversation.elaborate.ElaborateRequest
import com.fsp.coreserver.conversation.elaborate.ElaborateResponse
import com.fsp.coreserver.conversation.enum.Role
import com.fsp.coreserver.poem.PoemRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux

//AI <-> USER 채팅 저장 로직 담당
@Service
class ConversationService(
    private val aiService: AiService,
    private val conversationRepository: ConversationRepository,
    private val redisTemplate: RedisTemplate<String, Any> // Redis 캐시
) {


    @Transactional
    fun generateConversation(request: ConversationRequest) : ConversationResponse{
        val conversation = conversationRepository.save(Conversation(request.userId,request.poemId))
        val res = ConversationResponse(
            userId = conversation.userId,
            poemId = conversation.poemId,
            conversationId = conversation.id,
            chats = emptyList())
        val session = SessionConversation(request.userId, request.poemId)
        redisTemplate.opsForValue().set(conversation.id.toString(), session)
        return res
    }

    @Transactional
    fun generateChat(request: ElaborateRequest): Flux<ElaborateResponse> {
        // 1. Redis에서 세션 조회
        val session = redisTemplate.opsForValue().get(request.conversationId.toString()) as? SessionConversation
            ?: throw IllegalArgumentException("Session not found")

        // 2. 유저 메시지 세션에 추가
        session.chats.add(SessionChat(Role.USER, request.content))

        val conversation = conversationRepository.findById(request.conversationId)
            .orElseThrow { IllegalArgumentException("Conversation not found") }


        val prompt = """
        ${request.content}
    """.trimIndent()
        val aiResponseTokens = mutableListOf<String>()
        return aiService.elaborate(prompt,session,conversation)
            .map { token ->
                aiResponseTokens.add(token)
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
            .doOnComplete {
                // AI 응답 끝나면 토큰 합쳐서 Redis에 저장
                val fullResponse = aiResponseTokens.joinToString("")
                session.chats.add(SessionChat(Role.ASSISTANT, fullResponse))
                redisTemplate.opsForValue().set(request.conversationId.toString(), session)
            }
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
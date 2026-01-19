package com.fsp.coreserver.conversation

import com.fsp.coreserver.ai.ollama.AiService
import com.fsp.coreserver.conversation.elaborate.ElaborateRequest
import com.fsp.coreserver.conversation.elaborate.ElaborateResponse
import com.fsp.coreserver.conversation.enum.Role
import com.fsp.coreserver.poem.PoemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
    fun generateChat(request: ElaborateRequest): ElaborateResponse {

        val conversation = conversationRepository.findById(request.conversationId)
            .orElseThrow { IllegalArgumentException("Conversation not found") }

        val poem = poemRepository.findById(conversation.poemId)
            .orElseThrow { IllegalArgumentException("Poem not found") }

        conversation.addChat(
            Chat(
                role = Role.USER,
                content = request.content
            )
        )

        val prompt = """
        [시]
        제목: ${poem.title}
        작가: ${poem.author}
        본문: ${poem.content}
        느낀점: ${request.content}
    """.trimIndent()

        val aiResponse = aiService.elaborate(prompt)

        conversation.addChat(
            Chat(
                role = Role.ASSISTANT,
                content = aiResponse
            )
        )

        return ElaborateResponse(
            conversationId = conversation.id,
            content = aiResponse
        )
    }
    @Transactional(readOnly = true)
    fun getConversation(conversationId: Long): ConversationResponse {
        val conversation = conversationRepository.findById(conversationId)
            .orElseThrow { IllegalArgumentException("Conversation not found") }

        return ConversationResponse(
            conversation.userId,
            conversation.poemId,
            conversation.id,
            conversation.chats
        )
    }


}
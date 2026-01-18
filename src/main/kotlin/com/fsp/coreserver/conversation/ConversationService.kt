package com.fsp.coreserver.conversation

import com.fsp.coreserver.ai.ollama.AiService
import com.fsp.coreserver.ai.py_server.AiServiceFacade
import com.fsp.coreserver.conversation.enum.Role
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

//AI <-> USER 채팅 저장 로직 담당
@Service
class ConversationService(
    private val aiService: AiService,
    private val conversationRepository: ConversationRepository
) {

    @Transactional
    fun generatedChat(request: ChatMessageRequest): ChatMessageResponse {
        // 1️⃣ 세션ID 생성 or 주입
        val resolvedSessionId = request.sessionId ?: UUID.randomUUID().toString()
        val content = """
            [시]
             제목:${request.poem.getTitle()}
             작가:${request.poem.getAuthor()}
             본문:${request.poem.getContent()}
            느낀점 : ${request.content}
        """.trimIndent()
        // 2️⃣ 유저 메시지 저장
        conversationRepository.save(
            Chat(sessionId = resolvedSessionId, role = Role.USER, content = request.content)
        )
        // 3️⃣ AI 서버 호출
        val aiResponse = aiService.elaborate(content)
        // 4️⃣ AI 응답 저장
        conversationRepository.save(
            Chat(sessionId = resolvedSessionId, role = Role.ASSISTANT, content = aiResponse)
        )
        return ChatMessageResponse(
            sessionId = resolvedSessionId,
            role = Role.ASSISTANT,
            content = aiResponse
        )
    }
}
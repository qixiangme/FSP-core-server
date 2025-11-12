package com.fsp.coreserver.service

import com.fsp.coreserver.domain.Chat
import com.fsp.coreserver.domain.Summary
import com.fsp.coreserver.dto.ChatMessageRequest
import com.fsp.coreserver.dto.ChatMessageResponse
import com.fsp.coreserver.dto.Role
import com.fsp.coreserver.dto.SummaryRequest
import com.fsp.coreserver.dto.SummaryResponse
import com.fsp.coreserver.repository.ConversationRepository
import com.fsp.coreserver.repository.SumarryRepository
import com.fsp.coreserver.service.ai.AiServiceFacade
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SumarryService(
    private val aiService: AiServiceFacade,
    private val sumarryRepostiroy: SumarryRepository,
    private val userService: UserService
){

    @Transactional
    fun generateSummary(request: SummaryRequest): SummaryResponse {
        // 1️⃣ JWT 또는 SecurityContext에서 userId 추출
        val user = userService.getUserById(request.userId)
        // 2️⃣ conversation 리스트를 하나의 문자열로 합치기
        val combinedContent = request.conversation.joinToString(separator = "\n") { msg ->
            "[${msg.role}] ${msg.content}"
        }

        // 3️⃣ AI 서버 호출 (요약)
        val summaryContent = aiService.summarizeConversation(combinedContent)

        // 4️⃣ Summary 엔티티 생성 및 저장
        val summary = sumarryRepostiroy.save(
            Summary(
                content = summaryContent,
                user = user
            )
        )

        // 5️⃣ Response DTO 반환
        return SummaryResponse(
            id = summary.id,
            content = summary.content,
            userId = user.id,
            userName = user.name,
            createdAt = summary.createdAt.toString()
        )
    }



}


@Transactional
fun generatedChat(request: ChatMessageRequest): ChatMessageResponse {
    // 1️⃣ 세션ID 생성 or 주입
    val resolvedSessionId = request.sessionId ?: UUID.randomUUID().toString()
    val content = """
            시: ${request.poem},
            느낀점 : ${request.content}
        """.trimIndent()
    // 2️⃣ 유저 메시지 저장
    conversationRepository.save(
        Chat(sessionId = resolvedSessionId, role = Role.USER, content = request.content)
    )

    // 3️⃣ AI 서버 호출
    val aiResponse = aiService.elaborateText(request.content)

    // 4️⃣ AI 응답 저장
    conversationRepository.save(
        Chat(sessionId = resolvedSessionId, role = Role.ASSISTANT, content = aiResponse)
    )
    return ChatMessageResponse(
        sessionId = resolvedSessionId,
        role =Role.ASSISTANT,
        content = content
    )
}
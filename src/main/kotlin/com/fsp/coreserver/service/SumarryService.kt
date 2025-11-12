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


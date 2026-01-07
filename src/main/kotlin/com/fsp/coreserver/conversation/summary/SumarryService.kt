package com.fsp.coreserver.conversation.summary

import com.fsp.coreserver.ai.AiServiceFacade
import com.fsp.coreserver.user.UserService
import jakarta.transaction.Transactional
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.Long

@Service
class SumarryService(
    private val aiService: AiServiceFacade,
    private val summaryRepostiroy: SumarryRepository,
    private val userService: UserService
){

    @Transactional
    fun generateSummary(request: SummaryRequest): SummaryResponse {
        // AI 서버 호출 (누적된 대화를 기반으로 요약)
        val user = userService.getUserById(request.userId) // User 엔티티 조회

        val summaryContent = aiService.summarizeConversation(request.conversation)

        // Summary 엔티티 저장
        val summary = summaryRepostiroy.save(
            Summary(
                content = summaryContent,
                user = user)
        )
        return SummaryResponse(
            content = summaryContent,
            userId = user.id,
            userName = user.name,
        )
    }



}
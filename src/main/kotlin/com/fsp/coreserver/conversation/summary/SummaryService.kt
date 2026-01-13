package com.fsp.coreserver.conversation.summary

import com.fsp.coreserver.ai.AiServiceFacade
import com.fsp.coreserver.user.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SummaryService(
    private val aiService: AiServiceFacade,
    private val summaryRepository: SummaryRepository,
    private val userService: UserService
){

    @Transactional
    fun generateSummary(request: SummaryRequest): SummaryResponse {
        // AI 서버 호출 (누적된 대화를 기반으로 요약)
        val user = userService.getUserById(request.userId) // User 엔티티 조회

        val summaryContent = aiService.summarizeConversation(request.conversation)

        // Summary 엔티티 저장
        val summary = summaryRepository.save(
            Summary(
                content = summaryContent,
                user = user)
        )
        return SummaryResponse(
            content = summaryContent,
            userId = user.getId(),
            userName = user.getName(),
        )
    }



}
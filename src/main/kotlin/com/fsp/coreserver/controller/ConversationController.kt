package com.fsp.coreserver.controller

import com.fsp.coreserver.controller.PoemController.ElaborateRequest
import com.fsp.coreserver.controller.PoemController.SummarizeRequest
import com.fsp.coreserver.domain.Summary
import com.fsp.coreserver.service.ai.AiServiceFacade
import com.fsp.coreserver.service.ConversationService
import com.fsp.coreserver.service.PoemService
import com.fsp.coreserver.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.collections.plus

@RestController
@RequestMapping("/chat")
class ConversationController(
    private val conversationService: ConversationService,
    private val poemService: PoemService,
    private val aiServiceFacade: AiServiceFacade,
    private val userService: UserService
) {
    // ----------------- elaborate -----------------
    @PostMapping("/{userId}/{poemId}/elaborate")
    fun elaboratePoem(
        @PathVariable userId: Long,
        @PathVariable poemId: Long,
        @RequestBody request: ElaborateRequest
    ): ResponseEntity<String> {
        val poem = poemService.getPoemDetail(poemId) // 필요시 DB 내용과 함께 사용 가능
        // AI 서버 호출 및 대화 누적
        val aiResult = conversationService.generatedChat(
            sessionId =  request.sessionId,
            userMessage = request.text,
            poem = poem
        )
        return ResponseEntity.ok(aiResult)
    }

    // ----------------- summarize -----------------
    @PostMapping("/{userId}/{poemId}/summarize")
    fun summarizePoem(
        @PathVariable userId: Long,
        @PathVariable poemId: Long,
        @RequestBody request: SummarizeRequest
    ): ResponseEntity<String> {
        val conversation = request.conversation.toMutableList()

        if (conversation.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ 대화가 없습니다. 먼저 elaborate를 사용하세요.")
        }

        // AI 서버 호출 (누적된 대화를 기반으로 요약)
        val summaryContent = aiServiceFacade.summarizeConversation(conversation)

        // Summary 엔티티 저장
        val user = userService.getUserById(userId) // User 엔티티 조회
        val summary = Summary(
            content = summaryContent,
            user = user
        )
        .save(summary) // SummaryRepository 필요

        // 시스템 응답도 대화 누적 (ConversationService 사용)
        conversationService.generatedChat(
            sessionId = request.,
            messages = conversation + mapOf("role" to "assistant", "content" to summaryContent)
        )

        return ResponseEntity.ok(summaryContent)
    }


    // ----------------- 대화 초기화 -----------------
    @PostMapping("/{id}/clearConversation")
    fun clearConversation(@PathVariable id: Long): ResponseEntity<String> {
        conversationService.clearConversation(id)
        return ResponseEntity.ok("Conversation cleared for poem $id")
    }
}
package com.fsp.coreserver.conversation

import com.fsp.coreserver.ai.AiServiceFacade
import com.fsp.coreserver.conversation.Chat
import com.fsp.coreserver.poem.PoemController
import com.fsp.coreserver.conversation.summary.Summary
import com.fsp.coreserver.conversation.ConversationService
import com.fsp.coreserver.conversation.summary.SumarryRepository
import com.fsp.coreserver.poem.PoemService
import com.fsp.coreserver.user.UserService
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ConversationController(
    private val conversationService: ConversationService,
    private val poemService: PoemService,
    private val aiServiceFacade: AiServiceFacade,
    private val userService: UserService,
    private val summaryRepository: SumarryRepository
) {
    // ----------------- elaborate -----------------
    @PostMapping("/{userId}/{poemId}/elaborate")
    fun elaboratePoem(
        @PathVariable userId: Long,
        @PathVariable poemId: Long,
        @RequestBody request: PoemController.ElaborateRequest
    ): ResponseEntity<String> {
        val poem = poemService.getPoemDetail(poemId) // 필요시 DB 내용과 함께 사용 가능
        // AI 서버 호출 및 대화 누적
        val chatReq = ChatMessageRequest(
            sessionId =  request.sessionId,
            role = Role.USER,
            content = request.text,
            poem = poem
        )
        val aiResult = conversationService.generatedChat(
        chatReq
        )
        return ResponseEntity.ok(aiResult.content)
    }

    // ----------------- summarize -----------------
    @PostMapping("/{userId}/{poemId}/summarize")
    fun summarizePoem(
        @PathVariable userId: Long,
        @PathVariable poemId: Long,
        @RequestBody request: PoemController.SummarizeRequest
    ): ResponseEntity<String> {
        val conversation = request.conversation.toMutableList()

        if (conversation.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ 대화가 없습니다. 먼저 elaborate를 사용하세요.")
        }

        // AI 서버 호출 (누적된 대화를 기반으로 요약)
        val summaryContent = aiServiceFacade.summarizeConversation(conversation)

        // Summary 엔티티 저장
        val user = userService.getUserById(userId) // User 엔티티 조회
        val summary = summaryRepository.save(
            Summary(
                id = user.id,
                content = summaryContent,
                user = user)
        )
        return ResponseEntity.ok(summaryContent)
    }

}
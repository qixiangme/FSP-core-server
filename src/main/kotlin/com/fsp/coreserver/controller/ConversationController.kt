package com.fsp.coreserver.controller

import com.fsp.coreserver.dto.ChatMessageRequest
import com.fsp.coreserver.dto.ChatMessageResponse
import com.fsp.coreserver.dto.Role
import com.fsp.coreserver.dto.SummaryRequest
import com.fsp.coreserver.dto.SummaryResponse
import com.fsp.coreserver.dto.conversation.ElaborateRequest
import com.fsp.coreserver.service.ConversationService
import com.fsp.coreserver.service.PoemService
import com.fsp.coreserver.service.SumarryService
import com.fsp.coreserver.service.ai.AiServiceFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/chat")
class ConversationController(
    private val conversationService: ConversationService,
    private val poemService: PoemService,
    private val sumarryService: SumarryService
) {

    /**
     * 1) 시 사용자가 입력한 내용을 AI에게 전달해 elaboration 수행
     */
    @PostMapping("/{userId}/{poemId}/elaborate")
    fun elaboratePoem(
        @PathVariable userId: Long,
        @PathVariable poemId: Long,
        @RequestBody request: ElaborateRequest
    ): ResponseEntity<ChatMessageResponse> {

        val poem = poemService.getPoemDetail(poemId)

        val userChat = ChatMessageRequest(
            sessionId = request.sessionId,
            role = Role.USER,
            poem = poem,
            content = request.text
        )

        val aiResult = conversationService.generatedChat(userChat)

        return ResponseEntity.ok(aiResult)
    }


    /**
     * 2) 누적된 대화를 기반으로 요약 및 DB 저장
     */
    @PostMapping("/{userId}/{poemId}/summarize")
    fun summarizePoem(
        @PathVariable userId: Long,
        @PathVariable poemId: Long,
        @RequestBody request: SummaryRequest
    ): ResponseEntity<Any> {

        if (request.conversation.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ 대화가 없습니다. 먼저 elaborate를 사용하세요.")
        }

        val saved = sumarryService.generateSummary(
            request = request
        )

        val response = SummaryResponse(
            id = userId,
            content = saved.content,
            createdAt = saved.createdAt.format(DateTimeFormatter.ISO_DATE_TIME)
        )

        return ResponseEntity.ok(response)
    }
}

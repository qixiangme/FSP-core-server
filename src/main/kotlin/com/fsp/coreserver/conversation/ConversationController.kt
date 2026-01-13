package com.fsp.coreserver.conversation

import com.fsp.coreserver.ai.AiServiceFacade
import com.fsp.coreserver.conversation.summary.SummaryRepository
import com.fsp.coreserver.conversation.summary.SummaryService
import com.fsp.coreserver.conversation.summary.SummaryRequest
import com.fsp.coreserver.conversation.summary.SummaryResponse
import com.fsp.coreserver.poem.PoemService
import com.fsp.coreserver.user.UserService
import org.springframework.http.ResponseEntity
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
    private val summaryRepository: SummaryRepository,
    private val summaryService : SummaryService
) {
    // ----------------- elaborate -----------------
    //TODO chatMessageResponse를 Elaborate로 변경
    @PostMapping("/{userId}/{poemId}/elaborate")
    fun elaboratePoem(
        @PathVariable userId: Long,
        @PathVariable poemId: Long,
        @RequestBody request: ChatMessageRequest
    ): ResponseEntity<ChatMessageResponse> {

        return ResponseEntity.ok(conversationService.generatedChat(request))
    }

    // ----------------- summarize -----------------
    @PostMapping("/{userId}/{poemId}/summarize")
    fun summarizePoem(
        @PathVariable userId: Long,
        @PathVariable poemId: Long,
        @RequestBody request: SummaryRequest,
    ): ResponseEntity<SummaryResponse> {
        return ResponseEntity.ok(summaryService.generateSummary(request))

    }

}
package com.fsp.coreserver.conversation

import com.fsp.coreserver.conversation.elaborate.ElaborateRequest
import com.fsp.coreserver.conversation.elaborate.ElaborateResponse
import com.fsp.coreserver.conversation.summary.SummaryService
import com.fsp.coreserver.conversation.summary.SummaryRequest
import com.fsp.coreserver.conversation.summary.SummaryResponse
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
    private val summaryService : SummaryService
) {
    // ----------------- elaborate -----------------
    //TODO chatMessageResponse를 Elaborate로 변경
    @PostMapping("/{userId}/{poemId}/elaborate")
    fun elaboratePoem(
        @PathVariable userId: Long,
        @PathVariable poemId: Long,
        @RequestBody request: ElaborateRequest
    ): ResponseEntity<ElaborateResponse> {

        return ResponseEntity.ok(conversationService.generatedElaborate(request))
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
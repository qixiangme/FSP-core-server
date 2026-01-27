package com.fsp.coreserver.conversation

import com.fsp.coreserver.conversation.elaborate.ElaborateRequest
import com.fsp.coreserver.conversation.elaborate.ElaborateResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/conversations")
class ConversationController(
    private val conversationService: ConversationService,
) {

    @PostMapping("/elaborate/{conversationId}",
        produces = ["text/event-stream"])
    fun elaborate(
        @PathVariable conversationId: Long,
        @RequestBody request: ElaborateRequest
    ): Flux<ElaborateResponse> {

        val enrichedRequest = request.copy(conversationId = conversationId)
        return conversationService.generateChat(enrichedRequest)

    }

    @GetMapping("/{conversationId}")
    fun getConversation(
        @PathVariable conversationId: Long
    ): ResponseEntity<ConversationResponse> {

        return ResponseEntity.ok(
            conversationService.getConversation(conversationId)
        )
    }

    @PostMapping
    fun startConversation(
        @RequestBody conversationRequest: ConversationRequest
    ): ResponseEntity<ConversationResponse>{
        return ResponseEntity.ok(
            conversationService.generateConversation(conversationRequest)
        )
    }
}

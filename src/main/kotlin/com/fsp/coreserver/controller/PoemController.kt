package com.fsp.coreserver.controller

import com.fsp.coreserver.domain.Poem
import com.fsp.coreserver.dto.PoemRequest
import com.fsp.coreserver.dto.PoemResponse
import com.fsp.coreserver.service.ai.AiServiceFacade
import com.fsp.coreserver.service.ConversationService
import com.fsp.coreserver.service.PoemService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/poem")
class PoemController(
    private val poemService: PoemService,
    private val aiServiceFacade: AiServiceFacade,
    private val conversationService: ConversationService
) {

    // ----------------- DTO -----------------
    data class ElaborateRequest(val text: String, val sessionId: String?)
    // DTO 수정
    data class SummarizeRequest(val conversation: List<Map<String, String>>
    )


    // ----------------- 시 생성 / 조회 -----------------
    @PostMapping()
    fun createPoem(@RequestBody request: PoemRequest): ResponseEntity<PoemResponse> {
        val response = poemService.createPoem(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping()
    fun getPoems(): List<Poem> {
        return poemService.getPoems()
    }

    @GetMapping("/{id}")
    fun getPoemById(@PathVariable id: Long): Poem? {
        return poemService.getPoemDetail(id)
    }


}

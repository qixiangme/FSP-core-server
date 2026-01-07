package com.fsp.coreserver.poem

import com.fsp.coreserver.ai.AiServiceFacade
import com.fsp.coreserver.conversation.ConversationService
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController
@RequestMapping("/poem")
class PoemController(
    private val poemService: PoemService,
    private val aiServiceFacade: AiServiceFacade,
    private val conversationService: ConversationService
) {


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


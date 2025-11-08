package com.fsp.coreserver.controller

import com.fsp.coreserver.domain.Poem
import com.fsp.coreserver.dto.PoemRequest
import com.fsp.coreserver.dto.PoemResponse
import com.fsp.coreserver.service.AiServiceFacade
import com.fsp.coreserver.service.PoemService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/poem")
class PoemController(
    private val poemService: PoemService,
    private val aiServiceFacade: AiServiceFacade
){
    @PostMapping()
    fun createPoem(@RequestBody request: PoemRequest): ResponseEntity<PoemResponse>{
        val response = poemService.createPoem(request)
        return ResponseEntity.ok(response)
    }
    @GetMapping()
    fun getPoems() : List<Poem>{
        return poemService.getPoems()
    }
    @GetMapping("/{id}")
    fun getPoemById(@PathVariable id : Long) : Poem?{
        return poemService.getPoemDetail(id)
    }
    @PostMapping("/{id}/interpret")
    fun interpretPoem(@PathVariable id: Long): ResponseEntity<String> {
        val poem = poemService.getPoemDetail(id)
        val aiResult = aiServiceFacade.analyzePoem(poem.content)
        return ResponseEntity.ok(aiResult)
    }
}
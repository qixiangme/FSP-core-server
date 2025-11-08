package com.fsp.coreserver.controller

import com.fsp.coreserver.dto.CommunityRequest
import com.fsp.coreserver.dto.CommunityResponse
import com.fsp.coreserver.service.CommunityService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/communities")
class CommunityController(
    private val communityService: CommunityService
) {

    @PostMapping
    fun createCommunity(@RequestBody request: CommunityRequest): CommunityResponse =
        communityService.createCommunity(request)

    @GetMapping
    fun getAllCommunities(): List<CommunityResponse> =
        communityService.getAllCommunities()

    @GetMapping("/{id}")
    fun getCommunityById(@PathVariable id: Long): CommunityResponse =
        communityService.getCommunityById(id)

    @PostMapping("/{id}/like")
    fun addLike(@PathVariable id: Long): CommunityResponse =
        communityService.addLike(id)
}

package com.fsp.coreserver.community

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
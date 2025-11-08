package com.fsp.coreserver.service

import com.fsp.coreserver.domain.Community
import com.fsp.coreserver.dto.CommunityRequest
import com.fsp.coreserver.dto.CommunityResponse
import com.fsp.coreserver.repository.CommunityRepository
import org.springframework.stereotype.Service

@Service
class CommunityService(
    private val communityRepository: CommunityRepository
) {

    fun createCommunity(request: CommunityRequest): CommunityResponse {
        val community = Community(
            title = request.title,
            author = request.author,
            content = request.content,
            hashtags = request.hashtags
        )
        val saved = communityRepository.save(community)
        return toResponse(saved)
    }

    fun getAllCommunities(): List<CommunityResponse> =
        communityRepository.findAll().map { toResponse(it) }

    fun getCommunityById(id: Long): CommunityResponse =
        communityRepository.findById(id)
            .map { toResponse(it) }
            .orElseThrow { IllegalArgumentException("Community not found with id: $id") }

    fun addLike(id: Long): CommunityResponse {
        val community = communityRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Community not found with id: $id") }

        val updated = Community(
            id = community.id,
            title = community.title,
            author = community.author,
            content = community.content,
            likes = community.likes + 1,
            hashtags = community.hashtags
        )

        val saved = communityRepository.save(updated)
        return toResponse(saved)
    }


    private fun toResponse(community: Community) = CommunityResponse(
        id = community.id,
        title = community.title,
        author = community.author,
        content = community.content,
        likes = community.likes,
        hashtags = community.hashtags
    )
}

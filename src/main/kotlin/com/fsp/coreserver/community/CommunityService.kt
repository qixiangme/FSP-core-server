package com.fsp.coreserver.community

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CommunityService(
    private val communityRepository: CommunityRepository
) {
    @CacheEvict(cacheNames = ["community"], key = "'all'")
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
    @Cacheable(cacheNames = ["community"], key = "'all'")    fun getAllCommunities(): List<CommunityResponse> =
        communityRepository.findAll().map { toResponse(it) }
    @Cacheable(cacheNames = ["community"], key = "#id")
    fun getCommunityById(id: Long): CommunityResponse =
        communityRepository.findById(id)
            .map { toResponse(it) }
            .orElseThrow { IllegalArgumentException("Community not found with id: $id") }
    @CacheEvict(cacheNames = ["community"], allEntries = true)
    fun addLike(id: Long): CommunityResponse {
        val community = communityRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Community not found with id: $id") }

        community.like()

        val saved = communityRepository.save(community)
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
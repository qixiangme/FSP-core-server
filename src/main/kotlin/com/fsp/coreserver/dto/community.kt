package com.fsp.coreserver.dto


data class CommunityRequest(
    val title: String,
    val author: String,
    val content: String,
    val hashtags: List<String> = emptyList()
)

data class CommunityResponse(
    val id: Long,
    val title: String,
    val author: String,
    val content: String,
    val likes: Int,
    val hashtags: List<String>
)

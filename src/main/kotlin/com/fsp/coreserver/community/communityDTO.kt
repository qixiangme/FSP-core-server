package com.fsp.coreserver.community


data class CommunityRequest(
    val title: String,
    val author: String,
    val content: String,
    val hashtags: List<String> = emptyList()
)

data class CommunityResponse(
    val id: Long = 0L,
    val title: String = "",
    val author: String= "",
    val content: String = "",
    val likes: Int = 0,
    val hashtags: List<String> = emptyList()
)

package com.fsp.coreserver.community

import jakarta.validation.constraints.NotBlank
import javax.validation.constraints.Size


data class CommunityRequest(
    @field:NotBlank
    @field:Size(max = 100)
    val title: String,
    @field:NotBlank
    @field:Size(max = 50)
    val author: String,
    @field:NotBlank
    @field:Size(max = 5000)
    val content: String,
    val hashtags: List<String> = emptyList()
)

data class CommunityResponse(
    val id: Long = 0L,
    val title: String = "",
    val author: String= "",
    val content: String= "",
    val likes: Int = 0,
    val hashtags: List<String> = emptyList()
)

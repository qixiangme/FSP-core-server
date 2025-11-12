package com.fsp.coreserver.dto

import com.fsp.coreserver.domain.Poem

data class ChatMessageRequest(
    val sessionId: String?,
    val role: Role,
    val poem: Poem,
    val content: String
)
data class ChatMessageResponse(
    val sessionId: String,
    val role: Role,
    val content: String
)

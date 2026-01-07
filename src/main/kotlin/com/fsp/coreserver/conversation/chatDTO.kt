package com.fsp.coreserver.conversation

import com.fsp.coreserver.conversation.enum.Role
import com.fsp.coreserver.poem.Poem

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

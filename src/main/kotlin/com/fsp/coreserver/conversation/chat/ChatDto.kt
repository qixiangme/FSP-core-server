package com.fsp.coreserver.conversation.chat

import com.fsp.coreserver.conversation.enum.Role

data class ChatResponse(
    val role: Role,
    val content: String
)

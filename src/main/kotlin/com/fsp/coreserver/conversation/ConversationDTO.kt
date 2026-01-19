package com.fsp.coreserver.conversation

import com.fsp.coreserver.conversation.chat.ChatResponse


data class ConversationRequest (
    val userId: Long,
    val poemId: Long,
    val conversationId: Long?,
    )

data class ConversationResponse (
    val userId: Long,
    val poemId: Long,
    val conversationId: Long,
    val chats : List<ChatResponse>
)
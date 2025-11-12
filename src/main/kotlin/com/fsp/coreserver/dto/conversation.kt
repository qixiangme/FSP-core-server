package com.fsp.coreserver.dto


data class ConversationRequest(
    val sessionId: String,
    val messages: List<ChatMessageResponse>
)

data class ConversationResponse(
    val sessionId: String,
    val messages: List<ChatMessageResponse>
)
enum class Role {
    USER,
    ASSISTANT,
    SYSTEM
}
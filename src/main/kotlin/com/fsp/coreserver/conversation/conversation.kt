package com.fsp.coreserver.conversation


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
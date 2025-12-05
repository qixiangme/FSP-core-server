package com.fsp.coreserver.dto

data class ConversationRequest(
    val sessionId: String,
    val messages: List<ChatMessageResponse>
)

data class ConversationResponse(
    val sessionId: String,
    val messages: List<ChatMessageResponse>
)

data class ElaborateRequest(
    val text: String,
    val sessionId: String? = null
)

data class SummarizeRequest(
    val conversation: List<ChatMessageResponse>,
    val userId: Long
)

enum class Role {
    USER,
    ASSISTANT,
    SYSTEM
}

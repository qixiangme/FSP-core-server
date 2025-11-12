package com.fsp.coreserver.dto
data class SummaryRequest(
    val conversation: List<ChatMessageResponse>, // Conversation 메시지 리스트
    val userId: Long
)
data class SummaryResponse(
    val id: Long,
    val content: String,
    val userId: Long,
    val userName: String,
    val createdAt: String
)
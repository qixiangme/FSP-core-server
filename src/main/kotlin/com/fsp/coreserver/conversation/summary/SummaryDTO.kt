package com.fsp.coreserver.conversation.summary

import com.fsp.coreserver.conversation.elaborate.ElaborateResponse

data class SummaryRequest(
    val conversation: List<ElaborateResponse>, // Conversation 메시지 리스트
    val userId: Long
)
data class SummaryResponse(
    val content: String,
    val userId: Long,
    val userName: String,
)
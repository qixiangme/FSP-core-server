package com.fsp.coreserver.conversation.elaborate


data class ElaborateRequest(
    val conversationId: Long,
    val content: String
)
data class ElaborateResponse(
    val conversationId: Long,
    val delta: String,
    val done: Boolean = false
)

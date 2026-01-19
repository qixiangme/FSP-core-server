package com.fsp.coreserver.conversation.elaborate

import com.fsp.coreserver.conversation.enum.Role
import com.fsp.coreserver.poem.Poem

data class ElaborateRequest(
    val conversationId: Long,
    val content: String
)
data class ElaborateResponse(
    val conversationId: Long,
    val content: String
)

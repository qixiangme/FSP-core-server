package com.fsp.coreserver.conversation.elaborate

import com.fsp.coreserver.conversation.enum.Role
import com.fsp.coreserver.poem.Poem

data class ElaborateRequest(
    val sessionId: String?,
    val role: Role,
    val poem: Poem,
    val content: String
)
data class ElaborateResponse(
    val sessionId: String,
    val role: Role,
    val content: String
)

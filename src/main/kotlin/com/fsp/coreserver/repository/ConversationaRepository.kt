package com.fsp.coreserver.repository

import com.fsp.coreserver.domain.Chat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConversationRepository : JpaRepository<Chat, Long> {
    fun findBySessionIdOrderByIdAsc(sessionId: String): List<Chat>
}

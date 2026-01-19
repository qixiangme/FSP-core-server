package com.fsp.coreserver.conversation

import org.springframework.data.jpa.repository.JpaRepository

interface ConversationRepository : JpaRepository<Conversation, Long> {
    fun findConversationById(id: Long): Conversation?
}
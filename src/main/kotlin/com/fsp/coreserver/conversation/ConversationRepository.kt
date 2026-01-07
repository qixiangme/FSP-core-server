package com.fsp.coreserver.conversation

import org.springframework.data.jpa.repository.JpaRepository

interface ConversationRepository : JpaRepository<Chat, Long> {
}
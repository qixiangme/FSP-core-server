package com.fsp.coreserver.conversation

import com.fsp.coreserver.conversation.enum.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table
data class Chat(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val sessionId: String,

    @Enumerated(EnumType.STRING)
    val role: Role, // "user" / "ai"

    @Column(columnDefinition = "TEXT")
    val content: String
)
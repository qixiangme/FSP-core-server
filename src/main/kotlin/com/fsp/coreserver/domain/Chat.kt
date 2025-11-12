package com.fsp.coreserver.domain

import com.fsp.coreserver.dto.Role
import jakarta.persistence.*

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

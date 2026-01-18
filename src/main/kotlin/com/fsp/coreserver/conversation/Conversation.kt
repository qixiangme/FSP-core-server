package com.fsp.coreserver.conversation

import com.fsp.coreserver.conversation.enum.Role
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
class Conversation(
    @Column(nullable = true)
    val userId: String,
    @Column(nullable = false)
    val poemId: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @OneToMany(
        mappedBy = "conversation",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    private val _chats: MutableList<Chat> = mutableListOf()

    val chats: List<Chat> get() = _chats.toList()

    // 편의 메서드: 양방향 관계를 한 번에 설정
    fun addChat(role: Role, content: String) {
        val chat = Chat(
            role = role,
            content = content
        )
        _chats.add(chat)
    }
}
package com.fsp.coreserver.conversation

import com.fsp.coreserver.conversation.chat.Chat
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Conversation(
    @Column(nullable = false)
    val userId: Long,
    @Column(nullable = false)
    val poemId: Long
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
    fun addChat(chat: Chat) {

        _chats.add(chat)
    }
}
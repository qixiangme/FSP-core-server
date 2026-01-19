package com.fsp.coreserver.conversation.chat

import com.fsp.coreserver.conversation.Conversation
import com.fsp.coreserver.conversation.enum.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table


@Entity
@Table(name = "chats") // 관례상 테이블명은 복수형을 많이 사용합니다
class Chat(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val role: Role, // "USER" / "ASSISTANT" 등
    content : String,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    val conversation: Conversation

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String = content // 업데이트 가능하도록 var로 변경
        private set

    fun updateContent(newContent: String) {
        this.content = newContent
    }
}
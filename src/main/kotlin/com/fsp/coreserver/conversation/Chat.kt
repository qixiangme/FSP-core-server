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
@Table(name = "chats") // 관례상 테이블명은 복수형을 많이 사용합니다
class Chat(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val role: Role, // "USER" / "ASSISTANT" 등
    content : String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String // 업데이트 가능하도록 var로 변경
        private set
    // 생성 시간 등을 관리하고 싶다면 아래와 같이 추가할 수 있습니다.
    // val createdAt: LocalDateTime = LocalDateTime.now()

    fun updateContent(newContent: String) {
        this.content = newContent
    }
}
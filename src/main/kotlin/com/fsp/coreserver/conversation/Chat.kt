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
@Table(name = "chat")
class Chat(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long = 0,

    private var sessionId: String,

    @Enumerated(EnumType.STRING)
    private var role: Role, // "user" / "ai"

    @Column(columnDefinition = "TEXT")
    private var content: String
) {
    // 읽기 전용 getter
    fun getId(): Long = id
    fun getSessionId(): String = sessionId
    fun getRole(): Role = role
    fun getContent(): String = content

    // content는 필요하면 업데이트 가능
    fun updateContent(newContent: String) {
        content = newContent
    }

    // sessionId와 role은 외부에서 변경할 필요가 거의 없으므로 setter 없음
}
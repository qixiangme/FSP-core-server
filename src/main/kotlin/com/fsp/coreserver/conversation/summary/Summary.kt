package com.fsp.coreserver.conversation.summary

import com.fsp.coreserver.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "summary")
class Summary(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long = 0,

    @Column(nullable = false, columnDefinition = "TEXT")
    private var content: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private var user: User,

    @Column(nullable = false, updatable = false)
    private var createdAt: LocalDateTime = LocalDateTime.now()

) {

    // 읽기 전용 getter
    fun getId(): Long = id
    fun getContent(): String = content
    fun getUser(): User = user
    fun getCreatedAt(): LocalDateTime = createdAt

    // content는 필요 시 수정 가능
    fun updateContent(newContent: String) {
        content = newContent
    }
}
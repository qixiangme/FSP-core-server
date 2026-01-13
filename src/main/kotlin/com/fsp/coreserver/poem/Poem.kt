package com.fsp.coreserver.poem

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Poem(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long = 0,

    @Column(nullable = false)
    private var title: String,

    @Column(nullable = false)
    private var author: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    private var content: String,
) {
    // 읽기 전용 getter
    fun getId(): Long = id
    fun getTitle(): String = title
    fun getAuthor(): String = author
    fun getContent(): String = content

    // 내용 수정 함수
    fun updateContent(newContent: String) {
        content = newContent
    }

    // 제목 수정 함수 필요하면 추가 가능
    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    // 작가는 보통 수정하지 않으므로 setter 없음
}
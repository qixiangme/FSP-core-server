package com.fsp.coreserver.poem

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Poem(
    title: String, // 초기화를 위해 생성자 인자로 받음
    author: String, // 불변
    content: String // 초기화를 위해 생성자 인자로 받음
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false)
    var author : String = author
        private set
    @Column(nullable = false)
    var title: String = title
        private set // 외부에서 'poem.title = ...' 금지

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String = content
        private set // 외부에서 'poem.content = ...' 금지

    // 반드시 이 메서드들을 통해서만 수정 가능 (도메인 로직 보호)
    fun updateContent(newContent: String) {
        // 수정 시 글자 수 제한 등 비즈니스 로직 추가 가능
        this.content = newContent
    }

    fun updateTitle(newTitle: String) {
        this.title = newTitle
    }
}
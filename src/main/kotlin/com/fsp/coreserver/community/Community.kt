package com.fsp.coreserver.community

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn

@Entity
class Community(
    title: String,
    content: String,

    @Column(nullable = false, updatable = false)
    val author: String,

    @ElementCollection
    @CollectionTable(
        name = "community_hashtags",
        joinColumns = [JoinColumn(name = "community_id")]
    )
    @Column(name = "hashtag")
    var hashtags: List<String> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false)
    var title: String = title
        private set // 본문으로 빼야 private set 설정이 가능합니다.

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String = content
        private set

    @Column(nullable = false)
    var likes: Int = 0
        private set

    // 비즈니스 로직
    fun like() {
        this.likes += 1
    }

    fun unlike() {
        if (this.likes > 0) this.likes -= 1
    }

    fun update(newTitle: String, newContent: String) {
        this.title = newTitle
        this.content = newContent
    }
}
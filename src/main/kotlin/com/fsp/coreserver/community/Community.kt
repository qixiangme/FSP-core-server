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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long = 0,

    @Column(nullable = false)
    private var title: String,

    @Column(nullable = false)
    private var author: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    private var content: String,

    @Column(nullable = false)
    private var likes: Int = 0,

    @ElementCollection
    @CollectionTable(
        name = "community_hashtags",
        joinColumns = [JoinColumn(name = "community_id")]
    )
    @Column(name = "hashtag")
    private var hashtags: List<String> = emptyList()
){
    fun getId(): Long = id
    fun getTitle(): String = title
    fun getAuthor(): String = author
    fun getLikes(): Int = likes
    fun getHashtags(): List<String> = hashtags
    fun getContent(): String = content

    fun like() {
        likes += 1
    }
    fun unlike() {
        if (likes > 0) likes -= 1
    }
    }
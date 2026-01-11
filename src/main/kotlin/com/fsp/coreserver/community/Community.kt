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
    val id: Long = 0,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val author: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Column(nullable = false)
    val likes: Int = 0,

    @ElementCollection
    @CollectionTable(
        name = "community_hashtags",
        joinColumns = [JoinColumn(name = "community_id")]
    )
    @Column(name = "hashtag")
    val hashtags: List<String> = emptyList()
)
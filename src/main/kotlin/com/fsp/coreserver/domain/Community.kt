package com.fsp.coreserver.domain

import jakarta.persistence.*

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
) {
    constructor() : this(0, "", "", "", 0, emptyList())
}

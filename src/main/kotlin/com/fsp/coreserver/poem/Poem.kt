package com.fsp.coreserver.poem

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
 class Poem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,

    @Column(nullable = false)
    val title : String,
    @Column(nullable = false)
    val author : String,
    @Column(nullable = false,columnDefinition = "TEXT")
    val content : String,
)
{
     constructor() : this(0, "", "", "")
}
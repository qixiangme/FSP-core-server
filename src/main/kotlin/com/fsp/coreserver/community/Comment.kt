package com.fsp.coreserver.community

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Comment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,

    @Column(nullable = false)
    val author : String,

    @Column(nullable = false,columnDefinition = "TEXT")
    val content : String,


) {

    constructor() : this(0, "", "")

}
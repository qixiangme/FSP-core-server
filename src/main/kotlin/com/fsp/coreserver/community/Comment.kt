package com.fsp.coreserver.community

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Comment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id : Long = 0,

    @Column(nullable = false)
    private var author : String,

    @Column(nullable = false,columnDefinition = "TEXT")
    private var content : String,
){
    fun getId() : Long = id
    fun getAuthor(): String = author
    fun getContent(): String  = content
}
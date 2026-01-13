package com.fsp.coreserver.poem

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PoemRepository: JpaRepository<Poem, Long> {
    fun findByAuthor(author: String): Optional<List<Poem>>
    fun findByTitle(title: String): Optional<List<Poem>>
    fun existsByTitle(title: String): Boolean
    fun existsByAuthor(author: String): Boolean
    fun existsByTitleAndAuthor(title: String, author: String): Boolean
}
package com.fsp.coreserver.repository

import com.fsp.coreserver.domain.Poem
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PoemRepository: JpaRepository<Poem, Long>{
    fun findByAuthor(author: String): Optional<List<Poem>>
    fun findByTitle(title: String): Optional<List<Poem>>
    fun existsByTitle(title: String): Boolean
    fun existsByAuthor(author: String): Boolean
}
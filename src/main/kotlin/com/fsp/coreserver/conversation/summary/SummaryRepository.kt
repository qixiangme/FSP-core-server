package com.fsp.coreserver.conversation.summary

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SummaryRepository : JpaRepository<Summary, Long> {

    @Query("""
         select s
        from Summary s
        join fetch s.user
    """)
    fun findAllWithUser(): List<Summary>
}
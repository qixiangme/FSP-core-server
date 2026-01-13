package com.fsp.coreserver.conversation.summary

import com.fsp.coreserver.conversation.summary.Summary
import com.fsp.coreserver.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SumarryRepository : JpaRepository<Summary, Long> {

    @Query("""
         select s
        from Summary s
        join fetch s.user
    """)
    fun findAllWithUser(): List<Summary>
}
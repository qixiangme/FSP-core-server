package com.fsp.coreserver.repository

import com.fsp.coreserver.domain.Summary
import com.fsp.coreserver.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface SummaryRepository : JpaRepository<Summary, Long> {
    fun findByUser(user: User): MutableList<Summary>
}
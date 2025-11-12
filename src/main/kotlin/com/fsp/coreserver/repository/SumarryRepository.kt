package com.fsp.coreserver.repository

import com.fsp.coreserver.domain.Summary
import com.fsp.coreserver.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface SumarryRepository : JpaRepository<Summary, Long> {
    fun user(user: User): MutableList<Summary>
}
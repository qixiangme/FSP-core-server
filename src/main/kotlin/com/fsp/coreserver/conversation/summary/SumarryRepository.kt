package com.fsp.coreserver.conversation.summary

import com.fsp.coreserver.conversation.summary.Summary
import com.fsp.coreserver.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface SumarryRepository : JpaRepository<Summary, Long> {
}
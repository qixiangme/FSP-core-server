package com.fsp.coreserver.repository

import com.fsp.coreserver.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email:String): Optional<User>
    fun existsByEmail(email: String): Boolean
}


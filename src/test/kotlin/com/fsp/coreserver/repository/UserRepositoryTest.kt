package com.fsp.coreserver.repository

import com.fsp.coreserver.user.User
import com.fsp.coreserver.user.UserRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@Transactional
open class UserRepositoryTest(
    @Autowired private val userRepository: UserRepository
) {

    @Test
    fun `유저 저장 후 조회`() {
        val user = User(email = "repo@test.com", password = "1234", name = "RepoUser")
        userRepository.save(user)

        val found = userRepository.findByEmail("repo@test.com")
        assertTrue(found.isPresent)
        assertEquals("RepoUser", found.get().name)
    }

    @Test
    fun `중복 이메일 저장 시 예외 발생`() {
        val user1 = User(email = "dup@test.com", password = "1234", name = "User1")
        val user2 = User(email = "dup@test.com", password = "5678", name = "User2")

        userRepository.save(user1)
        assertThrows(Exception::class.java) {
            userRepository.saveAndFlush(user2)
        }
    }
}
package com.fsp.coreserver.service

import com.fsp.coreserver.domain.User
import com.fsp.coreserver.dto.LoginRequest
import com.fsp.coreserver.dto.SignUpRequest
import com.fsp.coreserver.repository.UserRepository
import com.fsp.coreserver.security.JwtUtil
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.nio.charset.StandardCharsets
import kotlin.math.log

@SpringBootTest
@Transactional
open class UserServiceTest(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserRepository,
    @Autowired private var jwtUtil: JwtUtil
) {

    @BeforeEach
    fun setup() {
        userRepository.deleteAll()
    }

    @Test
    fun `회원가입 성공`() {
        val request = SignUpRequest(email = "service@test.com", password = "1234", name = "ServiceUser")
        val savedUser = userService.signup(request)

        val found = userRepository.findByEmail("service@test.com")
        assertTrue(found.isPresent)
        assertEquals("ServiceUser", found.get().name)
        assertNotEquals("1234", found.get().password) // 암호화 확인
    }

    @Test
    fun `로그인 성공`() {
        val password = "1234"
        val encrypted = BCryptPasswordEncoder().encode(password)
        userRepository.save(User(email = "login@test.com", password = encrypted, name = "LoginUser"))

        val loginResponse = userService.login(LoginRequest(email = "login@test.com", password = password))

        // LoginResponse 안에 토큰이 있다고 가정
        val token = loginResponse.accessToken
        assertFalse(token.isEmpty(), "토큰이 비어있으면 안 됩니다")

        // JwtUtil로 토큰 검증
        val emailFromToken = jwtUtil.getEmail(token)
        assertEquals("login@test.com", emailFromToken)
        assert(jwtUtil.validateToken(token))
    }
}
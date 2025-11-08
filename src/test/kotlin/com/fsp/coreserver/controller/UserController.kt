package com.fsp.coreserver.controller
import com.fasterxml.jackson.databind.ObjectMapper
import com.fsp.coreserver.dto.LoginRequest
import com.fsp.coreserver.dto.SignUpRequest
import com.fsp.coreserver.repository.UserRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
open class UserControllerTest(@Autowired private val userRepository: UserRepository) {
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @Test
    fun `회원가입 성공`(){
        val request = SignUpRequest(
            email = "tes212t@example.com",
            password = "1234",
            name = "테스트유저"
        )

        mockMvc.perform(
            post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `로그인 성공`() {
        val request = LoginRequest(
            email = "test@example.com",
            password = "1234"
        )

        mockMvc.perform(
            post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
    }
}

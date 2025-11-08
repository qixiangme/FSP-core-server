package com.fsp.coreserver.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fsp.coreserver.dto.PoemRequest
import com.fsp.coreserver.repository.PoemRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
open class PoemControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var poemRepository: PoemRepository

    @Test
    fun `시 생성 성공`() {
        val request = PoemRequest(
            title = "test title",
            content = "test content",
            author = "tester"
        )

        mockMvc.perform(
            post("/poem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `시 목록 조회 성공`() {
        // given: 테스트 데이터 삽입
        poemRepository.save(
            com.fsp.coreserver.domain.Poem(
                title = "시 제목",
                author = "작가",
                content = "내용"
            )
        )

        // when & then
        mockMvc.perform(get("/poem"))
            .andExpect(status().isOk)
    }
}
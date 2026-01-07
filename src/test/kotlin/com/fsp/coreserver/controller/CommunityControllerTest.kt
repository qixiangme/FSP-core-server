package com.fsp.coreserver.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fsp.coreserver.community.Community
import com.fsp.coreserver.community.CommunityRequest
import com.fsp.coreserver.community.CommunityRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
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
open class CommunityControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var communityRepository: CommunityRepository

    @BeforeEach
    fun setup() {
        communityRepository.deleteAll()
    }

    @Test
    fun `커뮤니티 글 생성 성공`() {
        val request = CommunityRequest(
            title = "테스트 글",
            author = "tester",
            content = "테스트 내용",
            hashtags = listOf("테스트", "커뮤니티")
        )

        mockMvc.perform(
            post("/communities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("테스트 글"))
            .andExpect(jsonPath("$.hashtags[0]").value("테스트"))
    }

    @Test
    fun `커뮤니티 전체 조회 성공`() {
        // given
        communityRepository.save(Community(title = "글1", author = "A", content = "내용1"))
        communityRepository.save(Community(title = "글2", author = "B", content = "내용2"))

        // when & then
        mockMvc.perform(get("/communities"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].title").value("글1"))
            .andExpect(jsonPath("$[1].title").value("글2"))
    }

    @Test
    fun `커뮤니티 단건 조회 성공`() {
        // given
        val community = communityRepository.save(
            Community(title = "단건 글", author = "C", content = "단건 내용")
        )

        // when & then
        mockMvc.perform(get("/communities/${community.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("단건 글"))
            .andExpect(jsonPath("$.author").value("C"))
    }

    @Test
    fun `좋아요 증가 성공`() {
        // given
        val community = communityRepository.save(
            Community(title = "좋아요 테스트", author = "D", content = "내용")
        )

        // when & then
        mockMvc.perform(post("/communities/${community.id}/like"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.likes").value(1))
    }
}
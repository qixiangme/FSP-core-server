package com.fsp.coreserver.service

import com.fsp.coreserver.poem.Poem
import com.fsp.coreserver.poem.PoemRequest
import com.fsp.coreserver.poem.PoemRepository
import com.fsp.coreserver.poem.PoemService
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
open class PoemServiceTest(
    @Autowired private val poemService: PoemService,
    @Autowired private val poemRepository: PoemRepository
) {

    @BeforeEach
    fun setup() {
        poemRepository.deleteAll()
    }

    @Test
    fun `시 생성 성공`() {
        // given
        val request = PoemRequest(
            title = "서비스 테스트 시",
            author = "테스트 작가",
            content = "이것은 서비스 계층 테스트입니다."
        )

        // when
        val savedPoem = poemService.createPoem(request)

        // then
        val found = poemRepository.findById(savedPoem.id)
        assertTrue(found.isPresent)
        assertEquals("서비스 테스트 시", found.get().getTitle())
        assertEquals("테스트 작가", found.get().getAuthor())
    }

    @Test
    fun `시 전체 조회 성공`() {
        // given
        poemRepository.save(Poem(title = "시1", author = "A", content = "내용1"))
        poemRepository.save(Poem(title = "시2", author = "B", content = "내용2"))

        // when
        val poems = poemService.getPoems()

        // then
        assertEquals(2, poems.size)
        assertTrue(poems.any { it.getTitle() == "시1" })
    }

    @Test
    fun `시 단건 조회 성공`() {
        // given
        val poem = poemRepository.save(Poem(title = "단건시", author = "C", content = "테스트 내용"))

        // when
        val found = poemService.getPoemDetail(poem.getId())

        // then
        assertEquals("단건시", found.getTitle())
        assertEquals("C", found.getAuthor())
    }
}
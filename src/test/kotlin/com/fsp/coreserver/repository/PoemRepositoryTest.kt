//package com.fsp.coreserver.repository
//
//import com.fsp.coreserver.poem.Poem
//import com.fsp.coreserver.user.User
//import com.fsp.coreserver.poem.PoemRepository
//import jakarta.transaction.Transactional
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
//
//
//@DataJpaTest
//@Transactional
//open class PoemRepositoryTest {
//    @Autowired
//    private lateinit var  poemRepository: PoemRepository
//
//    @Test
//    fun `시 저장 후 조회`(){
//        val poem = Poem(title = "test", content = "test", author = "test")
//        poemRepository.save(poem)
//        val found = poemRepository.findById(poem.getId())
//        assertTrue(found.isPresent)
//        assertEquals("test", found.get().getTitle())
//        assertEquals("test", found.get().getAuthor())
//    }
//
//
//    @Test
//    fun `모든 시 조회`() {
//        // given
//        poemRepository.save(Poem(title = "시1", author = "A", content = "내용1"))
//        poemRepository.save(Poem(title = "시2", author = "B", content = "내용2"))
//
//        // when
//        val poems = poemRepository.findAll()
//
//        // then
//        assertEquals(2, poems.size)
//    }
//    @Test
//    fun `특정 제목으로 시 조회`() {
//        // given
//        val poem = Poem(title = "특별한 시", author = "시인", content = "특별한 내용")
//        poemRepository.save(poem)
//
//        // when
//        val found = poemRepository.findAll().firstOrNull { it.getTitle() == "특별한 시" }
//
//        // then
//        assertNotNull(found)
//        assertEquals("시인", found?.getAuthor())
//    }
//}
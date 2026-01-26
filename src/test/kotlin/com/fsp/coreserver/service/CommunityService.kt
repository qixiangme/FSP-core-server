//package com.fsp.coreserver.service
//
//import com.fsp.coreserver.community.Community
//import com.fsp.coreserver.community.CommunityRequest
//import com.fsp.coreserver.community.CommunityRepository
//import com.fsp.coreserver.community.CommunityService
//import jakarta.transaction.Transactional
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//
//@SpringBootTest
//@Transactional
//open class CommunityServiceTest(
//    @Autowired private val communityService: CommunityService,
//    @Autowired private val communityRepository: CommunityRepository
//) {
//
//    @BeforeEach
//    fun setup() {
//        communityRepository.deleteAll()
//    }
//
//    @Test
//    fun `커뮤니티 글 생성 성공`() {
//        // given
//        val request = CommunityRequest(
//            title = "서비스 테스트 글",
//            author = "테스트 작성자",
//            content = "서비스 계층 테스트 중입니다.",
//            hashtags = listOf("테스트", "커뮤니티")
//        )
//
//        // when
//        val saved = communityService.createCommunity(request)
//
//        // then
//        val found = communityRepository.findById(saved.id)
//        assertTrue(found.isPresent)
//        assertEquals("서비스 테스트 글", found.get().getTitle())
//        assertEquals("테스트 작성자", found.get().getAuthor())
//        assertEquals(2, found.get().getHashtags().size)
//    }
//
//    @Test
//    fun `커뮤니티 전체 조회 성공`() {
//        // given
//        communityRepository.save(Community(title = "글1", author = "A", content = "내용1"))
//        communityRepository.save(Community(title = "글2", author = "B", content = "내용2"))
//
//        // when
//        val list = communityService.getAllCommunities()
//
//        // then
//        assertEquals(2, list.size)
//        assertTrue(list.any { it.title == "글1" })
//        assertTrue(list.any { it.title == "글2" })
//    }
//
//    @Test
//    fun `커뮤니티 단건 조회 성공`() {
//        // given
//        val community = communityRepository.save(Community(title = "단건 글", author = "C", content = "테스트 내용"))
//
//        // when
//        val found = communityService.getCommunityById(community.getId())
//
//        // then
//        assertEquals("단건 글", found.title)
//        assertEquals("C", found.author)
//    }
//
//    @Test
//    fun `좋아요 증가 성공`() {
//        // given
//        val community = communityRepository.save(Community(title = "좋아요 테스트", author = "D", content = "내용"))
//
//        // when
//        val updated = communityService.addLike(community.getId())
//
//        // then
//        assertEquals(1, updated.likes)
//    }
//}
package com.fsp.coreserver.repository

import com.fsp.coreserver.community.CommunityRepository
import com.fsp.coreserver.community.Community
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
open class CommunityRepositoryTest @Autowired constructor(
    val communityRepository: CommunityRepository
) {

    @Test
    fun `커뮤니티 저장 후 조회`() {
        val community = Community(
            title = "테스트 글",
            author = "tester",
            content = "내용 테스트",
            hashtags = listOf("테스트", "해시태그")
        )

        val saved = communityRepository.save(community)
        val found = communityRepository.findById(saved.id).get()

        assertEquals(saved.title, found.title)
        assertEquals(saved.hashtags.size, found.hashtags.size)
    }
}
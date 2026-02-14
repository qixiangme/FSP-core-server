package com.fsp.coreserver

import com.fsp.coreserver.conversation.ConversationService
import com.fsp.coreserver.conversation.chat.SessionConversation
import com.fsp.coreserver.poem.PoemRequest
import com.fsp.coreserver.poem.PoemService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate

@SpringBootTest
class RedisIntegrationTest {

    @Autowired
    lateinit var poemService: PoemService

    @Autowired
    lateinit var conversationService: ConversationService

    // 명시적으로 본인이 만든 빈 주입 확인
    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, Any>

    @BeforeEach
    fun setup() {
        // 기존 잘못 저장된 (타입 정보 없는) 데이터 완전 삭제
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

    @Test
    @DisplayName("Poem 상세 조회 시 캐시가 적용되는지 확인")
    fun poemCacheTest() {
        val request = PoemRequest("서시", "죽는 날까지...", "윤동주")
        val savedPoem = poemService.createPoem(request)
        val id = savedPoem.id!!

        // 1. Service 호출 (이때 @Cacheable에 의해 Redis에 저장됨)
        poemService.getPoemDetail(id)

        // 2. 직접 조회 시도
        val cacheKey = "poem::$id"

        // 여기서 에러가 난다면, @Cacheable이 쓰는 Serializer와 이 template이 쓰는 놈이 다르다는 뜻입니다.
        val cachedData = redisTemplate.opsForValue().get(cacheKey)

        assertThat(cachedData).isNotNull
        println("캐시 성공: $cachedData")
    }

    @Test
    @DisplayName("Conversation 세션 역직렬화 테스트")
    fun conversationRedisTest() {
        val conversationId = 100L
        val session = SessionConversation(userId = 1L, poemId = 1L)

        // 직접 저장
        redisTemplate.opsForValue().set(conversationId.toString(), session)

        // 다시 꺼낼 때 @class 정보를 보고 SessionConversation으로 변환 시도
        val pulledData = redisTemplate.opsForValue().get(conversationId.toString())

        assertThat(pulledData).isInstanceOf(SessionConversation::class.java)
    }
}
package com.fsp.coreserver.config

import org.springframework.cache.Cache
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.CacheErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@EnableCaching
@Configuration
class CacheConfig {

    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): RedisCacheManager {
        val config = RedisCacheConfiguration
            .defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5))
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    StringRedisSerializer()
                )
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer()
                )
            )

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build()
    }

    @Bean
    fun cacheErrorHandler(): CacheErrorHandler = object : CacheErrorHandler {
        override fun handleCacheGetError(exception: RuntimeException, cache: Cache, key: Any) {
            // Redis 장애 시 캐시를 우회하고 비즈니스 로직은 계속 진행
        }

        override fun handleCachePutError(exception: RuntimeException, cache: Cache, key: Any, value: Any?) {
            // Redis 장애 시 캐시 저장 실패 무시
        }

        override fun handleCacheEvictError(exception: RuntimeException, cache: Cache, key: Any) {
            // Redis 장애 시 캐시 삭제 실패 무시
        }

        override fun handleCacheClearError(exception: RuntimeException, cache: Cache) {
            // Redis 장애 시 캐시 초기화 실패 무시
        }
    }
}

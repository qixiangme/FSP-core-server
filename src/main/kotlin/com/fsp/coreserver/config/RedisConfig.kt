package com.fsp.coreserver.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig {

    @Bean
    fun commonSerializer(): GenericJackson2JsonRedisSerializer {
        val mapper = jacksonObjectMapper()
            .registerModule(kotlinModule())
            .registerModule(JavaTimeModule())
            // (중요) 클래스 타입 정보를 JSON에 포함시키는 핵심 설정
            .activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.EVERYTHING, // 혹은 NON_FINAL
                JsonTypeInfo.As.PROPERTY
            )

        return GenericJackson2JsonRedisSerializer(mapper)
    }

    @Bean
    fun redisTemplate(
        connectionFactory: RedisConnectionFactory,
        commonSerializer: GenericJackson2JsonRedisSerializer
    ): RedisTemplate<String, Any> {
        return RedisTemplate<String, Any>().apply {
            setConnectionFactory(connectionFactory)
            keySerializer = StringRedisSerializer()
            valueSerializer = commonSerializer
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = commonSerializer
            afterPropertiesSet()
        }
    }
    @Bean
    fun cacheManager(
        connectionFactory: RedisConnectionFactory,
        commonSerializer: GenericJackson2JsonRedisSerializer
    ): RedisCacheManager {
        val config = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer())
            )
            .serializeValuesWith(
                // 여기서 commonSerializer를 직접 박아넣는게 핵심임
                RedisSerializationContext.SerializationPair.fromSerializer(commonSerializer)
            )

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build()
    }
}
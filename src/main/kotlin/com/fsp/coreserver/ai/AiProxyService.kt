package com.fsp.coreserver.ai

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class AiProxyService(
    private val webClient: WebClient
) {
    fun interpret(poem : String): String{
        return webClient.post()
            .uri("http://ai-service:8081/api/interpret")
            .bodyValue(mapOf("poem" to poem))
            .retrieve()
            .bodyToMono(String::class.java)
            .block() ?: "AI 응답 없음"
    }
}
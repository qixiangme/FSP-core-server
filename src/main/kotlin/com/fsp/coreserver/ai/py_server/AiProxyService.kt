package com.fsp.coreserver.ai.py_server

import com.fsp.coreserver.conversation.ChatMessageResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class AiProxyService(
    private val webClient: WebClient
) {
    fun elaborate(text: String): String {
        return webClient.post()
            .uri("http://aiserver:8000/api/elaborate") // ✅ 컨테이너 내부 포트와 이름
            .bodyValue(mapOf("text" to text))
            .retrieve()
            .bodyToMono(String::class.java)
            .block() ?: "AI 응답 없음"
    }

    fun summarize(conversation: List<ChatMessageResponse>): String {
        return webClient.post()
            .uri("http://aiserver:8000/api/summarize")
            .bodyValue(conversation)
            .retrieve()
            .bodyToMono(String::class.java)
            .block() ?: "AI 응답 없음"
    }
}
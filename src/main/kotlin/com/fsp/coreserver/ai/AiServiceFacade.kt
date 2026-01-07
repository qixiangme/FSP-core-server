package com.fsp.coreserver.ai

import org.springframework.stereotype.Service

@Service
class AiServiceFacade(
    private val aiProxyService: AiProxyService
) {
    fun elaborateText(text: String): String = aiProxyService.elaborate(text)
    fun summarizeConversation(conversation: List<Map<String, String>>): String =
        aiProxyService.summarize(conversation)
}
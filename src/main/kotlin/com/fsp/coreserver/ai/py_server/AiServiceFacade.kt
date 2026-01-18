package com.fsp.coreserver.ai.py_server

import com.fsp.coreserver.conversation.ChatMessageResponse
import org.springframework.stereotype.Service

@Service
class AiServiceFacade(
    private val aiProxyService: AiProxyService
) {
    fun elaborateText(text: String): String = aiProxyService.elaborate(text)
    fun summarizeConversation(conversation: List<ChatMessageResponse>): String =
        aiProxyService.summarize(conversation)
}
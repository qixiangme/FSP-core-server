package com.fsp.coreserver.ai.py_server

import com.fsp.coreserver.conversation.elaborate.ElaborateResponse
import org.springframework.stereotype.Service

@Service
class AiServiceFacade(
    private val aiProxyService: AiProxyService
) {
    fun elaborateText(text: String): String = aiProxyService.elaborate(text)
    fun summarizeConversation(conversation: List<ElaborateResponse>): String =
        aiProxyService.summarize(conversation)
}
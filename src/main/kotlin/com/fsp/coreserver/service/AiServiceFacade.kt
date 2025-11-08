package com.fsp.coreserver.service

import com.fsp.coreserver.ai.AiProxyService
import org.springframework.stereotype.Service

@Service
class AiServiceFacade(
    private val aiProxyService: AiProxyService
) {
    fun analyzePoem(poemText: String): String {
        return aiProxyService.interpret(poemText)
    }
}
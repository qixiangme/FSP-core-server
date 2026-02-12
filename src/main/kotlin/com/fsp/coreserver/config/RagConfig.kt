package com.fsp.coreserver.config

import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.vectorstore.SimpleVectorStore
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RagConfig {
    @Bean
    open fun vectorStore(embeddingModel: EmbeddingModel): VectorStore {
        return SimpleVectorStore(embeddingModel)
    }
}

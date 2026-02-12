package com.fsp.coreserver.ai.rag

import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service

@Service
class RagRetriever(
    private val vectorStore: VectorStore
) {
    fun retrieve(question: String, topK: Int = 4, similarityThreshold: Double = 0.55): List<Document> {
        val request = SearchRequest.builder()
            .query(question)
            .topK(topK)
            .similarityThreshold(similarityThreshold)
            .build()

        return vectorStore.similaritySearch(request) ?: emptyList()
    }

    fun retrieveContext(question: String, topK: Int = 4): String {
        val docs = retrieve(question, topK)
        if (docs.isEmpty()) {
            return ""
        }

        return docs.joinToString("\n\n---\n\n") {
            it.text + "\n[meta] title=" + (it.metadata["title"] ?: "unknown") + ", author=" + (it.metadata["author"] ?: "unknown")
        }
    }
}

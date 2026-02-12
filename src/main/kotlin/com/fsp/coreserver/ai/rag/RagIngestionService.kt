package com.fsp.coreserver.ai.rag

import com.fsp.coreserver.poem.Poem
import com.fsp.coreserver.poem.PoemRepository
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RagIngestionService(
    private val poemRepository: PoemRepository,
    private val vectorStore: VectorStore
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun bootstrapIndex() {
        runCatching { reindexAll() }
            .onFailure { ex ->
                log.warn("RAG bootstrap indexing failed. service will continue without fresh index", ex)
            }
    }

    @Transactional(readOnly = true)
    fun reindexAll() {
        val docs = poemRepository.findAll().map { poem -> toDocument(poem) }
        if (docs.isEmpty()) {
            log.info("RAG indexing skipped - no poem data")
            return
        }

        runCatching { vectorStore.add(docs) }
            .onSuccess { log.info("RAG indexing completed - {} poems indexed", docs.size) }
            .onFailure { ex ->
                log.warn("RAG full indexing failed. docs={}", docs.size, ex)
            }
    }

    fun indexPoem(poem: Poem) {
        vectorStore.add(listOf(toDocument(poem)))
    }

    private fun toDocument(poem: Poem): Document {
        val content = "제목: ${poem.title}\n작가: ${poem.author}\n본문: ${poem.content}"
        return Document.builder()
            .id("poem-${poem.id}")
            .text(content)
            .metadata(
                mapOf(
                    "poemId" to poem.id,
                    "title" to poem.title,
                    "author" to poem.author
                )
            )
            .build()
    }
}

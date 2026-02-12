package com.fsp.coreserver.poem

import com.fsp.coreserver.ai.rag.RagIngestionService
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class PoemService(
    private val poemRepository: PoemRepository,
    private val ragIngestionService: RagIngestionService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @CacheEvict(cacheNames = ["poem"], allEntries = true)
    fun createPoem(request: PoemRequest): PoemResponse {
        if (poemRepository.existsByTitleAndAuthor(request.title, request.author)) {
            throw DuplicatePoemException("이미 존재하는 시입니다.")
        }

        val poem = Poem(
            title = request.title,
            content = request.content,
            author = request.author
        )

        val saved = poemRepository.save(poem)

        runCatching { ragIngestionService.indexPoem(saved) }
            .onFailure { ex ->
                log.warn("Poem created but RAG indexing failed. poemId={}", saved.id, ex)
            }

        return PoemResponse(
            id = saved.id,
            title = saved.title,
            content = saved.content,
            author = saved.author
        )
    }

    @Cacheable(cacheNames = ["poem"], key = "#id")
    fun getPoemDetail(id: Long): Poem {
        return poemRepository.findById(id).orElseThrow {
            IllegalArgumentException("해당 ID의 시가 없습니다: $id")
        }
    }

    @Cacheable(cacheNames = ["poem"], key = "'all'")
    fun getPoems(): List<Poem> {
        return poemRepository.findAll()
    }
}

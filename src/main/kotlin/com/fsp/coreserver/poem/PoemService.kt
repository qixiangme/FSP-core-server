package com.fsp.coreserver.poem

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.Optional


@Service
class PoemService(
    private val poemRepository: PoemRepository,
) {

    @CacheEvict(cacheNames = ["poem"], allEntries = true)
    fun createPoem(request: PoemRequest): PoemResponse {
        if (poemRepository.existsByTitleAndAuthor(request.title, request.author)) {
            throw RuntimeException("이미 존재하는 시입니다.")
        }

        val poem = Poem(
            title = request.title,
            content = request.content,
            author = request.author,
            public = request.public

        )

        val saved = poemRepository.save(poem)

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

    fun getPublicPoems() : Optional<List<Poem>>{
        return poemRepository.findPoemsByPublic(public = true)
    }
    fun getPrivatePoems() : Optional<List<Poem>>{
        return poemRepository.findPoemsByPublic(public = false)
    }
    fun increaseView(poem: Poem) {
        return poem.increaseView()
    }
}

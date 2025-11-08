package com.fsp.coreserver.service

import com.fsp.coreserver.domain.Poem
import com.fsp.coreserver.dto.PoemRequest
import com.fsp.coreserver.dto.PoemResponse
import com.fsp.coreserver.repository.PoemRepository
import org.hibernate.query.Page
import org.springframework.stereotype.Service
import java.awt.print.Pageable

@Service
class PoemService(
    private val poemRepositorty: PoemRepository,
){
    fun createPoem(request: PoemRequest): PoemResponse{
        if(poemRepositorty.existsByTitle(request.title) && poemRepositorty.existsByAuthor(request.author)){
            throw RuntimeException("이미 존재하는 시입니다.")
        }
        val poem = Poem(
            title = request.title,
            content = request.content,
            author = request.author
        )
        val saved = poemRepositorty.save(poem)

        return PoemResponse(
            id = saved.id,
            title = saved.title,
            content = saved.content,
            author = saved.author
        )
    }

    fun getPoemDetail(id: Long): Poem{
        return poemRepositorty.findById(id).orElseThrow {
            IllegalArgumentException("해당 ID의 시가 없습니다: $id")
        }
    }

    fun getPoems(): List<Poem> {
        return poemRepositorty.findAll()
    }


}
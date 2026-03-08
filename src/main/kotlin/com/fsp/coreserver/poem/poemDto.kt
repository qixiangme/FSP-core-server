package com.fsp.coreserver.poem

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PoemRequest(
    @field:NotBlank
    @field:Size(max = 120)
    val title: String,
    @field:NotBlank
    @field:Size(max = 10000)
    val content: String,
    @field:NotBlank
    @field:Size(max = 50)
    val author: String,

    val public: Boolean,
)

data class PoemResponse(val title: String, val content: String, val author: String, val id: Long)
package com.fsp.coreserver.dto

data class PoemRequest(val title: String, val content: String, val author : String,   )
data class PoemResponse(val title: String, val content: String, val author : String,val id: Long)
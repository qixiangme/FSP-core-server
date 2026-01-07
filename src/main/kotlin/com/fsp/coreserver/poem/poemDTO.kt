package com.fsp.coreserver.poem

data class PoemRequest(val title: String, val content: String, val author : String,   )
data class PoemResponse(val title: String, val content: String, val author : String,val id: Long)
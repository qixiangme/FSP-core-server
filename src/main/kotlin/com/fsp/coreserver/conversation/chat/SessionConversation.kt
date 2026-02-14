package com.fsp.coreserver.conversation.chat

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fsp.coreserver.conversation.enum.Role

data class SessionConversation @JsonCreator constructor(
    @JsonProperty("userId") val userId: Long = 0,
    @JsonProperty("poemId") val poemId: Long = 0,
    @JsonProperty("chats") val chats: MutableList<SessionChat> = mutableListOf()
)

data class SessionChat @JsonCreator constructor(
    @JsonProperty("role") val role: Role = Role.USER,
    @JsonProperty("content") val content: String = ""
)


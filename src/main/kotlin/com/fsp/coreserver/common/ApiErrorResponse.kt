package com.fsp.coreserver.common

import java.time.Instant

data class ApiErrorResponse(
    val code: String,
    val message: String,
    val timestamp: Instant = Instant.now(),
    val details: List<String> = emptyList()
)
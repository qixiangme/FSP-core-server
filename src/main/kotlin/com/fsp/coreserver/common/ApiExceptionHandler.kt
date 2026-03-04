package com.fsp.coreserver.common


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiErrorResponse(code = "BAD_REQUEST", message = ex.message ?: "잘못된 요청입니다."))

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiErrorResponse(code = "BUSINESS_ERROR", message = ex.message ?: "요청을 처리할 수 없습니다."))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiErrorResponse> {
        val fieldErrors = ex.bindingResult.allErrors
            .filterIsInstance<FieldError>()
            .map { "${it.field}: ${it.defaultMessage}" }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ApiErrorResponse(
                    code = "VALIDATION_ERROR",
                    message = "요청 값 검증에 실패했습니다.",
                    details = fieldErrors
                )
            )
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiErrorResponse(code = "FORBIDDEN", message = "접근 권한이 없습니다."))

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiErrorResponse(code = "INTERNAL_ERROR", message = "서버 내부 오류가 발생했습니다."))
}
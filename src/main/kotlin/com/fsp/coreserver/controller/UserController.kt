package com.fsp.coreserver.controller

import com.fsp.coreserver.dto.LoginRequest
import com.fsp.coreserver.dto.LoginResponse
import com.fsp.coreserver.dto.SignUpRequest
import com.fsp.coreserver.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/signup")
    fun signup(@RequestBody request: SignUpRequest): ResponseEntity<LoginResponse> {
        val response = userService.signup(request)
        return ResponseEntity.ok(response)
    }
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = userService.login(request)
        return ResponseEntity.ok(response)
    }

}


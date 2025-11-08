package com.fsp.coreserver.dto

data class SignUpRequest(val email: String,val password:String, val name: String)
data class LoginRequest(val email:String, val password: String)
data class LoginResponse(val accessToken: String, val refreshToken: String)


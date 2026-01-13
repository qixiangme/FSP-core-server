package com.fsp.coreserver.user

import com.fsp.coreserver.security.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {
    fun signup(request: SignUpRequest): LoginResponse {
        if(userRepository.existsByEmail(request.email)) {
            throw RuntimeException("이미 존재하는 이메일")
        }
        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name
        )
        userRepository.save(user)
        val accessToken = jwtUtil.generateToken(user.getEmail())
        val refreshToken = jwtUtil.generateToken(user.getEmail())

        return LoginResponse(accessToken, refreshToken)
    }

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(email = request.email)
            .orElseThrow { RuntimeException("이메일이 존재하지 않습니다.") }

        if(!passwordEncoder.matches(request.password,user.getPassword())){
            throw RuntimeException("비밀번호가 일치하지 않습니다.")
        }

        val accessToken = jwtUtil.generateToken(user.getEmail())
        val refreshToken = jwtUtil.generateRefreshToken(user.getEmail())

        return LoginResponse(accessToken, refreshToken)
    }

    fun getUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { RuntimeException("유저를 찾을 수 없습니다. ID: $userId") }
    }

}
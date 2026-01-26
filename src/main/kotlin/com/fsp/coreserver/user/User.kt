package com.fsp.coreserver.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
@Table(name = "users")
class User(
    email: String,
    password: String,
    name: String // 이름도 생성 시점에 필요하므로 추가
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(unique = true, nullable = false)
    var email: String = email
        private set

    @Column(nullable = false)
    private var password: String = password // 생성자 인자로 초기화

    @Column(nullable = false)
    var name: String = name
        private set

    /**
     * 비밀번호 변경 (암호화된 비밀번호가 들어온다고 가정)
     */
    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }

    /**
     * 비밀번호 확인
     */
    fun checkPassword(input: String, passwordEncoder: PasswordEncoder): Boolean {
        return passwordEncoder.matches(input, this.password)
    }

    /**
     * 이름 변경 기능이 필요하다면 추가
     */
    fun updateName(newName: String) {
        this.name = newName
    }
}
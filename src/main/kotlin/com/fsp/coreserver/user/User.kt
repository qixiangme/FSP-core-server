package com.fsp.coreserver.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "USERS")
@Entity
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long = 0,

    @Column(unique = true, nullable = false)
    private var email: String,

    @Column(nullable = false)
    private var password: String,

    @Column(nullable = false)
    private var name: String,
) {

    // id는 변경 불가
    fun getId(): Long = id

    fun getEmail(): String = email
    fun getName(): String = name
    fun getPassword(): String = password

    // password 변경만 함수로 허용
    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun checkPassword(input: String): Boolean = password == input
}

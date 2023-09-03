package com.ssuspot.auth.model.entity

import jakarta.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "accounts")
class Account(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id:Long? = null,
        @field:NotNull
        @Column(length = 64, unique = true)
        var userId: String,
        @field:NotNull
        @Column(length = 80, unique = true)
        var email: String,
        @field:NotNull
        var password: String,
        @field:NotNull
        var refreshToken: String,
){
    fun renewRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }
}
package com.ssuspot.auth.model.entity

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "accounts")
class Account(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @field:NotNull
        @field:Column(length = 80, unique = true)
        var email: String,

        @field:NotNull
        @field:Column(length = 100)
        var password: String,

        @field:NotNull
        @field:Column(length = 100)
        var refreshToken: String,

        @field:OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], orphanRemoval = true)
        var serviceClients: MutableList<AccountServiceClient> = mutableListOf()
) {
    fun renewRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    fun addServiceClient(accountServiceClient: AccountServiceClient) {
        serviceClients.add(accountServiceClient)
        accountServiceClient.account = this
    }
}
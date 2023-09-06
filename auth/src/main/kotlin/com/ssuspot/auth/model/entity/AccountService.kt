package com.ssuspot.auth.model.entity

import javax.persistence.*

@Entity
@Table(name = "account_services")
class AccountService(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "account_id")
    val account: Account,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "service_id")
    val service: Service
)

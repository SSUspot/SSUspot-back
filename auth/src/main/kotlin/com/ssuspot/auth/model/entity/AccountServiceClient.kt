package com.ssuspot.auth.model.entity

import javax.persistence.*

@Entity
@Table(name = "account_service_clients")
class AccountServiceClient(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "account_id")
    var account: Account,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "service_id")
    var serviceClient: ServiceClient
)

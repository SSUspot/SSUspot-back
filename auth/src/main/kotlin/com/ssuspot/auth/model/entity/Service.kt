package com.ssuspot.auth.model.entity

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "services")
class Service(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotNull
    @Column(length = 100, unique = true)
    var serviceName: String,

    @field:NotNull
    @field:Lob
    @Column(length = 100, unique = true)
    var serviceDescription:String,
)
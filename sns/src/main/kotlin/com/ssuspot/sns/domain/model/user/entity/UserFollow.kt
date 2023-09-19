package com.ssuspot.sns.domain.model.user.entity

import javax.persistence.*

@Entity
@Table(name = "user_follows")
class UserFollow(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "follower_id")
    val follower: User,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "followee_id")
    val followee: User
)
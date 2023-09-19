package com.ssuspot.sns.domain.model.user.entity

import com.ssuspot.sns.domain.model.post.entity.Comment
import com.ssuspot.sns.domain.model.common.BaseTimeEntity
import com.ssuspot.sns.domain.model.post.entity.Like
import com.ssuspot.sns.domain.model.post.entity.Post
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
        name = "users",
        //id를 기준으로 unique index를 생성한다.
        uniqueConstraints = [
            UniqueConstraint(name = "user_name_unique", columnNames = ["user_name"]),
            UniqueConstraint(name = "email_unique", columnNames = ["email"]),
            UniqueConstraint(name = "nickname_unique", columnNames = ["nickname"]),
        ],
        indexes = [
            Index(name = "user_name_index", columnList = "user_name"),
            Index(name = "email_index", columnList = "email"),
            Index(name = "nickname_index", columnList = "nickname"),
        ]
)
class User(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @field:NotNull
        @field:Column(length = 64, name = "user_name", unique = true)
        var userName: String,

        @field:NotNull
        @field:Column(name = "password")
        var password: String,

        @field:NotNull
        @field:Column(length = 64, name = "email", unique = true)
        var email: String,

        @field:NotNull
        @field:Column(length = 64, name = "nickname", unique = true)
        var nickname: String,

        @field:Column(name = "profile_message")
        var profileMessage: String?,

        @field:Column(name = "profile_image_link")
        var profileImageLink: String?,

        // spring cache 이용해 refresh token 저장

        @field:OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        val posts: MutableList<Post> = mutableListOf(),

        @field:OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        val comments: MutableList<Comment> = mutableListOf(),

        @field:OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        val likes: MutableList<Like> = mutableListOf()

): BaseTimeEntity(){
        fun updateNickname(newNickname: String) {
                this.nickname = newNickname
        }
}
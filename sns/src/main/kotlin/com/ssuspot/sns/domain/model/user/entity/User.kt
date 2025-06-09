package com.ssuspot.sns.domain.model.user.entity

import com.ssuspot.sns.application.dto.user.UserResponseDto
import com.ssuspot.sns.domain.model.post.entity.Comment
import com.ssuspot.sns.domain.model.common.BaseTimeEntity
import com.ssuspot.sns.domain.model.post.entity.PostLike
import com.ssuspot.sns.domain.model.post.entity.Post
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.BatchSize

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
        @field:Column(length = 64, name = "nickname")
        var nickname: String,

        @field:Column(name = "profile_message")
        var profileMessage: String?,

        @field:Column(name = "profile_image_link")
        var profileImageLink: String?,

        @field:OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        @field:BatchSize(size = 20)
        val posts: MutableList<Post> = mutableListOf(),

        @field:OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        @field:BatchSize(size = 20)
        val comments: MutableList<Comment> = mutableListOf(),

        @field:OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        @field:BatchSize(size = 20)
        val postLikes: MutableList<PostLike> = mutableListOf(),

        @field:OneToMany(mappedBy = "followingUser", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        @field:BatchSize(size = 20)
        val following: MutableList<UserFollow> = mutableListOf(),

        @field:OneToMany(mappedBy = "followedUser", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        @field:BatchSize(size = 20)
        val followers: MutableList<UserFollow> = mutableListOf(),

): BaseTimeEntity(){
        fun updateNickname(newNickname: String) {
                this.nickname = newNickname
        }
        fun updateProfileMessage(newProfileMessage: String?) {
                this.profileMessage = newProfileMessage
        }
        fun updateProfileImageLink(newProfileImageLink: String?) {
                this.profileImageLink = newProfileImageLink
        }

        fun toDto(): UserResponseDto {
                return UserResponseDto(
                        id = this.id!!,
                        email = this.email,
                        userName = this.userName,
                        nickname = this.nickname,
                        profileMessage = this.profileMessage,
                        profileImageLink = this.profileImageLink
                )
        }
}
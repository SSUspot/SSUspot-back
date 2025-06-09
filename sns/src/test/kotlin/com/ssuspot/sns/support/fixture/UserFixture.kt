package com.ssuspot.sns.support.fixture

import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.application.dto.user.RegisterDto
import com.ssuspot.sns.application.dto.user.UserResponseDto

object UserFixture {
    
    fun createUser(
        id: Long? = 1L,
        userName: String = "testuser",
        email: String = "test@example.com",
        password: String = "password123",
        nickname: String = "Test User",
        profileMessage: String? = "Hello, I'm a test user",
        profileImageLink: String? = "https://example.com/profile.jpg"
    ): User {
        return User(
            id = id,
            userName = userName,
            email = email,
            password = password,
            nickname = nickname,
            profileMessage = profileMessage,
            profileImageLink = profileImageLink
        )
    }
    
    fun createRegisterDto(
        email: String = "test@example.com",
        password: String = "password123",
        userName: String = "testuser",
        nickname: String = "Test User",
        profileMessage: String? = "Hello, I'm a test user",
        profileImageLink: String? = "https://example.com/profile.jpg"
    ): RegisterDto {
        return RegisterDto(
            email = email,
            password = password,
            userName = userName,
            nickname = nickname,
            profileMessage = profileMessage,
            profileImageLink = profileImageLink
        )
    }
    
    fun createUserResponseDto(
        id: Long = 1L,
        email: String = "test@example.com",
        userName: String = "testuser",
        nickname: String = "Test User",
        profileMessage: String? = "Hello, I'm a test user",
        profileImageLink: String? = "https://example.com/profile.jpg"
    ): UserResponseDto {
        return UserResponseDto(
            id = id,
            email = email,
            userName = userName,
            nickname = nickname,
            profileMessage = profileMessage,
            profileImageLink = profileImageLink
        )
    }
    
    fun createUsers(count: Int): List<User> {
        return (1..count).map { i ->
            createUser(
                id = i.toLong(),
                userName = "testuser$i",
                email = "test$i@example.com",
                nickname = "Test User $i"
            )
        }
    }
}
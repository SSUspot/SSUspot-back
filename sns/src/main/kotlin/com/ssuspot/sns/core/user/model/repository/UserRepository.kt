package com.ssuspot.sns.core.user.model.repository

import com.ssuspot.sns.core.user.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository:JpaRepository<User, Long> {
}
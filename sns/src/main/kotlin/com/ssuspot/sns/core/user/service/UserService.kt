package com.ssuspot.sns.core.user.service

import com.ssuspot.sns.core.user.model.dto.RegisterDto
import com.ssuspot.sns.core.user.model.entity.User
import com.ssuspot.sns.core.user.model.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
        val userRepository: UserRepository,
) {
    @Transactional(rollbackFor = [Exception::class])
    fun createUser(registerDto: RegisterDto){
        //TODO: password encoder 적용, refreshtoken 적용
        userRepository.save(
                User(
                        userName = registerDto.userName,
                        email = registerDto.email,
                        password = registerDto.password,
                        nickname = registerDto.nickname,
                        profileMessage = registerDto.profileMessage,
                        profileImageLink = registerDto.profileImageLink
                )
        )
    }


}
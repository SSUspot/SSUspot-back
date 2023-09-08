package com.ssuspot.auth.service

import com.ssuspot.auth.model.entity.Account
import com.ssuspot.auth.model.repository.AccountRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AccountService(
        val accountRepository: AccountRepository,
) {
    @Transactional
    fun createAccount(account: Account): Account {
        return accountRepository.save(account)
    }
}
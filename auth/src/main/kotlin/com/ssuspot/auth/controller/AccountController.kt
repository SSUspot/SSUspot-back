package com.ssuspot.auth.controller

import com.ssuspot.auth.request.AccountCreateRequest
import com.ssuspot.auth.service.AccountService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/account")
class AccountController(
        val accountService: AccountService
) {
    @PostMapping
    fun createAccount(
            @RequestBody accountCreateRequest: AccountCreateRequest
    ) {
        accountService.createAccount(accountCreateRequest)
    }
}
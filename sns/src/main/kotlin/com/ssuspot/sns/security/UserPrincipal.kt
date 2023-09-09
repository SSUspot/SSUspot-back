package com.ssuspot.sns.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
        private val email: String
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf()
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return email
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }
}
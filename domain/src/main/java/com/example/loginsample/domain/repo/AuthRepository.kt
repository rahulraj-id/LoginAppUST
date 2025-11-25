package com.example.loginsample.domain.repo

interface AuthRepository {
    fun startLogin(state: String)
    fun handleCode(code: String): Boolean
    fun silentAuth(): Boolean
    fun logout()
    fun hasToken(): Boolean
}

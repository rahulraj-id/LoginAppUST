package com.example.loginsample.domain.usecase

import com.example.loginsample.domain.repo.AuthRepository
import javax.inject.Inject

class SilentAuthUseCase @Inject constructor(private val repo: AuthRepository) {
    fun hasToken(): Boolean = repo.hasToken()
    fun refresh(): Boolean = repo.silentAuth()
    fun logout() = repo.logout()
}

package com.example.loginsample.domain.usecase

import com.example.loginsample.domain.repo.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repo: AuthRepository) {
    fun start(state: String) = repo.startLogin(state)
    fun handle(code: String): Boolean = repo.handleCode(code)
}

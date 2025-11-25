package com.example.loginsample.presentation.auth

import androidx.lifecycle.ViewModel
import com.example.loginsample.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val login: LoginUseCase) : ViewModel() {
    fun login() { login.start("state") }
    fun handleCode(code: String): Boolean = login.handle(code)
}

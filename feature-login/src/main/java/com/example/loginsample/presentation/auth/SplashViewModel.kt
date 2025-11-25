package com.example.loginsample.presentation.auth

import androidx.lifecycle.ViewModel
import com.example.loginsample.domain.usecase.SilentAuthUseCase
import com.example.loginsample.data.util.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val silent: SilentAuthUseCase, private val net: NetworkUtil) : ViewModel() {
    fun trySilentAuth(): Boolean { return if (silent.hasToken()) { val ok = if (net.probe()) silent.refresh() else false; if (!ok) silent.logout(); ok } else false }
}

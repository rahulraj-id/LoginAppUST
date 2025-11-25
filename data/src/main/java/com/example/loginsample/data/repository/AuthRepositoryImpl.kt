package com.example.loginsample.data.repository

import com.example.loginsample.data.local.Prefs
import com.example.loginsample.data.oauth.OAuthManager
import com.example.loginsample.domain.repo.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val prefs: Prefs, private val manager: OAuthManager) : AuthRepository {
    override fun startLogin(state: String) { manager.startLogin(state) }
    override fun handleCode(code: String): Boolean {
        val token = manager.exchange(code)
        if (token.accessToken.isEmpty()) return false
        prefs.saveToken(token.accessToken, token.refreshToken)
        return true
    }
    override fun silentAuth(): Boolean {
        val refresh = prefs.getRefreshToken()
        val access = prefs.getAccessToken()
        if (refresh != null) {
            val t = manager.refresh(refresh)
            if (t.accessToken.isEmpty()) return false
            prefs.saveToken(t.accessToken, t.refreshToken)
            return true
        }
        return access != null
    }
    override fun logout() { prefs.clear() }
    override fun hasToken(): Boolean = prefs.getAccessToken() != null
}

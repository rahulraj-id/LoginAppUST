package com.example.loginsample.data.repository

import android.content.Context
import com.example.loginsample.data.local.Prefs
import com.example.loginsample.data.oauth.OAuthManager
import com.example.loginsample.domain.model.AuthToken

class AuthRepository(private val context: Context, private val manager: OAuthManager) {
    private val prefs = Prefs(context)
    fun startLogin(state: String) { manager.startLogin(state) }
    fun handleCode(code: String): Boolean {
        val token = manager.exchange(code)
        if (token.accessToken.isEmpty()) return false
        prefs.saveToken(token.accessToken, token.refreshToken)
        return true
    }
    fun silentAuth(): Boolean {
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
    fun logout() { prefs.clear() }
    fun hasToken(): Boolean = prefs.getAccessToken() != null
}

package com.example.loginsample.presentation.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.loginsample.R
import com.example.loginsample.data.repository.AuthRepository
import com.example.loginsample.data.oauth.OAuthManager
import com.example.loginsample.data.util.NetworkUtil
import com.example.loginsample.presentation.home.HomeActivity

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = authRepo()
        val net = NetworkUtil(this)
        if (repo.hasToken()) {
            val ok = if (net.probe()) repo.silentAuth() else false
            if (ok) startActivity(Intent(this, HomeActivity::class.java)) else {
                repo.logout()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        } else startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    private fun authRepo(): AuthRepository {
        val manager = OAuthManager(this, getString(R.string.auth_url), getString(R.string.token_url), getString(R.string.client_id), getString(R.string.redirect_uri), getString(R.string.scope))
        return AuthRepository(this, manager)
    }
}

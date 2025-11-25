package com.example.loginsample.presentation.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginsample.R
import com.example.loginsample.data.repository.AuthRepository
import com.example.loginsample.data.oauth.OAuthManager
import com.example.loginsample.presentation.home.HomeActivity

class LoginActivity : Activity() {
    private lateinit var vm: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        vm = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val manager = OAuthManager(this@LoginActivity, getString(R.string.auth_url), getString(R.string.token_url), getString(R.string.client_id), getString(R.string.redirect_uri), getString(R.string.scope))
                return LoginViewModel(AuthRepository(this@LoginActivity, manager)) as T
            }
        })[LoginViewModel::class.java]
        findViewById<Button>(R.id.btnLogin).setOnClickListener { vm.login() }
        val data: Uri? = intent?.data
        if (data != null && data.toString().startsWith(getString(R.string.redirect_uri))) {
            val code = data.getQueryParameter("code")
            if (code != null && vm.handleCode(code)) startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}

class LoginViewModel(private val repo: AuthRepository) : ViewModel() {
    fun login() { repo.startLogin("state") }
    fun handleCode(code: String): Boolean = repo.handleCode(code)
}

package com.example.loginsample.presentation.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.example.loginsample.presentation.home.HomeActivity
import com.example.loginsample.feature.login.R

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val vm: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.btnLogin).setOnClickListener { vm.login() }
        val data: Uri? = intent?.data
        if (data != null && data.toString().startsWith(getString(com.example.loginsample.R.string.redirect_uri))) {
            val code = data.getQueryParameter("code")
            if (code != null && vm.handleCode(code)) startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}

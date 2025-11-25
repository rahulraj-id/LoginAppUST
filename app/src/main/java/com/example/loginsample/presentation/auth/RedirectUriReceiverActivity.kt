package com.example.loginsample.presentation.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class RedirectUriReceiverActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val i = Intent(this, LoginActivity::class.java)
        i.data = intent?.data
        startActivity(i)
        finish()
    }
}

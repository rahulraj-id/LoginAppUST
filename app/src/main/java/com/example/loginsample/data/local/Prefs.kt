package com.example.loginsample.data.local

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    fun saveToken(accessToken: String, refreshToken: String?) {
        prefs.edit().putString("access_token", accessToken).putString("refresh_token", refreshToken).apply()
    }
    fun getAccessToken(): String? = prefs.getString("access_token", null)
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)
    fun clear() { prefs.edit().clear().apply() }
}

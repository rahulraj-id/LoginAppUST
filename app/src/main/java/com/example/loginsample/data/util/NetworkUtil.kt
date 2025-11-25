package com.example.loginsample.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.loginsample.data.network.HttpClient

class NetworkUtil(private val context: Context) {
    fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(nw) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
    fun probe(): Boolean {
        if (!isNetworkAvailable()) return false
        return try { HttpClient().get("https://api.ipify.org?format=json", timeoutMs = 3000).isNotEmpty() } catch (_: Throwable) { false }
    }
}

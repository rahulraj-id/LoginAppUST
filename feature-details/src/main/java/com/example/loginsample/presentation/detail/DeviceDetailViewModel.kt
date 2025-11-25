package com.example.loginsample.presentation.detail

import androidx.lifecycle.ViewModel
import com.example.loginsample.data.network.HttpClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeviceDetailViewModel @Inject constructor(private val http: HttpClient) : ViewModel() {
    fun fetchPublicIp(): String { val json = try { http.get("https://api.ipify.org?format=json") } catch (_: Throwable) { "" }; return Regex("\"ip\":\"(.*?)\"").find(json)?.groupValues?.get(1) ?: "" }
    fun fetchIpDetails(ip: String): String { val url = "https://ipinfo.io/${ip}/geo"; return try { http.get(url) } catch (_: Throwable) { "" } }
}

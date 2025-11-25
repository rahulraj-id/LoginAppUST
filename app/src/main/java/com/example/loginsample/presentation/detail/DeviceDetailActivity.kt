package com.example.loginsample.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginsample.R
import com.example.loginsample.data.network.HttpClient

class DeviceDetailActivity : AppCompatActivity() {
    private lateinit var vm: DeviceDetailViewModel
    private lateinit var txtPublic: TextView
    private lateinit var txtDetails: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_detail)
        txtPublic = findViewById(R.id.txtPublicIp)
        txtDetails = findViewById(R.id.txtIpDetails)
        vm = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T { return DeviceDetailViewModel() as T }
        })[DeviceDetailViewModel::class.java]
        Thread {
            val ip = vm.fetchPublicIp()
            val details = if (ip.isNotEmpty()) vm.fetchIpDetails(ip) else ""
            runOnUiThread {
                txtPublic.text = ip
                txtDetails.text = details
            }
        }.start()
    }
}

class DeviceDetailViewModel : ViewModel() {
    fun fetchPublicIp(): String {
        val json = try { HttpClient().get("https://api.ipify.org?format=json") } catch (_: Throwable) { "" }
        return Regex("\"ip\":\"(.*?)\"").find(json)?.groupValues?.get(1) ?: ""
    }
    fun fetchIpDetails(ip: String): String {
        val url = "https://ipinfo.io/${ip}/geo"
        return try { HttpClient().get(url) } catch (_: Throwable) { "" }
    }
}

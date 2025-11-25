package com.example.loginsample.presentation.detail

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.example.loginsample.feature.details.R

@AndroidEntryPoint
class DeviceDetailActivity : AppCompatActivity() {
    private val vm: DeviceDetailViewModel by viewModels()
    private lateinit var txtPublic: TextView
    private lateinit var txtDetails: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_detail)
        txtPublic = findViewById(R.id.txtPublicIp)
        txtDetails = findViewById(R.id.txtIpDetails)
        Thread { val ip = vm.fetchPublicIp(); val details = if (ip.isNotEmpty()) vm.fetchIpDetails(ip) else ""; runOnUiThread { txtPublic.text = ip; txtDetails.text = details } }.start()
    }
}

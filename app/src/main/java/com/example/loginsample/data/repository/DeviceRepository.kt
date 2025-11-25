package com.example.loginsample.data.repository

import android.content.Context
import com.example.loginsample.data.local.DeviceDbHelper
import com.example.loginsample.domain.model.Device

class DeviceRepository(context: Context) {
    private val db = DeviceDbHelper(context)
    fun upsert(name: String, ip: String, online: Boolean) { db.upsert(Device(null, name, ip, System.currentTimeMillis(), online)) }
    fun list(): List<Device> = db.list()
    fun setOnline(ip: String, online: Boolean) { db.setOnline(ip, online) }
    fun setAllOffline() { db.setAllOffline() }
}

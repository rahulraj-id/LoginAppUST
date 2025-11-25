package com.example.loginsample.domain.repo

import com.example.loginsample.domain.model.Device

interface DeviceRepository {
    fun upsert(name: String, ip: String, online: Boolean)
    fun list(): List<Device>
    fun setOnline(ip: String, online: Boolean)
    fun setAllOffline()
}

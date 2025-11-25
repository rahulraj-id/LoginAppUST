package com.example.loginsample.data.repository

import com.example.loginsample.data.local.DeviceDbHelper
import com.example.loginsample.domain.model.Device
import com.example.loginsample.domain.repo.DeviceRepository
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(private val db: DeviceDbHelper) : DeviceRepository {
    override fun upsert(name: String, ip: String, online: Boolean) { db.upsert(Device(null, name, ip, System.currentTimeMillis(), online)) }
    override fun list(): List<Device> = db.list()
    override fun setOnline(ip: String, online: Boolean) { db.setOnline(ip, online) }
    override fun setAllOffline() { db.setAllOffline() }
}

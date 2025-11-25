package com.example.loginsample.domain.usecase

import com.example.loginsample.domain.model.Device
import com.example.loginsample.domain.repo.DeviceRepository
import javax.inject.Inject

class LoadDevicesUseCase @Inject constructor(private val repo: DeviceRepository) { fun invoke(): List<Device> = repo.list() }
class UpsertDeviceUseCase @Inject constructor(private val repo: DeviceRepository) { fun invoke(name: String, ip: String) = repo.upsert(name, ip, true) }
class SetAllOfflineUseCase @Inject constructor(private val repo: DeviceRepository) { fun invoke() = repo.setAllOffline() }

package com.example.loginsample.data.mdns

import android.content.Context
import android.net.wifi.WifiManager
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import javax.inject.Inject

class NsdDiscoveryManager @Inject constructor(context: Context) {
    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    private val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private var lock: WifiManager.MulticastLock? = null
    private val listeners = mutableListOf<(String, String) -> Unit>()
    private val serviceTypes = listOf("_airplay._tcp.", "_googlecast._tcp.", "_ipp._tcp.")
    fun addListener(l: (name: String, ip: String) -> Unit) { listeners += l }
    fun start() { lock = wifi.createMulticastLock("mdns_lock").apply { setReferenceCounted(true); acquire() }; serviceTypes.forEach { type -> nsdManager.discoverServices(type, NsdManager.PROTOCOL_DNS_SD, object : NsdManager.DiscoveryListener { override fun onDiscoveryStarted(serviceType: String) {}; override fun onServiceFound(serviceInfo: NsdServiceInfo) { resolve(serviceInfo) }; override fun onServiceLost(serviceInfo: NsdServiceInfo) {}; override fun onDiscoveryStopped(serviceType: String) {}; override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {}; override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {} }) } }
    private fun resolve(info: NsdServiceInfo) { nsdManager.resolveService(info, object : NsdManager.ResolveListener { override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {}; override fun onServiceResolved(serviceInfo: NsdServiceInfo) { val host = serviceInfo.host?.hostAddress ?: return; val name = serviceInfo.serviceName ?: host; listeners.forEach { it(name, host) } } }) }
    fun stop() { try { lock?.release() } catch (_: Throwable) {} }
}

package com.example.loginsample.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsample.feature.dashboard.R
import com.example.loginsample.domain.model.Device
import com.example.loginsample.presentation.detail.DeviceDetailActivity
import com.example.loginsample.data.mdns.NsdDiscoveryManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    @Inject lateinit var nsd: NsdDiscoveryManager
    private val vm: HomeViewModel by viewModels()
    private lateinit var adapter: DeviceAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var empty: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        recycler = findViewById(R.id.recyclerDevices)
        empty = findViewById(R.id.txtEmpty)
        adapter = DeviceAdapter { d -> val i = Intent(this, DeviceDetailActivity::class.java); i.putExtra("name", d.name); i.putExtra("ip", d.ip); startActivity(i) }
        recycler.layoutManager = LinearLayoutManager(this); recycler.adapter = adapter
        vm.setAllOffline()
        val list = vm.load()
        adapter.submit(list); empty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        nsd.addListener { name, ip -> vm.upsert(name, ip); val updated = vm.load(); runOnUiThread { adapter.submit(updated); empty.visibility = if (updated.isEmpty()) View.VISIBLE else View.GONE } }
        nsd.start()
    }
    override fun onDestroy() { super.onDestroy(); nsd.stop() }
}

@dagger.hilt.android.lifecycle.HiltViewModel
class HomeViewModel @Inject constructor(private val loader: com.example.loginsample.domain.usecase.LoadDevicesUseCase, private val upserter: com.example.loginsample.domain.usecase.UpsertDeviceUseCase, private val offlineAll: com.example.loginsample.domain.usecase.SetAllOfflineUseCase) : androidx.lifecycle.ViewModel() {
    fun load(): List<Device> = loader.invoke()
    fun upsert(name: String, ip: String) { upserter.invoke(name, ip) }
    fun setAllOffline() { offlineAll.invoke() }
}

class DeviceAdapter(private val onClick: (Device) -> Unit) : RecyclerView.Adapter<DeviceViewHolder>() {
    private val items = mutableListOf<Device>()
    fun submit(list: List<Device>) { items.clear(); items.addAll(list); notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): DeviceViewHolder { val v = android.view.LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false); return DeviceViewHolder(v, onClick) }
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) { holder.bind(items[position]) }
}

class DeviceViewHolder(itemView: android.view.View, private val onClick: (Device) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val name = itemView.findViewById<android.widget.TextView>(R.id.txtName)
    private val ip = itemView.findViewById<android.widget.TextView>(R.id.txtIp)
    fun bind(d: Device) { name.text = d.name; ip.text = d.ip; itemView.setOnClickListener { onClick(d) } }
}

package com.example.loginsample.presentation.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginsample.R
import com.example.loginsample.data.mdns.NsdDiscoveryManager
import com.example.loginsample.data.repository.DeviceRepository
import com.example.loginsample.domain.model.Device
import com.example.loginsample.presentation.detail.DeviceDetailActivity

class HomeActivity : Activity() {
    private lateinit var vm: HomeViewModel
    private lateinit var adapter: DeviceAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var empty: View
    private lateinit var nsd: NsdDiscoveryManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        recycler = findViewById(R.id.recyclerDevices)
        empty = findViewById(R.id.txtEmpty)
        adapter = DeviceAdapter { d ->
            val i = Intent(this, DeviceDetailActivity::class.java)
            i.putExtra("name", d.name)
            i.putExtra("ip", d.ip)
            startActivity(i)
        }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        vm = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(DeviceRepository(this@HomeActivity)) as T
            }
        })[HomeViewModel::class.java]
        vm.setAllOffline()
        val list = vm.load()
        adapter.submit(list)
        empty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        nsd = NsdDiscoveryManager(this)
        nsd.addListener { name, ip ->
            vm.upsert(name, ip)
            val updated = vm.load()
            runOnUiThread {
                adapter.submit(updated)
                empty.visibility = if (updated.isEmpty()) View.VISIBLE else View.GONE
            }
        }
        nsd.start()
    }
    override fun onDestroy() { super.onDestroy(); nsd.stop() }
}

class HomeViewModel(private val repo: DeviceRepository) : ViewModel() {
    fun load(): List<Device> = repo.list()
    fun upsert(name: String, ip: String) { repo.upsert(name, ip, true) }
    fun setAllOffline() { repo.setAllOffline() }
}

class DeviceAdapter(private val onClick: (Device) -> Unit) : RecyclerView.Adapter<DeviceViewHolder>() {
    private val items = mutableListOf<Device>()
    fun submit(list: List<Device>) { items.clear(); items.addAll(list); notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): DeviceViewHolder {
        val v = android.view.LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
        return DeviceViewHolder(v, onClick)
    }
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) { holder.bind(items[position]) }
}

class DeviceViewHolder(itemView: android.view.View, private val onClick: (Device) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val name = itemView.findViewById<android.widget.TextView>(R.id.txtName)
    private val ip = itemView.findViewById<android.widget.TextView>(R.id.txtIp)
    fun bind(d: Device) { name.text = d.name; ip.text = d.ip; itemView.setOnClickListener { onClick(d) } }
}

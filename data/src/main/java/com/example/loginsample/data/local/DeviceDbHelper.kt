package com.example.loginsample.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import com.example.loginsample.domain.model.Device
import javax.inject.Inject

class DeviceDbHelper @Inject constructor(context: Context) : SQLiteOpenHelper(context, "devices.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) { db.execSQL("CREATE TABLE devices(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, ip TEXT, last_seen INTEGER, is_online INTEGER)") }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun upsert(device: Device) { val db = writableDatabase; val cv = ContentValues(); cv.put("name", device.name); cv.put("ip", device.ip); cv.put("last_seen", device.lastSeen); cv.put("is_online", if (device.isOnline) 1 else 0); val cursor = db.query("devices", arrayOf("id"), "ip=?", arrayOf(device.ip), null, null, null); val exists = cursor.moveToFirst(); cursor.close(); if (exists) db.update("devices", cv, "ip=?", arrayOf(device.ip)) else db.insert("devices", null, cv) }
    fun list(): List<Device> { val db = readableDatabase; val c = db.query("devices", arrayOf("id","name","ip","last_seen","is_online"), null, null, null, null, null); val res = mutableListOf<Device>(); while (c.moveToNext()) { res += Device(c.getInt(0), c.getString(1), c.getString(2), c.getLong(3), c.getInt(4) == 1) }; c.close(); return res }
    fun setOnline(ip: String, online: Boolean) { val db = writableDatabase; val cv = ContentValues(); cv.put("is_online", if (online) 1 else 0); cv.put("last_seen", System.currentTimeMillis()); db.update("devices", cv, "ip=?", arrayOf(ip)) }
    fun setAllOffline() { val db = writableDatabase; val cv = ContentValues(); cv.put("is_online", 0); db.update("devices", cv, null, null) }
}

package com.example.loginsample.data.network

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class HttpClient {
    fun get(url: String, headers: Map<String, String> = emptyMap(), timeoutMs: Int = 8000): String {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.connectTimeout = timeoutMs
        conn.readTimeout = timeoutMs
        headers.forEach { conn.setRequestProperty(it.key, it.value) }
        val code = conn.responseCode
        val stream = if (code in 200..299) conn.inputStream else conn.errorStream
        val reader = BufferedReader(InputStreamReader(stream))
        val sb = StringBuilder()
        var line: String?
        while (true) {
            line = reader.readLine()
            if (line == null) break
            sb.append(line)
        }
        reader.close()
        conn.disconnect()
        return sb.toString()
    }
    fun post(url: String, body: String, headers: Map<String, String> = mapOf("Content-Type" to "application/x-www-form-urlencoded"), timeoutMs: Int = 8000): String {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.connectTimeout = timeoutMs
        conn.readTimeout = timeoutMs
        conn.doOutput = true
        headers.forEach { conn.setRequestProperty(it.key, it.value) }
        val os: OutputStream = conn.outputStream
        os.write(body.toByteArray(Charsets.UTF_8))
        os.flush()
        os.close()
        val code = conn.responseCode
        val stream = if (code in 200..299) conn.inputStream else conn.errorStream
        val reader = BufferedReader(InputStreamReader(stream))
        val sb = StringBuilder()
        var line: String?
        while (true) {
            line = reader.readLine()
            if (line == null) break
            sb.append(line)
        }
        reader.close()
        conn.disconnect()
        return sb.toString()
    }
}

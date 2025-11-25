package com.example.loginsample.data.oauth

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import com.example.loginsample.data.network.HttpClient
import com.example.loginsample.domain.model.AuthToken
import javax.inject.Inject

class OAuthManager @Inject constructor(private val context: Context, private val authUrl: String, private val tokenUrl: String, private val clientId: String, private val redirectUri: String, private val scope: String, private val http: HttpClient) {
    private var codeVerifier: String = ""
    fun startLogin(state: String) { codeVerifier = randomString(); val challenge = sha256(codeVerifier); val uri = Uri.parse(authUrl).buildUpon().appendQueryParameter("response_type", "code").appendQueryParameter("client_id", clientId).appendQueryParameter("redirect_uri", redirectUri).appendQueryParameter("scope", scope).appendQueryParameter("code_challenge", challenge).appendQueryParameter("code_challenge_method", "S256").appendQueryParameter("state", state).build(); CustomTabsIntent.Builder().build().launchUrl(context, uri) }
    fun exchange(code: String): AuthToken { val body = "grant_type=authorization_code&code=${encode(code)}&redirect_uri=${encode(redirectUri)}&client_id=${encode(clientId)}&code_verifier=${encode(codeVerifier)}"; val json = http.post(tokenUrl, body); val access = Regex("\"access_token\":\"(.*?)\"").find(json)?.groupValues?.get(1) ?: ""; val refresh = Regex("\"refresh_token\":\"(.*?)\"").find(json)?.groupValues?.get(1); return AuthToken(access, refresh) }
    fun refresh(refreshToken: String): AuthToken { val body = "grant_type=refresh_token&refresh_token=${encode(refreshToken)}&client_id=${encode(clientId)}"; val json = http.post(tokenUrl, body); val access = Regex("\"access_token\":\"(.*?)\"").find(json)?.groupValues?.get(1) ?: ""; val newRefresh = Regex("\"refresh_token\":\"(.*?)\"").find(json)?.groupValues?.get(1) ?: refreshToken; return AuthToken(access, newRefresh) }
    private fun sha256(s: String): String { val md = MessageDigest.getInstance("SHA-256"); val bytes = md.digest(s.toByteArray(Charsets.UTF_8)); return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes) }
    private fun randomString(): String { val bytes = ByteArray(32); SecureRandom().nextBytes(bytes); return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes) }
    private fun encode(s: String) = Uri.encode(s)
}

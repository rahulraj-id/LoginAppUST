package com.example.loginsample.data.di

import android.content.Context
import com.example.loginsample.data.local.DeviceDbHelper
import com.example.loginsample.data.local.Prefs
import com.example.loginsample.data.network.HttpClient
import com.example.loginsample.data.oauth.OAuthManager
import com.example.loginsample.data.repository.AuthRepositoryImpl
import com.example.loginsample.data.repository.DeviceRepositoryImpl
import com.example.loginsample.data.util.NetworkUtil
import com.example.loginsample.domain.repo.AuthRepository
import com.example.loginsample.domain.repo.DeviceRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingsModule {
    @Binds abstract fun bindAuthRepo(impl: AuthRepositoryImpl): AuthRepository
    @Binds abstract fun bindDeviceRepo(impl: DeviceRepositoryImpl): DeviceRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ProvidersModule {
    @Provides @Singleton fun provideHttpClient(): HttpClient = HttpClient()
    @Provides @Singleton fun providePrefs(context: Context): Prefs = Prefs(context)
    @Provides @Singleton fun provideDb(context: Context): DeviceDbHelper = DeviceDbHelper(context)
    @Provides @Singleton fun provideNetworkUtil(context: Context, client: HttpClient): NetworkUtil = NetworkUtil(context, client)
    @Provides @Singleton fun provideOAuthManager(context: Context, client: HttpClient): OAuthManager = OAuthManager(
        context,
        context.getString(com.example.loginsample.R.string.auth_url),
        context.getString(com.example.loginsample.R.string.token_url),
        context.getString(com.example.loginsample.R.string.client_id),
        context.getString(com.example.loginsample.R.string.redirect_uri),
        context.getString(com.example.loginsample.R.string.scope),
        client
    )
}

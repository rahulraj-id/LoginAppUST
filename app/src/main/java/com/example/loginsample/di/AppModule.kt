package com.example.loginsample.di

import android.content.Context
import com.example.loginsample.R
import com.example.loginsample.data.network.HttpClient
import com.example.loginsample.data.oauth.OAuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideOAuthManager(context: Context, client: HttpClient): OAuthManager = OAuthManager(
        context,
        context.getString(R.string.auth_url),
        context.getString(R.string.token_url),
        context.getString(R.string.client_id),
        context.getString(R.string.redirect_uri),
        context.getString(R.string.scope),
        client
    )
}

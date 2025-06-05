/*
 * *
 *  * Created by Nguyễn Kim Khánh on 7/18/23, 10:10 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/18/23, 10:10 AM
 *
 */

package com.koai.base.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseApiController<T : Any> {
    companion object {
        const val TIME_OUT = 30L
    }

    fun getService(
        context: Context,
        allowVpn: Boolean = true,
    ): T? {
        val baseUrl = getBaseUrl()

        val builder = okHttpClientBuilder()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)
        getNetworkInterceptor()?.let { interceptor ->
            builder.addInterceptor(interceptor)
        }

        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1
        val okHttpClient =
            builder
                .connectTimeout(timeOut(), TimeUnit.SECONDS)
                .readTimeout(timeOut(), TimeUnit.SECONDS)
                .dispatcher(dispatcher)
                .build()

        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: Network? = connectivityManager.activeNetwork
        val caps: NetworkCapabilities? =
            connectivityManager.getNetworkCapabilities(activeNetwork)
        val vpnInUse = caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ?: false
        return if (!vpnInUse || allowVpn) (retrofit.create(getApiService()) as T) else null
    }

    abstract fun getBaseUrl(): String

    abstract fun getApiService(): Class<*>

    open fun getNetworkInterceptor(): Interceptor? = null

    open fun timeOut() = TIME_OUT

    open fun okHttpClientBuilder() = OkHttpClient.Builder()
}

package vn.semicolon.base.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by HuynhMH on 7/13/2018
 */
object SemiHttpFactory {

    fun <S> create(
        serviceClass: Class<S>,
        baseUrl: String,
        interceptors: List<Interceptor>? = null,
        callAdapterFactories: List<CallAdapter.Factory>? = null,
        converterFactories: List<Converter.Factory> = listOf(GsonConverterFactory.create()),
        readTimeout: Long = 120,
        readTimeoutUnit: TimeUnit = TimeUnit.SECONDS,
        connectTimeOut: Long = 120,
        connectTimeOutUnit: TimeUnit = TimeUnit.SECONDS
    ): S {
        val httpClient = OkHttpClient.Builder()
        // set timeout
        httpClient.readTimeout(readTimeout, readTimeoutUnit)
        httpClient.connectTimeout(connectTimeOut, connectTimeOutUnit)
        // add logging
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)
        // add interceptors
        if (interceptors != null && interceptors.isNotEmpty()) {
            for (i in interceptors) {
                httpClient.addInterceptor(i)
            }
        }
        // create builder
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        // add call adapter factories
        if (callAdapterFactories != null && callAdapterFactories.isNotEmpty()) {
            for (c in callAdapterFactories) {
                builder.addCallAdapterFactory(c)
            }
        }
        // add converter factories
        if (converterFactories.isNotEmpty()) {
            for (cf in converterFactories) {
                builder.addConverterFactory(cf)
            }
        }
        builder.client(httpClient.build())
        val retrofit = builder.build()
        return retrofit.create(serviceClass)
    }

    fun <S> create(
        serviceClass: Class<S>,
        baseUrl: String,
        client: OkHttpClient,
        callAdapterFactories: List<CallAdapter.Factory>? = null,
        converterFactories: List<Converter.Factory>? = null
    ): S {
        // create builder
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        // add call adapter factories
        if (callAdapterFactories != null && callAdapterFactories.isNotEmpty()) {
            for (c in callAdapterFactories) {
                builder.addCallAdapterFactory(c)
            }
        }
        // add converter factories
        if (converterFactories != null && converterFactories.isNotEmpty()) {
            for (cf in converterFactories) {
                builder.addConverterFactory(cf)
            }
        }
        builder.client(client)
        val retrofit = builder.build()
        return retrofit.create(serviceClass)
    }
}
package com.irfan.dansjob.di

import com.irfan.dansjob.BuildConfig
import com.irfan.dansjob.extension.common.clazz
import com.irfan.dansjob.network.RetrofitInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single<OkHttpClient> {
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(20, TimeUnit.MINUTES)
            .readTimeout(20, TimeUnit.MINUTES)
            .writeTimeout(20, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .build()
    }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("http://dev3.dansmultipro.co.id/api/recruitment/")
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(clazz<RetrofitInterface>()) }
}
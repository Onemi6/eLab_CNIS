package com.example.eLab_CNIS.http

import com.example.eLab_CNIS.http.interceptor.AddCookiesInterceptor
import com.example.eLab_CNIS.http.interceptor.ReceivedCookiesInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    private var apiServer: API.WebApi

    fun getApiService(): API.WebApi {
        return apiServer
    }

    //初始化retrofit
    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(ReceivedCookiesInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()

        //关联okhttp并加上rxjava和gson的配置和baseurl
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API.BASE_URL)
            .build()

        apiServer = retrofit.create(API.WebApi::class.java)
    }
}
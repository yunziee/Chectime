package com.example.chectime

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.aladin.co.kr/ttb/api/"

    val apiService: AladinApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Gson을 사용해 JSON을 Kotlin 객체로 변환
            .build()
            .create(AladinApiService::class.java)
    }
}
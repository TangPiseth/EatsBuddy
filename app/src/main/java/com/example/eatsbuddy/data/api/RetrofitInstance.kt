package com.example.eatsbuddy.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(MealDbApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val mealDbApi: MealDbApi by lazy {
        retrofit.create(MealDbApi::class.java)
    }
}

// servicio_swapi.kt
package com.example.clon_fulanito.API.SWAPI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SWAPIService {
    private const val BASE_URL = "https://swapi.dev/api/" // SWAPI base URL

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: SWAPIApi = retrofit.create(SWAPIApi::class.java)
}
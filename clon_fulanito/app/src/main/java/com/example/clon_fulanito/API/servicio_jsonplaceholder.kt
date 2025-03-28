package com.example.clon_fulanito.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InstanciaRetrofitJSONplaceholder {
    private const val url_base = "https://jsonplaceholder.typicode.com"

    private val servicio: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url_base)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Lazy es un constructor que solo va a crear el objeto cuando sea solicitado y no desde un inicio. PAra evitar tener
    // muchas cosas en la llamadas generales.
    val consumir_servicio: FulanitoAPIServicio by lazy {
        servicio.create(FulanitoAPIServicio::class.java)
    }
}
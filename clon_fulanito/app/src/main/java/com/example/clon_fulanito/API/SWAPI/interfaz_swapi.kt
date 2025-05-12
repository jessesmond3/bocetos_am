// interfaz_swapi.kt
package com.example.clon_fulanito.API.SWAPI

import com.example.clon_fulanito.modelos.swapi.NaveEspacial
import retrofit2.http.GET

interface SWAPIApi {
    @GET("starships") // Replace with the actual endpoint
    suspend fun getStarships(): StarshipResponse // Assuming a wrapper object
}

data class StarshipResponse(
    val results: List<NaveEspacial>
)
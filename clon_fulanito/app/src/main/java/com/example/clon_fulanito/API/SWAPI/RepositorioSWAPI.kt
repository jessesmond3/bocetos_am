// RepositorioSWAPI.kt
package com.example.clon_fulanito.API.SWAPI

import com.example.clon_fulanito.modelos.swapi.NaveEspacial

class RepositorioSWAPI {
    suspend fun getStarships(): List<NaveEspacial> {
        return try {
            SWAPIService.api.getStarships().results
        } catch (e: Exception) {
            // Handle error properly (e.g., log, return empty list, etc.)
            emptyList()
        }
    }
}
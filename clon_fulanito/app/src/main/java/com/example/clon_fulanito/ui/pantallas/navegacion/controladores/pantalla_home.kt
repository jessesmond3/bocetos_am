package com.example.clon_fulanito.ui.pantallas.navegacion.controladores

interface SWAPIInterfaz {
    @GET("starships")
    suspend fun obtener_naves_espaciales(): PaginaContenedora

    @GET("starships/{id}")
 }
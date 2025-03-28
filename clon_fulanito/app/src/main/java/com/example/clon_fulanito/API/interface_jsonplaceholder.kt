package com.example.clon_fulanito.API


import com.example.clon_fulanito.modelos.Publicacion
import retrofit2.http.GET
import retrofit2.http.Path


interface FulanitoAPIServicio{
    // Cuando se use la repsuesta de itnernet se resolvera como : https://jsonplaceholder.typicode.com/posts
    @GET("/posts") // https://jsonplaceholder.typicode.com/posts
    suspend fun obtener_publicaciones(): List<Publicacion>

    @GET("/posts/{id}/comments")
    suspend fun <Comentario> obtener_comentarios_de_publicacion(@Path("id") id: Int): List<Comentario>
}
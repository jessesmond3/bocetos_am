package com.example.clon_fulanito.vista_modelos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clon_fulanito.API.SWAPI.RepositorioSWAPI
import kotlinx.coroutines.launch

class SWAPIModelo: ViewModel(){
    private val repositorio = RepositorioSWAPI()

    private val _pagina_actual = MutableLiveData<PaginaContenedroa>()
    val pagina_actual: LiveData<PaginaContenedora> = _pagina_actual

    fun descargar_pagina(){
        viewModelScope.launch {
            try{
                val pegina = repositorio.obtener_naves_espaciales()
                _pagina_actual.value = pagina
            }
            catch (error: Exception){
                log.v("DESCARGA DE PAGINA SWAPI", "${error.message}")
            }
        }
    }
}
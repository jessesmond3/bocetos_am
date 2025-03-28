package com.example.clon_fulanito.vista_modelos


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clon_fulanito.API.RepositorioFulanito
import com.example.clon_fulanito.modelos.Comentario
import com.example.clon_fulanito.modelos.Publicacion
import kotlinx.coroutines.launch

class FulanitoViewModel: ViewModel() {
    private val repositorio_de_datos = RepositorioFulanito()

    private val _publicaciones = MutableLiveData<List<Publicacion>>()
    val publicaciones: LiveData<List<Publicacion>> = _publicaciones

    private val _publicacion_seleccionada = MutableLiveData<Publicacion>()
    val publicacion_seleccionada: LiveData<Publicacion> = _publicacion_seleccionada

    private val _comentarios_de_publicacion = MutableLiveData<List<Comentario>>()
    val comentarios_de_publicacion: LiveData<List<Comentario>> = _comentarios_de_publicacion

    fun descargar_todas_las_publicaciones() {
        // Aqui estan las corutinas
        viewModelScope.launch {
            try {
                val publicaciones_obtenidas = repositorio_de_datos.obtener_publicaciones()
                _publicaciones.value = publicaciones_obtenidas
            }
            catch (error: Exception) {
                Log.v("DESCARGA DE PUBLICACIONES", "${error.message}")
            }
        }
    }

    fun descargar_los_comentarios_de() {
        // Aqui estan las corutinas
        viewModelScope.launch {
            try {
                if (publicacion_seleccionada.value != null){
                    val comentarios_descargados = repositorio_de_datos.obtener_comentarios_en_publicacion(
                        publicacion_seleccionada.value!!.id)
                    _comentarios_de_publicacion.value = comentarios_descargados
                }
            }
            catch (error: Exception) {
                Log.v("DESCARGA DE PUBLICACIONES", "${error.message}")
            }
        }
    }

    fun seleccionar_publicacion(id: Int): Boolean{
        for(publicacion in publicaciones.value!!){
            if(publicacion.id == id){
                _publicacion_seleccionada.value = publicacion
                descargar_los_comentarios_de()
                return true
            }
        }
        return false
    }
}
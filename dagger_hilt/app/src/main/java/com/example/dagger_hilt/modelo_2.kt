package com.example.daggerhilt

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VistaModeloComentarios @Inject constructor(): ViewModel(){
    var comentarios = mutableStateOf("texto loco")
}
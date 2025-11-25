package com.dmc.goyiyi.feature.events.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmc.goyiyi.feature.events.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NuevaOpinionViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _resultado = MutableLiveData<Boolean>()
    val resultado: LiveData<Boolean> = _resultado

    fun enviarOpinion(idEvento: String, comentario: String, valoracion: Int) {
        println("🔥 VIEWMODEL: enviarOpinion llamado con $idEvento, $valoracion, $comentario")
        viewModelScope.launch {
            try {
                println("🔥 VIEWMODEL: llamando repository.postOpinion...")
                repository.postOpinion(idEvento, comentario, valoracion)
                println("🔥 VIEWMODEL: éxito")
                _resultado.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                println("❌ VIEWMODEL ERROR: ${e.message}")
                _resultado.postValue(false)
            }
        }
    }
}

package com.dmc.goyiyi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmc.goyiyi.data.EventoRepositorio
import com.dmc.goyiyi.model.Evento

class MapaViewModel(
    private val repo: EventoRepositorio = EventoRepositorio()
) : ViewModel() {

    private val _eventos = MutableLiveData<List<Evento>>()
    val eventos: LiveData<List<Evento>> get() = _eventos

    init {
        _eventos.value = repo.obtenerEventos()
    }
}

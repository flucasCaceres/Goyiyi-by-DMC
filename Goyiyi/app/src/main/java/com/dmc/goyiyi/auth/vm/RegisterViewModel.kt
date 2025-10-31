package com.dmc.goyiyi.auth.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    private val _fechaNacimientoIso = MutableLiveData<String?>(null)
    val fechaNacimientoIso: LiveData<String?> = _fechaNacimientoIso

    fun setFechaNacimientoIso(value: String?) {
        _fechaNacimientoIso.value = value
    }
}

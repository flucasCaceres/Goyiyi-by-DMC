package com.dmc.goyiyi.auth.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmc.goyiyi.auth.data.RegisterRepository
import com.dmc.goyiyi.core.network.dto.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo: RegisterRepository
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val state: StateFlow<RegisterState> = _state

    fun register(nombre: String, correo: String, pass: String, confPass: String) {

        // Validaciones de campos vacíos
        if (nombre.isBlank() || correo.isBlank() || pass.isBlank() || confPass.isBlank()) {
            _state.value = RegisterState.Error("Por favor complete todos los campos")
            return
        }

        if (pass != confPass) {
            _state.value = RegisterState.Error("Las contraseñas no coinciden")
            return
        }

        if (pass.length < 6) {
            _state.value = RegisterState.Error("La contraseña debe tener mínimo 6 caracteres")
            return
        }

        if (containsEmoji(pass)) {
            _state.value = RegisterState.Error("Por favor no utilice emojis en la contraseña")
            return
        }

        viewModelScope.launch {
            _state.value = RegisterState.Loading

            val request = RegisterRequest(
                nombreUsuario = nombre,
                correo = correo,
                contrasena = pass,
                confirmarContrasena = confPass
            )

            val result = repo.register(request)

            result.onSuccess {
                _state.value = RegisterState.Success(it.message)
            }.onFailure {
                _state.value = RegisterState.Error("Error: ${it.message}")
            }
        }
    }

    private fun containsEmoji(text: String): Boolean {
        return text.codePoints().anyMatch { it > 0x1F000 }
    }
}

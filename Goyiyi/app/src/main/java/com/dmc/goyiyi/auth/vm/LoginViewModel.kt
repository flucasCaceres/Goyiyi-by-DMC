package com.dmc.goyiyi.auth.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmc.goyiyi.auth.data.LoginRepository
import com.dmc.goyiyi.core.network.dto.LoginRequest
import com.dmc.goyiyi.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: LoginRepository,
    private val prefs: UserPreferences   // guardar tokens aquí
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun login(correo: String, pass: String) {

        if (correo.isBlank() || pass.isBlank()) {
            _state.value = LoginState.Error("Por favor completá todos los campos")
            return
        }

        viewModelScope.launch {
            _state.value = LoginState.Loading

            val req = LoginRequest(correo, pass)

            repo.login(req).onSuccess { data ->

                // guardar tokens
                prefs.saveToken(data.token)
                prefs.saveRefreshToken(data.refreshToken)
                prefs.saveUserId(data.usuario.idUsuario)

                _state.value = LoginState.Success("Bienvenido ${data.usuario.nombreUsuario}")

            }.onFailure { err ->
                _state.value = LoginState.Error("Correo o contraseña incorrectos")
            }
        }
    }
}

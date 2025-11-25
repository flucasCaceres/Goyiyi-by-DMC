package com.dmc.goyiyi.core.network.dto

data class LoginResponse(
    val message: String,
    val token: String,
    val refreshToken: String,
    val usuario: UsuarioLogin
)

data class UsuarioLogin(
    val confiabilidad: String,
    val correo: String,
    val estado: String,
    val fechaCreacion: String,
    val fechaModificacion: String,
    val idUsuario: String,
    val nombreUsuario: String,
    val roles: List<String>
)

package com.dmc.goyiyi.core.network.dto

data class RegisterResponse(
    val message: String,
    val data: UserData
)

data class UserData(
    val idusuario: String,
    val nombreusuario: String,
    val correo: String,
    val telefono: String?,
    val nombreCompleto: String?,
    val fechaNacimiento: String?,
    val biografia: String?,
    val fotoPerfil: String?,
    val roles: List<String>,
    val confiabilidad: String,
    val estado: String,
    val fechaCreacion: String,
    val fechaModificacion: String
)

package com.dmc.goyiyi.core.network.dto

data class RegisterRequest(
    val nombreUsuario: String,
    val correo: String,
    val contrasena: String,
    val confirmarContrasena: String
)
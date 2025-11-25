package com.dmc.goyiyi.auth.data

import com.dmc.goyiyi.core.network.ApiService
import com.dmc.goyiyi.core.network.dto.LoginRequest
import com.dmc.goyiyi.core.network.dto.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val res = api.login(request)
            Result.success(res)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

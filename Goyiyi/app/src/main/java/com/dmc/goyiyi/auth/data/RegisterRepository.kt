package com.dmc.goyiyi.auth.data

import com.dmc.goyiyi.core.network.ApiService
import com.dmc.goyiyi.core.network.dto.RegisterRequest
import com.dmc.goyiyi.core.network.dto.RegisterResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val result = api.register(request)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

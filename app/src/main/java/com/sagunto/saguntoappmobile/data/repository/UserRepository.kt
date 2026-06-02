package com.sagunto.saguntoappmobile.data.repository

import android.util.Log
import com.sagunto.saguntoappmobile.data.network.dto.CreateUserRequest
import com.sagunto.saguntoappmobile.domain.interfaces.IUserRepository
import com.sagunto.saguntoappmobile.domain.models.User
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class UserRepository (
    private val httpClient: HttpClient
) : IUserRepository {

    override suspend fun addUser(user: User): Result<Unit> {
        return try{
            val request = CreateUserRequest(
                name = user.name,
                surname = user.surname,
                roleId = user.rolId
            )

            val response = httpClient.post("api/users") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Fallo en la API. Código HTTP: ${response.status.value}"))
            }

        } catch (e: Exception) {
            Log.e("API_ERROR", "💥 Ha fallado la petición HTTP", e)
            return Result.failure(e)
        }
    }
}
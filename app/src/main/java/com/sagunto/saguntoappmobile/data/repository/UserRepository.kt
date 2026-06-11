package com.sagunto.saguntoappmobile.data.repository

import android.util.Log
import com.sagunto.saguntoappmobile.data.network.dto.createUser.*
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.SearchUsersResponse
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.UserResponse
import com.sagunto.saguntoappmobile.domain.interfaces.IUserRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class UserRepository (
    private val httpClient: HttpClient
) : IUserRepository {

    private val saguntinoCodeRegex = Regex("^[a-zA-Z]\\d{2}$")

    override suspend fun addUser(user: CreateUserRequest): Result<CreateUserResponse> {
        return try{
            val response = httpClient.post("api/users") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }

            if (response.status.isSuccess()) {
                val responseData = response.body<CreateUserResponse>()
                Result.success(responseData)
            } else {
                Result.failure(Exception("Fallo en la API add_User. Código HTTP: ${response.status.value}"))
            }

        } catch (e: Exception) {
            Log.e("API_ERROR_ADD_USER", "💥 Ha fallado la petición HTTP", e)
            return Result.failure(e)
        }
    }

    override suspend fun getUserBySaguntinoCode(code: String): Result<UserResponse> {
        return try{
            val response = httpClient.get("api/users/saguntino_code/${code}") {
                contentType(ContentType.Application.Json)
            }

            if(response.status.isSuccess()){
                val responseData = response.body<UserResponse>()
                Result.success(responseData)
            }else{
                Result.failure(Exception("Fallo en la API get_User. Código HTTP: ${response.status.value}"))
            }
        }catch (e: Exception) {
            Log.e("API_ERROR_GET_USER", "💥 Ha fallado la petición HTTP", e)
            return Result.failure(e)
        }
    }

    override suspend fun getUserByName(name: String): Result<List<UserResponse>> {
        return try{
            val response = httpClient.get("api/users/name/${name}") {
                contentType(ContentType.Application.Json)
            }

            if(response.status.isSuccess()){
                val responseData = response.body<List<UserResponse>>()
                Result.success(responseData)
            }else{
                Result.failure(Exception("Fallo en la API get_User. Código HTTP: ${response.status.value}"))
            }
        }catch (e: Exception) {
            Log.e("API_ERROR_GET_USER", "💥 Ha fallado la petición HTTP", e)
            return Result.failure(e)
        }
    }

    override suspend fun searchUsers(query: String): SearchUsersResponse {
        if (query.isEmpty()) return SearchUsersResponse.Error("El campo de búsqueda está vacío")
        query.trim()
        return try {
            if (saguntinoCodeRegex.matches(query)) {
                val response = httpClient.get("api/users/saguntino_code/${query.uppercase()}") {
                    contentType(ContentType.Application.Json)
                }

                if (response.status.isSuccess())
                    SearchUsersResponse.SingleResult(response.body())
                else
                    SearchUsersResponse.Error("No existe ningún saguntino con ese código")

            } else {
                val response = httpClient.get("api/users/name/$query") {
                    contentType(ContentType.Application.Json)
                }

                if (response.status.isSuccess())
                    SearchUsersResponse.MultipleResults(response.body())
                else
                    SearchUsersResponse.Error("Error al buscar usuarios por nombre en el servidor")
            }
        } catch (e: Exception) {
            Log.e("API_ERROR_GET_USER", "💥 Ha fallado la petición HTTP", e)
            SearchUsersResponse.Error("Error crítico de conexión: ${e.localizedMessage}")
        }
    }
}
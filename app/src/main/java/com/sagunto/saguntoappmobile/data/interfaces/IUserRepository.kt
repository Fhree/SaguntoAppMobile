package com.sagunto.saguntoappmobile.data.interfaces

import com.sagunto.saguntoappmobile.data.network.dto.createUser.*
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.SearchUsersResponse
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.UserResponse

interface IUserRepository {
    suspend fun addUser(user: CreateUserRequest): Result<CreateUserResponse>
    suspend fun getUserByName(name: String): Result<List<UserResponse>>
    suspend fun getUserBySaguntinoCode(code: String): Result<UserResponse>

    suspend fun searchUsers(query: String): SearchUsersResponse
}
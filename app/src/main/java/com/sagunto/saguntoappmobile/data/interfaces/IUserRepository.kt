package com.sagunto.saguntoappmobile.data.interfaces

import com.sagunto.saguntoappmobile.data.network.dto.createUser.*
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.SearchUsersResponse
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.UserResponse
import kotlinx.coroutines.flow.StateFlow // 🛠️ IMPORTANTE: No te olvides de esta importación

interface IUserRepository {

    val userRole: StateFlow<Int?>

    suspend fun fetchUserProfile(jwtToken: String): Result<Unit>
    suspend fun createUser(user: CreateUserRequest, jwtToken: String): Result<CreateUserResponse>
    suspend fun addOfflineUser(user: CreateUserRequest, jwtToken: String): Result<CreateUserResponse>
    suspend fun getUserByName(name: String): Result<List<UserResponse>>
    suspend fun getUserBySaguntinoCode(code: String): Result<UserResponse>
    suspend fun searchUsers(query: String): SearchUsersResponse
}
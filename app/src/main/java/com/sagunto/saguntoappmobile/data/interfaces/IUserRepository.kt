package com.sagunto.saguntoappmobile.data.interfaces

import com.sagunto.saguntoappmobile.data.network.dto.createOfflineUser.*
import com.sagunto.saguntoappmobile.data.network.dto.searchUsers.*
import com.sagunto.saguntoappmobile.data.network.dto.userProfile.UserProfileResponse
import com.sagunto.saguntoappmobile.data.network.dto.userRegister.*
import kotlinx.coroutines.flow.StateFlow

interface IUserRepository {

    val userRole: StateFlow<Int?>

    suspend fun fetchUserProfile(): Result<UserProfileResponse>
    suspend fun userRegister(user: UserRegisterRequest): Result<UserRegisterResponse>
    suspend fun addOfflineUser(user: CreateOfflineUserRequest): Result<CreateOfflineUserResponse>
    suspend fun getUserByName(name: String): Result<List<UserResponse>>
    suspend fun getUserBySaguntinoCode(code: String): Result<UserResponse>
    suspend fun searchUsers(query: String): SearchUsersResponse
}

package com.sagunto.saguntoappmobile.domain.interfaces

import com.sagunto.saguntoappmobile.data.network.dto.createUser.*
import com.sagunto.saguntoappmobile.data.network.dto.getUserByNameOrSaguntinoCode.GetUserByNameOrSaguntinoCodeResponse

interface IUserRepository {
    suspend fun addUser(user: CreateUserRequest): Result<CreateUserResponse>
    suspend fun getUserByName(name: String): Result<List<GetUserByNameOrSaguntinoCodeResponse>>
    suspend fun getUserBySaguntinoCode(code: String): Result<GetUserByNameOrSaguntinoCodeResponse>
}
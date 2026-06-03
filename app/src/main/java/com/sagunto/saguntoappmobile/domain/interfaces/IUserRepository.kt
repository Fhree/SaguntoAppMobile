package com.sagunto.saguntoappmobile.domain.interfaces

import com.sagunto.saguntoappmobile.data.network.dto.createUser.*

interface IUserRepository {
    suspend fun addUser(user: CreateUserRequest): Result<CreateUserResponse>
}
package com.sagunto.saguntoappmobile.domain.interfaces

import com.sagunto.saguntoappmobile.domain.models.User

interface IUserRepository {
    suspend fun addUser(user: User): Result<Unit>
}
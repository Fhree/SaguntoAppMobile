package com.sagunto.saguntoappmobile.data.interfaces

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

interface IAuthRepository {
    val currentUser: StateFlow<FirebaseUser?>

    fun logout()

    suspend fun getJwtToken(refresh: Boolean = false): String?

    suspend fun registerUser(email: String, password: String): Result<Unit>
    suspend fun loginWithEmail(email: String, password: String): Boolean
    suspend fun deleteUserWithEmail(email: String): Boolean
}
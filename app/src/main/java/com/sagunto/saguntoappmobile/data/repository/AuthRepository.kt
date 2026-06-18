package com.sagunto.saguntoappmobile.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sagunto.saguntoappmobile.data.interfaces.IAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class AuthRepository: IAuthRepository {
    private val auth = FirebaseAuth.getInstance()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    override val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    override fun logout() {
        auth.signOut()
    }

    override suspend fun getJwtToken(refresh: Boolean): String? {
        return try {
            auth.currentUser?.getIdToken(refresh)?.await()?.token
        } catch (e: Exception) {
            Log.e("AUTH_ERROR", "Error al obtener el token JWT", e)
            null
        }
    }

    override suspend fun registerUser(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)

        } catch (e: Exception) {
            Log.e("AUTH_ERROR_FIREBASE", "💥 Error al crear el usuario en los servidores de Google", e)
            Result.failure(e)
        }
    }
}
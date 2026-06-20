package com.sagunto.saguntoappmobile.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sagunto.saguntoappmobile.data.interfaces.IAuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
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

    override suspend fun loginWithEmail(email: String, password: String): Boolean{
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            Log.e("FIREBASE_AUTH", "💥 Fallo en loginWithEmail", e)
            false
        }
    }

    override suspend fun deleteUserWithEmail(email: String): Boolean {
        return try {
            val user = auth.currentUser

            if (user != null && user.email == email) {
                user.delete().await()
                true
            } else {
                Log.w("FIREBASE_AUTH", "⚠️ Cancelado: El email proporcionado no coincide con el usuario activo o no hay sesión.")
                false
            }
        } catch (e: Exception) {
            Log.e("FIREBASE_AUTH", "💥 Fallo crítico al intentar borrar el usuario de Firebase", e)
            false
        }
    }
}
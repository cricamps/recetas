package com.example.recetas.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * Servicio de autenticación con Firebase Auth.
 * Maneja login, registro y cierre de sesión de usuarios.
 * Utiliza extensiones KTX para código Kotlin idiomático.
 */
object AuthService {

    // Instancia de Firebase Auth usando extensión KTX
    private val auth: FirebaseAuth = Firebase.auth

    /**
     * Retorna el usuario actualmente autenticado, o null si no hay sesión.
     */
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /**
     * Verifica si hay un usuario con sesión activa.
     */
    val isLoggedIn: Boolean
        get() = auth.currentUser != null

    /**
     * Registra un nuevo usuario con correo y contraseña.
     * @return Result con el FirebaseUser o excepción en caso de error
     */
    suspend fun registrar(email: String, password: String): Result<FirebaseUser> {
        return try {
            val resultado = auth.createUserWithEmailAndPassword(email, password).await()
            val user = resultado.user ?: throw Exception("Error al crear usuario")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Inicia sesión con correo y contraseña.
     * @return Result con el FirebaseUser o excepción en caso de error
     */
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val resultado = auth.signInWithEmailAndPassword(email, password).await()
            val user = resultado.user ?: throw Exception("Error al iniciar sesión")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    fun logout() {
        auth.signOut()
    }

    /**
     * Envía correo de recuperación de contraseña.
     * @return Result<Unit> indicando éxito o error
     */
    suspend fun recuperarPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retorna el email del usuario actual.
     */
    fun getEmailActual(): String {
        return auth.currentUser?.email ?: ""
    }

    /**
     * Retorna el UID del usuario actual.
     */
    fun getUidActual(): String {
        return auth.currentUser?.uid ?: ""
    }
}

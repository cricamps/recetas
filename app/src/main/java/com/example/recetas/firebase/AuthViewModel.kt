package com.example.recetas.firebase

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI para autenticación.
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val userEmail: String = ""
)

/**
 * ViewModel de autenticación Firebase.
 * Coordina AuthService y FirestoreService con la UI.
 * Usa coroutines y StateFlow para estado reactivo.
 */
class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        // Verificar si hay sesión activa al iniciar
        if (AuthService.isLoggedIn) {
            _uiState.value = AuthUiState(
                isLoggedIn = true,
                userEmail = AuthService.getEmailActual()
            )
        }
    }

    /**
     * Registra un nuevo usuario con Firebase Auth y guarda su perfil en Firestore.
     */
    fun registrar(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val resultado = AuthService.registrar(email, password)

            resultado.fold(
                onSuccess = { user ->
                    // Guardar perfil en Firestore
                    val datosUsuario = mapOf(
                        "nombre" to nombre,
                        "email" to email,
                        "uid" to user.uid,
                        "fechaRegistro" to System.currentTimeMillis(),
                        "activo" to true
                    )
                    FirestoreService.guardarUsuario(user.uid, datosUsuario)

                    // Registrar dispositivo
                    registrarDispositivoActual(user.uid)

                    _uiState.value = AuthUiState(
                        isLoggedIn = true,
                        userEmail = email,
                        successMessage = "¡Cuenta creada exitosamente!"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = traducirError(error.message ?: "Error desconocido")
                    )
                }
            )
        }
    }

    /**
     * Inicia sesión con Firebase Auth.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val resultado = AuthService.login(email, password)

            resultado.fold(
                onSuccess = { user ->
                    // Actualizar último acceso en Firestore
                    FirestoreService.actualizarUsuario(
                        user.uid,
                        mapOf("ultimoAcceso" to System.currentTimeMillis())
                    )

                    _uiState.value = AuthUiState(
                        isLoggedIn = true,
                        userEmail = email,
                        successMessage = "¡Bienvenido!"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = traducirError(error.message ?: "Error desconocido")
                    )
                }
            )
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    fun logout() {
        AuthService.logout()
        _uiState.value = AuthUiState(isLoggedIn = false)
    }

    /**
     * Recupera la contraseña por correo.
     */
    fun recuperarPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val resultado = AuthService.recuperarPassword(email)

            resultado.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Correo de recuperación enviado a $email"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = traducirError(error.message ?: "Error desconocido")
                    )
                }
            )
        }
    }

    /**
     * Guarda una receta como favorita en Firestore.
     */
    fun guardarFavorito(recetaId: String, nombreReceta: String) {
        val uid = AuthService.getUidActual()
        if (uid.isEmpty()) return

        viewModelScope.launch {
            val datos = mapOf(
                "recetaId" to recetaId,
                "nombre" to nombreReceta,
                "fechaGuardado" to System.currentTimeMillis()
            )
            FirestoreService.guardarRecetaFavorita(uid, recetaId, datos)
        }
    }

    /**
     * Elimina una receta de favoritos en Firestore.
     */
    fun eliminarFavorito(recetaId: String) {
        val uid = AuthService.getUidActual()
        if (uid.isEmpty()) return

        viewModelScope.launch {
            FirestoreService.eliminarRecetaFavorita(uid, recetaId)
        }
    }

    /**
     * Registra el dispositivo actual del usuario en Firestore.
     */
    private suspend fun registrarDispositivoActual(uid: String) {
        val infoDispositivo = mapOf(
            "marca" to Build.MANUFACTURER,
            "modelo" to Build.MODEL,
            "version_android" to Build.VERSION.RELEASE,
            "sdk" to Build.VERSION.SDK_INT,
            "fechaRegistro" to System.currentTimeMillis()
        )
        FirestoreService.registrarDispositivo(uid, infoDispositivo)
    }

    /**
     * Limpia los mensajes de error o éxito.
     */
    fun limpiarMensajes() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    /**
     * Traduce errores de Firebase al español.
     */
    private fun traducirError(mensaje: String): String {
        return when {
            mensaje.contains("email address is already in use") ->
                "Este correo ya está registrado"
            mensaje.contains("password is invalid") || mensaje.contains("wrong-password") ->
                "Contraseña incorrecta"
            mensaje.contains("no user record") || mensaje.contains("user-not-found") ->
                "No existe una cuenta con este correo"
            mensaje.contains("badly formatted") || mensaje.contains("invalid-email") ->
                "Formato de correo inválido"
            mensaje.contains("password should be at least") ->
                "La contraseña debe tener al menos 6 caracteres"
            mensaje.contains("network") || mensaje.contains("Network") ->
                "Error de conexión. Verifica tu internet"
            mensaje.contains("too-many-requests") ->
                "Demasiados intentos. Espera unos minutos"
            else -> "Error: $mensaje"
        }
    }
}

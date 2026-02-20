package com.example.recetas.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * Servicio de base de datos con Cloud Firestore.
 * Maneja operaciones CRUD para usuarios, recetas y minutas.
 * Usa extensiones KTX para código Kotlin idiomático.
 */
object FirestoreService {

    // Instancia de Firestore usando extensión KTX
    private val db: FirebaseFirestore = Firebase.firestore

    // Nombres de colecciones
    private const val COL_USUARIOS = "usuarios"
    private const val COL_RECETAS = "recetas"
    private const val COL_MINUTAS = "minutas"
    private const val COL_DISPOSITIVOS = "dispositivos"

    // ==================== CRUD USUARIOS ====================

    /**
     * Guarda o actualiza el perfil de un usuario en Firestore.
     */
    suspend fun guardarUsuario(uid: String, datos: Map<String, Any>): Result<Unit> {
        return try {
            db.collection(COL_USUARIOS).document(uid).set(datos).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene los datos de un usuario por su UID.
     */
    suspend fun obtenerUsuario(uid: String): Result<Map<String, Any>> {
        return try {
            val doc = db.collection(COL_USUARIOS).document(uid).get().await()
            if (doc.exists()) {
                Result.success(doc.data ?: emptyMap())
            } else {
                Result.failure(Exception("Usuario no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza campos específicos del perfil de un usuario.
     */
    suspend fun actualizarUsuario(uid: String, campos: Map<String, Any>): Result<Unit> {
        return try {
            db.collection(COL_USUARIOS).document(uid).update(campos).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina un usuario de Firestore.
     */
    suspend fun eliminarUsuario(uid: String): Result<Unit> {
        return try {
            db.collection(COL_USUARIOS).document(uid).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== CRUD RECETAS ====================

    /**
     * Guarda una receta favorita del usuario en Firestore.
     */
    suspend fun guardarRecetaFavorita(uid: String, recetaId: String, datos: Map<String, Any>): Result<Unit> {
        return try {
            db.collection(COL_USUARIOS)
                .document(uid)
                .collection(COL_RECETAS)
                .document(recetaId)
                .set(datos)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene todas las recetas favoritas de un usuario.
     */
    suspend fun obtenerRecetasFavoritas(uid: String): Result<List<Map<String, Any>>> {
        return try {
            val snapshot = db.collection(COL_USUARIOS)
                .document(uid)
                .collection(COL_RECETAS)
                .get()
                .await()
            val recetas = snapshot.documents.map { it.data ?: emptyMap() }
            Result.success(recetas)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina una receta favorita del usuario.
     */
    suspend fun eliminarRecetaFavorita(uid: String, recetaId: String): Result<Unit> {
        return try {
            db.collection(COL_USUARIOS)
                .document(uid)
                .collection(COL_RECETAS)
                .document(recetaId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== CRUD MINUTA ====================

    /**
     * Guarda la minuta semanal de un usuario en Firestore.
     */
    suspend fun guardarMinuta(uid: String, semana: String, datos: Map<String, Any>): Result<Unit> {
        return try {
            db.collection(COL_MINUTAS)
                .document("${uid}_${semana}")
                .set(datos)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene la minuta de una semana específica del usuario.
     */
    suspend fun obtenerMinuta(uid: String, semana: String): Result<Map<String, Any>> {
        return try {
            val doc = db.collection(COL_MINUTAS)
                .document("${uid}_${semana}")
                .get()
                .await()
            if (doc.exists()) {
                Result.success(doc.data ?: emptyMap())
            } else {
                Result.failure(Exception("Minuta no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== BUSCAR DISPOSITIVO ====================

    /**
     * Registra información del dispositivo del usuario en Firestore.
     */
    suspend fun registrarDispositivo(uid: String, infoDispositivo: Map<String, Any>): Result<Unit> {
        return try {
            db.collection(COL_DISPOSITIVOS)
                .document(uid)
                .set(infoDispositivo)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene la información del dispositivo registrado de un usuario.
     */
    suspend fun obtenerDispositivo(uid: String): Result<Map<String, Any>> {
        return try {
            val doc = db.collection(COL_DISPOSITIVOS).document(uid).get().await()
            if (doc.exists()) {
                Result.success(doc.data ?: emptyMap())
            } else {
                Result.failure(Exception("Dispositivo no registrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

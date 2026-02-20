package com.example.recetas.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

/**
 * CONTENT PROVIDER - RecetasContentProvider
 *
 * Implementa android.content.ContentProvider para exponer datos de recetas
 * chilenas a otras aplicaciones o componentes mediante URIs estándar.
 *
 * URI de acceso: content://com.example.recetas.provider/recetas
 *
 * Métodos implementados:
 * - query()   → Recuperar recetas (todas o por ID)
 * - insert()  → Agregar nueva receta
 * - update()  → Actualizar receta existente
 * - delete()  → Eliminar receta
 * - getType() → Devolver tipo MIME del URI
 */
class RecetasContentProvider : ContentProvider() {

    companion object {
        // Autoridad del Content Provider (debe coincidir con AndroidManifest.xml)
        const val AUTHORITY = "com.example.recetas.provider"

        // URIs de acceso
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/recetas")

        // Tipos MIME
        const val MIME_TYPE_DIR  = "vnd.android.cursor.dir/vnd.$AUTHORITY.recetas"
        const val MIME_TYPE_ITEM = "vnd.android.cursor.item/vnd.$AUTHORITY.receta"

        // Columnas disponibles
        const val COLUMN_ID          = "_id"
        const val COLUMN_NOMBRE      = "nombre"
        const val COLUMN_DESCRIPCION = "descripcion"
        const val COLUMN_CATEGORIA   = "categoria"
        const val COLUMN_ORIGEN      = "origen"
        const val COLUMN_DIFICULTAD  = "dificultad"
        const val COLUMN_TIEMPO      = "tiempo_preparacion"

        // Códigos para UriMatcher
        private const val CODE_RECETAS    = 1
        private const val CODE_RECETA_ID  = 2

        // UriMatcher para distinguir consultas a toda la tabla vs. un ítem
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "recetas",    CODE_RECETAS)
            addURI(AUTHORITY, "recetas/#",  CODE_RECETA_ID)
        }

        // Datos en memoria (simula BD local con recetas chilenas)
        private val recetasData = mutableListOf(
            mapOf(
                COLUMN_ID          to "1",
                COLUMN_NOMBRE      to "Charquicán",
                COLUMN_DESCRIPCION to "Guiso tradicional chileno con charqui, papas y verduras",
                COLUMN_CATEGORIA   to "Plato principal",
                COLUMN_ORIGEN      to "Chile",
                COLUMN_DIFICULTAD  to "Fácil",
                COLUMN_TIEMPO      to "45 min"
            ),
            mapOf(
                COLUMN_ID          to "2",
                COLUMN_NOMBRE      to "Pastel de Papas",
                COLUMN_DESCRIPCION to "Pastel horneado con carne molida y puré de papas",
                COLUMN_CATEGORIA   to "Plato principal",
                COLUMN_ORIGEN      to "Chile",
                COLUMN_DIFICULTAD  to "Media",
                COLUMN_TIEMPO      to "60 min"
            ),
            mapOf(
                COLUMN_ID          to "3",
                COLUMN_NOMBRE      to "Cazuela",
                COLUMN_DESCRIPCION to "Sopa espesa chilena con carne, papas y verduras de estación",
                COLUMN_CATEGORIA   to "Sopa",
                COLUMN_ORIGEN      to "Chile",
                COLUMN_DIFICULTAD  to "Fácil",
                COLUMN_TIEMPO      to "90 min"
            ),
            mapOf(
                COLUMN_ID          to "4",
                COLUMN_NOMBRE      to "Empanadas de Pino",
                COLUMN_DESCRIPCION to "Empanadas horneadas rellenas con pino de carne, cebolla, huevo y aceitunas",
                COLUMN_CATEGORIA   to "Entrada",
                COLUMN_ORIGEN      to "Chile",
                COLUMN_DIFICULTAD  to "Media",
                COLUMN_TIEMPO      to "120 min"
            ),
            mapOf(
                COLUMN_ID          to "5",
                COLUMN_NOMBRE      to "Porotos Granados",
                COLUMN_DESCRIPCION to "Guiso de verano con porotos, zapallo, choclo y albahaca",
                COLUMN_CATEGORIA   to "Plato principal",
                COLUMN_ORIGEN      to "Chile",
                COLUMN_DIFICULTAD  to "Fácil",
                COLUMN_TIEMPO      to "50 min"
            )
        )
    }

    override fun onCreate(): Boolean {
        // Inicialización exitosa del provider
        return true
    }

    /**
     * Consulta recetas desde el Content Provider.
     * Soporta consulta a toda la colección o a una receta específica por ID.
     */
    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        val columnas = projection ?: arrayOf(
            COLUMN_ID, COLUMN_NOMBRE, COLUMN_DESCRIPCION,
            COLUMN_CATEGORIA, COLUMN_ORIGEN, COLUMN_DIFICULTAD, COLUMN_TIEMPO
        )
        val cursor = MatrixCursor(columnas)

        val filas = when (uriMatcher.match(uri)) {
            CODE_RECETAS   -> recetasData
            CODE_RECETA_ID -> {
                val id = uri.lastPathSegment
                recetasData.filter { it[COLUMN_ID] == id }
            }
            else -> throw IllegalArgumentException("URI desconocida: $uri")
        }

        filas.forEach { fila ->
            cursor.addRow(columnas.map { col -> fila[col] ?: "" })
        }

        return cursor
    }

    /**
     * Devuelve el tipo MIME según si es una colección o un ítem individual.
     */
    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            CODE_RECETAS   -> MIME_TYPE_DIR
            CODE_RECETA_ID -> MIME_TYPE_ITEM
            else           -> throw IllegalArgumentException("URI desconocida: $uri")
        }
    }

    /**
     * Inserta una nueva receta en la colección.
     * Retorna la URI del elemento insertado.
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (uriMatcher.match(uri) != CODE_RECETAS) {
            throw IllegalArgumentException("Inserción no permitida en URI: $uri")
        }
        val nuevoId = (recetasData.size + 1).toString()
        val nuevaReceta = mapOf(
            COLUMN_ID          to nuevoId,
            COLUMN_NOMBRE      to (values?.getAsString(COLUMN_NOMBRE)      ?: ""),
            COLUMN_DESCRIPCION to (values?.getAsString(COLUMN_DESCRIPCION) ?: ""),
            COLUMN_CATEGORIA   to (values?.getAsString(COLUMN_CATEGORIA)   ?: ""),
            COLUMN_ORIGEN      to (values?.getAsString(COLUMN_ORIGEN)      ?: "Chile"),
            COLUMN_DIFICULTAD  to (values?.getAsString(COLUMN_DIFICULTAD)  ?: "Fácil"),
            COLUMN_TIEMPO      to (values?.getAsString(COLUMN_TIEMPO)      ?: "30 min")
        )
        recetasData.add(nuevaReceta)
        context?.contentResolver?.notifyChange(uri, null)
        return ContentUris.withAppendedId(CONTENT_URI, nuevoId.toLong())
    }

    /**
     * Elimina receta(s) según el URI o criterio de selección.
     * Retorna el número de filas eliminadas.
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val eliminados: Int
        when (uriMatcher.match(uri)) {
            CODE_RECETA_ID -> {
                val id = uri.lastPathSegment
                val tamanoAntes = recetasData.size
                recetasData.removeAll { it[COLUMN_ID] == id }
                eliminados = tamanoAntes - recetasData.size
            }
            CODE_RECETAS -> {
                eliminados = recetasData.size
                recetasData.clear()
            }
            else -> throw IllegalArgumentException("URI desconocida: $uri")
        }
        if (eliminados > 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }
        return eliminados
    }

    /**
     * Actualiza receta(s) según el URI o criterio de selección.
     * Retorna el número de filas actualizadas.
     */
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        if (uriMatcher.match(uri) != CODE_RECETA_ID) {
            throw IllegalArgumentException("Actualización solo permitida por ID: $uri")
        }
        val id = uri.lastPathSegment
        var actualizados = 0
        val indice = recetasData.indexOfFirst { it[COLUMN_ID] == id }
        if (indice >= 0) {
            val recetaActual = recetasData[indice].toMutableMap()
            values?.let {
                if (it.containsKey(COLUMN_NOMBRE))      recetaActual[COLUMN_NOMBRE]      = it.getAsString(COLUMN_NOMBRE)
                if (it.containsKey(COLUMN_DESCRIPCION)) recetaActual[COLUMN_DESCRIPCION] = it.getAsString(COLUMN_DESCRIPCION)
                if (it.containsKey(COLUMN_CATEGORIA))   recetaActual[COLUMN_CATEGORIA]   = it.getAsString(COLUMN_CATEGORIA)
                if (it.containsKey(COLUMN_ORIGEN))      recetaActual[COLUMN_ORIGEN]      = it.getAsString(COLUMN_ORIGEN)
                if (it.containsKey(COLUMN_DIFICULTAD))  recetaActual[COLUMN_DIFICULTAD]  = it.getAsString(COLUMN_DIFICULTAD)
                if (it.containsKey(COLUMN_TIEMPO))      recetaActual[COLUMN_TIEMPO]      = it.getAsString(COLUMN_TIEMPO)
            }
            recetasData[indice] = recetaActual
            actualizados = 1
            context?.contentResolver?.notifyChange(uri, null)
        }
        return actualizados
    }
}

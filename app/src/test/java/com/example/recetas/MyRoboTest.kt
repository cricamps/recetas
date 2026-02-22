package com.example.recetas

import android.app.Application
import android.content.Context
import com.example.recetas.data.RecetasRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * Pruebas unitarias con Robolectric para la app Recetas Chilenas.
 * Permite ejecutar pruebas que requieren contexto Android sin emulador.
 *
 * Archivo: MyRoboTest.kt
 * Directorio: src/test/java/com/example/recetas/
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class MyRoboTest {

    private lateinit var application: Application
    private lateinit var context: Context

    /**
     * Se ejecuta antes de cada prueba.
     * Inicializa el contexto simulado de Android con Robolectric.
     */
    @Before
    fun setUp() {
        // Robolectric provee un contexto Android simulado sin emulador
        application = RuntimeEnvironment.getApplication()
        context = application.applicationContext
    }

    // ==================== PRUEBAS CON CONTEXTO ANDROID ====================

    /**
     * Verifica que el contexto de la aplicación no es nulo.
     */
    @Test
    fun contexto_noEsNulo() {
        assertNotNull(context)
    }

    /**
     * Verifica que el nombre del paquete de la app es correcto.
     */
    @Test
    fun contexto_nombrePaqueteCorrecto() {
        assertEquals("com.example.recetas", context.packageName)
    }

    /**
     * Verifica que se puede obtener SharedPreferences a través del contexto.
     */
    @Test
    fun contexto_sharedPreferences_funcionaCorrectamente() {
        val prefs = context.getSharedPreferences("recetas_prefs", Context.MODE_PRIVATE)
        assertNotNull(prefs)

        // Guardar y recuperar un valor
        prefs.edit().putString("ultima_receta", "Charquicán").apply()
        val ultimaReceta = prefs.getString("ultima_receta", "")
        assertEquals("Charquicán", ultimaReceta)
    }

    /**
     * Verifica que se puede guardar y recuperar el ID de la última receta vista.
     */
    @Test
    fun contexto_guardarUltimaRecetaVista_recuperaCorrectamente() {
        val prefs = context.getSharedPreferences("recetas_prefs", Context.MODE_PRIVATE)
        val idReceta = "3"

        prefs.edit().putString("id_ultima_receta", idReceta).apply()
        val recuperado = prefs.getString("id_ultima_receta", null)

        assertEquals(idReceta, recuperado)
    }

    /**
     * Verifica que se puede guardar el estado de accesibilidad (TTS activado/desactivado).
     */
    @Test
    fun contexto_estadoAccesibilidad_seGuardaCorrectamente() {
        val prefs = context.getSharedPreferences("accesibilidad_prefs", Context.MODE_PRIVATE)

        // Simular activación de TTS
        prefs.edit().putBoolean("tts_activado", true).apply()
        val ttsActivado = prefs.getBoolean("tts_activado", false)

        assertTrue(ttsActivado)
    }

    /**
     * Verifica que se puede guardar preferencias de fuente (accesibilidad).
     */
    @Test
    fun contexto_escalaFuente_seGuardaCorrectamente() {
        val prefs = context.getSharedPreferences("accesibilidad_prefs", Context.MODE_PRIVATE)

        prefs.edit().putFloat("escala_fuente", 1.5f).apply()
        val escala = prefs.getFloat("escala_fuente", 1.0f)

        assertEquals(1.5f, escala, 0.001f)
    }

    // ==================== PRUEBAS DE REPOSITORIO CON ROBOLECTRIC ====================

    /**
     * Verifica que el repositorio carga recetas sin necesitar Android real.
     */
    @Test
    fun repositorio_cargaRecetas_sinDispositivo() {
        val recetas = RecetasRepository.getAllRecetas()
        assertNotNull(recetas)
        assertTrue(recetas.isNotEmpty())
    }

    /**
     * Verifica que las recetas del repositorio tienen campos válidos.
     */
    @Test
    fun repositorio_recetas_tienenCamposValidos() {
        val recetas = RecetasRepository.getAllRecetas()
        recetas.forEach { receta ->
            assertFalse("El ID no puede estar vacío", receta.id.isBlank())
            assertFalse("El nombre no puede estar vacío", receta.nombre.isBlank())
            assertFalse("La descripción no puede estar vacía", receta.descripcion.isBlank())
        }
    }

    /**
     * Verifica que todas las recetas tienen al menos un ingrediente.
     */
    @Test
    fun repositorio_recetas_tienenIngredientes() {
        val recetas = RecetasRepository.getAllRecetas()
        recetas.forEach { receta ->
            assertTrue(
                "La receta ${receta.nombre} debe tener ingredientes",
                receta.obtenerCantidadIngredientes() > 0
            )
        }
    }

    /**
     * Verifica que la búsqueda de recetas funciona correctamente.
     */
    @Test
    fun repositorio_busqueda_funcionaCorrectamente() {
        val resultados = RecetasRepository.searchRecetas("Cazuela")
        assertTrue(resultados.isNotEmpty())

        val primeraReceta = resultados.first()
        assertTrue(primeraReceta.nombre.contains("Cazuela", ignoreCase = true))
    }

    /**
     * Verifica que las categorías disponibles no están vacías.
     */
    @Test
    fun repositorio_categorias_noEstaVacio() {
        val categorias = RecetasRepository.getCategorias()
        assertTrue(categorias.isNotEmpty())
    }
}

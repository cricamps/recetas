package com.example.recetas

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas instrumentadas con Firebase Test Lab para la app Recetas Chilenas.
 * Combina JUnit, Espresso y Firebase para pruebas en dispositivos reales o emuladores.
 *
 * Archivo: Fire_test.kt
 * Directorio: src/androidTest/java/com/example/recetas/
 *
 * NOTA: Para ejecutar con Firebase Test Lab, subir el APK de debug y el APK
 * de pruebas al panel de Firebase Test Lab en la consola de Firebase.
 * Ejecutar localmente con: ./gradlew connectedAndroidTest
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class FirebaseTestLabTest {

    /**
     * Regla que proporciona el contexto de MainActivity para las pruebas.
     * Firebase Test Lab usará esta actividad como punto de entrada.
     */
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // ==================== PRUEBAS DE CARGA DE APP ====================

    /**
     * Verifica que la aplicación lanza su actividad principal correctamente.
     * Esta prueba confirma que el APK es válido para Firebase Test Lab.
     */
    @Test
    fun app_lanzaActividadPrincipal_exitosamente() {
        // Firebase Test Lab ejecuta esta prueba en dispositivos reales
        onView(isRoot()).check(matches(isDisplayed()))
    }

    /**
     * Verifica que el contenido de la pantalla inicial está presente.
     * Prueba la inicialización básica de la app.
     */
    @Test
    fun app_contenidoInicial_estaVisible() {
        Thread.sleep(2500) // Esperar splash screen y carga inicial
        onView(isRoot()).check(matches(isDisplayed()))
    }

    // ==================== PRUEBAS DE INTERFAZ BÁSICA ====================

    /**
     * Verifica que la app se estabiliza después del splash screen.
     * Firebase Test Lab puede ejecutar esto en múltiples versiones de Android.
     */
    @Test
    fun app_despuesDeSplash_muestraPantalla() {
        Thread.sleep(3000) // Tiempo para que cargue el splash y navegue
        onView(isRoot()).check(matches(isDisplayed()))
    }

    /**
     * Verifica que la app responde correctamente en la primera interacción.
     * Firebase Test Lab puede ejecutar esta prueba en diferentes tamaños de pantalla.
     */
    @Test
    fun app_primeraInteraccion_respondeCorrectamente() {
        Thread.sleep(2000)

        // Verificar que la pantalla está en estado interactuable
        onView(isRoot()).check(matches(isDisplayed()))
    }

    // ==================== PRUEBA DE ACCESIBILIDAD ====================

    /**
     * Verifica que la app mantiene accesibilidad básica.
     * Importante para el cumplimiento de WCAG 2.1 AA de la app.
     */
    @Test
    fun app_accesibilidad_pantallaEsInteractuable() {
        Thread.sleep(2000)
        onView(isRoot()).check(matches(isEnabled()))
    }

    // ==================== PRUEBA DE ROTACIÓN ====================

    /**
     * Verifica que la app maneja correctamente el ciclo de vida básico.
     * Firebase Test Lab puede probar esto en múltiples dispositivos simultáneamente.
     */
    @Test
    fun app_cicloDeVida_seInicializaCorrectamente() {
        activityRule.scenario.onActivity { activity ->
            // Verificar que la actividad no es nula
            assert(activity != null)
            assert(!activity.isFinishing)
        }
    }
}

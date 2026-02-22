package com.example.recetas

import com.example.recetas.data.Receta
import com.example.recetas.data.RecetasRepository
import com.example.recetas.data.Usuario
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.Test

/**
 * Pruebas unitarias con JUnit para la app Recetas Chilenas.
 * Verifica el comportamiento de las clases del modelo de datos.
 *
 * Archivo: MyUnitTest.kt
 * Directorio: src/test/java/com/example/recetas/
 */
class MyUnitTest {

    // Instancia de receta de prueba
    private lateinit var recetaPrueba: Receta

    /**
     * Se ejecuta antes de cada prueba.
     * Inicializa los objetos necesarios.
     */
    @Before
    fun setUp() {
        recetaPrueba = Receta(
            id = "test_001",
            nombre = "Charquicán",
            descripcion = "Guiso tradicional chileno",
            tiempoPreparacion = "60 min",
            dificultad = "Fácil",
            ingredientes = listOf("papas", "carne molida", "zapallo"),
            preparacion = listOf("Picar verduras", "Dorar carne", "Cocinar todo junto")
        )
    }

    /**
     * Se ejecuta después de cada prueba.
     * Limpia los recursos utilizados.
     */
    @After
    fun tearDown() {
        // Limpieza de recursos si es necesario
    }

    // ==================== PRUEBAS DE RECETA ====================

    /**
     * Verifica que el nombre de la receta se almacena correctamente.
     */
    @Test
    fun receta_nombreEsCorrecto() {
        assertEquals("Charquicán", recetaPrueba.nombre)
    }

    /**
     * Verifica que el ID de la receta se almacena correctamente.
     */
    @Test
    fun receta_idEsCorrecto() {
        assertEquals("test_001", recetaPrueba.id)
    }

    /**
     * Verifica que el tiempo de preparación retorna correctamente.
     */
    @Test
    fun receta_tiempoPreparacionCorrecto() {
        assertEquals("60 min", recetaPrueba.obtenerTiempoPreparacion())
    }

    /**
     * Verifica que la dificultad retorna correctamente.
     */
    @Test
    fun receta_nivelDificultadCorrecto() {
        assertEquals("Fácil", recetaPrueba.obtenerNivelDificultad())
    }

    /**
     * Verifica que la lista de ingredientes tiene el tamaño correcto.
     */
    @Test
    fun receta_cantidadIngredientesCorrecta() {
        assertEquals(3, recetaPrueba.obtenerCantidadIngredientes())
    }

    /**
     * Verifica que la lista de pasos de preparación tiene el tamaño correcto.
     */
    @Test
    fun receta_cantidadPasosCorrecta() {
        assertEquals(3, recetaPrueba.obtenerCantidadPasos())
    }

    /**
     * Verifica que la receta NO es vegetariana (contiene carne molida).
     */
    @Test
    fun receta_noEsVegetariana_cuandoTieneCarne() {
        assertFalse(recetaPrueba.esVegetariana())
    }

    /**
     * Verifica que una receta SÍ es vegetariana cuando no tiene carne.
     */
    @Test
    fun receta_esVegetariana_cuandoNoTieneCarne() {
        val recetaVeg = Receta(
            id = "veg_001",
            nombre = "Ensalada Chilena",
            descripcion = "Ensalada fresca",
            tiempoPreparacion = "10 min",
            dificultad = "Fácil",
            ingredientes = listOf("tomate", "cebolla", "cilantro"),
            preparacion = listOf("Picar todo", "Aliñar")
        )
        assertTrue(recetaVeg.esVegetariana())
    }

    /**
     * Verifica que una receta rápida (menos de 40 min) se identifica correctamente.
     */
    @Test
    fun receta_esRapida_cuandoTiempoMenor40() {
        val recetaRapida = Receta(
            id = "rapid_001",
            nombre = "Arroz Graneado",
            descripcion = "Arroz suelto",
            tiempoPreparacion = "30 min",
            dificultad = "Fácil",
            ingredientes = listOf("arroz", "agua", "sal"),
            preparacion = listOf("Lavar arroz", "Cocinar")
        )
        assertTrue(recetaRapida.esRecetaRapida())
    }

    /**
     * Verifica que una receta lenta (más de 40 min) NO es rápida.
     */
    @Test
    fun receta_noEsRapida_cuandoTiempoMayor40() {
        assertFalse(recetaPrueba.esRecetaRapida()) // 60 min
    }

    // ==================== PRUEBAS DE REPOSITORIO ====================

    /**
     * Verifica que el repositorio retorna recetas.
     */
    @Test
    fun repositorio_retornaRecetas() {
        val recetas = RecetasRepository.getAllRecetas()
        assertTrue(recetas.isNotEmpty())
    }

    /**
     * Verifica que se puede buscar receta por ID correctamente.
     */
    @Test
    fun repositorio_buscaPorId_retornaRecetaCorrecta() {
        val receta = RecetasRepository.getRecetaById("1")
        assertNotNull(receta)
        assertEquals("Charquicán", receta?.nombre)
    }

    /**
     * Verifica que buscar por ID inexistente retorna null.
     */
    @Test
    fun repositorio_buscaPorIdInexistente_retornaNull() {
        val receta = RecetasRepository.getRecetaById("id_que_no_existe")
        assertNull(receta)
    }

    /**
     * Verifica que el repositorio retorna las categorías disponibles.
     */
    @Test
    fun repositorio_retornaCategorias() {
        val categorias = RecetasRepository.getCategorias()
        assertTrue(categorias.isNotEmpty())
        assertTrue(categorias.contains("Plato Principal"))
    }

    /**
     * Verifica que la búsqueda por texto funciona correctamente.
     */
    @Test
    fun repositorio_busquedaPorTexto_encuentraResultados() {
        val resultados = RecetasRepository.searchRecetas("Cazuela")
        assertTrue(resultados.isNotEmpty())
    }

    /**
     * Verifica que la búsqueda vacía retorna todas las recetas.
     */
    @Test
    fun repositorio_busquedaVacia_retornaTodas() {
        val todas = RecetasRepository.getAllRecetas()
        val busquedaVacia = RecetasRepository.searchRecetas("")
        assertEquals(todas.size, busquedaVacia.size)
    }

    // ==================== PRUEBAS DE USUARIO ====================

    /**
     * Verifica que una instancia de Usuario se crea correctamente.
     */
    @Test
    fun usuario_creaInstanciaCorrecta() {
        val usuario = Usuario(
            id = "user_001",
            nombre = "Cristóbal",
            email = "cri.camps@duocuc.cl",
            password = "pass123"
        )
        assertEquals("user_001", usuario.id)
        assertEquals("Cristóbal", usuario.nombre)
        assertEquals("cri.camps@duocuc.cl", usuario.email)
    }

    /**
     * Verifica que la suma básica es correcta (prueba de sanidad).
     */
    @Test
    fun suma_esCorrecta() {
        assertEquals(4, 2 + 2)
    }
}

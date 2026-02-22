package com.example.recetas

import com.example.recetas.data.Receta
import com.example.recetas.data.RecetasRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Pruebas unitarias con Mockito para la app Recetas Chilenas.
 * Simula dependencias externas para probar lógica de negocio de forma aislada.
 *
 * Archivo: MyMockTest.kt
 * Directorio: src/test/java/com/example/recetas/
 */
@RunWith(MockitoJUnitRunner.Silent::class)
class MyMockTest {

    // Objeto simulado (mock) del repositorio de recetas
    @Mock
    private lateinit var mockRepositorio: IRecetasRepository

    // Mock de una receta individual
    @Mock
    private lateinit var mockReceta: Receta

    /**
     * Se ejecuta antes de cada prueba.
     * Configura el comportamiento de los mocks.
     */
    @Before
    fun setUp() {
        // Configurar comportamiento del mock de receta
        `when`(mockReceta.nombre).thenReturn("Cazuela Simulada")
        `when`(mockReceta.obtenerTiempoPreparacion()).thenReturn("80 min")
        `when`(mockReceta.obtenerNivelDificultad()).thenReturn("Fácil")
        `when`(mockReceta.obtenerCantidadIngredientes()).thenReturn(8)
        `when`(mockReceta.esVegetariana()).thenReturn(false)
        `when`(mockReceta.esRecetaRapida()).thenReturn(false)

        // Configurar comportamiento del mock del repositorio
        `when`(mockRepositorio.getAllRecetas()).thenReturn(listOf(mockReceta))
        `when`(mockRepositorio.getRecetaById("1")).thenReturn(mockReceta)
        `when`(mockRepositorio.getRecetaById("999")).thenReturn(null)
        `when`(mockRepositorio.getCategorias()).thenReturn(listOf("Plato Principal", "Postre", "Ensalada"))
        `when`(mockRepositorio.searchRecetas("Cazuela")).thenReturn(listOf(mockReceta))
        `when`(mockRepositorio.searchRecetas("xyz_no_existe")).thenReturn(emptyList())
    }

    // ==================== PRUEBAS CON MOCK DE RECETA ====================

    /**
     * Verifica que el mock retorna el nombre configurado.
     */
    @Test
    fun mockReceta_retornaNombreSimulado() {
        assertEquals("Cazuela Simulada", mockReceta.nombre)
        verify(mockReceta).nombre
    }

    /**
     * Verifica que el mock retorna el tiempo de preparación configurado.
     */
    @Test
    fun mockReceta_retornaTiempoPreparacion() {
        val tiempo = mockReceta.obtenerTiempoPreparacion()
        assertEquals("80 min", tiempo)
        verify(mockReceta).obtenerTiempoPreparacion()
    }

    /**
     * Verifica que el mock retorna la dificultad configurada.
     */
    @Test
    fun mockReceta_retornaDificultad() {
        val dificultad = mockReceta.obtenerNivelDificultad()
        assertEquals("Fácil", dificultad)
    }

    /**
     * Verifica que el mock de receta indica correctamente que no es vegetariana.
     */
    @Test
    fun mockReceta_noEsVegetariana() {
        assertFalse(mockReceta.esVegetariana())
        verify(mockReceta).esVegetariana()
    }

    /**
     * Verifica que el mock de receta indica que no es rápida.
     */
    @Test
    fun mockReceta_noEsRecetaRapida() {
        assertFalse(mockReceta.esRecetaRapida())
    }

    // ==================== PRUEBAS CON MOCK DE REPOSITORIO ====================

    /**
     * Verifica que getAllRecetas() del mock retorna la lista configurada.
     */
    @Test
    fun mockRepositorio_getAllRecetas_retornaListaSimulada() {
        val recetas = mockRepositorio.getAllRecetas()
        assertFalse(recetas.isEmpty())
        assertEquals(1, recetas.size)
        verify(mockRepositorio).getAllRecetas()
    }

    /**
     * Verifica que getRecetaById con ID válido retorna la receta mock.
     */
    @Test
    fun mockRepositorio_getRecetaById_conIdValido_retornaReceta() {
        val receta = mockRepositorio.getRecetaById("1")
        assertNotNull(receta)
        verify(mockRepositorio).getRecetaById("1")
    }

    /**
     * Verifica que getRecetaById con ID inválido retorna null.
     */
    @Test
    fun mockRepositorio_getRecetaById_conIdInvalido_retornaNull() {
        val receta = mockRepositorio.getRecetaById("999")
        assertNull(receta)
        verify(mockRepositorio).getRecetaById("999")
    }

    /**
     * Verifica que getCategorias del mock retorna las categorías configuradas.
     */
    @Test
    fun mockRepositorio_getCategorias_retornaCategoriasSimuladas() {
        val categorias = mockRepositorio.getCategorias()
        assertEquals(3, categorias.size)
        assertTrue(categorias.contains("Plato Principal"))
        assertTrue(categorias.contains("Postre"))
    }

    /**
     * Verifica que la búsqueda por "Cazuela" retorna resultados.
     */
    @Test
    fun mockRepositorio_search_conTerminoValido_retornaResultados() {
        val resultados = mockRepositorio.searchRecetas("Cazuela")
        assertFalse(resultados.isEmpty())
        verify(mockRepositorio).searchRecetas("Cazuela")
    }

    /**
     * Verifica que la búsqueda por término inexistente retorna lista vacía.
     */
    @Test
    fun mockRepositorio_search_conTerminoInexistente_retornaVacio() {
        val resultados = mockRepositorio.searchRecetas("xyz_no_existe")
        assertTrue(resultados.isEmpty())
    }

    /**
     * Verifica que el repositorio real funciona sin mocks (integración básica).
     */
    @Test
    fun repositorioReal_getAllRecetas_retornaDatosReales() {
        val recetas = RecetasRepository.getAllRecetas()
        assertTrue(recetas.size >= 5)
    }
}

/**
 * Interfaz para el repositorio de recetas, permite crear mocks con Mockito.
 * Separa la implementación de la interfaz para facilitar el testing.
 */
interface IRecetasRepository {
    fun getAllRecetas(): List<Receta>
    fun getRecetaById(id: String): Receta?
    fun getCategorias(): List<String>
    fun searchRecetas(query: String): List<Receta>
    fun getRecetasPorCategoria(categoria: String): List<Receta>
}

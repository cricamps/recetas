package com.example.recetas.data

import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

/**
 * Repositorio que gestiona las minutas semanales con información nutricional.
 * Demuestra el uso de objetos singleton y colecciones en Kotlin.
 * 
 * Este repositorio proporciona una minuta semanal de 5 recetas chilenas tradicionales
 * con información nutricional completa y recomendaciones.
 * NUEVA CARACTERÍSTICA: Selección aleatoria semanal de recetas.
 */
object MinutasRepository {
    
    // Cache para almacenar minutas de diferentes semanas
    private val cacheminutas = mutableMapOf<Int, MinutaSemanal>()
    
    // Cache para recetas combinadas (por ID único)
    private val cacheRecetasCombinadas = mutableMapOf<String, Receta>()
    
    /**
     * Minuta semanal predeterminada con 5 recetas chilenas seleccionadas.
     * Cada receta incluye información nutricional detallada.
     */
    private val minutaSemanalPredeterminada = MinutaSemanal(
        nombre = "Recetas Tradicionales Chilenas",
        semana = 1,
        recetas = arrayOf(
            // LUNES - Charquicán
            Receta(
                id = "1",
                nombre = "Charquicán",
                origen = "Chile",
                descripcion = "Guiso tradicional con papas, zapallo y carne molida",
                tiempoPreparacion = "60 min",
                dificultad = "Fácil",
                ingredientes = listOf(
                    "500g de carne molida",
                    "4 papas grandes",
                    "1/2 zapallo",
                    "2 zanahorias",
                    "1 cebolla",
                    "Ajo, orégano, comino",
                    "Sal y pimienta"
                ),
                preparacion = listOf(
                    "Picar las verduras en cubos pequeños",
                    "Dorar la carne con la cebolla y ajo",
                    "Agregar las papas, zapallo y zanahorias",
                    "Cocinar con agua hasta que las verduras estén blandas",
                    "Moler las papas para espesar el guiso",
                    "Servir caliente"
                ),
                infoNutricional = NutritionalInfo(
                    calorias = 420,
                    proteinas = 28.5,
                    carbohidratos = 45.2,
                    grasas = 12.8,
                    fibra = 6.5,
                    sodio = 380
                ),
                categoria = "Plato Principal",
                porciones = 4
            ),
            
            // MARTES - Pastel de Papas
            Receta(
                id = "2",
                nombre = "Pastel de Papas",
                origen = "Chile",
                descripcion = "Delicioso pastel con carne molida y puré gratinado",
                tiempoPreparacion = "90 min",
                dificultad = "Media",
                ingredientes = listOf(
                    "1 kg de papas",
                    "500g de carne molida",
                    "2 cebollas",
                    "Aceitunas negras",
                    "3 huevos duros",
                    "Pasas (opcional)",
                    "Leche y mantequilla",
                    "Comino, orégano, sal"
                ),
                preparacion = listOf(
                    "Preparar el pino: dorar carne con cebolla y especias",
                    "Cocer y moler las papas para hacer puré",
                    "En una fuente, colocar una capa de puré",
                    "Agregar el pino, huevos, aceitunas y pasas",
                    "Cubrir con otra capa de puré",
                    "Hornear a 180°C por 30-40 minutos hasta dorar"
                ),
                infoNutricional = NutritionalInfo(
                    calorias = 520,
                    proteinas = 26.3,
                    carbohidratos = 52.1,
                    grasas = 22.4,
                    fibra = 5.8,
                    sodio = 420
                ),
                categoria = "Plato Principal",
                porciones = 6
            ),
            
            // MIÉRCOLES - Cazuela
            Receta(
                id = "3",
                nombre = "Cazuela",
                origen = "Chile",
                descripcion = "Sopa nutritiva con verduras frescas y carne tierna",
                tiempoPreparacion = "80 min",
                dificultad = "Fácil",
                ingredientes = listOf(
                    "1 kg de carne de vacuno o pollo",
                    "2 papas",
                    "2 choclos",
                    "1/2 zapallo",
                    "1 zanahoria",
                    "Arroz o fideos",
                    "Ají de color",
                    "Orégano, sal"
                ),
                preparacion = listOf(
                    "Cocer la carne en abundante agua con sal",
                    "Agregar las verduras cortadas en trozos grandes",
                    "Añadir el arroz o fideos",
                    "Cocinar hasta que todo esté blando",
                    "Servir caliente con pebre"
                ),
                infoNutricional = NutritionalInfo(
                    calorias = 380,
                    proteinas = 32.5,
                    carbohidratos = 38.7,
                    grasas = 10.2,
                    fibra = 7.2,
                    sodio = 320
                ),
                categoria = "Sopa/Guiso",
                porciones = 4
            ),
            
            // JUEVES - Porotos con Riendas
            Receta(
                id = "4",
                nombre = "Porotos con Riendas",
                origen = "Chile",
                descripcion = "Porotos con fideos y zapallo, receta campesina tradicional",
                tiempoPreparacion = "120 min",
                dificultad = "Media",
                ingredientes = listOf(
                    "500g de porotos",
                    "200g de fideos (tallarines)",
                    "1/2 zapallo",
                    "1 cebolla",
                    "2 dientes de ajo",
                    "Ají de color",
                    "Orégano, comino",
                    "Aceite, sal"
                ),
                preparacion = listOf(
                    "Remojar los porotos durante la noche",
                    "Cocer los porotos con agua, cebolla y ajo",
                    "Agregar el zapallo en cubos",
                    "Cuando estén casi cocidos, añadir los fideos",
                    "Aliñar con ají de color y especias",
                    "Servir caliente"
                ),
                infoNutricional = NutritionalInfo(
                    calorias = 450,
                    proteinas = 18.6,
                    carbohidratos = 68.4,
                    grasas = 9.8,
                    fibra = 12.5,
                    sodio = 280
                ),
                categoria = "Plato Principal",
                porciones = 4
            ),
            
            // VIERNES - Empanadas de Pino
            Receta(
                id = "5",
                nombre = "Empanadas de Pino",
                origen = "Chile",
                descripcion = "Empanadas rellenas con carne, cebolla, aceitunas y huevo",
                tiempoPreparacion = "120 min",
                dificultad = "Media",
                ingredientes = listOf(
                    "Para la masa: 1kg harina, manteca, agua, sal",
                    "500g de carne molida",
                    "4 cebollas grandes",
                    "Aceitunas negras",
                    "3 huevos duros",
                    "Pasas",
                    "Comino, orégano, merkén"
                ),
                preparacion = listOf(
                    "Preparar la masa mezclando harina, manteca, agua y sal",
                    "Hacer el pino: dorar cebolla, agregar carne y especias",
                    "Extender la masa y cortar círculos",
                    "Rellenar con pino, aceituna, huevo y pasas",
                    "Cerrar las empanadas haciendo el repulgue",
                    "Hornear a 200°C por 25-30 minutos"
                ),
                infoNutricional = NutritionalInfo(
                    calorias = 480,
                    proteinas = 22.4,
                    carbohidratos = 48.5,
                    grasas = 21.3,
                    fibra = 4.2,
                    sodio = 450
                ),
                categoria = "Plato Principal",
                porciones = 12
            )
        )
    )
    
    /**
     * Obtiene la minuta semanal con selección aleatoria.
     * Las recetas cambian automáticamente cada semana.
     * @param semana Número de semana (opcional, se calcula automáticamente)
     * @return MinutaSemanal con 5 recetas aleatorias
     */
    fun obtenerMinutaSemanal(semana: Int = obtenerSemanaActual()): MinutaSemanal {
        // Si ya existe en el cache, devolverla
        if (cacheminutas.containsKey(semana)) {
            return cacheminutas[semana]!!
        }
        
        // Si no existe, generar nueva minuta y guardarla en cache
        val nuevaMinuta = generarMinutaAleatoria(semana)
        cacheminutas[semana] = nuevaMinuta
        
        return nuevaMinuta
    }
    
    /**
     * Obtiene el número de la semana actual del año.
     * @return Número de semana (1-52)
     */
    fun obtenerSemanaActual(): Int {
        return try {
            LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())
        } catch (e: Exception) {
            // Fallback si hay problemas con la API de tiempo
            (System.currentTimeMillis() / (1000 * 60 * 60 * 24 * 7)).toInt() % 52 + 1
        }
    }
    
    /**
     * Genera una minuta semanal con 5 recetas aleatorias del repositorio.
     * Considera reglas especiales:
     * - Platos que requieren acompañamiento se combinan automáticamente
     * - Acompañamientos no aparecen solos
     * - Postres son complemento, no plato único del día
     * - Salsa blanca no se incluye
     * @param semana Número de semana
     * @return MinutaSemanal con recetas aleatorias
     */
    private fun generarMinutaAleatoria(semana: Int): MinutaSemanal {
        // Platos que requieren acompañamiento
        val platosQueRequierenAcompanamiento = listOf(
            "Plateada", "Pollo Asado", "Pino de Carne", "Pescado sobre Cebolla"
        )
        
        // Obtener todas las recetas disponibles
        val todasLasRecetas = RecetasRepository.getAllRecetas()
        
        // Filtrar recetas excluidas de la planificación
        val recetasValidas = todasLasRecetas.filter { receta ->
            // Excluir Salsa Blanca
            receta.nombre != "Salsa Blanca" &&
            // Excluir acompañamientos solos (se agregarán con platos principales)
            receta.categoria != "Acompañamiento" &&
            // Excluir postres solos (se agregarán como complemento)
            receta.categoria != "Postre"
        }
        
        // Separar recetas por tipo
        val platosCompletos = recetasValidas.filter { 
            it.categoria == "Plato Principal" && 
            !platosQueRequierenAcompanamiento.contains(it.nombre)
        }
        val platosConAcompanamiento = recetasValidas.filter {
            platosQueRequierenAcompanamiento.contains(it.nombre)
        }
        val sopasGuisos = recetasValidas.filter { it.categoria == "Sopa/Guiso" }
        
        // Obtener acompañamientos, postres y ensaladas disponibles
        val acompanamientos = todasLasRecetas.filter { 
            it.categoria == "Acompañamiento" && it.nombre != "Salsa Blanca"
        }
        val postres = todasLasRecetas.filter { it.categoria == "Postre" }
        val ensaladas = todasLasRecetas.filter { it.categoria == "Ensalada" }
        
        // Generar selección balanceada de 5 días
        val recetasSeleccionadas = mutableListOf<Receta>()
        
        // Día 1: Plato completo (no requiere acompañamiento)
        platosCompletos.shuffled().firstOrNull()?.let { recetasSeleccionadas.add(it) }
        
        // Día 2: Plato que requiere acompañamiento + acompañamiento
        if (platosConAcompanamiento.isNotEmpty() && acompanamientos.isNotEmpty()) {
            val platoConAcomp = platosConAcompanamiento.shuffled().first()
            val acomp = acompanamientos.shuffled().first()
            
            // Crear receta combinada
            val recetaCombinada = crearRecetaCombinada(platoConAcomp, acomp)
            recetasSeleccionadas.add(recetaCombinada)
        } else {
            // Si no hay disponibles, usar otro plato completo
            platosCompletos.shuffled().firstOrNull()?.let { recetasSeleccionadas.add(it) }
        }
        
        // Día 3: Sopa o Guiso
        sopasGuisos.shuffled().firstOrNull()?.let { recetasSeleccionadas.add(it) }
        
        // Día 4: Otro plato completo
        platosCompletos.shuffled()
            .firstOrNull { it !in recetasSeleccionadas }
            ?.let { recetasSeleccionadas.add(it) }
        
        // Día 5: Otro plato completo
        platosCompletos.shuffled()
            .firstOrNull { it !in recetasSeleccionadas }
            ?.let { recetasSeleccionadas.add(it) }
        
        // Si no alcanzamos 5 recetas, completar con recetas válidas
        while (recetasSeleccionadas.size < 5 && recetasValidas.isNotEmpty()) {
            recetasValidas.shuffled()
                .firstOrNull { it !in recetasSeleccionadas }
                ?.let { recetasSeleccionadas.add(it) }
        }
        
        // Agregar postres como complemento a 1-2 días aleatorios
        if (postres.isNotEmpty() && recetasSeleccionadas.size >= 5) {
            // Determinar cuántos postres agregar (1 o 2)
            val cantidadPostres = if (Math.random() > 0.6) 2 else 1
            
            // Seleccionar días aleatorios para agregar postres
            val diasDisponibles = recetasSeleccionadas.indices.shuffled().take(cantidadPostres)
            
            diasDisponibles.forEach { dia ->
                val recetaOriginal = recetasSeleccionadas[dia]
                val postreAleatorio = postres.shuffled().first()
                
                // Crear receta con postre como complemento
                val recetaConPostre = agregarPostreComplemento(recetaOriginal, postreAleatorio)
                recetasSeleccionadas[dia] = recetaConPostre
            }
        }
        
        // Agregar ensalada a CADA día (DESPUÉS de los postres para acumular)
        if (ensaladas.isNotEmpty() && recetasSeleccionadas.size >= 5) {
            // Seleccionar 5 ensaladas aleatorias (una por día)
            val ensaladasSeleccionadas = ensaladas.shuffled().take(5)
            
            recetasSeleccionadas.indices.forEach { dia ->
                val recetaActual = recetasSeleccionadas[dia]  // Ya puede tener postre
                val ensaladaDia = if (dia < ensaladasSeleccionadas.size) {
                    ensaladasSeleccionadas[dia]
                } else {
                    ensaladas.shuffled().first()
                }
                
                // Crear receta con ensalada como complemento (acumulando)
                val recetaConEnsalada = agregarEnsaladaComplemento(recetaActual, ensaladaDia)
                recetasSeleccionadas[dia] = recetaConEnsalada
            }
        }
        
        return MinutaSemanal(
            nombre = "Recetas de la Semana #$semana",
            semana = semana,
            recetas = recetasSeleccionadas.toTypedArray()
        )
    }
    
    /**
     * Crea una receta combinada (plato principal + acompañamiento).
     */
    private fun crearRecetaCombinada(platoPrincipal: Receta, acompanamiento: Receta): Receta {
        val recetaCombinada = platoPrincipal.copiar(
            nuevoId = "${platoPrincipal.id}_con_${acompanamiento.id}",  // ID único
            nuevoNombre = "${platoPrincipal.nombre} con ${acompanamiento.nombre}",
            nuevaDescripcion = "${platoPrincipal.descripcion}. Acompañado de ${acompanamiento.nombre.lowercase()}.",
            nuevosIngredientes = platoPrincipal.ingredientes + listOf("--- Acompañamiento: ${acompanamiento.nombre} ---") + acompanamiento.ingredientes,
            nuevaPreparacion = platoPrincipal.obtenerPasosPreparacion() + listOf("\nPreparación del acompañamiento:") + acompanamiento.obtenerPasosPreparacion()
        )
        // Guardar en cache
        cacheRecetasCombinadas[recetaCombinada.id] = recetaCombinada
        return recetaCombinada
    }
    
    /**
     * Agrega un postre como complemento a una receta.
     */
    private fun agregarPostreComplemento(recetaPrincipal: Receta, postre: Receta): Receta {
        val recetaConPostre = recetaPrincipal.copiar(
            nuevoId = "${recetaPrincipal.id}_postre_${postre.id}",  // ID único
            nuevoNombre = "${recetaPrincipal.nombre} + ${postre.nombre}",
            nuevaDescripcion = "${recetaPrincipal.descripcion} Incluye postre: ${postre.nombre.lowercase()}.",
            nuevosIngredientes = recetaPrincipal.ingredientes + listOf("--- Postre: ${postre.nombre} ---") + postre.ingredientes,
            nuevaPreparacion = recetaPrincipal.obtenerPasosPreparacion() + listOf("\nPreparación del postre:") + postre.obtenerPasosPreparacion()
        )
        // Guardar en cache
        cacheRecetasCombinadas[recetaConPostre.id] = recetaConPostre
        return recetaConPostre
    }
    
    /**
     * Agrega una ensalada como complemento a una receta.
     */
    private fun agregarEnsaladaComplemento(recetaPrincipal: Receta, ensalada: Receta): Receta {
        val recetaConEnsalada = recetaPrincipal.copiar(
            nuevoId = "${recetaPrincipal.id}_ensalada_${ensalada.id}",  // ID único
            nuevoNombre = "${recetaPrincipal.nombre} + ${ensalada.nombre}",
            nuevaDescripcion = "${recetaPrincipal.descripcion} Acompañado de ${ensalada.nombre.lowercase()}.",
            nuevosIngredientes = recetaPrincipal.ingredientes + listOf("--- Ensalada: ${ensalada.nombre} ---") + ensalada.ingredientes,
            nuevaPreparacion = recetaPrincipal.obtenerPasosPreparacion() + listOf("\nPreparación de la ensalada:") + ensalada.obtenerPasosPreparacion()
        )
        // Guardar en cache
        cacheRecetasCombinadas[recetaConEnsalada.id] = recetaConEnsalada
        return recetaConEnsalada
    }
    
    /**
     * Busca una receta por ID, incluyendo recetas combinadas.
     * @param recetaId ID de la receta a buscar
     * @return Receta encontrada o null
     */
    fun getRecetaById(recetaId: String): Receta? {
        // Primero buscar en recetas combinadas
        return cacheRecetasCombinadas[recetaId]
            // Si no está, buscar en recetas originales
            ?: RecetasRepository.getRecetaById(recetaId)
    }
    
    /**
     * Obtiene todas las recetas de la minuta como lista.
     * @return Lista de las 5 recetas semanales
     */
    fun obtenerRecetasSemanales(): List<Receta> {
        return obtenerMinutaSemanal().obtenerTodasLasRecetas()
    }
    
    /**
     * Obtiene una receta específica de la minuta por día.
     * @param dia Día de la semana (0=Lunes, 1=Martes, ..., 4=Viernes)
     * @return Receta del día especificado
     */
    fun obtenerRecetaPorDia(dia: Int): Receta {
        require(dia in 0..4) { "El día debe estar entre 0 (Lunes) y 4 (Viernes)" }
        return obtenerMinutaSemanal().obtenerReceta(dia)
    }
    
    /**
     * Obtiene el resumen completo de la minuta semanal.
     * @return String con toda la información
     */
    fun obtenerResumenSemanal(): String {
        return obtenerMinutaSemanal().obtenerResumenSemanal()
    }
    
    /**
     * Obtiene las recomendaciones nutricionales de la semana.
     * @return Lista de recomendaciones
     */
    fun obtenerRecomendacionesSemanales(): List<String> {
        return obtenerMinutaSemanal().obtenerRecomendacionesSemanales()
    }
    
    /**
     * Crea un array de recetas para uso en arrays tradicionales.
     * Cumple con el requerimiento de "generar un array en Kotlin".
     * @return Array con las 5 recetas semanales
     */
    fun obtenerArrayRecetas(): Array<Receta> {
        return arrayOf(
            obtenerRecetaPorDia(0),  // Lunes
            obtenerRecetaPorDia(1),  // Martes
            obtenerRecetaPorDia(2),  // Miércoles
            obtenerRecetaPorDia(3),  // Jueves
            obtenerRecetaPorDia(4)   // Viernes
        )
    }
    
    /**
     * Obtiene información nutricional agregada de toda la semana.
     * @return Map con totales nutricionales semanales
     */
    fun obtenerInfoNutricionalSemanal(): Map<String, Any> {
        val recetas = obtenerRecetasSemanales()
        
        return mapOf(
            "totalCalorias" to recetas.sumOf { it.obtenerCaloriasTotales() },
            "promedioCalorias" to (recetas.sumOf { it.obtenerCaloriasTotales() } / 5.0),
            "totalProteinas" to recetas.sumOf { 
                it.obtenerInfoNutricional()?.proteinas?.toInt() ?: 0 
            },
            "totalCarbohidratos" to recetas.sumOf { 
                it.obtenerInfoNutricional()?.carbohidratos?.toInt() ?: 0 
            },
            "totalGrasas" to recetas.sumOf { 
                it.obtenerInfoNutricional()?.grasas?.toInt() ?: 0 
            },
            "recetasVegetarianas" to recetas.count { it.esVegetariana() },
            "recetasRapidas" to recetas.count { it.esRecetaRapida() }
        )
    }
    
    /**
     * Ejemplo de uso de colecciones: filtra recetas por criterios múltiples.
     * @param maxCalorias Máximo de calorías permitidas
     * @param dificultadMaxima Dificultad máxima aceptable
     * @return Lista de recetas que cumplen los criterios
     */
    fun filtrarRecetas(maxCalorias: Int = 500, dificultadMaxima: String = "Media"): List<Receta> {
        val ordenDificultad = mapOf("Fácil" to 1, "Media" to 2, "Difícil" to 3)
        val nivelMaximo = ordenDificultad[dificultadMaxima] ?: 2
        
        return obtenerRecetasSemanales().filter { receta ->
            receta.obtenerCaloriasTotales() <= maxCalorias &&
            (ordenDificultad[receta.obtenerNivelDificultad()] ?: 3) <= nivelMaximo
        }
    }
    
    /**
     * Genera un Set con todos los ingredientes únicos de la semana.
     * Útil para crear una lista de compras.
     * @return Set de ingredientes únicos
     */
    fun obtenerIngredientesUnicos(): Set<String> {
        return obtenerRecetasSemanales()
            .flatMap { it.obtenerIngredientes() }
            .toSet()
    }
    
    /**
     * Crea un mapa de recetas agrupadas por categoría.
     * Demuestra el uso de funciones de orden superior.
     * @return Map con categorías y sus recetas
     */
    fun obtenerRecetasPorCategoria(): Map<String, List<Receta>> {
        return obtenerRecetasSemanales().groupBy { it.categoria }
    }
}

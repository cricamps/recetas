package com.example.recetas.data

/**
 * Repositorio que gestiona la lista de recetas tradicionales chilenas.
 * Proporciona métodos para obtener, buscar y gestionar recetas.
 */
object RecetasRepository {
    
    /**
     * Lista de recetas tradicionales chilenas con información completa.
     * Incluye ingredientes y pasos de preparación para cada receta.
     */
    private val recetas = listOf(
        Receta(
            id = "1",
            nombre = "Charquicán",
            origen = "Chile",
            tiempoPreparacion = "60 min",
            dificultad = "Fácil",
            descripcion = "Guiso tradicional con papas, zapallo y carne",
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
            )
        ),
        Receta(
            id = "2",
            nombre = "Pastel de Papas",
            origen = "Chile",
            tiempoPreparacion = "90 min",
            dificultad = "Media",
            descripcion = "Delicioso pastel con carne molida y puré",
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
            )
        ),
        Receta(
            id = "3",
            nombre = "Cazuela",
            origen = "Chile",
            tiempoPreparacion = "80 min",
            dificultad = "Fácil",
            descripcion = "Sopa nutritiva con verduras y carne",
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
            )
        ),
        Receta(
            id = "4",
            nombre = "Porotos con Riendas",
            origen = "Chile",
            tiempoPreparacion = "120 min",
            dificultad = "Media",
            descripcion = "Porotos con fideos y zapallo",
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
            )
        ),
        Receta(
            id = "5",
            nombre = "Empanadas de Pino",
            origen = "Chile",
            tiempoPreparacion = "120 min",
            dificultad = "Media",
            descripcion = "Empanadas rellenas con carne, cebolla y aceitunas",
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
            )
        ),
        Receta(
            id = "6",
            nombre = "Carbonada",
            origen = "Chile",
            tiempoPreparacion = "70 min",
            dificultad = "Fácil",
            descripcion = "Guiso con zapallo, choclo y carne",
            ingredientes = listOf(
                "500g de carne de vacuno",
                "1/2 zapallo",
                "2 choclos",
                "2 papas",
                "1 zanahoria",
                "1 cebolla",
                "Arroz",
                "Orégano, comino, ají"
            ),
            preparacion = listOf(
                "Cortar la carne en cubos y dorar con cebolla",
                "Agregar las verduras cortadas",
                "Añadir agua y cocinar hasta que esté blando",
                "Incorporar el arroz",
                "Sazonar con especias",
                "Servir caliente con pebre"
            )
        ),
        Receta(
            id = "7",
            nombre = "Plateada",
            origen = "Chile",
            tiempoPreparacion = "180 min",
            dificultad = "Difícil",
            descripcion = "Carne de vacuno cocida lentamente",
            ingredientes = listOf(
                "1.5 kg de plateada de vacuno",
                "2 cebollas",
                "4 zanahorias",
                "Vino tinto",
                "Caldo de carne",
                "Laurel, tomillo",
                "Sal y pimienta"
            ),
            preparacion = listOf(
                "Sellar la carne por todos lados",
                "Agregar cebolla, zanahoria y hierbas",
                "Añadir vino tinto y caldo",
                "Cocinar a fuego lento por 2-3 horas",
                "La carne debe quedar muy tierna",
                "Servir con la salsa reducida"
            )
        ),
        Receta(
            id = "8",
            nombre = "Arroz Graneado",
            origen = "Chile",
            tiempoPreparacion = "30 min",
            dificultad = "Fácil",
            descripcion = "Arroz suelto y perfectamente cocido",
            ingredientes = listOf(
                "2 tazas de arroz",
                "4 tazas de agua",
                "2 cucharadas de aceite",
                "1 diente de ajo",
                "Sal al gusto"
            ),
            preparacion = listOf(
                "Lavar el arroz hasta que el agua salga clara",
                "Dorar el ajo en aceite",
                "Agregar el arroz y tostar ligeramente",
                "Añadir el agua y sal",
                "Cocinar tapado a fuego bajo por 15-20 minutos",
                "Dejar reposar 5 minutos antes de servir"
            )
        ),
        Receta(
            id = "9",
            nombre = "Pollo Asado",
            origen = "Chile",
            tiempoPreparacion = "50 min",
            dificultad = "Fácil",
            descripcion = "Pollo dorado al horno con especias",
            ingredientes = listOf(
                "1 pollo entero",
                "Jugo de limón",
                "Ajo molido",
                "Merkén",
                "Orégano",
                "Aceite de oliva",
                "Sal y pimienta"
            ),
            preparacion = listOf(
                "Marinar el pollo con limón, ajo y especias",
                "Dejar reposar por 30 minutos",
                "Precalentar el horno a 200°C",
                "Colocar el pollo en una fuente",
                "Hornear por 40-50 minutos hasta dorar",
                "Servir con ensalada chilena"
            )
        ),
        Receta(
            id = "10",
            nombre = "Leche Asada",
            origen = "Chile",
            tiempoPreparacion = "70 min",
            dificultad = "Media",
            descripcion = "Postre tradicional con leche y caramelo",
            ingredientes = listOf(
                "1 litro de leche",
                "6 huevos",
                "1 taza de azúcar",
                "Esencia de vainilla",
                "Azúcar para caramelo"
            ),
            preparacion = listOf(
                "Hacer caramelo con azúcar en un molde",
                "Batir huevos con azúcar",
                "Calentar la leche con vainilla",
                "Mezclar todo y verter en el molde",
                "Hornear a baño maría a 180°C por 45-60 minutos",
                "Enfriar y desmoldar"
            )
        )
    )
    
    /**
     * Obtiene todas las recetas disponibles.
     * @return Lista completa de recetas
     */
    fun getAllRecetas(): List<Receta> = recetas
    
    /**
     * Busca una receta específica por su ID.
     * @param id Identificador de la receta
     * @return La receta encontrada o null si no existe
     */
    fun getRecetaById(id: String): Receta? = recetas.find { it.id == id }
    
    /**
     * Busca recetas que coincidan con un término de búsqueda.
     * @param query Término de búsqueda
     * @return Lista de recetas que coinciden con la búsqueda
     */
    fun searchRecetas(query: String): List<Receta> {
        return if (query.isBlank()) {
            recetas
        } else {
            recetas.filter { receta ->
                receta.nombre.contains(query, ignoreCase = true) ||
                receta.descripcion.contains(query, ignoreCase = true)
            }
        }
    }
}

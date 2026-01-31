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
            ),
            infoNutricional = NutritionalInfo(
                calorias = 420,
                proteinas = 28.5,
                carbohidratos = 45.2,
                grasas = 12.8,
                fibra = 6.5,
                sodio = 380,
                azucar = 3.5
            ),
            categoria = "Plato Principal",
            porciones = 4
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
            ),
            infoNutricional = NutritionalInfo(
                calorias = 520,
                proteinas = 26.3,
                carbohidratos = 52.1,
                grasas = 22.4,
                fibra = 5.8,
                sodio = 420,
                azucar = 4.2
            ),
            categoria = "Plato Principal",
            porciones = 6
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
            ),
            infoNutricional = NutritionalInfo(
                calorias = 380,
                proteinas = 32.5,
                carbohidratos = 38.7,
                grasas = 10.2,
                fibra = 7.2,
                sodio = 320,
                azucar = 5.8
            ),
            categoria = "Sopa/Guiso",
            porciones = 4
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
            ),
            infoNutricional = NutritionalInfo(
                calorias = 450,
                proteinas = 18.6,
                carbohidratos = 68.4,
                grasas = 9.8,
                fibra = 12.5,
                sodio = 280,
                azucar = 2.8
            ),
            categoria = "Plato Principal",
            porciones = 4
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
            ),
            infoNutricional = NutritionalInfo(
                calorias = 480,
                proteinas = 22.4,
                carbohidratos = 48.5,
                grasas = 21.3,
                fibra = 4.2,
                sodio = 450,
                azucar = 3.5
            ),
            categoria = "Plato Principal",
            porciones = 12
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
            ),
            infoNutricional = NutritionalInfo(
                calorias = 410,
                proteinas = 25.8,
                carbohidratos = 42.3,
                grasas = 13.5,
                fibra = 6.8,
                sodio = 350,
                azucar = 6.2
            ),
            categoria = "Plato Principal",
            porciones = 4
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
            ),
            infoNutricional = NutritionalInfo(
                calorias = 550,
                proteinas = 45.2,
                carbohidratos = 12.5,
                grasas = 32.8,
                fibra = 3.2,
                sodio = 480,
                azucar = 2.5
            ),
            categoria = "Plato Principal",
            porciones = 6
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
            ),
            infoNutricional = NutritionalInfo(
                calorias = 280,
                proteinas = 5.2,
                carbohidratos = 58.4,
                grasas = 3.8,
                fibra = 1.2,
                sodio = 180,
                azucar = 0.2
            ),
            categoria = "Acompañamiento",
            porciones = 4
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
            ),
            infoNutricional = NutritionalInfo(
                calorias = 320,
                proteinas = 38.5,
                carbohidratos = 2.1,
                grasas = 16.8,
                fibra = 0.5,
                sodio = 420,
                azucar = 0.5
            ),
            categoria = "Plato Principal",
            porciones = 4
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
            ),
            infoNutricional = NutritionalInfo(
                calorias = 290,
                proteinas = 12.5,
                carbohidratos = 38.2,
                grasas = 9.8,
                fibra = 0.0,
                sodio = 150,
                azucar = 32.5
            ),
            categoria = "Postre",
            porciones = 8
        ),
        Receta(
            id = "11",
            nombre = "Arroz con Leche",
            origen = "Chile",
            tiempoPreparacion = "40 min",
            dificultad = "Fácil",
            descripcion = "Clásico postre cremoso",
            ingredientes = listOf(
                "3/4 taza de arroz",
                "1 litro de leche",
                "1/2 taza de azúcar",
                "1 yema de huevo",
                "Vainilla",
                "Canela"
            ),
            preparacion = listOf(
                "Lavar el arroz",
                "Cocinar arroz en leche a fuego lento",
                "Cocinar 20 minutos hasta que esté blando",
                "Agregar azúcar",
                "Batir yema con leche fría",
                "Agregar al final y apagar",
                "Servir con canela"
            ),
            infoNutricional = NutritionalInfo(
                calorias = 320,
                proteinas = 9.2,
                carbohidratos = 52.4,
                grasas = 8.5,
                fibra = 0.8,
                sodio = 120,
                azucar = 38.2
            ),
            categoria = "Postre",
            porciones = 6
        ),
        Receta(
            id = "12",
            nombre = "Bavarois",
            origen = "Chile",
            tiempoPreparacion = "30 min",
            dificultad = "Media",
            descripcion = "Postre ligero y elegante",
            ingredientes = listOf(
                "1 caja de jalea",
                "1 leche evaporada fría",
                "1 tarro de leche condensada",
                "3/4 taza de jugo de limón",
                "Frutas congeladas"
            ),
            preparacion = listOf(
                "Preparar jalea con mitad del agua",
                "Mezclar limón con leche condensada",
                "Batir leche evaporada",
                "Juntar los tres batidos",
                "Agregar frutas congeladas",
                "Refrigerar hasta cuajar"
            ),
            infoNutricional = NutritionalInfo(
                calorias = 280,
                proteinas = 7.8,
                carbohidratos = 38.6,
                grasas = 11.4,
                fibra = 1.2,
                sodio = 95,
                azucar = 28.5
            ),
            categoria = "Postre",
            porciones = 8
        ),
        Receta(
            id = "13",
            nombre = "Clafoutie",
            origen = "Chile",
            tiempoPreparacion = "50 min",
            dificultad = "Media",
            descripcion = "Postre francés con frutas",
            ingredientes = listOf(
                "Frutas de estación",
                "4 huevos",
                "2 cucharadas de mantequilla",
                "1 cajita de crema",
                "Licor de naranja o ron",
                "1 taza de azúcar",
                "Vainilla",
                "3/4 taza de harina"
            ),
            preparacion = listOf(
                "Enmantequillar fuente de horno",
                "Colocar frutas frescas",
                "Batir todos los ingredientes",
                "Verter sobre las frutas",
                "Hornear 30 minutos a 180°C",
                "Probar con cuchillo si está listo"
            ),
            infoNutricional = NutritionalInfo(
                calorias = 310,
                proteinas = 10.5,
                carbohidratos = 42.8,
                grasas = 12.6,
                fibra = 2.4,
                sodio = 140,
                azucar = 35.8
            ),
            categoria = "Postre",
            porciones = 8
        ),
        // RECETAS ADICIONALES FALTANTES
        Receta(
            id = "21",
            nombre = "Pino de Carne",
            origen = "Chile",
            tiempoPreparacion = "45 min",
            dificultad = "Fácil",
            descripcion = "Base versátil para múltiples preparaciones",
            ingredientes = listOf(
                "500g de carne molida o picada",
                "1 cebolla picada fina",
                "Merkén",
                "Orégano",
                "Ajo",
                "Pimentón",
                "Zanahoria",
                "Perejil",
                "Sal"
            ),
            preparacion = listOf(
                "Freír la cebolla con los aliños",
                "Agregar la carne",
                "Tapar y cocinar a fuego lento 30 minutos",
                "Revolver ocasionalmente",
                "Usar para tacos, pastel de papas, empanadas, etc."
            ),
            infoNutricional = NutritionalInfo(
                calorias = 280,
                proteinas = 26.4,
                carbohidratos = 8.5,
                grasas = 15.8,
                fibra = 2.1,
                sodio = 320,
                azucar = 3.2
            ),
            categoria = "Plato Principal",
            porciones = 4
        ),
        Receta(
            id = "22",
            nombre = "Pescado sobre Cebolla",
            origen = "Chile",
            tiempoPreparacion = "35 min",
            dificultad = "Fácil",
            descripcion = "Pescado jugoso sobre cama de cebollas",
            ingredientes = listOf(
                "Filetes de pescado",
                "2 cebollas",
                "200g de crema",
                "Sal",
                "Pimienta",
                "Limón"
            ),
            preparacion = listOf(
                "Saltear cebollas en pluma hasta cocinar",
                "Aliñar pescado con sal, pimienta y limón",
                "Poner pescado sobre cebollas cocidas",
                "Cocinar 15 minutos",
                "Agregar crema 3 minutos antes de servir",
                "Acompañar con papas o arroz"
            ),
            infoNutricional = NutritionalInfo(
                calorias = 320,
                proteinas = 28.5,
                carbohidratos = 12.4,
                grasas = 18.6,
                fibra = 2.2,
                sodio = 380,
                azucar = 2.8
            ),
            categoria = "Plato Principal",
            porciones = 4
        ),
        Receta(
            id = "24",
            nombre = "Puré de Espinacas",
            origen = "Chile",
            tiempoPreparacion = "25 min",
            dificultad = "Fácil",
            descripcion = "Nutritivo y cremoso puré verde",
            ingredientes = listOf(
                "500g de espinacas",
                "Leche",
                "1 cucharada de harina",
                "1 cucharada de mantequilla",
                "Sal",
                "Crema (opcional)"
            ),
            preparacion = listOf(
                "Lavar espinacas y cocinar con su agua 10 minutos",
                "Moler las espinacas con minipimer",
                "Volver a la olla con su agua",
                "Mezclar leche con harina sin grumos",
                "Agregar a espinacas hirviendo",
                "Agregar mantequilla y sal",
                "Cocinar 5-10 minutos"
            ),
            infoNutricional = NutritionalInfo(
                calorias = 150,
                proteinas = 7.5,
                carbohidratos = 15.8,
                grasas = 6.8,
                fibra = 4.2,
                sodio = 320,
                azucar = 1.5
            ),
            categoria = "Acompañamiento",
            porciones = 4
        ),
        Receta(
            id = "25",
            nombre = "Salsa Blanca",
            origen = "Chile",
            tiempoPreparacion = "15 min",
            dificultad = "Fácil",
            descripcion = "Base perfecta para múltiples salsas",
            ingredientes = listOf(
                "500ml de leche",
                "2 cucharadas de harina",
                "2 cucharadas de mantequilla",
                "Sal",
                "Pimienta",
                "Nuez moscada"
            ),
            preparacion = listOf(
                "Calentar la leche",
                "Mezclar leche fría con harina",
                "Agregar a leche caliente revolviendo",
                "Agregar mantequilla, sal, pimienta y nuez moscada",
                "Cocinar 5-10 minutos hasta espesar",
                "Usar como base para otras salsas"
            ),
            infoNutricional = NutritionalInfo(
                calorias = 180,
                proteinas = 6.2,
                carbohidratos = 16.4,
                grasas = 9.8,
                fibra = 0.5,
                sodio = 280,
                azucar = 2.8
            ),
            categoria = "Acompañamiento",
            porciones = 4
        ),
        Receta(
            id = "27",
            nombre = "Guiso de Zapallitos Italianos",
            origen = "Chile",
            tiempoPreparacion = "35 min",
            dificultad = "Fácil",
            descripcion = "Guiso cremoso y nutritivo",
            ingredientes = listOf(
                "Zapallitos italianos",
                "1 cebolla rallada",
                "1 zanahoria rallada",
                "1/2 pimentón rallado",
                "Perejil",
                "1/2 taza de leche",
                "1 cucharada de harina",
                "1 cucharada de mantequilla",
                "Crema (opcional)"
            ),
            preparacion = listOf(
                "Freír cebolla, zanahoria, pimentón y perejil",
                "Rallar zapallitos en redondo",
                "Agregar zapallitos a la fritanga",
                "Cocinar 20 minutos hasta blandos",
                "Mezclar leche con harina y agregar",
                "Agregar mantequilla, sal, pimienta",
                "Opcional: agregar crema al final"
            ),
            infoNutricional = NutritionalInfo(
                calorias = 210,
                proteinas = 8.4,
                carbohidratos = 22.6,
                grasas = 10.2,
                fibra = 5.4,
                sodio = 280,
                azucar = 4.5
            ),
            categoria = "Plato Principal",
            porciones = 4
        ),
        // ENSALADAS
        Receta(
            id = "28",
            nombre = "Ensalada Chilena",
            origen = "Chile",
            tiempoPreparacion = "10 min",
            dificultad = "Fácil",
            descripcion = "Ensalada fresca con tomate y cebolla",
            ingredientes = listOf(
                "3 tomates",
                "1 cebolla",
                "Cilantro fresco",
                "Aceite de oliva",
                "Limón",
                "Sal"
            ),
            preparacion = listOf(
                "Cortar los tomates en rodajas finas",
                "Cortar la cebolla en pluma",
                "Picar el cilantro",
                "Mezclar todo en un bowl",
                "Aliñar con aceite, limón y sal",
                "Servir inmediatamente"
            ),
            infoNutricional = NutritionalInfo(
                calorias = 45,
                proteinas = 1.2,
                carbohidratos = 6.8,
                grasas = 2.1,
                fibra = 2.5,
                sodio = 8,
                azucar = 3.8
            ),
            categoria = "Ensalada",
            porciones = 4
        ),
        Receta(
            id = "29",
            nombre = "Ensalada Verde",
            origen = "Chile",
            tiempoPreparacion = "10 min",
            dificultad = "Fácil",
            descripcion = "Ensalada de lechuga fresca",
            ingredientes = listOf(
                "1 lechuga",
                "Aceite de oliva",
                "Limón o vinagre",
                "Sal"
            ),
            preparacion = listOf(
                "Lavar y escurrir la lechuga",
                "Cortar o trozar con las manos",
                "Aliñar con aceite y limón",
                "Agregar sal al gusto",
                "Servir fresca"
            ),
            infoNutricional = NutritionalInfo(
                calorias = 25,
                proteinas = 0.8,
                carbohidratos = 2.5,
                grasas = 1.5,
                fibra = 1.8,
                sodio = 5,
                azucar = 1.2
            ),
            categoria = "Ensalada",
            porciones = 4
        ),
        Receta(
            id = "30",
            nombre = "Ensalada de Repollo",
            origen = "Chile",
            tiempoPreparacion = "15 min",
            dificultad = "Fácil",
            descripcion = "Ensalada crujiente de repollo",
            ingredientes = listOf(
                "1/2 repollo",
                "1 zanahoria",
                "Mayonesa o yogurt",
                "Limón",
                "Sal"
            ),
            preparacion = listOf(
                "Rallar el repollo fino",
                "Rallar la zanahoria",
                "Mezclar en un bowl",
                "Aliñar con mayonesa y limón",
                "Sazonar con sal",
                "Refrigerar 10 minutos antes de servir"
            ),
            infoNutricional = NutritionalInfo(
                calorias = 65,
                proteinas = 1.5,
                carbohidratos = 8.2,
                grasas = 3.5,
                fibra = 3.2,
                sodio = 85,
                azucar = 4.5
            ),
            categoria = "Ensalada",
            porciones = 4
        ),
        Receta(
            id = "31",
            nombre = "Ensalada Mixta",
            origen = "Chile",
            tiempoPreparacion = "15 min",
            dificultad = "Fácil",
            descripcion = "Ensalada variada con verduras frescas",
            ingredientes = listOf(
                "Lechuga",
                "Tomate",
                "Pepino",
                "Zanahoria",
                "Aceite de oliva",
                "Vinagre o limón",
                "Sal"
            ),
            preparacion = listOf(
                "Lavar todas las verduras",
                "Cortar la lechuga",
                "Cortar tomate y pepino en rodajas",
                "Rallar la zanahoria",
                "Mezclar todo",
                "Aliñar con aceite, vinagre y sal"
            ),
            infoNutricional = NutritionalInfo(
                calorias = 40,
                proteinas = 1.3,
                carbohidratos = 5.8,
                grasas = 1.8,
                fibra = 2.5,
                sodio = 6,
                azucar = 3.2
            ),
            categoria = "Ensalada",
            porciones = 4
        ),
        Receta(
            id = "32",
            nombre = "Ensalada de Betarraga",
            origen = "Chile",
            tiempoPreparacion = "20 min",
            dificultad = "Fácil",
            descripcion = "Ensalada colorida con betarraga",
            ingredientes = listOf(
                "2 betarragas cocidas",
                "Aceite de oliva",
                "Vinagre",
                "Cebolla picada (opcional)",
                "Sal"
            ),
            preparacion = listOf(
                "Pelar y cortar las betarragas en cubos",
                "Colocar en un bowl",
                "Agregar cebolla si se desea",
                "Aliñar con aceite y vinagre",
                "Sazonar con sal",
                "Dejar reposar 10 minutos"
            ),
            infoNutricional = NutritionalInfo(
                calorias = 55,
                proteinas = 1.8,
                carbohidratos = 9.5,
                grasas = 1.5,
                fibra = 2.8,
                sodio = 65,
                azucar = 7.2
            ),
            categoria = "Ensalada",
            porciones = 4
        )
    )
    
    /**
     * Obtiene todas las recetas disponibles.
     * @return Lista completa de recetas
     */
    fun getAllRecetas(): List<Receta> = recetas
    
    /**
     * Obtiene recetas filtradas por categoría.
     * @param categoria Categoría a filtrar ("Plato Principal", "Postre", "Sopa/Guiso", "Acompañamiento")
     * @return Lista de recetas de esa categoría
     */
    fun getRecetasPorCategoria(categoria: String): List<Receta> {
        return recetas.filter { it.categoria == categoria }
    }
    
    /**
     * Obtiene recetas agrupadas por categoría.
     * @return Map con categorías como claves y listas de recetas como valores
     */
    fun getRecetasAgrupadas(): Map<String, List<Receta>> {
        return recetas.groupBy { it.categoria }
    }
    
    /**
     * Obtiene todas las categorías disponibles.
     * @return Lista de categorías únicas
     */
    fun getCategorias(): List<String> {
        return recetas.map { it.categoria }.distinct().sorted()
    }
    
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

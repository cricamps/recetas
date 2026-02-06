package com.example.recetas.navigation

/**
 * Define las rutas de navegación de la aplicación.
 * Cada objeto representa una pantalla diferente en la app.
 */
sealed class Screen(val route: String) {
    /**
     * Pantalla de permisos.
     */
    object Permisos : Screen("permisos")
    
    /**
     * Pantalla de inicio de sesión (Login).
     */
    object Login : Screen("login")
    
    /**
     * Pantalla principal con la lista de recetas.
     */
    object Recetas : Screen("recetas")
    
    /**
     * Pantalla de detalle de una receta específica.
     * @param recetaId ID de la receta a mostrar
     */
    object DetalleReceta : Screen("detalle/{recetaId}") {
        /**
         * Crea la ruta completa para navegar al detalle de una receta.
         * @param recetaId ID de la receta
         * @return Ruta formateada con el ID
         */
        fun createRoute(recetaId: String) = "detalle/$recetaId"
    }
    
    /**
     * Pantalla para agregar una nueva receta.
     */
    object AgregarReceta : Screen("agregar_receta")
    
    /**
     * Pantalla de Minuta Semanal con 5 recetas y recomendaciones nutricionales.
     * Implementa el requerimiento de la Semana 4.
     */
    object Minuta : Screen("minuta")
    
    /**
     * Pantalla para recuperar contraseña - Paso 1: Solicitar email
     */
    object RecuperarPassword : Screen("recuperar_password")
    
    /**
     * Pantalla para verificar código de recuperación - Paso 2
     */
    object VerificarCodigo : Screen("verificar_codigo/{email}") {
        fun createRoute(email: String) = "verificar_codigo/$email"
    }
    
    /**
     * Pantalla para establecer nueva contraseña - Paso 3
     */
    object NuevaPassword : Screen("nueva_password/{email}") {
        fun createRoute(email: String) = "nueva_password/$email"
    }
}

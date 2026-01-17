# 📱 Aplicación de Recetas Chilenas

**Curso:** DSY2204 - Desarrollo de Aplicaciones Móviles  
**Semana:** 2 - Actividad Sumativa 1  
**Estudiante:** Cristobal Camps  
**Fecha:** Enero 2026

---

## 🏗️ Arquitectura del Proyecto

```
com.example.recetas/
│
├── data/                          # Capa de datos
│   ├── Receta.kt                 # Modelo de datos
│   └── RecetasRepository.kt      # Repositorio con 10 recetas
│
├── navigation/                    # Sistema de navegación
│   ├── Screen.kt                 # Definición de rutas
│   └── NavigationGraph.kt        # Grafo de navegación
│
├── ui/
│   ├── screens/                  # Pantallas de la aplicación
│   │   ├── LoginScreen.kt        # Pantalla de inicio de sesión
│   │   ├── RecetasScreen.kt      # Lista de recetas con búsqueda
│   │   ├── DetalleRecetaScreen.kt # Detalle completo de receta
│   │   └── AgregarRecetaScreen.kt # Formulario para nueva receta
│   │
│   └── theme/                    # Temas y estilos
│       ├── Color.kt              # Paleta de colores
│       ├── Theme.kt              # Configuración del tema
│       └── Type.kt               # Tipografía
│
└── MainActivity.kt               # Actividad principal

# 📱 Aplicación de Recetas Chilenas - Con Animaciones y Accesibilidad

**Curso:** DSY2204 - Desarrollo de Aplicaciones Móviles  
**Semana:** 3 - Implementación de Kotlin + Sistema de Animaciones  
**Estudiante:** Cristobal Camps  
**Fecha:** Enero 2026


## 🌈 Características de Accesibilidad Completa

🔤 **Aumento de Tamaño de Letra**: 5 niveles de escalado (0.85x a 1.5x)  
🔊 **Lectura en Voz Alta (TTS)**: Text-to-Speech para recetas completas  
🎨 **Alto Contraste**: 4 niveles de contraste (WCAG AA + AAA)  
🌙 **Tema Claro/Oscuro**: Modo día y noche  
💾 **Persistencia**: Todas las preferencias se guardan automáticamente

📝 **Ver documentación completa**:  
- [RESUMEN_ACCESIBILIDAD_COMPLETA.md](RESUMEN_ACCESIBILIDAD_COMPLETA.md) - Resumen ejecutivo  
- [CONTRASTE_README.md](CONTRASTE_README.md) - Detalles del sistema de contraste  
- [GUIA_PRUEBAS_ACCESIBILIDAD.md](GUIA_PRUEBAS_ACCESIBILIDAD.md) - Guía de pruebas  
- [COMPARATIVA_OPCIONES_ACCESIBILIDAD.md](COMPARATIVA_OPCIONES_ACCESIBILIDAD.md) - Análisis de decisiones

---

## 🏗️ Arquitectura del Proyecto

```
com.example.recetas/
│
├── animations/                    # 🎬 NUEVO S3: Sistema de Animaciones
│   └── RecetaAnimations.kt        # Specs de animación reutilizables
│
├── accessibility/                 # ✨ Módulo de Accesibilidad
│   ├── AccessibilityUtils.kt     # Funciones helper de accesibilidad
│   ├── FontScale.kt              # Sistema de escalado de fuente
│   ├── FontScaleUtils.kt         # Control visual de fuente
│   ├── ContrastMode.kt           # Niveles de contraste
│   ├── ContrastManager.kt        # Gestión de contraste
│   ├── ContrastModeControl.kt    # Control visual de contraste
│   ├── SpeechManager.kt          # Gestión de TTS
│   └── SpeechUtils.kt            # Funciones de Text-to-Speech
│
├── data/                          # Capa de datos
│   ├── Receta.kt                 # Modelo de datos
│   └── RecetasRepository.kt      # Repositorio con 10 recetas
│
├── navigation/                    # Sistema de navegación
│   ├── Screen.kt                 # Definición de rutas
│   └── NavigationGraph.kt        # Grafo de navegación (con accesibilidad)
│
├── ui/
│   ├── components/                # 🎬 NUEVO S3: Componentes Animados
│   │   ├── AnimatedButtons.kt     # Botones con animaciones
│   │   └── AnimatedCards.kt       # Cards con animaciones
│   │
│   ├── screens/                  # Pantallas con accesibilidad completa
│   │   ├── PermisosScreen.kt     # Solicitud de permisos
│   │   ├── LoginScreen.kt        # Login con TTS
│   │   ├── RecetasScreen.kt      # Lista con búsqueda
│   │   ├── DetalleRecetaScreen.kt # Detalle con TTS completo
│   │   └── AgregarRecetaScreen.kt # Formulario accesible
│   │
│   └── theme/                    # Temas con contraste
│       ├── Color.kt              # Paleta de colores base
│       ├── ContrastColorSchemes.kt # ✨ 8 esquemas de contraste
│       ├── Theme.kt              # Tema con accesibilidad
│       └── Type.kt               # Tipografía
│
└── MainActivity.kt               # Estado global de accesibilidad

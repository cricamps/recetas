package com.example.recetas.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Pantalla de solicitud de permisos de la aplicación.
 * 
 * Permisos solicitados:
 * - Ubicación (OBLIGATORIO): Para encontrar recetas cercanas y tiendas
 * - Micrófono (OPTATIVO): Para búsqueda por voz
 * 
 * Componentes UI utilizados:
 * - Card: Contenedor de cada permiso
 * - Button: Para solicitar permisos
 * - Switch: Para permisos optativos
 * - Text: Títulos y descripciones
 * - Icon (emoji): Representación visual
 * 
 * @param isDarkTheme Estado del tema oscuro/claro
 * @param onThemeChange Callback para cambiar el tema
 * @param onPermissionsGranted Callback cuando los permisos obligatorios son concedidos
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermisosScreen(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    onPermissionsGranted: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope() // 100% Jetpack Compose
    
    // Estados de permisos
    var locationPermissionGranted by remember { 
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, 
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    var microphonePermissionGranted by remember { 
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, 
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    var microphoneEnabled by remember { mutableStateOf(true) }
    var showLocationRationale by remember { mutableStateOf(false) }
    
    // Launcher para permiso de micrófono (optativo) - DEBE IR PRIMERO
    val microphonePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        microphonePermissionGranted = isGranted
    }
    
    // Launcher para permisos de ubicación (obligatorio)
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                                   permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        
        if (locationPermissionGranted) {
            showLocationRationale = false
            
            // Solicitar micrófono automáticamente si está activado
            if (microphoneEnabled && !microphonePermissionGranted) {
                microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            
            // Navegar automáticamente después de 1 segundo (100% Jetpack Compose)
            coroutineScope.launch {
                delay(1000)
                onPermissionsGranted()
            }
        } else {
            showLocationRationale = true
        }
    }
    
    // Efecto para solicitar permisos automáticamente al abrir la app
    LaunchedEffect(Unit) {
        if (locationPermissionGranted) {
            // Si ya tiene el permiso, navegar directamente
            onPermissionsGranted()
        } else {
            // Si no tiene el permiso, solicitarlo automáticamente
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Permisos Necesarios",
                        style = MaterialTheme.typography.headlineSmall
                    ) 
                },
                actions = {
                    IconButton(onClick = onThemeChange) {
                        Text(
                            text = if (isDarkTheme) "☀️" else "🌙",
                            fontSize = 24.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono principal
            Text(
                text = "🔐",
                fontSize = 80.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Título
            Text(
                text = "Permisos de la App",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Para brindarte la mejor experiencia, solicitaremos acceso a:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )
            
            if (!locationPermissionGranted) {
                Text(
                    text = "⏳ Solicitando permisos...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Card de Ubicación (OBLIGATORIO)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (locationPermissionGranted) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else 
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "📍",
                            fontSize = 32.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ubicación",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "OBLIGATORIO",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (locationPermissionGranted) {
                            Text(
                                text = "✅",
                                fontSize = 28.sp
                            )
                        }
                    }
                    
                    Text(
                        text = "Necesitamos tu ubicación para:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "• Encontrar recetas típicas de tu región\n" +
                               "• Sugerir ingredientes disponibles cerca\n" +
                               "• Recomendarte mercados y tiendas locales",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    if (showLocationRationale) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "⚠️ La ubicación es necesaria para usar la app. Por favor, concede el permiso.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    if (!locationPermissionGranted) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Conceder Ubicación")
                        }
                    }
                }
            }
            
            // Card de Micrófono (OPTATIVO)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (microphonePermissionGranted) 
                        MaterialTheme.colorScheme.secondaryContainer 
                    else 
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "🎤",
                            fontSize = 32.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Micrófono",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "OPCIONAL",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (microphonePermissionGranted) {
                            Text(
                                text = "✅",
                                fontSize = 28.sp
                            )
                        }
                    }
                    
                    Text(
                        text = "El micrófono te permite:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "• Buscar recetas con tu voz\n" +
                               "• Dictar ingredientes\n" +
                               "• Comandos de voz mientras cocinas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (microphonePermissionGranted) 
                                "Micrófono activado" 
                            else 
                                "Activar micrófono",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Switch(
                            checked = microphoneEnabled && microphonePermissionGranted,
                            onCheckedChange = { enabled ->
                                if (enabled && !microphonePermissionGranted) {
                                    microphonePermissionLauncher.launch(
                                        Manifest.permission.RECORD_AUDIO
                                    )
                                }
                                microphoneEnabled = enabled
                            }
                        )
                    }
                    
                    if (!microphonePermissionGranted && microphoneEnabled) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = {
                                microphonePermissionLauncher.launch(
                                    Manifest.permission.RECORD_AUDIO
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Conceder Micrófono (Opcional)")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Información adicional
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🔒 Privacidad",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Tus datos están seguros. Solo usamos tu ubicación para " +
                               "mejorar tu experiencia. Nunca compartimos tu información " +
                               "con terceros sin tu consentimiento.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Botón para continuar (solo si ubicación está concedida)
            if (locationPermissionGranted) {
                Button(
                    onClick = onPermissionsGranted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Continuar a la App",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text = "⚠️ Debes conceder el permiso de ubicación para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

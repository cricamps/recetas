package com.example.recetas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetas.accessibility.ContrastMode
import com.example.recetas.accessibility.ContrastModeControl
import com.example.recetas.accessibility.FontScale
import com.example.recetas.accessibility.FontSizeButton

/**
 * Pantalla para agregar una nueva receta.
 * Incluye formulario con todos los campos necesarios.
 * 
 * Componentes UI utilizados:
 * - Scaffold con TopAppBar
 * - OutlinedTextField: Campos de entrada
 * - DropdownMenu: Selector de dificultad
 * - Button: Guardar y cancelar
 * - Snackbar: Confirmación de guardado
 * - FontSizeButton: Control de tamaño de fuente
 * - ContrastModeControl: Control de nivel de contraste
 * 
 * @param isDarkTheme Estado del tema oscuro/claro
 * @param onThemeChange Callback para cambiar el tema
 * @param fontScale Estado de la escala de fuente
 * @param onFontScaleChange Callback para cambiar la escala de fuente
 * @param contrastMode Estado del modo de contraste
 * @param onContrastModeChange Callback para cambiar el modo de contraste
 * @param onNavigateBack Callback para volver atrás
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarRecetaScreen(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    fontScale: MutableState<FontScale>,
    onFontScaleChange: (FontScale) -> Unit,
    contrastMode: MutableState<ContrastMode>,
    onContrastModeChange: (ContrastMode) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Estados para los campos del formulario
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tiempoPreparacion by remember { mutableStateOf("") }
    var dificultad by remember { mutableStateOf("Media") }
    var ingredientes by remember { mutableStateOf("") }
    var preparacion by remember { mutableStateOf("") }
    
    // Estado para el menú de dificultad
    var expanded by remember { mutableStateOf(false) }
    val dificultades = listOf("Fácil", "Media", "Difícil")
    
    // Estado para el snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessMessage by remember { mutableStateOf(false) }
    
    // Mostrar mensaje de éxito
    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            snackbarHostState.showSnackbar(
                message = "¡Receta guardada exitosamente!",
                duration = SnackbarDuration.Short
            )
            showSuccessMessage = false
            // Volver atrás después de mostrar el mensaje
            kotlinx.coroutines.delay(1000)
            onNavigateBack()
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Nueva Receta",
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    // Control de contraste
                    ContrastModeControl(
                        contrastMode = contrastMode.value,
                        onContrastModeChange = onContrastModeChange,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    
                    // Botón de tamaño de fuente
                    FontSizeButton(
                        currentScale = fontScale,
                        onScaleChange = onFontScaleChange
                    )
                    
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Información de ayuda
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "💡 Completa los campos para agregar una nueva receta",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            
            // Campo: Nombre de la receta
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la receta *") },
                placeholder = { Text("Ej: Pastel de Choclo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            
            // Campo: Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción *") },
                placeholder = { Text("Breve descripción de la receta") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            
            // Campos en fila: Tiempo y Dificultad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Campo: Tiempo de preparación
                OutlinedTextField(
                    value = tiempoPreparacion,
                    onValueChange = { tiempoPreparacion = it },
                    label = { Text("Tiempo *") },
                    placeholder = { Text("60 min") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                
                // Selector: Dificultad
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = dificultad,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Dificultad *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        dificultades.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    dificultad = selectionOption
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            // Campo: Ingredientes
            OutlinedTextField(
                value = ingredientes,
                onValueChange = { ingredientes = it },
                label = { Text("Ingredientes") },
                placeholder = { Text("Un ingrediente por línea") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 8,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                supportingText = { 
                    Text(
                        text = "Escribe cada ingrediente en una línea nueva",
                        style = MaterialTheme.typography.bodySmall
                    ) 
                }
            )
            
            // Campo: Preparación
            OutlinedTextField(
                value = preparacion,
                onValueChange = { preparacion = it },
                label = { Text("Preparación") },
                placeholder = { Text("Pasos de preparación") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 12,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                supportingText = { 
                    Text(
                        text = "Escribe cada paso en una línea nueva",
                        style = MaterialTheme.typography.bodySmall
                    ) 
                }
            )
            
            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón: Cancelar
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                
                // Botón: Guardar
                Button(
                    onClick = {
                        // Validar campos obligatorios
                        if (nombre.isNotBlank() && 
                            descripcion.isNotBlank() && 
                            tiempoPreparacion.isNotBlank()) {
                            // Aquí normalmente se guardaría en una base de datos
                            // Por ahora solo mostramos mensaje de éxito
                            showSuccessMessage = true
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = nombre.isNotBlank() && 
                              descripcion.isNotBlank() && 
                              tiempoPreparacion.isNotBlank()
                ) {
                    Text("Guardar", fontWeight = FontWeight.Bold)
                }
            }
            
            // Nota informativa
            Text(
                text = "* Campos obligatorios",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

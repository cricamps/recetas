package com.example.recetas.frontend

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * COMPONENTE 1: FRAGMENTS
 * 
 * En arquitectura tradicional de Android, los Fragments son componentes reutilizables
 * de UI que representan una porción de la interfaz de usuario.
 * 
 * En Jetpack Compose, el equivalente moderno son las @Composable functions modulares
 * que pueden ser reutilizadas y combinadas.
 */

/**
 * Fragment de Header - Componente reutilizable para encabezados
 */
@Composable
fun HeaderFragment(
    titulo: String,
    subtitulo: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        subtitulo?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Fragment de Lista - Componente reutilizable para listas
 */
@Composable
fun <T> ListFragment(
    items: List<T>,
    onItemClick: (T) -> Unit,
    itemContent: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
    emptyMessage: String = "No hay elementos disponibles"
) {
    if (items.isEmpty()) {
        EmptyStateFragment(message = emptyMessage)
    } else {
        Column(modifier = modifier) {
            items.forEach { item ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onItemClick(item) }
                ) {
                    itemContent(item)
                }
            }
        }
    }
}

/**
 * Fragment de Estado Vacío
 */
@Composable
fun EmptyStateFragment(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Fragment de Carga
 */
@Composable
fun LoadingFragment(
    message: String = "Cargando...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Fragment de Error
 */
@Composable
fun ErrorFragment(
    mensaje: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚠️ Error",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyMedium
        )
        
        onRetry?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = it) {
                Text("Reintentar")
            }
        }
    }
}

/**
 * Fragment de Diálogo
 */
@Composable
fun DialogFragment(
    titulo: String,
    mensaje: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    textoConfirmar: String = "Aceptar",
    textoCancelar: String = "Cancelar"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titulo) },
        text = { Text(mensaje) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(textoConfirmar)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(textoCancelar)
            }
        }
    )
}

/**
 * Fragment de Contenedor
 */
@Composable
fun ContainerFragment(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

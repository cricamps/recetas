package com.example.recetas.frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * COMPONENTE 2: VIEWGROUPS
 * 
 * Equivalentes modernos de LinearLayout, FrameLayout, RelativeLayout, etc.
 */

@Composable
fun VerticalLinearLayout(
    modifier: Modifier = Modifier,
    spacing: Dp = 0.dp,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = if (spacing > 0.dp) {
            Arrangement.spacedBy(spacing)
        } else {
            verticalArrangement
        },
        content = content
    )
}

@Composable
fun HorizontalLinearLayout(
    modifier: Modifier = Modifier,
    spacing: Dp = 0.dp,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = if (spacing > 0.dp) {
            Arrangement.spacedBy(spacing)
        } else {
            horizontalArrangement
        },
        content = content
    )
}

@Composable
fun FrameLayout(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment,
        content = content
    )
}

@Composable
fun CardViewGroup(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    elevation: Dp = 4.dp,
    cornerRadius: Dp = 8.dp,
    padding: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .padding(padding)
    ) {
        content()
    }
}

@Composable
fun BorderedViewGroup(
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 4.dp,
    padding: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
            .padding(padding)
    ) {
        content()
    }
}

@Composable
fun SpacedColumnViewGroup(
    spacing: Dp = 8.dp,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
        content = content
    )
}

@Composable
fun SpacedRowViewGroup(
    spacing: Dp = 8.dp,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        content = content
    )
}

@Composable
fun CenteredViewGroup(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
        content = content
    )
}

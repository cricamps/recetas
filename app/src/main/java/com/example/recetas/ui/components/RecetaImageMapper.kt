package com.example.recetas.ui.components

import android.content.Context
import androidx.annotation.DrawableRes
import com.example.recetas.R

/**
 * Mapeador de imágenes para recetas.
 * Asocia cada ID de receta con su imagen real en res/drawable.
 * Si la imagen específica no existe, retorna el placeholder genérico.
 */
object RecetaImageMapper {

    /**
     * Devuelve el resource ID del drawable correspondiente al ID de receta.
     * Usa el nombre completo mapeado (ej: receta_1_charquican).
     */
    fun getDrawableForReceta(context: Context, recetaId: String): Int {
        // Intentar con nombre completo mapeado: receta_1_charquican
        val nombreCompleto = getDrawableName(recetaId)
        val resIdCompleto = context.resources.getIdentifier(
            nombreCompleto, "drawable", context.packageName
        )
        if (resIdCompleto != 0) return resIdCompleto

        // Fallback: receta_N
        val nombreSimple = "receta_$recetaId"
        val resIdSimple = context.resources.getIdentifier(
            nombreSimple, "drawable", context.packageName
        )
        return if (resIdSimple != 0) resIdSimple else R.drawable.ic_receta_placeholder
    }

    /**
     * Mapa estático: ID de receta -> nombre del drawable.
     */
    fun getDrawableName(recetaId: String): String {
        return mapOf(
            "1"  to "receta_1_charquican",
            "2"  to "receta_2_pastel_papas",
            "3"  to "receta_3_cazuela",
            "4"  to "receta_4_porotos",
            "5"  to "receta_5_empanadas",
            "6"  to "receta_6_carbonada",
            "7"  to "receta_7_plateada",
            "8"  to "receta_8_arroz",
            "9"  to "receta_9_pure_papas",
            "10" to "receta_10_sopaipillas",
            "11" to "receta_11_panqueques",
            "12" to "receta_12_leche_asada",
            "13" to "receta_13_arroz_leche",
            "14" to "receta_14_mote_huesillo",
            "15" to "receta_15_chilenitos",
            "16" to "receta_16_calzones_rotos",
            "17" to "receta_17_humitas",
            "18" to "receta_18_pastel_choclo",
            "19" to "receta_19_chorrillana",
            "20" to "receta_20_ceviche",
            "21" to "receta_21_curanto",
            "22" to "receta_22_porotos_granados",
            "23" to "receta_23_asado",
            "24" to "receta_24_anticuchos",
            "25" to "receta_25_caldo_patas",
            "26" to "receta_26_bistec_tomate",
            "27" to "receta_27_ensalada_chilena",
            "28" to "receta_28_ensalada_tomate",
            "29" to "receta_29_ensalada_verde",
            "30" to "receta_30_ensalada_repollo",
            "31" to "receta_31_ensalada_mixta",
            "32" to "receta_32_betarraga"
        )[recetaId] ?: "receta_placeholder"
    }
}

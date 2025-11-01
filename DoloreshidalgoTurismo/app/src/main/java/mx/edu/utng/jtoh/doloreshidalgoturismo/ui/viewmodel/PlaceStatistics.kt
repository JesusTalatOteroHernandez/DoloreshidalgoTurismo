package mx.edu.utng.jtoh.doloreshidalgoturismo.ui.viewmodel

import mx.edu.utng.jtoh.doloreshidalgoturismo.data.model.PlaceEntity

/**
 * Clase de datos para estadísticas
 */
data class PlaceStatistics(
    val totalPlaces: Int = 0,
    val favoriteCount: Int = 0,
    val categoryCounts: Map<String, Int> = emptyMap(),
    val mostRecentPlace: PlaceEntity? = null
)
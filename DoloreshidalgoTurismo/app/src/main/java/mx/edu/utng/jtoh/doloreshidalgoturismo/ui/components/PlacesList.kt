package mx.edu.utng.jtoh.doloreshidalgoturismo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import mx.edu.utng.jtoh.doloreshidalgoturismo.data.model.PlaceEntity
import mx.edu.utng.jtoh.doloreshidalgoturismo.utils.NavigationHelper

import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Share

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


/**
 * Lista horizontal de tarjetas de lugares turísticos
 * Aparece en la parte inferior del mapa
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesList(
    places: List<PlaceEntity>,
    onPlaceClick: (PlaceEntity) -> Unit,
    onEditClick: (PlaceEntity) -> Unit,
    onDeleteClick: (PlaceEntity) -> Unit,
    onFavoriteClick: (PlaceEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(places) { place ->
            PlaceCard(
                place = place,
                onClick = { onPlaceClick(place) },
                onEditClick = { onEditClick(place) },
                onDeleteClick = { onDeleteClick(place) },
                onFavoriteClick = { onFavoriteClick(place) }
            )
        }
    }
}

/**
 * Tarjeta individual de un lugar turístico
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceCard(
    place: PlaceEntity,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado para controlar la animación de entrada
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween (500)) +
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(500, easing = EaseOutCubic)
                ),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .width(280.dp)
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Encabezado con nombre y favorito
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (place.isFavorite) Icons.Default.Favorite
                        else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (place.isFavorite) Color.Red else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Categoría
            AssistChip(
                onClick = { },
                label = { Text(place.category) },
                leadingIcon = {
                    Icon(
                        imageVector = getCategoryIcon(place.category),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = place.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botones de acción secundarios
                Row {
                    val context = LocalContext.current

                    // Botón para abrir en Google Maps
                    IconButton(
                        onClick = { NavigationHelper.openGoogleMapsView(context, place) }
                    ) {
                        Icon(
                            Icons.Default.Map,
                            contentDescription = "Ver en Maps",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Botón para navegar
                    IconButton(
                        onClick = { NavigationHelper.openGoogleMapsNavigation(context, place) }
                    ) {
                        Icon(
                            Icons.Default.Navigation,
                            contentDescription = "Navegar",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    // Botón para compartir
                    IconButton(
                        onClick = { NavigationHelper.sharePlaceLocation(context, place) }
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Compartir"
                        )
                    }
                }

                // Botones de editar/eliminar
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

        }
    }
}

/**
 * Obtener icono según categoría
 */
fun getCategoryIcon(category: String) = when (category.lowercase()) {
    "iglesia" -> Icons.Default.Place
    "museo" -> Icons.Default.AccountBalance
    "restaurante" -> Icons.Default.Restaurant
    "plaza" -> Icons.Default.Park
    else -> Icons.Default.LocationOn
}

package uk.ac.tees.mad.d3896530.ui.favorite

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import uk.ac.tees.mad.d3896530.BottomNavigationBar
import uk.ac.tees.mad.d3896530.NavDestinations
import uk.ac.tees.mad.d3896530.models.LocationEntity
import uk.ac.tees.mad.d3896530.ui.detail.WeatherDetailNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    navController: NavHostController,
    onAdd: () -> Unit,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    val favoriteLocation by favoriteViewModel.favoriteLocations.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, modifier = Modifier)
        },
        topBar = {
            TopAppBar(title = {
                Text(text = "Favorite locations")
            })
        }
    ) { ip ->
        Column(Modifier.padding(ip)) {
            if (favoriteLocation.isEmpty()) {

                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "No saved or favorite locations",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }
            LazyColumn(Modifier.padding(16.dp)) {

                items(favoriteLocation) { fav ->
                    LocationItem(
                        location = fav,
                        onClick = {
                            navController.navigate(WeatherDetailNav.navRoute + "/${fav.lat}/${fav.lon}")
                        },
                        onDelete = {
                            favoriteViewModel.deleteLocation(fav)
                            Toast.makeText(
                                navController.context,
                                "Location deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LocationItem(location: LocationEntity, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${location.name}, ${location.country}",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${location.lat}, ${location.lon}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

object FavoriteDes : NavDestinations {
    override val navRoute = "favorite"
}
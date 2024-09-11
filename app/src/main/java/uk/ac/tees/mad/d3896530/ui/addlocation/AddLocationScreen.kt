package uk.ac.tees.mad.d3896530.ui.addlocation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.d3896530.NavDestinations
import uk.ac.tees.mad.d3896530.models.LocationResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationScreen(
    viewModel: AddLocationViewModel = hiltViewModel(),
    navigateToFavorite: () -> Unit,
    back: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Location") }, navigationIcon = {
                IconButton(onClick = back) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "back")
                }
            })

        }

    ) { ip ->
        Column(Modifier.padding(ip)) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search Location") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            focusManager.clearFocus()
                            if (query.isNotEmpty()) {
                                viewModel.searchLocation(query)
                            }
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Blue,
                        focusedTrailingIconColor = Color.Blue

                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(searchResults) { location ->
                        LocationItem(location) {
                            viewModel.addLocation(location)
                            query = ""
                            focusManager.clearFocus()
                            navigateToFavorite()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationItem(location: LocationResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${location.name}, ${location.country}",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Blue
                )
            }

        }
    }
}

object AddLocationDes : NavDestinations {
    override val navRoute = "add_location"
}

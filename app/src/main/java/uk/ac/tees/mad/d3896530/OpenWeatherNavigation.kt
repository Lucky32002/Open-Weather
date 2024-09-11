package uk.ac.tees.mad.d3896530

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import uk.ac.tees.mad.d3896530.ui.addlocation.AddLocationDes
import uk.ac.tees.mad.d3896530.ui.addlocation.AddLocationScreen
import uk.ac.tees.mad.d3896530.ui.auth.LoginDes
import uk.ac.tees.mad.d3896530.ui.auth.LoginScreen
import uk.ac.tees.mad.d3896530.ui.auth.RegisterDes
import uk.ac.tees.mad.d3896530.ui.auth.RegisterScreen
import uk.ac.tees.mad.d3896530.ui.detail.WeatherDetailNav
import uk.ac.tees.mad.d3896530.ui.detail.WeatherDetailScreen
import uk.ac.tees.mad.d3896530.ui.favorite.FavoriteDes
import uk.ac.tees.mad.d3896530.ui.favorite.FavoriteScreen
import uk.ac.tees.mad.d3896530.ui.home.HomeDes
import uk.ac.tees.mad.d3896530.ui.home.HomeScreen
import uk.ac.tees.mad.d3896530.ui.profile.ProfileDes
import uk.ac.tees.mad.d3896530.ui.profile.ProfileScreen
import uk.ac.tees.mad.d3896530.ui.splash.SplashDes
import uk.ac.tees.mad.d3896530.ui.splash.SplashScreen

interface NavDestinations {
    val navRoute: String
}

@Composable
fun OpenWeatherNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    NavHost(navController = navController, startDestination = SplashDes.navRoute) {
        composable(SplashDes.navRoute) {
            SplashScreen(
                onComplete = {
                    navController.navigate(if (currentUser != null) HomeDes.navRoute else LoginDes.navRoute)
                }
            )
        }

        composable(HomeDes.navRoute) {
            HomeScreen(
                navController = navController
            )
        }

        composable(LoginDes.navRoute) {
            LoginScreen(
                onRegister = {
                    navController.navigate(RegisterDes.navRoute)
                },
                onSuccess = {
                    navController.navigate(HomeDes.navRoute)
                }
            )
        }

        composable(RegisterDes.navRoute) {
            RegisterScreen(
                onLogin = {
                    navController.navigate(LoginDes.navRoute)
                },
                onSuccess = {
                    navController.navigate(HomeDes.navRoute)
                }
            )
        }

        composable(AddLocationDes.navRoute) {
            AddLocationScreen(
                navigateToFavorite = {
                    navController.navigate(FavoriteDes.navRoute)
                },
                back = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = WeatherDetailNav.routeWithArg,
            arguments = listOf(
                navArgument("lat") {
                    type = NavType.FloatType
                },
                navArgument("lon") {
                    type = NavType.FloatType
                }
            )
        ) { backStack ->
            val lat = backStack.arguments?.getFloat("lat")?.toDouble()
            val lon = backStack.arguments?.getFloat("lon")?.toDouble()
            if (lat != null && lon != null) {
                WeatherDetailScreen(onBack = {
                    navController.popBackStack()
                })
            } else {
                Toast.makeText(context, "Invalid coordinates", Toast.LENGTH_SHORT).show()
            }
        }
        composable(FavoriteDes.navRoute) {
            FavoriteScreen(
                navController = navController,
                onAdd = { navController.navigate(AddLocationDes.navRoute) })
        }

        composable(ProfileDes.navRoute) {
            ProfileScreen(navController = navController)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, modifier: Modifier) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )
    NavigationBar(modifier = modifier) {

        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Blue,
                    selectedTextColor = Color.Blue,
                    indicatorColor = Color.Blue.copy(0.2f),
                    unselectedIconColor = Color.Blue.copy(0.5f),
                    unselectedTextColor = Color.Blue.copy(0.5f),
                )
            )
        }
    }
}


sealed class BottomNavItem(var title: String, var icon: ImageVector, var route: String) {
    object Home : BottomNavItem("Home", Icons.Filled.Home, HomeDes.navRoute)
    object Favorites : BottomNavItem("Favorites", Icons.Filled.Favorite, FavoriteDes.navRoute)
    object Profile : BottomNavItem("Profile", Icons.Filled.Person, ProfileDes.navRoute)
}

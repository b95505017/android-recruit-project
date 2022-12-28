package `in`.hahow.recruit.ui

import `in`.hahow.recruit.ui.classes.HahowClassesScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { HahowClassesScreen() }
        // TODO: Add more destinations
    }
}

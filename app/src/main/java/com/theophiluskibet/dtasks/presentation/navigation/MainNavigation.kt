package com.theophiluskibet.dtasks.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.theophiluskibet.dtasks.presentation.login.LoginScreen
import com.theophiluskibet.dtasks.presentation.tasks.TasksScreen

@Composable
fun MainNavigation(
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    // Use dynamic start destination based on login status
    val startDestination = if (isLoggedIn) Destinations.Tasks else Destinations.Login

    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Destinations.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Destinations.Tasks) {
                        popUpTo(Destinations.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Destinations.Tasks> {
            TasksScreen()
        }
    }
}
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
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = if (isLoggedIn) Destinations.Tasks else Destinations.Login
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
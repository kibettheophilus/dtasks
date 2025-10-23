package com.theophiluskibet.dtasks.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.theophiluskibet.dtasks.presentation.components.DLoadingComponent
import com.theophiluskibet.dtasks.presentation.navigation.MainNavigation
import com.theophiluskibet.dtasks.presentation.ui.theme.DTasksTheme
import com.theophiluskibet.dtasks.sync.triggerImmediateSync
import org.koin.androidx.compose.koinViewModel

/**
 * The main activity for the app.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DTasksTheme {
                DTasksApp()
            }
        }
    }
}

/**
 * The main composable for the app.
 *
 * @param viewModel The [MainViewModel] for this screen.
 */
@Composable
fun DTasksApp(
    viewModel: MainViewModel = koinViewModel()
) {
    val context = LocalActivity.current
    val navController = rememberNavController()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    Log.d("DTasksApp", "DTasksApp: $isLoggedIn")

    // Show loading screen while determining authentication status
    when (isLoggedIn) {
        null -> {
            // Authentication status is being determined
            DLoadingComponent()
        }

        false -> {
            context?.triggerImmediateSync()
            MainNavigation(
                navController = navController,
                isLoggedIn = false
            )
        }

        true -> {
            MainNavigation(
                navController = navController,
                isLoggedIn = true
            )
        }
    }
}

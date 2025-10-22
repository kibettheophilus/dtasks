package com.theophiluskibet.dtasks.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.theophiluskibet.dtasks.presentation.navigation.MainNavigation
import com.theophiluskibet.dtasks.presentation.ui.theme.DTasksTheme
import org.koin.androidx.compose.koinViewModel

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

@Composable
fun DTasksApp(
    viewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    Log.d("Tasky","2 isLoggedIn: $isLoggedIn")

    MainNavigation(
        navController = navController,
        isLoggedIn = isLoggedIn == true
    )
}

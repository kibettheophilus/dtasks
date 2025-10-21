package com.theophiluskibet.dtasks.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.theophiluskibet.dtasks.presentation.login.LoginScreen
import com.theophiluskibet.dtasks.presentation.tasks.TasksScreen
import com.theophiluskibet.dtasks.presentation.ui.theme.DTasksTheme

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
fun DTasksApp() {
    var isLoggedIn by remember { mutableStateOf(true) }

    if (isLoggedIn) {
        TasksScreen()
    } else {
        LoginScreen(
            onLoginSuccess = {
                isLoggedIn = true
            }
        )
    }
}
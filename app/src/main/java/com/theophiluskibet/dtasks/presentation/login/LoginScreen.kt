package com.theophiluskibet.dtasks.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theophiluskibet.dtasks.helpers.isValidEmail
import com.theophiluskibet.dtasks.presentation.components.DTaskButton
import com.theophiluskibet.dtasks.presentation.ui.theme.DTasksTheme
import com.theophiluskibet.dtasks.presentation.ui.theme.PrimaryBlue
import com.theophiluskibet.dtasks.presentation.ui.theme.TextPrimary
import com.theophiluskibet.dtasks.presentation.ui.theme.TextSecondary
import org.koin.androidx.compose.koinViewModel

/**
 * A composable that provides the UI for the login screen.
 *
 * @param onLoginSuccess A callback to be invoked when the login is successful.
 * @param viewModel The [LoginViewModel] for this screen.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            onLoginSuccess()
            viewModel.resetLoginState()
        }
    }
    LoginScreenContent(
        onLoginClick = { email ->
            viewModel.login(email)
        },
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
    )
}

/**
 * The main content of the login screen.
 *
 * @param onLoginClick A callback to be invoked when the login button is clicked.
 * @param isLoading Whether the screen is in a loading state.
 * @param errorMessage An error message to display.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    onLoginClick: (String) -> Unit = {},
    isLoading: Boolean = false,
    errorMessage: String? = null,
) {
    var email by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val canLogin = email.isValidEmail() && !isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo and App Name Section
        LoginHeader()

        Spacer(modifier = Modifier.height(64.dp))

        // Sign In Title
        Text(
            text = "Sign In",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subtitle
        Text(
            text = "Enter your email to login to your account.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Email Input Section
        LoginEmailField(
            email = email,
            onEmailChange = { email = it },
            isError = errorMessage != null,
            focusRequester = focusRequester,
            onImeAction = {
                if (canLogin) {
                    keyboardController?.hide()
                    onLoginClick(email)
                }
            }
        )

        // Error Message
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        DTaskButton(
            onClick = {
                keyboardController?.hide()
                onLoginClick(email)
            },
            enabled = canLogin,
            isLoading = isLoading,
            text = "Login"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Additional spacing to center content better
        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * A composable that displays the login screen header.
 *
 * @param modifier The modifier to apply to this composable.
 */
@Composable
private fun LoginHeader(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = "Dtasks",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            fontSize = 32.sp
        )
    }
}

/**
 * A composable that displays the email input field.
 *
 * @param email The current email value.
 * @param onEmailChange A callback to be invoked when the email value changes.
 * @param isError Whether the email field is in an error state.
 * @param focusRequester The focus requester for this field.
 * @param onImeAction A callback to be invoked when the IME action is triggered.
 * @param modifier The modifier to apply to this composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginEmailField(
    email: String,
    onEmailChange: (String) -> Unit,
    isError: Boolean,
    focusRequester: FocusRequester,
    onImeAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Email",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = {
                Text(
                    text = "Enter your email",
                    color = TextSecondary
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = if (isError) MaterialTheme.colorScheme.error else TextSecondary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onImeAction() }
            ),
            singleLine = true,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                focusedLabelColor = PrimaryBlue,
                cursorColor = PrimaryBlue
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

/**
 * A preview of the login screen.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    DTasksTheme {
        LoginScreen()
    }
}

/**
 * A preview of the login screen in a loading state.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenLoadingPreview() {
    DTasksTheme {
        LoginScreenContent(
            isLoading = true
        )
    }
}

/**
 * A preview of the login screen with an error message.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenErrorPreview() {
    DTasksTheme {
        LoginScreenContent(
            errorMessage = "Invalid email address. Please try again."
        )
    }
}

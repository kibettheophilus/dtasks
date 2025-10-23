package com.theophiluskibet.dtasks.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theophiluskibet.dtasks.domain.repository.AuthRepository
import com.theophiluskibet.dtasks.helpers.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Login screen.
 *
 * @param authRepository The repository for handling authentication.
 */
class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Attempts to log in the user with the given email.
     *
     * @param email The user's email address.
     */
    fun login(email: String) {
        if (!email.isValidEmail()) {
            _uiState.update {
                it.copy(
                    errorMessage = "Please enter a valid email address"
                )
                return
            }
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }
            authRepository.login(email = email).onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccessful = true,
                        errorMessage = null
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Login failed. Please try again."
                    )
                }
            }
        }
    }

    /**
     * Clears any error messages from the UI state.
     */
    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    /**
     * Resets the login state to its initial value.
     */
    fun resetLoginState() {
        _uiState.update {
            it.copy(isLoginSuccessful = false)
        }
    }
}

/**
 * Represents the UI state for the Login screen.
 *
 * @param isLoading Whether a login operation is in progress.
 * @param isLoginSuccessful Whether the login was successful.
 * @param errorMessage An error message to display if login fails.
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val errorMessage: String? = null
)

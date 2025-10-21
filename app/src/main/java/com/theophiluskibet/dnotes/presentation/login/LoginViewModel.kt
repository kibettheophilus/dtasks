package com.theophiluskibet.dnotes.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theophiluskibet.dnotes.domain.repository.AuthRepository
import com.theophiluskibet.dnotes.helpers.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String) {
        if (!email.isValidEmail()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please enter a valid email address"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )
            authRepository.login(email = email).onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoginSuccessful = true,
                    errorMessage = null
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Login failed. Please try again."
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun resetLoginState() {
        _uiState.value = _uiState.value.copy(isLoginSuccessful = false)
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val errorMessage: String? = null
)
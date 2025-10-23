package com.theophiluskibet.dtasks.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theophiluskibet.dtasks.domain.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * The main ViewModel for the app.
 *
 * @param authRepository The repository for handling authentication.
 */
class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {

    /**
     * A [StateFlow] that emits the user's authentication status.
     * Emits `null` while the status is being determined, `true` if the user is logged in, and `false` otherwise.
     */
    val isLoggedIn: StateFlow<Boolean?> = authRepository.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}

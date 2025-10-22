package com.theophiluskibet.dtasks.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    data object Login : Destinations()

    @Serializable
    data object Tasks : Destinations()
}
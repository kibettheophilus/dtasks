package com.theophiluskibet.dtasks.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * A sealed class representing the destinations in the app.
 */
sealed class Destinations {
    /**
     * The login screen destination.
     */
    @Serializable
    data object Login : Destinations()

    /**
     * The tasks screen destination.
     */
    @Serializable
    data object Tasks : Destinations()
}

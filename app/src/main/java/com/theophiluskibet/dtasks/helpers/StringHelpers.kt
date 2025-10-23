package com.theophiluskibet.dtasks.helpers

import android.util.Patterns

/**
 * Checks if a string is a valid email address.
 *
 * @return `true` if the string is a valid email address, `false` otherwise.
 */
fun String.isValidEmail(): Boolean {
    return this.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

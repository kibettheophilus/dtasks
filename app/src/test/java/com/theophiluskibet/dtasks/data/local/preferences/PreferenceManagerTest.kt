package com.theophiluskibet.dtasks.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File

class PreferenceManagerTest {

    private lateinit var testDataStore: DataStore<Preferences>
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var testFile: File

    @Before
    fun setUp() {
        testFile = File.createTempFile("test_preferences", ".preferences_pb")
        testDataStore = PreferenceDataStoreFactory.create(
            produceFile = { testFile }
        )
        preferenceManager = PreferenceManager(testDataStore)
    }

    @Test
    fun updateToken_shouldStoreTokenSuccessfully() = runTest {
        // Given
        val testToken = "test-auth-token-123"

        // When
        preferenceManager.updateToken(testToken)

        // Then
        val storedToken = preferenceManager.fetchToken.first()
        assertEquals(testToken, storedToken)
    }

    @Test
    fun fetchToken_initialState_shouldReturnNull() = runTest {
        // When
        val token = preferenceManager.fetchToken.first()

        // Then
        assertNull(token)
    }

    @Test
    fun updateToken_multipleUpdates_shouldStoreLatestToken() = runTest {
        // Given
        val firstToken = "first-token"
        val secondToken = "second-token"

        // When
        preferenceManager.updateToken(firstToken)
        preferenceManager.updateToken(secondToken)

        // Then
        val storedToken = preferenceManager.fetchToken.first()
        assertEquals(secondToken, storedToken)
    }

    @Test
    fun updateLastSyncTime_shouldStoreTimestampSuccessfully() = runTest {
        // Given
        val testTimestamp = 1640995200000L // 2022-01-01 00:00:00 UTC

        // When
        preferenceManager.updateLastSyncTime(testTimestamp)

        // Then
        val storedTimestamp = preferenceManager.fetchLastSyncTime.first()
        assertEquals(testTimestamp, storedTimestamp)
    }

    @Test
    fun fetchLastSyncTime_initialState_shouldReturnNull() = runTest {
        // When
        val timestamp = preferenceManager.fetchLastSyncTime.first()

        // Then
        assertNull(timestamp)
    }

    @Test
    fun updateLastSyncTime_multipleUpdates_shouldStoreLatestTimestamp() = runTest {
        // Given
        val firstTimestamp = 1640995200000L
        val secondTimestamp = 1641081600000L // Next day

        // When
        preferenceManager.updateLastSyncTime(firstTimestamp)
        preferenceManager.updateLastSyncTime(secondTimestamp)

        // Then
        val storedTimestamp = preferenceManager.fetchLastSyncTime.first()
        assertEquals(secondTimestamp, storedTimestamp)
    }

    @Test
    fun updateIsLoggedIn_shouldStoreBooleanSuccessfully() = runTest {
        // Given
        val isLoggedIn = true

        // When
        preferenceManager.updateIsLoggedIn(isLoggedIn)

        // Then
        val storedValue = preferenceManager.isLoggedIn.first()
        assertEquals(isLoggedIn, storedValue)
    }

    @Test
    fun isLoggedIn_initialState_shouldReturnNull() = runTest {
        // When
        val isLoggedIn = preferenceManager.isLoggedIn.first()

        // Then
        assertNull(isLoggedIn)
    }

    @Test
    fun updateIsLoggedIn_defaultParameter_shouldStoreFalse() = runTest {
        // When - using default parameter (false)
        preferenceManager.updateIsLoggedIn(false)

        // Then
        val storedValue = preferenceManager.isLoggedIn.first()
        assertEquals(false, storedValue)
    }

    @Test
    fun updateIsLoggedIn_toggleValues_shouldStoreCorrectValues() = runTest {
        // When
        preferenceManager.updateIsLoggedIn(true)
        val firstValue = preferenceManager.isLoggedIn.first()

        preferenceManager.updateIsLoggedIn(false)
        val secondValue = preferenceManager.isLoggedIn.first()

        // Then
        assertTrue(firstValue ?: false)
        assertFalse(secondValue ?: true)
    }

    @Test
    fun multiplePreferences_shouldStoreIndependently() = runTest {
        // Given
        val testToken = "test-token"
        val testTimestamp = 1640995200000L
        val isLoggedIn = true

        // When
        preferenceManager.updateToken(testToken)
        preferenceManager.updateLastSyncTime(testTimestamp)
        preferenceManager.updateIsLoggedIn(isLoggedIn)

        // Then
        val storedToken = preferenceManager.fetchToken.first()
        val storedTimestamp = preferenceManager.fetchLastSyncTime.first()
        val storedIsLoggedIn = preferenceManager.isLoggedIn.first()

        assertEquals(testToken, storedToken)
        assertEquals(testTimestamp, storedTimestamp)
        assertEquals(isLoggedIn, storedIsLoggedIn)
    }

    @Test
    fun updateToken_withEmptyString_shouldStoreEmptyString() = runTest {
        // Given
        val emptyToken = ""

        // When
        preferenceManager.updateToken(emptyToken)

        // Then
        val storedToken = preferenceManager.fetchToken.first()
        assertEquals(emptyToken, storedToken)
    }

    @Test
    fun updateLastSyncTime_withZero_shouldStoreZero() = runTest {
        // Given
        val zeroTimestamp = 0L

        // When
        preferenceManager.updateLastSyncTime(zeroTimestamp)

        // Then
        val storedTimestamp = preferenceManager.fetchLastSyncTime.first()
        assertEquals(zeroTimestamp, storedTimestamp)
    }

    @Test
    fun updateLastSyncTime_withNegativeValue_shouldStoreNegativeValue() = runTest {
        // Given
        val negativeTimestamp = -1L

        // When
        preferenceManager.updateLastSyncTime(negativeTimestamp)

        // Then
        val storedTimestamp = preferenceManager.fetchLastSyncTime.first()
        assertEquals(negativeTimestamp, storedTimestamp)
    }
}
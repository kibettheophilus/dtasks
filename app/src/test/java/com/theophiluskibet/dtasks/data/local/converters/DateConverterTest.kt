package com.theophiluskibet.dtasks.data.local.converters

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Date

class DateConverterTest {

    private val converter = DateConverter()

    @Test
    fun `dateToTimestamp should convert Date to Long`() {
        val date = Date(1640995200000L) // 2022-01-01 00:00:00 UTC
        val timestamp = converter.dateToTimestamp(date)
        assertEquals(1640995200000L, timestamp)
    }

    @Test
    fun `dateToTimestamp should return null for null Date`() {
        val timestamp = converter.dateToTimestamp(null)
        assertNull(timestamp)
    }

    @Test
    fun `fromTimestamp should convert Long to Date`() {
        val timestamp = 1640995200000L
        val date = converter.fromTimestamp(timestamp)
        assertEquals(Date(1640995200000L), date)
    }

    @Test
    fun `fromTimestamp should return null for null Long`() {
        val date = converter.fromTimestamp(null)
        assertNull(date)
    }

    @Test
    fun `conversion should be bidirectional`() {
        val originalDate = Date()
        val timestamp = converter.dateToTimestamp(originalDate)
        val convertedDate = converter.fromTimestamp(timestamp)
        assertEquals(originalDate, convertedDate)
    }
}
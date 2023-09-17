package com.koeltv

import com.koeltv.plugins.LogReader
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTest {

    @Test
    fun testFunctionality() = testApplication {
        // Given
        val testString = "test"
        // When
        val response = client.post {
            setBody(testString)
        }
        //Then
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue("log file should exist") { LogReader.fileExists() }
        assertTrue("it should only contain the test string") {
            val logs = LogReader.getLogs()
            logs.size == 1 && logs[0].contains(testString)
        }
    }
}

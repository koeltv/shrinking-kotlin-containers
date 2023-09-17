package com.koeltv

import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import java.util.concurrent.TimeUnit

/**
 * Custom engine main for easy shutdown, uses [CIOApplicationEngine]
 */
object CustomEngineMain {
    private val semaphore = Semaphore(1, 1)

    @JvmStatic
    fun main(args: Array<String>) {
        val port = System.getenv("PORT")?.toInt() ?: 8080
        embeddedServer(CIO, host = "0.0.0.0", port = port) { module() }
            .apply {
                addShutdownHook { stop(0, 1, TimeUnit.SECONDS) }
                start(false)
            }
        runBlocking { semaphore.acquire() }
    }

    fun shutdown(): Unit = semaphore.release()
}
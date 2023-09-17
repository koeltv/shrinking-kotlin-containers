package com.koeltv

import com.koeltv.plugins.configureLogging
import com.koeltv.plugins.configureRouting
import io.ktor.server.application.*
import kotlinx.coroutines.*

fun main(args: Array<String>) {
    // Simple coroutine that shutdown the program after 20 seconds
    CoroutineScope(Dispatchers.Default).launch {
        delay(20000)
        print("20 seconds, shutting down...")
        CustomEngineMain.shutdown()
    }
    CustomEngineMain.main(args)
}

fun Application.module() {
    print("Waiting 2 seconds... ")
    Thread.sleep(2000)
    println("done")

    configureLogging()
    configureRouting()
}

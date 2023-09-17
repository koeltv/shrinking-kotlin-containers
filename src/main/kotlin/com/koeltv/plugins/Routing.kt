package com.koeltv.plugins

import com.koeltv.CustomEngineMain
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        post {
            val received = call.receiveText()

            if (received == "STOP") CustomEngineMain.shutdown()
            else {
                val remoteAddress = call.request.origin.let { "${it.remoteAddress}:${it.remotePort}" }
                writeToLog("$received $remoteAddress")
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

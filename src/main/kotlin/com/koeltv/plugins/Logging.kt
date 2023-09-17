package com.koeltv.plugins

import java.io.File

internal val logFile = File("./service.log")

fun configureLogging() {
    logFile.delete()
    logFile.createNewFile()
}

fun writeToLog(line: String) {
    logFile.appendText("$line\n")
}
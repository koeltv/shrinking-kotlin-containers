package com.koeltv.plugins

object LogReader {
    fun fileExists(): Boolean = logFile.exists()

    fun getLogs(): List<String> = logFile.readLines()
}
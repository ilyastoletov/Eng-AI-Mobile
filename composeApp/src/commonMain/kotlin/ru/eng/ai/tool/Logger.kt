package ru.eng.ai.tool

expect object Logger {
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun e(tag: String, message: String, error: Throwable?)
}
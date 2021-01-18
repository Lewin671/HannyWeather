package com.example.hannyweather

import android.util.Log

object Logger {
    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARN = 4
    private const val ERROR = 5
    private const val RELEASE = 6
    private const val level = RELEASE
    private const val TAG = "MyLoggerTag"

    private fun templateBlock(msg: String, block: (String, String) -> Unit) {
        block(TAG, msg)
    }

    fun v(msg: String) {
        if (VERBOSE >= level) {
            templateBlock(msg, Log::v)
        }
    }

    fun d(msg: String) {
        if (DEBUG >= level) {
            templateBlock(msg, Log::d)
        }
    }

    fun i(msg: String) {
        if (INFO >= level) {
            templateBlock(msg, Log::i)
        }
    }

    fun w(msg: String) {
        if (WARN >= level) {
            templateBlock(msg, Log::w)
        }
    }

    fun e(msg: String) {
        if (ERROR >= level) {
            templateBlock(msg, Log::e)
        }
    }
}
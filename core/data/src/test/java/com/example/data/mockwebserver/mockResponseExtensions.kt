package com.example.data.mockwebserver

import okhttp3.mockwebserver.MockResponse


fun MockResponse.fromJson(jsonFile: String): MockResponse =
    setBody(readJsonFile(jsonFile))

private fun readJsonFile(path: String): String =
    MockResponse::class.java.classLoader!!
        .getResourceAsStream(path)
        ?.bufferedReader()
        ?.use { it.readText() }
        ?: error("Resource not found in test resources: $path")
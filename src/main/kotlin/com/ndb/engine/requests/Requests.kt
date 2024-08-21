package com.ndb.engine.requests

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import okhttp3.OkHttpClient
import okhttp3.Request

object Requests {
    fun get(uri: String): JsonObject? {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(uri)
            .build()

        client.newCall(request).execute().use { res ->
            val rBody = res.body?.string()
            return if (rBody != null) {
                Json.parseToJsonElement(rBody).jsonObject
            } else {
                null
            }
        }
    }
}
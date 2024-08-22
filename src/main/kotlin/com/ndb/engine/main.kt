package com.ndb.engine

import com.ndb.engine.NDBClient.Companion.into
import com.ndb.engine.options.StrictOptions
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun main() {
    val ndbClient = NDBClient(NDB("http://localhost:3000", StrictOptions.NONSTRICT), "ids", "pw")

    ndbClient.build("database", "coll")

    val t1 = ndbClient.addOrReplaceDocument("sasdtr", JsonObject(mapOf("asdt3o" to JsonPrimitive("kills"))))
    println(t1)
}
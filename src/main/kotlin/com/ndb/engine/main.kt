package com.ndb.engine

import com.ndb.engine.NDBClient.Companion.into
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun main() {
    val ndbClient = NDBClient(NDB("http://localhost:3000"), "id", "pw", "database", "coll")
    val t1 = ndbClient.replaceDocumentName("asdf" into "test")
    println(t1)
}
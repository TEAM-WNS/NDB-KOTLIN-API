package com.ndb.engine

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun main() {
    val ndbClient = NDBClient(NDB("http://localhost:3000"), "id", "pw", "database", "coll")
    val t1 = ndbClient.editMapToJsonObject(mapOf("2" to 882))
    val t = ndbClient.addDocumentFieldArrayOrList("flfan", Pair("list", t1))
    println(t1)
    println(t)
}
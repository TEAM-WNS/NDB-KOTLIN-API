package com.ndb.engine

fun main() {
    val ndbClient = NDBClient(NDB("http://localhost:3000"), "id", "pw", "database", "coll")

    val t = ndbClient.editMapToJsonObject(mapOf("hi" to mapOf("1" to "3")))
    println(t)
}
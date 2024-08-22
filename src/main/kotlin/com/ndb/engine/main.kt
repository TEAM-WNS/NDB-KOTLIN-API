package com.ndb.engine

import com.ndb.engine.NDBClient.Companion.into
import com.ndb.engine.options.StrictOptions
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun main() {
    val ndbClient = NDBClient(NDB("http://localhost:3000", StrictOptions.NONSTRICT), "id", "pw", "database", "coll")
    val t1 = ndbClient.addOrReplaceDocumentForceStrict("sasdtr", JsonObject(mapOf("asdto" to JsonPrimitive("kills"))))
    println(t1)
}
package com.ndb.engine

import com.ndb.engine.requests.Requests
import com.ndb.engine.roles.Roles
import kotlinx.serialization.json.JsonObject
import kotlin.reflect.typeOf

class NDBClient(val ndb: NDB, val id: String, val pw: String, val db: String, val coll: String) {
    val dbUrl = ndb.uri + "/ndb/collection?id=$id&pw=$pw&dbName=$db&collectionName=$coll"
    val manageUrl = ndb.uri + "/ndb/create?id=$id&pw=$pw"

    fun createUser(id: String, pw: String, roles: Roles): JsonObject? {
        val creationString = "$id:$pw:${roles.role}"
        val res = Requests.get("$manageUrl&user=$creationString")
        return res
    }

    fun createDataBase(dbName: String, collectionName: String): JsonObject? {
        val res = Requests.get("$manageUrl&database=$dbName&collection=$collectionName")
        return res
    }

    fun getWholeDocument(): JsonObject? {
        val res = Requests.get(dbUrl)
        return res
    }

    fun getDocument(key: String): JsonObject? {
        val res = Requests.get(dbUrl + "&findBy=${key}")
        return res
    }

    fun editDocument(documentKey: String, jsonObject: JsonObject): JsonObject? {
        val res = Requests.get(dbUrl + "&findBy=${documentKey}&edit=${jsonObject.toString()}")
        return res
    }

    fun addDocumentFieldArray(documentKey: String, field: Pair<String, JsonObject>): JsonObject? {
        val res =
            Requests.get(dbUrl + "&findBy=${documentKey}&addField=${field.first}&fieldArray=${field.second.toString()}")
        return res
    }

    fun addDocumentFieldValue(documentKey: String, field: Pair<String, Any>): JsonObject? {
        val res = Requests.get(dbUrl + "&findBy=${documentKey}&addField=${field.first}&fieldValue=${field.second}&isStrict=false")
        return res
    }

    fun addDocumentFieldValueAsStrictMode(documentKey: String, field: Pair<String, Any>): JsonObject? {
        val res = Requests.get(dbUrl + "&findBy=${documentKey}&addField=${field.first}&fieldValue=${field.second}&isStrict=true")
        return res
    }

    fun deleteDocumentFieldValue(documentKey: String, field: String): JsonObject? {
        val res = Requests.get(dbUrl + "&findBy=${documentKey}&deleteField=$field")
        return res
    }

    fun addOrReplaceDocument(key: String, value: JsonObject): JsonObject? {
        val res = Requests.get(dbUrl + "&addKey=$key&keyValue=${value.toString()}")
        return res
    }
    fun deleteDocument(key: String): JsonObject? {
        val res = Requests.get(dbUrl + "&deleteKey=$key")
        return res
    }


}
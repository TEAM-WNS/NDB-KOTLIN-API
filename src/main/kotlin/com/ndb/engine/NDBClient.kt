package com.ndb.engine

import com.ndb.engine.options.StrictOptions
import com.ndb.engine.requests.Requests
import com.ndb.engine.roles.Roles
import kotlinx.serialization.json.*

class NDBClient(val ndb: NDB, val id: String, val pw: String) {
    lateinit var db: String
    lateinit var coll: String
    lateinit var dbUrl: String
    lateinit var manageUrl: String

    fun build(database: String, collection: String) {
        db = database
        coll = collection
        dbUrl = ndb.uri + "/ndb/collection?id=$id&pw=$pw&dbName=$db&collectionName=$coll"
        manageUrl = ndb.uri + "/ndb/create?id=$id&pw=$pw"
    }
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

    fun addDocumentFieldArrayOrList(documentKey: String, field: Pair<String, Any>): JsonObject? {
        val res =
            Requests.get(dbUrl + "&findBy=${documentKey}&addField=${field.first}&fieldArray=${field.second.toString()}")
        return res
    }

    fun addDocumentFieldValue(documentKey: String, field: Pair<String, Any>): JsonObject? {
        val res = Requests.get(dbUrl + "&findBy=${documentKey}&addField=${field.first}&fieldValue=${field.second}&${ndb.option}")
        return res
    }
    fun addDocumentFieldValueForceStrict(documentKey: String, field: Pair<String, Any>): JsonObject? {
        val res = Requests.get(dbUrl + "&findBy=${documentKey}&addField=${field.first}&fieldValue=${field.second}&${StrictOptions.STRICT}")
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
    fun addOrReplaceDocumentForceStrict(key: String, value: JsonObject): JsonObject? {
        val res = Requests.get(dbUrl + "&addKey=$key&keyValue=${value.toString()}")
        return res
    }
    fun deleteDocument(key: String): JsonObject? {
        val res = Requests.get("$dbUrl&deleteKey=$key")
        return res
    }

    fun findDocument(key: String): JsonObject? {
        val json = getWholeDocument()
        val document: JsonObject? = json!![key]?.jsonObject
        return document
    }

    fun getFieldValue(document: JsonObject, field: String): JsonElement? {
        return document[field]
    }

    fun editOrInsertField(documentKey: String, field: String, value: Any): JsonObject? {
        deleteDocumentFieldValue(documentKey, field)
        return if (value is JsonObject) {
            addDocumentFieldArrayOrList(documentKey, Pair(field, value))
        } else {
            addDocumentFieldValue(documentKey, Pair(field, value))
        }

    }
    fun editOrInsertFieldForceStrict(documentKey: String, field: String, value: Any): JsonObject? {
        deleteDocumentFieldValue(documentKey, field)
        return if (value is JsonObject) {
            addDocumentFieldArrayOrList(documentKey, Pair(field, value))
        } else {
            addDocumentFieldValueForceStrict(documentKey, Pair(field, value))
        }

    }

    fun getAllDocumentsAsMap(jsonObject: JsonObject): Map<String, JsonObject> {
        return jsonObject.mapValues { (_, value) ->
            value.jsonObject
        }
    }

    companion object {
        infix fun String.into(str: String): Pair<String, String> {
            return Pair(this, str)
        }
    }

    fun replaceDocumentName(strMap: Pair<String, String>): JsonObject? {
        val docum = getDocument(strMap.first)
        deleteDocument(strMap.first)
        return addOrReplaceDocument(strMap.second, docum!!)
    }

    fun editMapToJsonObject(map: Map<*, *>): JsonObject {
        val jsonMap = map.mapNotNull { (key, value) ->
            if (key is String) {
                val jsonElement = when (value) {
                    is Map<*, *> -> editMapToJsonObject(value)
                    is List<*> -> JsonArray(value.mapNotNull { JsonPrimitive(it.toString()) })
                    is Number -> JsonPrimitive(value)
                    is Boolean -> JsonPrimitive(value)
                    is String -> JsonPrimitive(value)
                    else -> JsonNull
                }
                key to jsonElement
            } else {
                null
            }
        }.toMap()

        return JsonObject(jsonMap)
    }

    fun editListToArray(list: List<*>): List<JsonElement> {
        return list.map { value ->
            when (value) {
                is String -> {
                    try {
                        val parsedList = value.split(",").map { it.trim().toInt() }
                        parsedList.map { JsonPrimitive(it) }
                    } catch (e: NumberFormatException) {
                        listOf(JsonPrimitive(value))
                    }
                }
                is Map<*, *> -> listOf(editMapToJsonObject(value))
                is List<*> -> editListToArray(value)
                is Number -> listOf(JsonPrimitive(value))
                is Boolean -> listOf(JsonPrimitive(value))
                else -> listOf(JsonNull)
            }
        }.flatten()
    }


}
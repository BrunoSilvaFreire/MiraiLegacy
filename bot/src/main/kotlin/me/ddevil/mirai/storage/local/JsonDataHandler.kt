package me.ddevil.mirai.storage.local

import me.ddevil.json.JsonObject
import me.ddevil.json.parse.JsonParser
import me.ddevil.util.Serializable
import java.io.File

class JsonDataHandler : DataHandler {
    override val fileExtension = "json"
    val parser = JsonParser()

    override fun load(file: File): JsonObject? {
        try {
            return parser.parseObject(file)
        } catch(e: Exception) {
            return null
        }
    }

    override fun save(obj: Serializable, file: File) {
        file.writeText(JsonObject(obj.serialize()).toJson())
    }
}
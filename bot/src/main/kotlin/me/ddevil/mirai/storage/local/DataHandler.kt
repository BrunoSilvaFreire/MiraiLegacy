package me.ddevil.mirai.storage.local

import me.ddevil.json.JsonObject
import me.ddevil.json.parse.JsonParser
import me.ddevil.util.Serializable
import java.io.File

interface DataHandler {
    val fileExtension: String

    fun load(file: File): Map<String, Any?>

    fun save(obj: Serializable, file: File)

}

class JsonDataHandler : DataHandler {
    override val fileExtension = "json"
    val parser = JsonParser()

    override fun load(file: File) = parser.parseObject(file)

    override fun save(obj: Serializable, file: File) {
        file.writeText(JsonObject(obj.serialize()).toJson())
    }
}

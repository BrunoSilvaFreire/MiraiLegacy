package me.ddevil.mirai

import me.ddevil.json.JsonObject
import me.ddevil.json.parse.JsonParser
import java.io.File

object Main {
    @JvmStatic
    fun main(args: Array<String>) {

        val miraiConfigFile = File("./$mainConfigFileName")
        if (!miraiConfigFile.exists()) {
            println("Config file was not found, creating a default one, plz configure it correctly before launching again")
            miraiConfigFile.writeText(JsonObject(MiraiConfig.example.serialize()).toJson())
            return
        }

        val json = JsonParser().parseObject(miraiConfigFile)
        val config = MiraiConfig.fromJson(json)
        Mirai(config)
    }
}
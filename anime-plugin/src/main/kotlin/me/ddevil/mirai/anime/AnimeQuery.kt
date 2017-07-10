package me.ddevil.mirai.anime

import me.ddevil.json.JsonObject
import me.ddevil.json.parse.JsonParser
import me.ddevil.mal.MALUser
import me.ddevil.mal.RequestManager
import me.ddevil.mirai.anime.command.AnimeCommand
import me.ddevil.mirai.plugin.Plugin

const val configPath = "maluser.json"
val exampleUser = MALUser("username", "password")
class AnimeQuery : Plugin() {

    lateinit var malUser: MALUser
        private set
    lateinit var requestManager: RequestManager
        private set

    override fun onEnable(): Boolean {
        val configFile = getConfigFile(configPath)
        if (!configFile.exists()) {
            println("The anime plugin requires a MAL account to work, define it in ${configFile.path}")
            configFile.writeText(JsonObject(exampleUser.serialize()).toJson())
            return false
        }
        val json = JsonParser().parseObject(configFile)
        malUser = MALUser(json as Map<String, Any>)
        requestManager = RequestManager(malUser)
        mirai.commandManager.registerCommand(AnimeCommand(this))
        return true
    }

    override fun onDisable() {
    }
}
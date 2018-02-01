package me.ddevil.mirai.welcome.storage

import me.ddevil.mirai.Mirai
import me.ddevil.mirai.storage.StorageLoader
import me.ddevil.util.getString

class WelcomeMessageManager(val mirai: Mirai) : StorageLoader<WelcomeMessage> {
    override fun load(map: Map<String, Any?>): WelcomeMessage {
        val message = map.getString(WelcomeMessage.messageKey)
        val server = map.getString(WelcomeMessage.serverKey)
        return WelcomeMessage(message, mirai.jda.getGuildById(server))
    }

    override val storageKey: String
        get() = "messages/"

}
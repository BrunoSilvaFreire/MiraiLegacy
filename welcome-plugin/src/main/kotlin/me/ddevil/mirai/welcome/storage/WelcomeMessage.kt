package me.ddevil.mirai.welcome.storage

import me.ddevil.mirai.locale.MessageVariable
import me.ddevil.mirai.locale.translateVariables
import me.ddevil.mirai.storage.StorageSavable
import me.ddevil.util.immutableMap
import me.ddevil.util.set
import net.dv8tion.jda.core.entities.Guild

data class WelcomeMessage(
        val message: String,
        val server: Guild
) : StorageSavable {
    companion object {
        const val messageKey = "message"
        const val serverKey = "server"
    }

    override val storagePath: String
        get() = server.id

    override fun serialize(): Map<String, Any> = immutableMap {
        this[messageKey] = message
        this[serverKey] = server.id
    }

    fun translate(vararg params: MessageVariable) = translateVariables(message, *params)
}
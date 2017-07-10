package me.ddevil.mirai

import me.ddevil.mirai.command.CommandManager
import me.ddevil.mirai.command.CommandOwner
import me.ddevil.mirai.event.EventManager
import me.ddevil.mirai.locale.Lang
import me.ddevil.mirai.locale.MessageVariable
import me.ddevil.mirai.locale.MiraiLocale
import me.ddevil.mirai.permission.PermissionManager
import me.ddevil.mirai.plugin.PluginManager
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.MessageChannel
import java.io.File

const val mainConfigFileName = "miraiConfig.json"
const val configTokenIdentifier = "token"
const val exampleToken = "Put ur token hear :D"

class Mirai(
        val config: MiraiConfig
) : CommandOwner {
    override val pluginPrefix = "mirai"
    val jda: JDA = JDABuilder(AccountType.BOT)
            .setToken(config.token)
            .buildAsync()
    val eventManager = EventManager()
    val permissionManager = PermissionManager(this)
    val pluginManager = PluginManager(this)
    val commandManager = CommandManager(this)
    val locale = MiraiLocale(File("./${MiraiLocale.fileName}"))


    init {
        pluginManager.init()
        broadcast(Lang.MIRAI_INIT,
                MessageVariable("plugins", pluginManager.plugins.size.toString()),
                MessageVariable("commands", commandManager.commands.size.toString())
        )
    }

    fun broadcast(lang: Lang, vararg variables: MessageVariable) {
        val msg = locale.getMsg(lang, *variables)
        broadcast(msg)
    }

    fun broadcast(s: String) {
        for (guild in jda.guilds) {
            val ch = guild.publicChannel
            if (ch != null) {
                sendMessage(ch, s)
            }
        }
    }

    fun sendMessage(channel: MessageChannel, lang: Lang, vararg variables: MessageVariable) {
        val msg = locale.getMsg(lang, *variables)
        sendMessage(channel, msg)
    }

    fun sendMessage(channel: MessageChannel, msg: String) {
        channel.sendMessage(msg).queue()
    }
}


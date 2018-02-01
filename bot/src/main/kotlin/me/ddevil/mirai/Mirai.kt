package me.ddevil.mirai

import me.ddevil.json.JsonObject
import me.ddevil.json.parse.JsonParser
import me.ddevil.mirai.command.CommandManager
import me.ddevil.mirai.command.CommandOwner
import me.ddevil.mirai.event.EventManager
import me.ddevil.mirai.locale.Lang
import me.ddevil.mirai.locale.MessageVariable
import me.ddevil.mirai.locale.MiraiLocale
import me.ddevil.mirai.permission.PermissionManager
import me.ddevil.mirai.plugin.PluginManager
import me.ddevil.util.logger
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.MessageChannel
import java.io.File
import java.util.logging.FileHandler
import java.util.logging.Logger

const val mainConfigFileName = "miraiConfig.json"
const val configTokenIdentifier = "token"
const val exampleToken = "Put ur token hear :D"

class Mirai
private
constructor(
        val config: MiraiConfig
) : CommandOwner {
    companion object {
        @JvmOverloads
        fun createLocal(path: String = "./$mainConfigFileName"): Mirai? {
            val miraiConfigFile = File(path)
            if (!miraiConfigFile.exists()) {
                logger.warning("Config file was not found ${miraiConfigFile.absolutePath}, creating a default one, plz configure it correctly before launching again")
                miraiConfigFile.writeText(JsonObject(MiraiConfig.example.serialize()).toJson())
                return null
            }
            logger.info("Using config file '${miraiConfigFile.absolutePath}'")
            val json = JsonParser().parseObject(miraiConfigFile)
            val config = MiraiConfig.fromJson(json)
            return createFromConfig(config)
        }

        fun createFromConfig(config: MiraiConfig): Mirai {
            return Mirai(config)
        }
    }

    override val pluginPrefix = "mirai"
    val jda: JDA = JDABuilder(AccountType.BOT)
            .setToken(config.token)
            .buildAsync()
    val eventManager = EventManager()
    val permissionManager = PermissionManager(this)
    val pluginManager = PluginManager(this)
    val commandManager = CommandManager(this)
    val locale = MiraiLocale(File("./${MiraiLocale.fileName}"))
    val logger = Logger.getLogger("Mirai")

    init {
        logger.addHandler(FileHandler())
        pluginManager.init()
        broadcast(Lang.MIRAI_INIT,
                MessageVariable("plugins", pluginManager.plugins.size.toString()),
                MessageVariable("commands", commandManager.commands.size.toString())
        )
    }

    fun info(msg: String) {
        logger.info("[Mirai] $msg")
    }

    fun severe(msg: String) {
        logger.severe("[Mirai] $msg")
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

    fun sendEmbed(channel: MessageChannel, builder: EmbedBuilder.() -> Unit) {
        val b = EmbedBuilder()
        b.builder()
        channel.sendMessage(b.build())
    }
}


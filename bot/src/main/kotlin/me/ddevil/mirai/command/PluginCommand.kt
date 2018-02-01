package me.ddevil.mirai.command

import me.ddevil.mirai.Mirai
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

class PluginCommand(mirai: Mirai) : Command(setOf("plugins", "pl"), listOf("Shows a list of available plugins"), mirai) {
    override fun execute(mirai: Mirai, event: MessageReceivedEvent, args: CommandArgs) {
        val msg = "```Known plugins: ${System.lineSeparator()}${mirai.pluginManager.plugins.joinToString()}```"
        mirai.sendMessage(event.channel, msg)
    }

}
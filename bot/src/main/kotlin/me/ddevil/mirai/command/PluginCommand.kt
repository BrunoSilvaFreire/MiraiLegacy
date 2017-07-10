package me.ddevil.mirai.command

import me.ddevil.mirai.Mirai
import me.ddevil.util.command.CommandArgs
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

class PluginCommand : Command(setOf("plugins", "pl"), listOf("Shows a list of available plugins")) {
    override fun execute(mirai: Mirai, event: MessageReceivedEvent, args: CommandArgs) {
        val msg = "```Known plugins: ${System.lineSeparator()}${mirai.pluginManager.plugins.joinToString()}```"
        mirai.sendMessage(event.channel, msg)
    }

}
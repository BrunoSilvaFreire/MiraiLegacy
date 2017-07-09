package me.ddevil.mirai.command

import me.ddevil.mirai.Mirai
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

class HelpCommand : Command(
        setOf("help", "たすけて"),
        listOf("Shows this menu")
) {
    override fun execute(mirai: Mirai, event: MessageReceivedEvent) {
        val cmds = mirai.commandManager.commands
        val msgs = "```Known commands: ${System.lineSeparator()}" + cmds.joinToString(System.lineSeparator()) {
            return@joinToString it.labels.joinToString()
        } + "```"
        mirai.sendMessage(event.channel, msgs)
    }
}
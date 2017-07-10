package me.ddevil.mirai.command

import me.ddevil.mirai.Mirai
import me.ddevil.util.command.CommandArgs
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

class HelpCommand : Command(
        setOf("help", "たすけて"),
        listOf("Shows this menu :D")
) {
    override fun execute(mirai: Mirai, event: MessageReceivedEvent, args: CommandArgs) {
        val cmds = mirai.commandManager.commands
        val msgs = "```Known commands: ${System.lineSeparator()}" + cmds.joinToString(System.lineSeparator()) {
            return@joinToString it.labels.joinToString() + ": " + it.description
        } + "```"
        mirai.sendMessage(event.channel, msgs)
    }
}
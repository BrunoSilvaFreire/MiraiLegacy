package me.ddevil.mirai.command

import me.ddevil.mirai.Mirai
import me.ddevil.util.emptyString
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

class HelpCommand(mirai: Mirai) : Command(
        setOf("help", "たすけて"),
        listOf("Shows this menu :D"),
        mirai
) {
    override fun execute(mirai: Mirai, event: MessageReceivedEvent, args: CommandArgs) {
        val cmds = mirai.commandManager.commands
        val header = "***Known commands: ${System.lineSeparator()}***"
        var msg = emptyString()
        for (cmd in cmds) {
            val labels = cmd.labels.joinToString { "**$it**" }
            msg += "$labels: *${cmd.description}*${System.lineSeparator()}"
        }
        mirai.sendMessage(event.channel, msg)
    }
}
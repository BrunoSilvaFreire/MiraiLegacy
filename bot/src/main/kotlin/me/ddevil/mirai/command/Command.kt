package me.ddevil.mirai.command

import me.ddevil.mirai.Mirai
import me.ddevil.util.command.CommandArgs
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

abstract class Command(
        val labels: Set<String>,
        val description: List<String>
) {
    abstract fun execute(mirai: Mirai, event: MessageReceivedEvent, args: CommandArgs)
}
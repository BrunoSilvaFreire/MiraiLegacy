package me.ddevil.mirai.command

import me.ddevil.mirai.Mirai
import me.ddevil.mirai.plugin.Plugin
import me.ddevil.util.command.CommandArgs
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

abstract class Command(
        val labels: Set<String>,
        val description: List<String>,
        val owner: CommandOwner
) {
    abstract fun execute(mirai: Mirai, event: MessageReceivedEvent, args: CommandArgs)
    val permission: String = "cmd.${owner.pluginPrefix}."
}

abstract class AbstractPluginCommand<out P : Plugin>(
        val plugin: P,
        labels: Set<String>,
        description: List<String>
) : Command(labels, description, plugin) {

}


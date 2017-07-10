package me.ddevil.mirai.command

import me.ddevil.mirai.Mirai
import me.ddevil.mirai.locale.Lang
import me.ddevil.mirai.util.exportVariables
import me.ddevil.util.command.CommandArgs
import me.ddevil.util.getStackTraceText
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.awt.Color

class CommandManager(val mirai: Mirai) : ListenerAdapter() {
    companion object {
        val defaultCommands = arrayOf(
                HelpCommand(),
                PluginCommand()
        )
    }

    private var knownCommands = ArrayList<Command>()

    val commandChar = '!'

    init {
        for (command in defaultCommands) {
            registerCommand(command)
        }
        mirai.jda.addEventListener(this)
    }

    fun registerCommand(command: Command) {
        if (command in knownCommands) {
            return
        }
        knownCommands.add(command)
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val message = event.message
        val content = message.content
        if (!content.startsWith(commandChar)) {
            return
        }
        val a = content.split(' ')
        val label = a[0].substring(1)
        val cmd = getCommand(label)
        if (cmd == null) {
            //No command
            mirai.sendMessage(event.channel, Lang.UNKNOWN_COMMAND, *event.exportVariables())
            return
        }


        //Check if has permission
        if (!mirai.permissionManager.hasPermission(event.member, cmd)) {
            mirai.sendMessage(event.channel, Lang.NO_PERMISSION, *event.exportVariables())
            return
        }
        val args = CommandArgs(label, a.slice(1..a.lastIndex).toTypedArray())
        try {
            cmd.execute(mirai, event, args)
        } catch (ex: Exception) {
            event.channel.sendMessage(EmbedBuilder()
                    .setTitle("Oh shit boi")
                    .setColor(Color.red)
                    .addField("Error", "An error occoured when executing command $label! ($cmd)", false)
                    .addField("Description", ensureLimit(ex.getStackTraceText()), false)
                    .build()).queue()
        }
    }

    fun getCommand(label: String) = knownCommands.firstOrNull {
        it.labels.any { it == label }
    }

    val commands: List<Command>
        get() = ArrayList(knownCommands)
}

const val DISCORD_LIMIT = 1000
private fun ensureLimit(synopsis: String): String {
    val size = synopsis.count()
    if (size > DISCORD_LIMIT) {
        return synopsis.substring(0, DISCORD_LIMIT)
    }
    return synopsis
}
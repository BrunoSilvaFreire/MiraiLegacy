package me.ddevil.mirai.command

import me.ddevil.mirai.Mirai
import me.ddevil.mirai.locale.Lang
import me.ddevil.mirai.util.exportVariables
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class CommandManager(val mirai: Mirai) : ListenerAdapter() {
    companion object {
        val defaultCommands = arrayOf(
                HelpCommand()
        )
    }

    private var knownCommands = ArrayList<Command>()

    val commandChar = ':'

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
        val label = message.content.substring(1)
        val cmd = getCommand(label)
        if (cmd == null) {
            //No command
            mirai.sendMessage(event.channel, Lang.UNKNOWN_COMMAND, *event.exportVariables())
            return
        }
        cmd.execute(mirai, event)
    }

    fun getCommand(label: String) = knownCommands.firstOrNull {
        it.labels.any { it == label }
    }

    val commands: List<Command>
        get() = ArrayList(knownCommands)
}
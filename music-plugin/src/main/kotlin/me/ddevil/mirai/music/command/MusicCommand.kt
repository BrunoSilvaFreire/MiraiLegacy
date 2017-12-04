package me.ddevil.mirai.music.command

import me.ddevil.mirai.Mirai
import me.ddevil.mirai.command.Command
import me.ddevil.mirai.music.MusicPlugin
import me.ddevil.util.command.CommandArgs
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

val knownSubCommands = ""

class MusicCommand(plugin: MusicPlugin) : Command(
        setOf("music", "moosic"),
        listOf("Controls music playing in the bot :D"), plugin
) {
    override fun execute(mirai: Mirai, event: MessageReceivedEvent, args: CommandArgs) {
        args.getStringOrElse(0, {

        }) { subCommand ->
            when (subCommand) {
                "join" -> {
                }
                else -> {
                    mirai.sendMessage(event.channel, "Unknown sub command :c Try these:")
                }
            }

        }
    }

}
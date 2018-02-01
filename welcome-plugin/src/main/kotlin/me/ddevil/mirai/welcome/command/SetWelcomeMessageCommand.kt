package me.ddevil.mirai.welcome.command

import me.ddevil.mirai.Mirai
import me.ddevil.mirai.command.Command
import me.ddevil.mirai.command.CommandArgs
import me.ddevil.mirai.welcome.WelcomePlugin
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

class SetWelcomeMessageCommand(welcomePlugin: WelcomePlugin) : Command(
        setOf("setWelcome"),
        listOf("Set's a welcome message for this server"),
        welcomePlugin
) {
    override fun execute(mirai: Mirai, event: MessageReceivedEvent, args: CommandArgs) {

    }

}
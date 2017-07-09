package me.ddevil.mirai.util

import me.ddevil.mirai.locale.MessageVariable
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

fun MessageReceivedEvent.exportVariables() = arrayOf(
        MessageVariable("author", this.author.name)
)
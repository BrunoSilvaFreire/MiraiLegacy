package me.ddevil.bot.test.discord

import me.ddevil.mirai.Mirai
import me.ddevil.mirai.MiraiConfig
import me.ddevil.util.globalLogger
import org.junit.Test
import java.util.logging.Level

class DiscordConnectTest {

    @Test
    fun miraiCreateTest() {
        val token = discordToken
        if (token == null) {
            globalLogger.info("No discord token, can't execute test")
            return
        }
        val config = MiraiConfig(token)
        val mirai = Mirai.createFromConfig(config)
        globalLogger.log(Level.INFO, "Created mirai = '$mirai'")
    }
}
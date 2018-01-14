package me.ddevil.bot.test.discord

import me.ddevil.mirai.Mirai
import me.ddevil.util.globalLogger
import org.junit.Test
import java.util.logging.Level

class DiscordConnectTest {

    @Test
    fun miraiCreateTest() {
        val mirai = Mirai.createLocal()
        globalLogger.log(Level.INFO, "Created mirai = '$mirai'")
    }
}
package me.ddevil.bot.test.discord

import me.ddevil.mirai.Mirai
import org.junit.Test

class DiscordConnectTest{

    @Test
    fun miraiCreateTest() {
        Mirai.createLocal()
    }
}
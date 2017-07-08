package me.ddevil.mirai

import me.ddevil.mirai.event.EventManager
import me.ddevil.mirai.plugin.PluginManager

class Mirai {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

        }
    }

    val eventManager = EventManager()
    val pluginManager = PluginManager()
}
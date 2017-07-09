package me.ddevil.mirai.plugin

import me.ddevil.mirai.Mirai
import me.ddevil.mirai.exception.plugin.PluginAlreadyInitializedException

abstract class Plugin {
    var initialized: Boolean = false
        private set
    lateinit var mirai: Mirai
        private set
    lateinit var info: PluginInfo
        private set

    fun init(info: PluginInfo, mirai: Mirai) {
        if (initialized) {
            throw PluginAlreadyInitializedException(this)
        }
        this.initialized = true
        this.info = info
        this.mirai = mirai
    }
}
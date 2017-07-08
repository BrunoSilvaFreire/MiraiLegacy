package me.ddevil.mirai.plugin

import me.ddevil.mirai.exception.plugin.PluginAlreadyInitializedException

abstract class Plugin {
    var initialized: Boolean = false
        private set

    lateinit var info: PluginInfo
        private set

    fun init(info: PluginInfo) {
        if (initialized) {
            throw PluginAlreadyInitializedException(this)
        }
        this.initialized = true
        this.info = info
    }
}
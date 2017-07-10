package me.ddevil.mirai.plugin

import me.ddevil.mirai.Mirai
import me.ddevil.mirai.exception.plugin.PluginAlreadyInitializedException
import java.io.File

abstract class Plugin {

    var initialized: Boolean = false
        private set

    lateinit var mirai: Mirai
        private set

    lateinit var info: PluginInfo
        private set

    lateinit var dataFolder: File
        private set

    fun getConfigFile(path: String): File {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }
        return File(dataFolder, path)
    }

    fun init(info: PluginInfo, mirai: Mirai) {
        if (initialized) {
            throw PluginAlreadyInitializedException(this)
        }
        this.initialized = true
        this.info = info
        this.mirai = mirai
        dataFolder = File(mirai.pluginManager.pluginsFolder, info.name)
    }

    abstract fun onEnable(): Boolean

    abstract fun onDisable()

    fun reload() {
        onDisable()
        onEnable()
    }

    override fun toString(): String {
        return "${info.name}(${info.version})"
    }
}
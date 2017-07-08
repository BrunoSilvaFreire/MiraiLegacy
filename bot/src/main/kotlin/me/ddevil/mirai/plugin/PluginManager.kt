package me.ddevil.mirai.plugin

import me.ddevil.json.parse.JsonParser
import me.ddevil.mirai.exception.plugin.PluginInfoMissingException
import me.ddevil.mirai.plugin.loader.pluginExtension
import me.ddevil.mirai.plugin.loader.pluginInfoEntryLocation
import java.io.File
import java.io.FileFilter
import java.util.jar.JarEntry
import java.util.jar.JarFile

class PluginManager {
    companion object {
        const val pluginsFolderName = "plugins"
    }

    val pluginsFolder = File("./$pluginsFolderName")

    private val loadedPlugins = HashSet<Plugin>()

    val plugins: Set<Plugin>
        get() {
            return HashSet(loadedPlugins)
        }

    fun init() {
        loadPlugins()
    }

    private fun loadPlugins() {
        val possiblePlugins = pluginsFolder.listFiles(FileFilter {
            return@FileFilter it.extension.endsWith(pluginExtension)
        })
        for (file in possiblePlugins) {
            try {
                val plugin = tryLoadPlugin(file)
                loadedPlugins += plugin
            } catch(e: Exception) {
            }
        }
    }

    private fun tryLoadPlugin(file: File): Plugin {
        val jar = JarFile(file)
        val infoEntry = jar.getJarEntry(pluginInfoEntryLocation) ?: throw PluginInfoMissingException(jar)
        val json = JsonParser().parseObject(jar.getInputStream(infoEntry))
        val pluginInfo = PluginInfo.fromJson(json)
        val main = Class.forName(pluginInfo.main)
        val mainInstance = main.newInstance()
        val plugin = mainInstance as Plugin
        plugin.init(pluginInfo)
        return plugin
    }

}
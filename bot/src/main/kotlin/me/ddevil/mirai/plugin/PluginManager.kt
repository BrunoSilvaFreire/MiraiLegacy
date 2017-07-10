package me.ddevil.mirai.plugin

import me.ddevil.json.parse.JsonParser
import me.ddevil.mirai.Mirai
import me.ddevil.mirai.exception.plugin.PluginInfoMissingException
import me.ddevil.mirai.plugin.loader.PluginClassLoader
import me.ddevil.mirai.plugin.loader.pluginExtension
import me.ddevil.mirai.plugin.loader.pluginInfoEntryLocation
import me.ddevil.util.getStackTraceText
import java.io.File
import java.io.FileFilter
import java.net.URLClassLoader
import java.util.jar.JarEntry
import java.util.jar.JarFile

class PluginManager(
        val mirai: Mirai
) {
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
        if (!pluginsFolder.exists()) {
            println("Plugins folder ${pluginsFolder.absolutePath} doesn't exists, making now.")
            pluginsFolder.mkdirs()
        }
        val possiblePlugins = pluginsFolder.listFiles { f ->
            return@listFiles f.extension.endsWith(pluginExtension)
        }
        if (possiblePlugins == null || possiblePlugins.isEmpty()) {
            println("Couldn't find any plugins. (${possiblePlugins})")
            return
        }

        for (file in possiblePlugins) {
            println("Trying to load file $file as plugin")
            try {
                val plugin = tryLoadPlugin(file)
                if (plugin.onEnable()) {
                    loadedPlugins += plugin
                    println("Plugin $plugin loaded")
                } else {
                    println("Plugin $plugin has notified us not to load it, skipping.")
                }
            } catch(e: Exception) {
                println("Found exception when loading plugin ${file.name}")
                println("```${e.getStackTraceText()}```")
            }
        }
    }

    private fun tryLoadPlugin(file: File): Plugin {
        val jar = JarFile(file)
        val infoEntry = jar.getJarEntry(pluginInfoEntryLocation) ?: throw PluginInfoMissingException(jar)
        val json = JsonParser().parseObject(jar.getInputStream(infoEntry))
        val pluginInfo = PluginInfo.fromJson(json)
        val loader = PluginClassLoader(file, this::class.java.classLoader)
        val mainInstance = loader[pluginInfo.main]
        val plugin = mainInstance as Plugin
        plugin.init(pluginInfo, mirai)
        return plugin
    }

}
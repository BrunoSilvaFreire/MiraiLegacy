package me.ddevil.mirai.plugin

import me.ddevil.mirai.Mirai
import me.ddevil.mirai.plugin.loader.PluginLoader
import me.ddevil.mirai.plugin.loader.pluginExtension
import me.ddevil.util.getStackTraceText
import java.io.File

class PluginManager(
        val mirai: Mirai
) {
    companion object {
        const val pluginsFolderName = "plugins"
    }

    val pluginLoader = PluginLoader()
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
            mirai.info("Plugins folder ${pluginsFolder.absolutePath} doesn't exists, making now.")
            pluginsFolder.mkdirs()
        }
        val possiblePlugins = pluginsFolder.listFiles { f ->
            return@listFiles f.extension.endsWith(pluginExtension)
        }
        if (possiblePlugins == null || possiblePlugins.isEmpty()) {
            mirai.info("Couldn't find any plugins. ($possiblePlugins)")
            return
        }

        for (file in possiblePlugins) {
            mirai.info("Trying to load file $file as plugin")
            try {
                val plugin = pluginLoader.tryLoadPlugin(file, mirai)
                if (plugin == null){
                    mirai.info("Could not load plugin '${file.name}'")
                    continue
                }
                if (plugin.onEnable()) {
                    loadedPlugins += plugin
                    mirai.info("Plugin $plugin loaded")
                } else {
                    mirai.info("Plugin $plugin has notified us not to load it, skipping.")
                }
            } catch (e: Exception) {
                mirai.severe("Found exception when loading plugin ${file.name}")
                mirai.severe("```${e.getStackTraceText()}```")
            } catch (e: ClassNotFoundException) {

            }
        }
    }


}
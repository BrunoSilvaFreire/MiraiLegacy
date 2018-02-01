package me.ddevil.mirai.plugin.loader

import me.ddevil.json.parse.JsonParser
import me.ddevil.mirai.Mirai
import me.ddevil.mirai.exception.plugin.PluginInfoMissingException
import me.ddevil.mirai.plugin.Plugin
import me.ddevil.mirai.plugin.PluginInfo
import java.io.File
import java.util.jar.JarFile

class PluginLoader {
    fun tryLoadPluginInfo(jar: JarFile): PluginInfo? {
        val infoEntry = jar.getJarEntry(pluginInfoEntryLocation) ?: throw PluginInfoMissingException(jar)
        val json = JsonParser().parseObject(jar.getInputStream(infoEntry))
        return PluginInfo.fromJson(json)
    }

    fun tryLoadPlugin(file: File, mirai: Mirai): Plugin? {
        val jar = JarFile(file)
        val pluginInfo = tryLoadPluginInfo(jar) ?: return null
        val loader = PluginClassLoader(file, this::class.java.classLoader)
        val mainInstance = loader[pluginInfo.main]
        val plugin = mainInstance as Plugin
        plugin.init(pluginInfo, mirai)
        return plugin
    }
}
package me.ddevil.mirai.exception.plugin

import java.util.jar.JarFile

class PluginInfoMissingException(
        val jarFile: JarFile
) : Exception("Couldn't find plugin info file in '${jarFile.name}'")
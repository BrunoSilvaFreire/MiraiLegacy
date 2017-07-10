package me.ddevil.mirai.plugin.loader

import java.io.File
import java.net.URLClassLoader

class PluginClassLoader(val file: File, parent: ClassLoader): URLClassLoader(arrayOf(file.toURI().toURL()), parent) {
    operator fun get(name: String?): Any? = if (name == null) null else Class.forName(name, true, this).newInstance()
}
package me.ddevil.mirai.exception.plugin

import me.ddevil.mirai.plugin.Plugin

class PluginAlreadyInitializedException(
        val plugin: Plugin
) : IllegalStateException("Plugin was already initialized ${plugin.info.name}")
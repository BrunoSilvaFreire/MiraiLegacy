package me.ddevil.mirai.exception.plugin

class PluginInfoMissingValuesException(
        val missingValues: List<String>
) : Exception("There are missingValues values in the plugin info file! (${missingValues.joinToString()})")
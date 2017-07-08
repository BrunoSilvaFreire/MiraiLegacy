package me.ddevil.mirai.plugin

import me.ddevil.json.JsonObject
import me.ddevil.mirai.exception.plugin.PluginInfoMissingValuesException
import me.ddevil.mirai.plugin.loader.missingParameterValue
import me.ddevil.mirai.plugin.loader.pluginInfoMainKey
import me.ddevil.mirai.plugin.loader.pluginInfoNameKey
import me.ddevil.mirai.plugin.loader.pluginInfoVersionKey

data class PluginInfo
constructor(
        val name: String,
        val version: String,
        val main: String
) {
    companion object {

        @Throws(PluginInfoMissingValuesException::class)
        fun fromJson(jsonObject: JsonObject): PluginInfo {

            val missingValues = ArrayList<String>()
            var name: String
            try {
                name = jsonObject.getString(pluginInfoNameKey)
            } catch(e: Exception) {
                name = missingParameterValue
                missingValues.add(pluginInfoNameKey)
            }

            var version: String
            try {
                version = jsonObject.getString(pluginInfoVersionKey)
            } catch(e: Exception) {
                version = missingParameterValue
                missingValues.add(pluginInfoVersionKey)
            }

            var main: String
            try {
                main = jsonObject.getString(pluginInfoMainKey)
            } catch(e: Exception) {
                main = missingParameterValue
                missingValues.add(pluginInfoMainKey)
            }
            if (missingValues.isNotEmpty()) {
                throw PluginInfoMissingValuesException(missingValues)
            }
            return PluginInfo(name, version, main)
        }
    }
}
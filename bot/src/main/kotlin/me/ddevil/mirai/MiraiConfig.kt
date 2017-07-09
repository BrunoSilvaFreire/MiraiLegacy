package me.ddevil.mirai

import me.ddevil.json.JsonObject
import me.ddevil.util.Serializable
import me.ddevil.util.immutableMap
import me.ddevil.util.set

data class MiraiConfig(
        val token: String
) : Serializable {
    companion object {
        val example = MiraiConfig(exampleToken)

        fun fromJson(json: JsonObject): MiraiConfig {
            return MiraiConfig(json.getString(configTokenIdentifier))
        }
    }

    override fun serialize(): Map<String, Any> = immutableMap {
        this[configTokenIdentifier] = token
    }
}
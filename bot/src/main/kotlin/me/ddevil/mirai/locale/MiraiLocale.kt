package me.ddevil.mirai.locale

import me.ddevil.json.JsonObject
import me.ddevil.json.parse.JsonParser
import me.ddevil.util.emptyString
import java.io.File

class MiraiLocale(
        file: File
) {
    companion object {
        const val fileName = "locale.json"
    }

    private val strings = HashMap<Lang, String>()

    init {
        val json = if (!file.exists()) {
            JsonObject()
        } else {
            JsonParser().parseObject(file)
        }
        for (lang in Lang.values()) {
            var lastObj = json
            val key = lang.key.split('.')
            var string = emptyString()
            for ((index, path) in key.withIndex()) {
                if (index == key.lastIndex) {
                    val s = lastObj.getStringOrNull(path)
                    if (s == null) {
                        println("Couldn't find value for lang $lang, using default one")
                        string = save(lang, json, file)
                    } else {
                        string = s
                    }
                    break
                }
                val obj = lastObj.getJsonOrNull(path)
                if (obj == null) {
                    println("Couldn't find value for lang $lang, using default one")
                    string = save(lang, json, file)
                    break
                }
                lastObj = obj
            }
            strings[lang] = string
        }
    }

    private fun save(lang: Lang, json: JsonObject, file: File): String {
        val key = lang.key.split('.')
        var lastObj = json
        for ((index, path) in key.withIndex()) {
            if (index == key.lastIndex) {
                lastObj[path] = lang.default
                break
            }
            var obj = lastObj.getJsonOrNull(path)
            if (obj == null) {
                obj = JsonObject()
                lastObj[path] = obj
            }
            lastObj = obj
        }
        file.writeText(json.toJson())
        return lang.default
    }

    fun getMsg(lang: Lang, vararg variables:  MessageVariable): String {
        val s = strings[lang]!!
        return translateVariables(s, *variables)
    }


}
package me.ddevil.mirai.storage.local

import me.ddevil.util.Serializable
import java.io.File

interface DataHandler {
    val fileExtension: String

    fun load(file: File): Map<String, Any?>?

    fun save(obj: Serializable, file: File)

}


package me.ddevil.mirai.storage

import me.ddevil.util.Serializable

interface StorageManager {

    fun getData(accessor: StorageAccessor, data: String): Map<String, Any?>?

    fun saveData(accessor: StorageAccessor, data: Serializable)

    fun <T> getData(accessor: StorageLoader<T>, data: String): T?
}


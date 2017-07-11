package me.ddevil.mirai.storage

interface StorageManager {

    fun getData(accessor: StorageAccessor, data: String): Map<String, Any?>?

    fun saveData(accessor: StorageAccessor, data: StorageSavable)

    fun <T> getData(accessor: StorageLoader<T>, data: String): T?
}


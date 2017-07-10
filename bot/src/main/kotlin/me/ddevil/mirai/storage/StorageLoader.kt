package me.ddevil.mirai.storage

interface StorageLoader<out T> : StorageAccessor {
    fun load(map: Map<String, Any?>): T
}
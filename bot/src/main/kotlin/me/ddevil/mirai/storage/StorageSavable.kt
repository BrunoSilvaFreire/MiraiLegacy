package me.ddevil.mirai.storage

import me.ddevil.util.Serializable

interface StorageSavable : Serializable {
    val storagePath: String
}
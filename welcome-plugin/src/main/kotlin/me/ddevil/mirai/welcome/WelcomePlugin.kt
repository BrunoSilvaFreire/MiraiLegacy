package me.ddevil.mirai.welcome

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import me.ddevil.mirai.plugin.Plugin
import me.ddevil.mirai.storage.local.FileStorageManager
import me.ddevil.mirai.storage.local.JsonDataHandler
import me.ddevil.mirai.welcome.storage.WelcomeMessage
import me.ddevil.mirai.welcome.storage.WelcomeMessageManager
import net.dv8tion.jda.core.entities.Guild

typealias GoogleFunction<K, V> = com.google.common.base.Function<K, V>
class WelcomePlugin : Plugin() {
    lateinit var messageManager: WelcomeMessageManager
        private set
    lateinit var cache: LoadingCache<Guild, WelcomeMessage>
        private set
    lateinit var storageManager: FileStorageManager
        private set

    fun getMessage(server: Guild): WelcomeMessage? = cache[server]

    override fun onEnable(): Boolean {
        storageManager = FileStorageManager(JsonDataHandler)
        messageManager = WelcomeMessageManager(mirai)
        val supplier = GoogleFunction<Guild, WelcomeMessage> {
            if (it == null) {
                return@GoogleFunction null
            }
            return@GoogleFunction storageManager.getData(messageManager, it.id)
        }
        cache = CacheBuilder.newBuilder().build(CacheLoader.from(supplier))
        return true
    }

    override fun onDisable() {
    }

}

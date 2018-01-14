package me.ddevil.mirai.music

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import me.ddevil.mirai.plugin.Plugin
import net.dv8tion.jda.core.entities.Guild

class MusicPlugin : Plugin() {
    private val audioHandlersCache = HashMap<Guild, AudioHandler>()
    val playerManager = DefaultAudioPlayerManager()

    init {
        AudioSourceManagers.registerRemoteSources(playerManager)
    }

    fun getAudioHandler(guild: Guild): AudioHandler {
        return audioHandlersCache.getOrPut(guild) {
            return@getOrPut AudioHandler(playerManager, MusicQueue(), mirai, guild.defaultChannel!!)
        }
    }

    override fun onEnable() = true

    override fun onDisable() {
    }

}
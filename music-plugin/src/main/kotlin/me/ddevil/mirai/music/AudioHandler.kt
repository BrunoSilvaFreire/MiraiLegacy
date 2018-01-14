package me.ddevil.mirai.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame
import me.ddevil.mirai.Mirai
import net.dv8tion.jda.core.audio.AudioSendHandler
import net.dv8tion.jda.core.entities.TextChannel


class AudioHandler(
        playerManager: AudioPlayerManager,
        val queue: MusicQueue,
        val mirai: Mirai,
        val channel: TextChannel
) : AudioSendHandler, AudioEventAdapter() {
    private val audioPlayer = playerManager.createPlayer()
    private var lastFrame: AudioFrame? = null

    var currentMusic: Music? = null
        private set

    override fun canProvide(): Boolean {
        lastFrame = audioPlayer.provide()
        return lastFrame != null
    }

    override fun provide20MsAudio(): ByteArray {
        return lastFrame!!.data
    }

    override fun isOpus(): Boolean {
        return true
    }

    override fun onEvent(event: AudioEvent) {
        if (event is TrackEndEvent) {
            onTrackEnded(event)
        }
    }

    private fun onTrackEnded(event: TrackEndEvent) {
        if (queue.hasNext) {
            play(queue.getNext())
        }
    }

    private fun play(music: Music) {
        currentMusic = music
        mirai.sendMessage(channel, "Now playing: **$currentMusic**")
        audioPlayer.playTrack(music.track)
    }

    init {
        audioPlayer.addListener(this)
    }

    fun addToQueue(track: AudioTrack) {
        val music = Music(track)
        if (queue.isEmpty) {
            play(music)
            return
        }
        mirai.sendMessage(channel, "Added: **$music** to the queue")
    }

    fun addToQueue(track: AudioPlaylist) {
        mirai.sendEmbed(channel) {
            setTitle("Added playlist ${track.name} to the queue")
            appendDescription("Added: **${track.tracks.size}** songs to the queue")
            for ((index, track) in track.tracks.withIndex()) {
                if (index > 10) {
                    break
                }
                val info=track.info
                addField(info.author, "${info.title}", false)
            }
        }
    }

}

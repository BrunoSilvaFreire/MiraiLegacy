package me.ddevil.mirai.music

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import java.util.*

class MusicQueue {
    private val queue: Queue<Music> = PriorityQueue()

    fun add(music: Music) {
        queue.add(music)
    }

    fun getNext() = queue.poll()!!
    val hasNext get() = queue.isNotEmpty()

    fun clear() = queue.clear()
    val isEmpty get() = queue.isEmpty()
}

data class Music(
        val track: AudioTrack
) {
    override fun toString() = track.toString()
}
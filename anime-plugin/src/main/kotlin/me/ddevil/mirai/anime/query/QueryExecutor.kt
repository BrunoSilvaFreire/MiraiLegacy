package me.ddevil.mirai.anime.query

import me.ddevil.mal.misc.Media
import me.ddevil.mirai.anime.AnimeQuery
import net.dv8tion.jda.core.entities.MessageChannel

interface QueryExecutor<E> where E : Media {
    fun execute(plugin: AnimeQuery, query: Query<E>): List<E>
    fun sendResult(channel: MessageChannel, element: E)
    fun resultToText(result: List<E>): String

}
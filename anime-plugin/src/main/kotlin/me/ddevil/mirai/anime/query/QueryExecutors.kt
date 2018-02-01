package me.ddevil.mirai.anime.query

import me.ddevil.mal.anime.Anime
import me.ddevil.mal.manga.Manga
import me.ddevil.mal.request.AnimeSearchRequest
import me.ddevil.mal.request.MangaSearchRequest
import me.ddevil.mirai.anime.AnimeQuery
import net.dv8tion.jda.core.entities.MessageChannel

object QueryExecutors {
    val mangaExecutor = MangaQueryExecutor()
    val animeExecutor = AnimeQueryExecutor()
}

class MangaQueryExecutor : QueryExecutor<Manga> {
    override fun resultToText(result: List<Manga>): String {
        return result.joinToString(System.lineSeparator()) {
            var msg = "${result.indexOf(it)} - ${it.title} [${it.type.name.toLowerCase().capitalize()}]"
            if (!it.englishTitle.isEmpty()) {
                msg += " (${it.englishTitle})"
            }
            return@joinToString msg
        }
    }

    override fun sendResult(channel: MessageChannel, element: Manga) {

    }

    override fun execute(plugin: AnimeQuery, query: Query<Manga>): List<Manga> {
        return plugin.requestManager.request(MangaSearchRequest(query.queryString)).toList()
    }
}

class AnimeQueryExecutor : QueryExecutor<Anime> {
    override fun resultToText(result: List<Anime>): String {
        return result.joinToString(System.lineSeparator()) {
            var msg = "**${result.indexOf(it)}** - *${it.title} [${it.type.name.toLowerCase().capitalize()}]*"
            if (!it.englishTitle.isEmpty()) {
                msg += " (${it.englishTitle})"
            }
            return@joinToString msg
        }
    }

    override fun sendResult(channel: MessageChannel, element: Anime) {

    }

    override fun execute(plugin: AnimeQuery, query: Query<Anime>): List<Anime> {
        return plugin.requestManager.request(AnimeSearchRequest(query.queryString)).toList()
    }
}
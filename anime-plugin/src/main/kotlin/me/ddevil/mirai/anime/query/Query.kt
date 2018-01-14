package me.ddevil.mirai.anime.query

import me.ddevil.mal.anime.Anime
import me.ddevil.mal.manga.Manga
import me.ddevil.mal.misc.Media
import me.ddevil.mirai.anime.AnimeQuery

class Query<T>
private constructor(
        val queryString: String,
        val executor: QueryExecutor<T>
) where T : Media {
    companion object {
        fun ofAnime(anime: String): Query<Anime> {
            return Query(anime, QueryExecutors.animeExecutor)
        }

        fun ofManga(anime: String): Query<Manga> {
            return Query(anime, QueryExecutors.mangaExecutor)
        }
    }

    private var queryResult: ArrayList<T>? = null

    fun execute(plugin: AnimeQuery): List<T> {
        queryResult = ArrayList(executor.execute(plugin, this))
        return result
    }

    val result: List<T>
        get() {
            val r = queryResult ?: throw IllegalStateException("This query was not executed yet!")
            return ArrayList(r)
        }
    val lastIndex: Int
        get() {
            val r = queryResult ?: throw IllegalStateException("This query was not executed yet!")
            return r.lastIndex
        }


    operator fun get(index: Int): T {
        return result[index]
    }
}

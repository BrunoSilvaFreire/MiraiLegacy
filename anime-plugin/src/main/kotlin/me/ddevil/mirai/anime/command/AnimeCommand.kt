package me.ddevil.mirai.anime.command

import me.ddevil.mal.anime.Anime
import me.ddevil.mal.anime.AnimeStatus
import me.ddevil.mal.misc.Media
import me.ddevil.mirai.Mirai
import me.ddevil.mirai.anime.AnimeQuery
import me.ddevil.mirai.anime.query.Query
import me.ddevil.mirai.anime.query.QueryExecutor
import me.ddevil.mirai.anime.query.UserHistory
import me.ddevil.mirai.command.Command
import me.ddevil.util.command.CommandArgs
import me.ddevil.util.exception.WTFException
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

const val DISCORD_LIMIT = 1000
const val ANIME_LABEL = "anime"
const val MANGA_LABEL = "manga"

class AnimeCommand(val plugin: AnimeQuery) : Command(
        setOf(ANIME_LABEL, MANGA_LABEL),
        listOf("Searches for the specified anime on MAL"),
        plugin
) {
    private val historyCache = HashMap<User, UserHistory>()
    override fun execute(mirai: Mirai, event: MessageReceivedEvent, args: CommandArgs) {
        val ch = event.channel
        args.getStringOrElse(0, {
            mirai.sendMessage(ch, "You must use either search or select!")
        }) { commandType ->
            val sender = event.author
            when (commandType) {
                "search" -> handleSearch(sender, args, ch, mirai)
                "select" -> handleSelect(sender, args, ch, mirai)
                else -> {
                    mirai.sendMessage(ch, "Unknown function :c")
                }
            }
        }
    }

    private fun handleSearch(sender: User, args: CommandArgs, ch: MessageChannel, mirai: Mirai) {
        args.joinFromAnd(1) { animeName ->
            val query = when (args.label) {
                ANIME_LABEL -> Query.ofAnime(animeName)
                MANGA_LABEL -> Query.ofManga(animeName)
                else -> throw WTFException()
            }

            val result = query.execute(plugin)
            val e = query.executor as QueryExecutor<Media>
            when {
                result.isEmpty() -> mirai.sendMessage(ch, "Couldn't find any anime with the name $animeName! :c")
                result.size == 1 -> e.sendResult(ch, result.first())
                else -> {
                    mirai.sendMessage(ch, "Found a total of ${result.size} results. Select which one to display using select:")
                    val msg = ensureLimit("```" + e.resultToText(result) + "```")
                    mirai.sendMessage(ch, msg)
                    getHistory(sender).addQuery(query)
                }
            }
        }
    }

    private fun getHistory(sender: User): UserHistory {
        return historyCache.getOrPut(sender) {
            UserHistory()
        }
    }


    private fun handleSelect(sender: User, args: CommandArgs, ch: MessageChannel, mirai: Mirai) {
        val history = getHistory(sender)
        if (history.hasAny) {

            args.getIntOrElse(1, {
                mirai.sendMessage(ch, "You must provide an index!")
            }, {
                mirai.sendMessage(ch, "Invalid number!")
            }) { index ->
                val search = history.last
                if (index < 0 || index > search.lastIndex) {
                    mirai.sendMessage(ch, "Index out of bounds!")
                }
                (search.executor as QueryExecutor<Media>).sendResult(ch, search[index])
            }
        } else {
            mirai.sendMessage(ch, "You don't have any past queries.")
        }
    }


    fun sendAnime(channel: MessageChannel, anime: Anime) {
        val msg = EmbedBuilder()
                .setTitle(anime.title)
                .addField("English", anime.englishTitle)
                .addField("Link", "https://myanimelist.net/anime/${anime.id}/")
                .addField("Episodes", anime.episodes.toString())
                .addField("Status", anime.status)
                .addField("Synopsis", ensureLimit(anime.synopsis))
                .addField("Synonyms", anime.synonyms)
                .addField("Start Date", anime.startDate)
                .setThumbnail(anime.imgUrl.toString())
        if (anime.status != AnimeStatus.NOT_YET_AIRED) {
            msg.addField("End Date", anime.endDate)
        }
        channel.sendMessage(msg.build()).queue()
    }

    private fun ensureLimit(synopsis: String): String {
        val size = synopsis.count()
        if (size > DISCORD_LIMIT) {
            return synopsis.substring(0, DISCORD_LIMIT)
        }
        return synopsis
    }

}

private fun EmbedBuilder.addField(key: String, value: Any): EmbedBuilder {
    addField(key, value.toString(), false)
    return this
}

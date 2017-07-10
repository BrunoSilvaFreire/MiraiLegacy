package me.ddevil.mirai.anime.command

import me.ddevil.mal.anime.Anime
import me.ddevil.mal.anime.AnimeStatus
import me.ddevil.mal.request.AnimeSearchRequest
import me.ddevil.mirai.Mirai
import me.ddevil.mirai.anime.AnimeQuery
import me.ddevil.mirai.command.Command
import me.ddevil.util.command.CommandArgs
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

const val DISCORD_LIMIT = 1000

class AnimeCommand(val plugin: AnimeQuery) : Command(setOf("anime"), listOf("Searches for the specified anime on MAL")) {
    private val lastResults = HashMap<User, List<Anime>>()
    override fun execute(mirai: Mirai, event: MessageReceivedEvent, args: CommandArgs) {
        val ch = event.channel
        args.getStringOrElse(0, {
            mirai.sendMessage(ch, "You must use either search or select!")
        }) {
            commandType ->
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
            val request = AnimeSearchRequest(animeName)
            val result = plugin.requestManager.request(request)
            when {
                result.isEmpty() -> mirai.sendMessage(ch, "Couldn't find any anime with the name $animeName! :c")
                result.size == 1 -> sendAnime(ch, result.first())
                else -> {
                    mirai.sendMessage(ch, "Found a total of ${result.size} results. Select which one to display using select:")
                    val msg = ensureLimit(getMsg(result))
                    mirai.sendMessage(ch, msg)
                    lastResults[sender] = result
                }
            }
        }
    }

    private fun getMsg(result: List<Anime>) = "```" + result.joinToString(System.lineSeparator()) {
        var msg = "${result.indexOf(it)} - ${it.title} [${it.type.name.toLowerCase().capitalize()}]"
        if (!it.englishTitle.isNullOrEmpty()) {
            msg += " (${it.englishTitle})"
        }
        return@joinToString msg
    } + "```"

    private fun handleSelect(sender: User, args: CommandArgs, ch: MessageChannel, mirai: Mirai) {
        if (sender in lastResults) {
            val search = lastResults[sender]!!
            args.getIntOrElse(1, {
                mirai.sendMessage(ch, "You must provide an index!")
            }, {
                mirai.sendMessage(ch, "Invalid number!")
            }) {
                index ->
                if (index < 0 || index > search.lastIndex) {
                    mirai.sendMessage(ch, "Index out of bounds!")
                }
                sendAnime(ch, search[index])
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

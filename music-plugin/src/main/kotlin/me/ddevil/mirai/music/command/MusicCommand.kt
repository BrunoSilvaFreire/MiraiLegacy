package me.ddevil.mirai.music.command

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import me.ddevil.mirai.Mirai
import me.ddevil.mirai.command.Command
import me.ddevil.mirai.music.MusicPlugin
import me.ddevil.util.command.CommandArgs
import net.dv8tion.jda.core.entities.VoiceChannel
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import java.awt.Color

val knownSubCommands = ""

class MusicCommand(val plugin: MusicPlugin) : Command(
        setOf("music", "moosic"),
        listOf("Controls music playing in the bot :D"), plugin
) {
    override fun execute(mirai: Mirai, event: MessageReceivedEvent, args: CommandArgs) {
        args.getStringOrElse(0, {

        }) { subCommand ->
            when (subCommand) {
                "play" -> {
                    val state = event.guild.getMember(event.author).voiceState
                    if (!state.inVoiceChannel()) {
                        mirai.sendMessage(event.channel, "You need to be connected to a voice channel to do this!")
                        return
                    }
                    val channel = state.channel
                    handlePlay(channel, event, mirai, args)
                }
                else -> {
                    mirai.sendMessage(event.channel, "Unknown sub command :c Try these:")
                }
            }

        }
    }

    private fun handlePlay(channel: VoiceChannel, event: MessageReceivedEvent, mirai: Mirai, args: CommandArgs) {
        val textChannel = event.channel
        args.joinFromAnd(1) {
            if (it.isBlank()) {
                mirai.sendMessage(textChannel, "You need to specify a music to play!")
                return
            }
            plugin.playerManager.loadItem(it, object : AudioLoadResultHandler {
                override fun loadFailed(exception: FriendlyException) {
                    mirai.sendEmbed(textChannel) {
                        this.setColor(Color.RED)
                        this.setTitle("O shit boi")
                        this.appendDescription("An error occoured while searching for music '$it'")
                        this.appendDescription(exception.message)

                    }

                }

                override fun trackLoaded(track: AudioTrack) {
                    plugin.getAudioHandler(event.guild).addToQueue(track)
                }

                override fun noMatches() {
                    mirai.sendMessage(textChannel, "Couldn't find any matches for '$it'")
                }

                override fun playlistLoaded(playlist: AudioPlaylist) {
                    plugin.getAudioHandler(event.guild).addToQueue(playlist)
                }
            })
        }
    }
}
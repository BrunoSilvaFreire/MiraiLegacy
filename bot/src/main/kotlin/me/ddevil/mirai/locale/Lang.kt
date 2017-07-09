package me.ddevil.mirai.locale

enum class Lang(
        val key: String,
        val default: String
) {
    UNKNOWN_COMMAND("commands.unknownCommand", "Sorry @{author}, I don't know that command :c"),
}
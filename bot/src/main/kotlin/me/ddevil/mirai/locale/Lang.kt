package me.ddevil.mirai.locale

enum class Lang(
        val key: String,
        val default: String
) {
    MIRAI_INIT("misc.init", "Mirai initialized with {plugins} plugins and {commands} commands."),
    UNKNOWN_COMMAND("commands.unknownCommand", "Sorry @{author}, I don't know that command :c"),
}
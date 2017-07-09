package me.ddevil.mirai.locale

data class MessageVariable(
        val key: String,
        val value: String
) {
    val replacer = "{$key}"
}

fun translateVariables(string: String, vararg variables: MessageVariable): String {
    var s = string
    for (variable in variables) {
        s = s.replace(variable.replacer, variable.value)
    }
    return s
}
package me.ddevil.mirai.plugin

import me.ddevil.util.misc.AbstractNameableDescribable
import me.ddevil.util.misc.Describable
import me.ddevil.util.misc.Nameable

interface Plugin : Nameable, Describable {

    fun onEnable()

    fun onDisable()

}

abstract class AbstractPlugin
@JvmOverloads
constructor(
        name: String,
        alias: String,
        description: List<String> = emptyList()
) : AbstractNameableDescribable(name, alias, description), Plugin
package me.ddevil.mirai.exception.permission

import net.dv8tion.jda.core.entities.Guild

class RoleNotFoundException(
        val guild: Guild,
        val roleId: Long
) : Exception("The role with id $roleId could not be found in guild ${guild.name}")
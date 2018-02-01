package me.ddevil.mirai.permission

import me.ddevil.mirai.Mirai
import me.ddevil.mirai.command.Command
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Role

class PermissionManager(val mirai: Mirai) {
    private val rolePermMap = HashMap<Role, PermissionStatus>()

    fun hasPermission(member: Member, command: Command): Boolean {
        if (member.isOwner) {
            return true
        }
        return member.roles.any {
            it.hasPermission(Permission.ADMINISTRATOR) || hasPermission(it, command)
        }
    }

    fun hasPermission(role: Role, command: Command) = getPermissionStatus(role).permissions.contains(command.permission)

    fun getPermissionStatus(role: Role) = rolePermMap.getOrPut(role) {
        PermissionStatus(role)
    }
}



package me.ddevil.mirai.permission

import me.ddevil.mirai.exception.permission.RoleNotFoundException
import me.ddevil.util.*
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Role


class PermissionStatus : Serializable {
    companion object {
        const val ROLE_KEY = "role"
        const val PERMISSIONS_KEY = "permissions"
    }

    val role: Role
    val permissions: List<String>
        get() = ArrayList(knownPermissions)

    private var knownPermissions: ArrayList<String>

    @JvmOverloads
    constructor(role: Role, permissions: List<String> = emptyList()) {
        this.role = role
        this.knownPermissions = ArrayList(permissions)
    }

    @Throws(RoleNotFoundException::class)
    constructor(guild: Guild, map: Map<String, Any?>) {
        val roleId = map.getLong(ROLE_KEY)
        this.role = guild.getRoleById(roleId) ?: throw RoleNotFoundException(guild, roleId)
        this.knownPermissions = ArrayList(map.getList<String>(PERMISSIONS_KEY))
    }

    override fun serialize(): Map<String, Any> = immutableMap {
        this[ROLE_KEY] = role.idLong
        this[PERMISSIONS_KEY] = permissions
    }
}
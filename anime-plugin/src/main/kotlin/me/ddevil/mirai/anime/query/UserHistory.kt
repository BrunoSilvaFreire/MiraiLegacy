package me.ddevil.mirai.anime.query

class UserHistory {
    private val latestSearches = ArrayList<Query<*>>()
    fun addQuery(query: Query<*>) {
        latestSearches.add(query)
    }

    inline fun <reified E> UserHistory.get(index: Int): E {
        return this[index, E::class.java]
    }

    operator fun get(index: Int): Query<*> {
        return latestSearches[index]
    }

    operator fun <T> get(index: Int, type: Class<T>): T {
        val result = get(index)
        if (!type.isInstance(result)) {
            throw IllegalStateException("Expected object $result to be of type $type!")
        }
        @Suppress("UNCHECKED_CAST")
        return result as T
    }

    operator fun contains(item: Query<*>): Boolean {
        return item in latestSearches
    }

    val hasAny get() = latestSearches.isNotEmpty()
    val last get() = latestSearches.last()
}


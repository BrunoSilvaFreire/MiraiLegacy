package me.ddevil.mirai.event

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.google.common.eventbus.EventBus
import me.ddevil.util.minusAssign

class EventManager {
    companion object {
        const val eventBusName = "MiraiEventBus"
    }

    private val eventBus = EventBus(eventBusName)
    private val registeredListeners: Multimap<Plugin, Any> = HashMultimap.create()

    fun unregisterListener(listener: Any) {
        registeredListeners -= listener
        eventBus.unregister(listener)
    }

    fun unregisterListeners(plugin: Plugin) {
        val listeners = registeredListeners[plugin]
        for (listener in listeners) {
            eventBus.unregister(listener)
        }
        registeredListeners.removeAll(plugin)
    }

    fun registerListener(plugin: Plugin, listener: Any) {
        registeredListeners.put(plugin, listener)
        eventBus.register(listener)
    }

    fun call(event: Event) {
        eventBus.post(event)
    }
}

class Event {
    fun call(manager: EventManager) {
        manager.call(this)
    }
}
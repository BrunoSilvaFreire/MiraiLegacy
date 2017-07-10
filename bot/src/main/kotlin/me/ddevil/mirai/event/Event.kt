package me.ddevil.mirai.event

class Event {
    fun call(manager: EventManager) {
        manager.call(this)
    }
}
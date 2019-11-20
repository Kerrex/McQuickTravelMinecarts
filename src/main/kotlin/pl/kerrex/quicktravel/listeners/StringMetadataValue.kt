package pl.kerrex.quicktravel.listeners

import org.bukkit.metadata.MetadataValue
import org.bukkit.plugin.Plugin

class StringMetadataValue(private val value: String, private val plugin: Plugin) : MetadataValue {
    override fun asFloat(): Float {
        return 0.0f
    }

    override fun value(): Any? {
         return value
    }

    override fun asLong(): Long {
        return 0
    }

    override fun invalidate() {
    }

    override fun asByte(): Byte {
return 0    }

    override fun asBoolean(): Boolean {
        return false
    }

    override fun asDouble(): Double {
        return 0.0
    }

    override fun asShort(): Short {
        return 0
    }

    override fun asString(): String {
        return value
    }

    override fun getOwningPlugin(): Plugin? {
        return plugin
    }

    override fun asInt(): Int {
        return 0
    }
}
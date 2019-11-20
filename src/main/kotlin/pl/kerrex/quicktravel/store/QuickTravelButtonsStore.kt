package pl.kerrex.quicktravel.store

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration

class QuickTravelButtonsStore(private val config: FileConfiguration) {

    init {
        loadFromConfig()
    }

    private fun loadFromConfig() {
        val data = config.getMapList("buttons.locations")
        if (data.isNullOrEmpty()) {
            cache = setOf()
            return
        }

        val preparedData = data.map {
            val x = it["x"].toString().toInt()
            val y = it["y"].toString().toInt()
            val z = it["z"].toString().toInt()
            QuickTravelItemLocation(x, y, z)
        }.toSet()

        cache = preparedData
    }


    fun saveToConfig() {
        val locationMaps = cache.map { it.toMap() }
        config.set("buttons.locations", locationMaps)
    }

    private lateinit var cache: Set<QuickTravelItemLocation>



    fun addQuickTravelButtonLocation(location: QuickTravelItemLocation) {
        cache = cache + location
    }

    fun removeQuickTravelButtonLocation(location: QuickTravelItemLocation) {
        cache = cache - location
    }

    fun getQuickTravelButtonLocations(): Set<QuickTravelItemLocation> {
        return cache
    }
}
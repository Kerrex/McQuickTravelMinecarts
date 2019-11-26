package pl.kerrex.quicktravel

import org.bukkit.plugin.java.JavaPlugin
import pl.kerrex.quicktravel.listeners.QtButtonPlacedListener

class QuickTravelPlugin : JavaPlugin() {
    override fun onEnable() {
        logger.info("plugin is enabled!!!")
        server.pluginManager.registerEvents(QtButtonPlacedListener(this), this)
    }

}
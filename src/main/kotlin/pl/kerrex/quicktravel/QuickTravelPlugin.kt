package pl.kerrex.quicktravel

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import pl.kerrex.quicktravel.commands.CreateQtButton
import pl.kerrex.quicktravel.listeners.QtButtonPlacedListener
import pl.kerrex.quicktravel.store.QuickTravelButtonsStore

class QuickTravelPlugin : JavaPlugin() {
    override fun onEnable() {
        //config.options().copyDefaults(true)
        saveDefaultConfig()

        logger.info("plugin is enabled!!!")
        getCommand("qt-button")!!.setExecutor(CreateQtButton())

        val configStore = QuickTravelButtonsStore(config)
        server.pluginManager.registerEvents(QtButtonPlacedListener(this, configStore), this)

        server.scheduler.scheduleSyncRepeatingTask(this, {
            configStore.saveToConfig()
            saveConfig()
        }, 100, 1000)
    }

}
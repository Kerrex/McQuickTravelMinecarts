package pl.kerrex.quicktravel.commands

import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class CreateQtButton : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return true
        }
        if (!sender.isOp) {
            sender.sendMessage("Only op can use this commmand!")
            return true
        }

        val button = ItemStack(Material.STONE_BUTTON)

        val meta = button.itemMeta
        meta!!.lore = listOf(QUICK_TRAVEL_BUTTON_LORE)
        button.itemMeta = meta

        sender.inventory.addItem(button)

        return true
    }

    companion object {
        const val QUICK_TRAVEL_BUTTON_LORE = "QUICK_TRAVEL_BUTTON"
    }
}
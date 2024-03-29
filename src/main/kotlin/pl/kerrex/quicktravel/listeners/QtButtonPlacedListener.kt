package pl.kerrex.quicktravel.listeners

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent
import org.bukkit.event.vehicle.VehicleMoveEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector

class QtButtonPlacedListener(private val plugin: Plugin) : Listener {

    @EventHandler
    fun onQtButtonPressed(buttonPressed: PlayerInteractEvent) {
        if (buttonPressed.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }

        val clickedBlock = buttonPressed.clickedBlock
        clickedBlock?.let {
            if (isQuickTravelButton(it)) {
                val minecart = spawnMinecartOnNearbyPoweredRail(clickedBlock)
                minecart?.let { cart -> putPlayerIntoTheMinecart(cart, buttonPressed.player) }
            }
        }
    }

    private fun putPlayerIntoTheMinecart(minecart: Minecart, player: Player) {
        minecart.addPassenger(player)
        player.noDamageTicks = 100
    }

    private fun spawnMinecartOnNearbyPoweredRail(clickedBlock: Block): Minecart? {
        val world = plugin.server.getWorld("world")
        val poweredRail = findPoweredRailNearby(clickedBlock) ?: return null

        val minecart = world!!.spawn(poweredRail.location, Minecart::class.java)
        minecart.maxSpeed = 0.4
        minecart.setMetadata("QUICK_TRAVEL_IGNORE_COLLISIONS", FixedMetadataValue(plugin, "true"))

        return minecart
    }

    private fun findPoweredRailNearby(clickedBlock: Block): Block? {
        val allowedCombinations = arrayOf(
                intArrayOf(1, 0, 0),
                intArrayOf(-1, 0, 0),
                intArrayOf(0, 1, 0),
                intArrayOf(0, -1, 0),
                intArrayOf(1, 1, 0),
                intArrayOf(1, -1, 0),
                intArrayOf(-1, 1, 0),
                intArrayOf(-1, -1, 0),
                intArrayOf(0, 0, 1),
                intArrayOf(0, 0, -1),
                intArrayOf(1, 0, 1),
                intArrayOf(1, 0, -1),
                intArrayOf(0, 1, 1),
                intArrayOf(0, 1, -1)
        )

        return allowedCombinations.asSequence()
                .map { clickedBlock.getRelative(it[0], it[1], it[2]) }
                .find { it.type == Material.POWERED_RAIL }

    }

    private fun isPoweredRail(potentialRail: Block) = potentialRail.type == Material.POWERED_RAIL

    private fun isQuickTravelButton(it: Block) =
            it.type == Material.STONE_BUTTON

    @EventHandler
    fun onMinecartDestroyed(minecartDestroyed: VehicleDestroyEvent) {
        val vehicle = minecartDestroyed.vehicle
        if (isQuickTravelMinecart(vehicle)) {
            minecartDestroyed.isCancelled = true
            vehicle.remove()
        }
    }

    private fun isQuickTravelMinecart(vehicle: Vehicle) =
            vehicle.type == EntityType.MINECART && vehicle.hasMetadata("QUICK_TRAVEL_IGNORE_COLLISIONS")

    @EventHandler
    fun onMinecartEntityCollision(minecartCollisionEvent: VehicleEntityCollisionEvent) {
        val vehicle = minecartCollisionEvent.vehicle
        if (isQuickTravelMinecart(vehicle)) {
            minecartCollisionEvent.isCollisionCancelled = true
        }
    }

    @EventHandler
    fun onMinecartBlockCollision(minecartBlockCollisionEvent: VehicleBlockCollisionEvent) {
        val vehicle = minecartBlockCollisionEvent.vehicle
        if (isQuickTravelMinecart(vehicle) && minecartBlockCollisionEvent.block.type == Material.HAY_BLOCK) {
            vehicle.remove()
        }
    }

    @EventHandler
    fun onMinecartStop(minecartMove: VehicleMoveEvent) {
        val vehicle = minecartMove.vehicle
        if (isQuickTravelMinecart(vehicle) && isMinecartNotMoving(vehicle)) {
            vehicle.remove()
        }
    }

    private fun isMinecartNotMoving(vehicle: Vehicle) = vehicle.velocity == Vector(0, 0, 0)

}
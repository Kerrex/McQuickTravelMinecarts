package pl.kerrex.quicktravel.store

import java.io.Serializable

data class QuickTravelItemLocation(val x: Int, val y: Int, val z: Int): Serializable {

    fun toMap(): Map<String, String>  {
        return mapOf("x" to x.toString(), "y" to y.toString(), "z" to z.toString())
    }
}
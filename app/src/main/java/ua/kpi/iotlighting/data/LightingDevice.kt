package ua.kpi.iotlighting.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lighting_devices")
data class LightingDevice(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val room: String,
    val wattage: Double,
    val brightness: Int,
    val operatorEmail: String,
    val createdAt: Long = System.currentTimeMillis()
)

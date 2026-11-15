package ua.kpi.iotlighting.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LightingRepository(
    private val dao: LightingDao,
    private val remote: RemoteLightingDataSource = RemoteLightingDataSource()
) {
    fun observeDevices(): Flow<List<LightingDevice>> = dao.observeAll()

    fun observeBrightDevices(minBrightness: Int = 50): Flow<List<LightingDevice>> =
        dao.observeBright(minBrightness)

    suspend fun addDevice(device: LightingDevice): Result<Long> {
        return try {
            Result.success(dao.insert(device))
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    suspend fun deleteDevice(device: LightingDevice): Result<Unit> {
        return try {
            dao.delete(device)
            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    fun observeSortedByPower(): Flow<List<LightingDevice>> =
        dao.observeAll().map { list -> list.sortedByDescending { it.wattage } }

    fun observeSortedByName(): Flow<List<LightingDevice>> =
        dao.observeAll().map { list -> list.sortedBy { it.name.lowercase() } }

    suspend fun refreshAggregates(): Result<MonitorSnapshot> {
        return try {
            val local = dao.observeAll().first()
            val remoteList = remote.fetchSnapshot(local)
            val totalPower = remoteList.sumOf { it.wattage * it.brightness / 100.0 }
            val avgBrightness = if (remoteList.isEmpty()) {
                0
            } else {
                remoteList.map { it.brightness }.average().toInt()
            }
            Result.success(
                MonitorSnapshot(
                    totalPower = totalPower,
                    activeDevices = remoteList.size,
                    averageBrightness = avgBrightness
                )
            )
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}

data class MonitorSnapshot(
    val totalPower: Double,
    val activeDevices: Int,
    val averageBrightness: Int
)

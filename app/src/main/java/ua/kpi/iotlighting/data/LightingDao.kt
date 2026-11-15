package ua.kpi.iotlighting.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LightingDao {
    @Query("SELECT * FROM lighting_devices ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<LightingDevice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: LightingDevice): Long

    @Delete
    suspend fun delete(device: LightingDevice)

    @Query("SELECT * FROM lighting_devices WHERE brightness > :minBrightness ORDER BY wattage DESC")
    fun observeBright(minBrightness: Int): Flow<List<LightingDevice>>
}

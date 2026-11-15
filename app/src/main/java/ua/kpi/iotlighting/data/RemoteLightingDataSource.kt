package ua.kpi.iotlighting.data

import kotlinx.coroutines.delay
import ua.kpi.iotlighting.constants.LightingConstants
import kotlin.random.Random

class RemoteLightingDataSource {
    private var cache: List<LightingDevice> = emptyList()
    private var cachedAt: Long = 0L

    suspend fun fetchSnapshot(local: List<LightingDevice>): List<LightingDevice> {
        delay(400)
        val now = System.currentTimeMillis()
        if (cache.isNotEmpty() && now - cachedAt < LightingConstants.CACHE_TTL_MS) {
            return cache
        }
        cache = local.map { device ->
            device.copy(
                brightness = (device.brightness + Random.nextInt(-5, 6))
                    .coerceIn(LightingConstants.MIN_BRIGHTNESS, LightingConstants.MAX_BRIGHTNESS)
            )
        }
        cachedAt = now
        return cache
    }
}

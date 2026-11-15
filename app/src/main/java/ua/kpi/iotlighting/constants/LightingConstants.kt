package ua.kpi.iotlighting.constants

object LightingConstants {
    const val MIN_PASSWORD_LENGTH = 6
    const val MIN_NAME_LENGTH = 2
    const val MIN_WATTAGE = 1.0
    const val MAX_WATTAGE = 500.0
    const val MIN_BRIGHTNESS = 1
    const val MAX_BRIGHTNESS = 100
    const val FORM_DELAY_MS = 700L
    const val MONITOR_TICK_MS = 1500L
    const val HOURS_PER_DAY_DEFAULT = 5.0
    const val CACHE_TTL_MS = 10_000L
}

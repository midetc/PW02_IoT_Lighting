package ua.kpi.iotlighting.data

import ua.kpi.iotlighting.constants.LightingConstants
import ua.kpi.iotlighting.i18n.AppStrings

object FormValidator {
    fun validate(
        name: String,
        room: String,
        wattage: String,
        brightness: String,
        email: String
    ): String? {
        if (name.isBlank() || room.isBlank() || wattage.isBlank() || brightness.isBlank() || email.isBlank()) {
            return AppStrings.FILL_ALL
        }
        if (name.trim().length < LightingConstants.MIN_NAME_LENGTH) {
            return AppStrings.NAME_SHORT
        }
        if (!email.contains("@") || !email.contains(".")) {
            return AppStrings.EMAIL_INVALID
        }
        val watts = wattage.toDoubleOrNull()
        if (watts == null || watts < LightingConstants.MIN_WATTAGE || watts > LightingConstants.MAX_WATTAGE) {
            return AppStrings.WATTAGE_INVALID
        }
        val level = brightness.toIntOrNull()
        if (
            level == null ||
            level < LightingConstants.MIN_BRIGHTNESS ||
            level > LightingConstants.MAX_BRIGHTNESS
        ) {
            return AppStrings.BRIGHTNESS_INVALID
        }
        return null
    }
}

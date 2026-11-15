package ua.kpi.iotlighting.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ua.kpi.iotlighting.i18n.AppStrings

class FormValidatorTest {
    @Test
    fun validate_acceptsCorrectDevice() {
        val error = FormValidator.validate(
            name = "Hall LED",
            room = "Hall",
            wattage = "12",
            brightness = "70",
            email = "ops@mail.com"
        )
        assertNull(error)
    }

    @Test
    fun validate_rejectsBadWattage() {
        val error = FormValidator.validate(
            name = "Hall LED",
            room = "Hall",
            wattage = "900",
            brightness = "70",
            email = "ops@mail.com"
        )
        assertEquals(AppStrings.WATTAGE_INVALID, error)
    }
}

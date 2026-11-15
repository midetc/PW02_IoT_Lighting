package ua.kpi.iotlighting.ui.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.kpi.iotlighting.constants.LightingConstants
import ua.kpi.iotlighting.data.FormValidator
import ua.kpi.iotlighting.data.LightingDevice
import ua.kpi.iotlighting.data.LightingRepository
import ua.kpi.iotlighting.i18n.AppStrings

data class FormUiState(
    val name: String = "",
    val room: String = "",
    val wattage: String = "",
    val brightness: String = "80",
    val email: String = "",
    val loading: Boolean = false,
    val message: String = "",
    val success: Boolean = false
)

class FormViewModel(
    private val repository: LightingRepository
) : ViewModel() {
    private val _state = MutableStateFlow(FormUiState())
    val state: StateFlow<FormUiState> = _state.asStateFlow()

    fun onName(value: String) = _state.update { it.copy(name = value, message = "") }
    fun onRoom(value: String) = _state.update { it.copy(room = value, message = "") }
    fun onWattage(value: String) = _state.update { it.copy(wattage = value, message = "") }
    fun onBrightness(value: String) = _state.update { it.copy(brightness = value, message = "") }
    fun onEmail(value: String) = _state.update { it.copy(email = value, message = "") }

    fun submit() {
        val current = _state.value
        val error = FormValidator.validate(
            current.name,
            current.room,
            current.wattage,
            current.brightness,
            current.email
        )
        if (error != null) {
            _state.update { it.copy(message = error, success = false) }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(loading = true, message = AppStrings.LOADING) }
            delay(LightingConstants.FORM_DELAY_MS)
            val result = repository.addDevice(
                LightingDevice(
                    name = current.name.trim(),
                    room = current.room.trim(),
                    wattage = current.wattage.toDouble(),
                    brightness = current.brightness.toInt(),
                    operatorEmail = current.email.trim()
                )
            )
            result.onSuccess {
                _state.update {
                    FormUiState(
                        brightness = "80",
                        loading = false,
                        message = AppStrings.FORM_OK,
                        success = true
                    )
                }
            }.onFailure {
                _state.update {
                    it.copy(loading = false, message = AppStrings.STATUS_ERROR, success = false)
                }
            }
        }
    }

    class Factory(private val repository: LightingRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FormViewModel(repository) as T
        }
    }
}

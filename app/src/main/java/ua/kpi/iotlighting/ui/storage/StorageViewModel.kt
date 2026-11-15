package ua.kpi.iotlighting.ui.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ua.kpi.iotlighting.data.LightingDevice
import ua.kpi.iotlighting.data.LightingRepository

enum class SortMode { POWER, NAME }

data class StorageUiState(
    val devices: List<LightingDevice> = emptyList(),
    val onlyBright: Boolean = false,
    val sortMode: SortMode = SortMode.POWER,
    val message: String = ""
)

class StorageViewModel(
    private val repository: LightingRepository
) : ViewModel() {
    private val onlyBright = MutableStateFlow(false)
    private val sortMode = MutableStateFlow(SortMode.POWER)

    val state: StateFlow<StorageUiState> = combine(
        repository.observeDevices(),
        onlyBright,
        sortMode
    ) { devices, bright, sort ->
        val filtered = if (bright) {
            devices.filter { it.brightness > 50 }
        } else {
            devices
        }
        val sorted = when (sort) {
            SortMode.POWER -> filtered.sortedByDescending { it.wattage }
            SortMode.NAME -> filtered.sortedBy { it.name.lowercase() }
        }
        StorageUiState(
            devices = sorted,
            onlyBright = bright,
            sortMode = sort
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StorageUiState())

    fun toggleBrightFilter() {
        onlyBright.value = !onlyBright.value
    }

    fun setSort(mode: SortMode) {
        sortMode.value = mode
    }

    fun delete(device: LightingDevice) {
        viewModelScope.launch {
            repository.deleteDevice(device)
        }
    }

    class Factory(private val repository: LightingRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StorageViewModel(repository) as T
        }
    }
}

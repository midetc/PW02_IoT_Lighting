package ua.kpi.iotlighting.ui.monitor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.kpi.iotlighting.data.LightingRepository
import ua.kpi.iotlighting.i18n.AppStrings

data class MonitorUiState(
    val totalPower: Double = 0.0,
    val activeDevices: Int = 0,
    val averageBrightness: Int = 0,
    val loading: Boolean = false,
    val message: String = AppStrings.STATUS_OK
)

class MonitorViewModel(
    private val repository: LightingRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MonitorUiState())
    val state: StateFlow<MonitorUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, message = AppStrings.STATUS_LOADING) }
            val result = repository.refreshAggregates()
            result.onSuccess { snapshot ->
                _state.update {
                    it.copy(
                        totalPower = snapshot.totalPower,
                        activeDevices = snapshot.activeDevices,
                        averageBrightness = snapshot.averageBrightness,
                        loading = false,
                        message = AppStrings.STATUS_OK
                    )
                }
            }.onFailure {
                _state.update {
                    it.copy(loading = false, message = AppStrings.STATUS_ERROR)
                }
            }
        }
    }

    class Factory(private val repository: LightingRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MonitorViewModel(repository) as T
        }
    }
}

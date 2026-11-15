package ua.kpi.iotlighting.ui.storage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.kpi.iotlighting.data.LightingDevice
import ua.kpi.iotlighting.data.LightingRepository
import ua.kpi.iotlighting.i18n.AppStrings

@Composable
fun StorageScreen(
    repository: LightingRepository,
    onBack: () -> Unit,
    viewModel: StorageViewModel = viewModel(factory = StorageViewModel.Factory(repository))
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(AppStrings.STORAGE_TITLE, style = MaterialTheme.typography.titleLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.setSort(SortMode.POWER) }) {
                Text(AppStrings.SORT_POWER)
            }
            Button(onClick = { viewModel.setSort(SortMode.NAME) }) {
                Text(AppStrings.SORT_NAME)
            }
        }
        Button(onClick = viewModel::toggleBrightFilter, modifier = Modifier.fillMaxWidth()) {
            Text(if (state.onlyBright) AppStrings.FILTER_ALL else AppStrings.FILTER_ACTIVE)
        }
        if (state.devices.isEmpty()) {
            Text(AppStrings.EMPTY_LIST)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.devices, key = { it.id }) { device ->
                    DeviceCard(device = device, onDelete = { viewModel.delete(device) })
                }
            }
        }
        TextButton(onClick = onBack) { Text(AppStrings.BACK) }
    }
}

@Composable
private fun DeviceCard(device: LightingDevice, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("${device.name} · ${device.room}", style = MaterialTheme.typography.titleMedium)
            Text("${device.wattage} ${AppStrings.WATTS}, ${device.brightness}${AppStrings.PERCENT}")
            Text(device.operatorEmail)
            TextButton(onClick = onDelete) { Text(AppStrings.DELETE) }
        }
    }
}

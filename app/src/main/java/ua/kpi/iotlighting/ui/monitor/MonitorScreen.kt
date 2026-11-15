package ua.kpi.iotlighting.ui.monitor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.kpi.iotlighting.data.LightingRepository
import ua.kpi.iotlighting.i18n.AppStrings

@Composable
fun MonitorScreen(
    repository: LightingRepository,
    onBack: () -> Unit,
    viewModel: MonitorViewModel = viewModel(factory = MonitorViewModel.Factory(repository))
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(AppStrings.MONITOR_TITLE, style = MaterialTheme.typography.titleLarge)
        MetricLine("${AppStrings.TOTAL_POWER}: ${"%.1f".format(state.totalPower)} ${AppStrings.WATTS}")
        MetricLine("${AppStrings.ACTIVE_DEVICES}: ${state.activeDevices}")
        MetricLine("${AppStrings.AVG_BRIGHTNESS}: ${state.averageBrightness}${AppStrings.PERCENT}")
        if (state.loading) {
            CircularProgressIndicator()
        }
        Text(state.message)
        Button(
            onClick = viewModel::refresh,
            enabled = !state.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(AppStrings.REFRESH)
        }
        TextButton(onClick = onBack) { Text(AppStrings.BACK) }
    }
}

@Composable
private fun MetricLine(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium)
}

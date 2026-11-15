package ua.kpi.iotlighting.ui.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
fun DeviceFormScreen(
    repository: LightingRepository,
    onBack: () -> Unit,
    viewModel: FormViewModel = viewModel(factory = FormViewModel.Factory(repository))
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FormHeader()
        FormField(state.name, AppStrings.DEVICE_NAME, AppStrings.HINT_NAME, viewModel::onName)
        FormField(state.room, AppStrings.ROOM_NAME, AppStrings.HINT_ROOM, viewModel::onRoom)
        FormField(state.wattage, AppStrings.WATTAGE, AppStrings.HINT_WATTAGE, viewModel::onWattage)
        FormField(
            state.brightness,
            AppStrings.BRIGHTNESS,
            AppStrings.HINT_BRIGHTNESS,
            viewModel::onBrightness
        )
        FormField(
            state.email,
            AppStrings.OPERATOR_EMAIL,
            AppStrings.HINT_EMAIL,
            viewModel::onEmail
        )
        Spacer(modifier = Modifier.height(8.dp))
        SubmitButton(enabled = !state.loading, onClick = viewModel::submit)
        if (state.loading) {
            CircularProgressIndicator()
            Text(AppStrings.LOADING)
        }
        if (state.message.isNotBlank()) {
            Text(
                text = state.message,
                color = if (state.success) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }
        TextButton(onClick = onBack) { Text(AppStrings.BACK) }
    }
}

@Composable
private fun FormHeader() {
    Text(AppStrings.MENU_FORM, style = MaterialTheme.typography.titleLarge)
}

@Composable
private fun FormField(
    value: String,
    label: String,
    hint: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        supportingText = { Text(hint) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SubmitButton(enabled: Boolean, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = enabled, modifier = Modifier.fillMaxWidth()) {
        Text(AppStrings.SAVE)
    }
}

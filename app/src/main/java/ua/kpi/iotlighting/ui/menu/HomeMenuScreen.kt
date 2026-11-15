package ua.kpi.iotlighting.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.kpi.iotlighting.i18n.AppStrings

@Composable
fun HomeMenuScreen(
    onOpenMonitor: () -> Unit,
    onOpenForm: () -> Unit,
    onOpenStorage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MenuHeader()
        MenuButton(AppStrings.MENU_MONITOR, onOpenMonitor)
        MenuButton(AppStrings.MENU_FORM, onOpenForm)
        MenuButton(AppStrings.MENU_STORAGE, onOpenStorage)
    }
}

@Composable
private fun MenuHeader() {
    Text(AppStrings.APP_TITLE, style = MaterialTheme.typography.titleLarge)
    Text(AppStrings.MENU_TITLE, style = MaterialTheme.typography.titleMedium)
}

@Composable
private fun MenuButton(label: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(label)
    }
}

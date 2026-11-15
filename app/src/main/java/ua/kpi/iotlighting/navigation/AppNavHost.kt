package ua.kpi.iotlighting.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.kpi.iotlighting.data.AppDatabase
import ua.kpi.iotlighting.data.LightingRepository
import ua.kpi.iotlighting.ui.form.DeviceFormScreen
import ua.kpi.iotlighting.ui.menu.HomeMenuScreen
import ua.kpi.iotlighting.ui.monitor.MonitorScreen
import ua.kpi.iotlighting.ui.storage.StorageScreen

@Composable
fun AppNavHost() {
    val context = LocalContext.current
    var repository by remember { mutableStateOf<LightingRepository?>(null) }
    LaunchedEffect(Unit) {
        val appContext = context.applicationContext
        repository = withContext(Dispatchers.IO) {
            LightingRepository(AppDatabase.get(appContext).lightingDao())
        }
    }
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Home.route) {
        composable(Routes.Home.route) {
            HomeMenuScreen(
                onOpenMonitor = { navController.navigate(Routes.Monitor.route) },
                onOpenForm = { navController.navigate(Routes.Form.route) },
                onOpenStorage = { navController.navigate(Routes.Storage.route) }
            )
        }
        composable(Routes.Monitor.route) {
            val repo = repository
            if (repo == null) {
                LoadingScreen()
            } else {
                MonitorScreen(repository = repo, onBack = { navController.popBackStack() })
            }
        }
        composable(Routes.Form.route) {
            val repo = repository
            if (repo == null) {
                LoadingScreen()
            } else {
                DeviceFormScreen(repository = repo, onBack = { navController.popBackStack() })
            }
        }
        composable(Routes.Storage.route) {
            val repo = repository
            if (repo == null) {
                LoadingScreen()
            } else {
                StorageScreen(repository = repo, onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text("Loading...")
    }
}

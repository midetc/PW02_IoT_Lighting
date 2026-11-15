package ua.kpi.iotlighting.navigation

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Form : Routes("form")
    data object Monitor : Routes("monitor")
    data object Storage : Routes("storage")
}

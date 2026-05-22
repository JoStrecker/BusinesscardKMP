package de.jstrecker.businesscard

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.jstrecker.businesscard.stores.PreferencesStore
import de.jstrecker.businesscard.stores.createDataStore
import kotlinx.coroutines.launch

sealed class Screen(
    val route: String,
    val icon: ImageVector,
) {
    data object Card : Screen("card", Icons.Rounded.Info)
    data object Settings : Screen("settings", Icons.Rounded.Settings)
}

@Composable
fun App() {
    val scope = rememberCoroutineScope()
    val platformActions = getPlatformActions()

    // DataStore
    val dataStore = remember { createDataStore("business_card_settings") }

    // Stores
    val nameStore = remember { PreferencesStore(dataStore, "name", "Name") }
    val titleStore = remember { PreferencesStore(dataStore, "title", "") }
    val phoneStore = remember { PreferencesStore(dataStore, "phone", "") }
    val mailStore = remember { PreferencesStore(dataStore, "mail", "") }
    val gitStore = remember { PreferencesStore(dataStore, "git", "") }
    val linkedinStore = remember { PreferencesStore(dataStore, "linkedin", "") }
    val imageStore = remember { PreferencesStore(dataStore, "image", "") }

    // States
    val name by nameStore.getAccessToken.collectAsState(initial = "Name")
    val title by titleStore.getAccessToken.collectAsState(initial = "")
    val phone by phoneStore.getAccessToken.collectAsState(initial = "")
    val mail by mailStore.getAccessToken.collectAsState(initial = "")
    val github by gitStore.getAccessToken.collectAsState(initial = "")
    val linkedin by linkedinStore.getAccessToken.collectAsState(initial = "")
    val imageUri by imageStore.getAccessToken.collectAsState(initial = "")

    MaterialTheme {
        val navController = rememberNavController()
        val items = listOf(Screen.Card, Screen.Settings)

        Scaffold(
            modifier = Modifier.windowInsetsPadding(WindowInsets.ime),
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.route) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().route!!) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Card.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(Screen.Card.route) {
                    BusinessCard(
                        name = name,
                        title = title,
                        phone = phone,
                        mail = mail,
                        github = github,
                        linkedin = linkedin,
                        imageUri = imageUri,
                        platformActions = platformActions
                    )
                }
                composable(Screen.Settings.route) {
                    Settings(
                        name = name,
                        title = title,
                        phone = phone,
                        mail = mail,
                        github = github,
                        linkedin = linkedin,
                        imageUri = imageUri,
                        onSave = { n, t, p, m, g, l ->
                            scope.launch {
                                nameStore.saveToken(n)
                                titleStore.saveToken(t)
                                phoneStore.saveToken(p)
                                mailStore.saveToken(m)
                                gitStore.saveToken(g)
                                linkedinStore.saveToken(l)
                            }
                        },
                        onImagePicked = { uri ->
                            scope.launch {
                                imageStore.saveToken(uri)
                            }
                        },
                        platformActions = platformActions
                    )
                }
            }
        }
    }
}

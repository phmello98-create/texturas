package com.texturas.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.texturas.R
import com.texturas.data.datastore.DataStoreUtil
import com.texturas.ui.moodjournal.JournalScreen
import com.texturas.ui.onboarding.OnboardingScreen
import com.texturas.ui.repertoire.RepertoireScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModelScope

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TexturasTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val context = LocalContext.current
    var showOnboarding by remember { mutableStateOf(false) }

    // Check if onboarding has been seen
    LaunchedEffect(Unit) {
        // Default to showing onboarding
        showOnboarding = true
        
        // Check in background
        viewModelScope.launch {
            val hasSeen = DataStoreUtil.hasSeenOnboarding(context)
            showOnboarding = !hasSeen
        }
    }

    if (showOnboarding) {
        OnboardingScreen(
            onFinished = {
                // Mark onboarding as seen and switch to main screen
                showOnboarding = false
                lifecycleScope.launchWhenStarted {
                    DataStoreUtil.setHasSeenOnboarding(context, true)
                }
            }
        )
    } else {
        MainApp()
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    
    // We'll use a Scaffold with BottomNavigation for main app screens
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val currentDestination = navController.currentBackStackEntryAsState()
                val currentRoute = currentDestination.value?.destination?.route

                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Início") },
                    selected = currentRoute == "home",
                    onClick = {
                        navController.navigate("home")
                    }
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Default.ListAlt, contentDescription = null) },
                    label = { Text("Repertório") },
                    selected = currentRoute == "repertoire",
                    onClick = {
                        navController.navigate("repertoire")
                    }
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Default.NoteAlt, contentDescription = null) },
                    label = { Text("Diário") },
                    selected = currentRoute == "mood_journal",
                    onClick = {
                        navController.navigate("mood_journal")
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") {
                MainScreen { action ->
                    // Handle the action from the crisis button
                    // For now, we'll just log it
                }
            }
            composable("repertoire") {
                RepertoireScreen(
                    onAddActivity = { /* Handle if needed */ }
                )
            }
            composable("mood_journal") {
                JournalScreen(
                    onAddTextureEntry = { description ->
                        // This lambda is called when user adds a texture entry from the dialog
                        // JournalScreen handles saving internally
                    }
                )
            }
        }
    }
}

    package com.ravit.alertatemprana

    import android.Manifest
    import android.content.pm.PackageManager
    import android.os.Bundle
    import android.widget.Toast
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Surface
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.ui.Modifier
    import androidx.core.content.ContextCompat
    import androidx.lifecycle.ViewModelProvider
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import com.ravit.alertatemprana.ui.navigation.NavigationEvent
    import com.ravit.alertatemprana.ui.theme.AlertaTempranaTheme
    import com.ravit.alertatemprana.ui.view.chat.ChatView
    import com.ravit.alertatemprana.ui.view.chat.ChatViewModel
    import com.ravit.alertatemprana.ui.view.home.HomeView
    import com.ravit.alertatemprana.ui.view.home.HomeViewModel
    import kotlinx.coroutines.launch

    class MainActivity : ComponentActivity(), HomeViewModel.LocationPermissionRequester {
        private lateinit var homeViewModel: HomeViewModel
        private lateinit var chatViewModel: ChatViewModel

        private val locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                homeViewModel.startLocationUpdates()
            } else {
                showLocationDisabledMessage()
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
            chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
            homeViewModel.locationPermissionRequester = this

            setContent {
                AlertaTempranaTheme {
                    val navController = rememberNavController()

                    LaunchedEffect(homeViewModel, chatViewModel) {
                        launch {
                            homeViewModel.navigationEvent.collect { event ->
                                if (event is NavigationEvent.NavigateToChat) {
                                    navController.navigate("chatView")
                                }
                            }
                        }
                        launch {
                            chatViewModel.navigationEvent.collect { event ->
                                if (event is NavigationEvent.GOBackToStop) {
                                    navController.popBackStack()
                                    homeViewModel.stopLocationUpdates()
                                }
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(navController = navController, startDestination = "homeView") {
                            composable("homeView") { HomeView(viewModel = homeViewModel) }
                            composable("chatView") { ChatView(viewModel = chatViewModel) }
                        }
                    }
                }
            }
            checkLocationPermission()
        }

        private fun checkLocationPermission() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        override fun requestLocationPermissions() {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        override fun showLocationDisabledMessage() {
            Toast.makeText(this, "Necesitas activar la ubicación desde ajustes", Toast.LENGTH_LONG).show()
        }
    }

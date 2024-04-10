    package com.ravit.alertatemprana

    import android.Manifest
    import android.annotation.SuppressLint
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
    import androidx.compose.runtime.collectAsState
    import androidx.compose.ui.Modifier
    import androidx.core.content.ContextCompat
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.ViewModelProvider
    import androidx.lifecycle.viewmodel.compose.viewModel
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

        private val locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                homeViewModel.startLocationUpdates()
            } else {
                showLocationDisabledMessage()
            }
        }

        @SuppressLint("MissingSuperCall")
        override fun onBackPressed() { }

        @SuppressLint("StateFlowValueCalledInComposition")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
            homeViewModel.locationPermissionRequester = this

            setContent {
                AlertaTempranaTheme {
                    val navController = rememberNavController()

                    LaunchedEffect(homeViewModel) {
                        if (navController.currentBackStackEntry?.destination?.route == "homeView") {
                            launch {
                                homeViewModel.navigationEvent.collect { event ->
                                    if (event is NavigationEvent.NavigateToChat) {
                                        val roomId = homeViewModel.room_id.value
                                        val chatViewModel = ViewModelProvider(this@MainActivity, ChatViewModelFactory(roomId))[ChatViewModel::class.java]
                                        navController.navigate("chatView")
                                    }
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
                            composable("chatView") {
                                val room_id = homeViewModel.room_id.value
                                val chatViewModel = viewModel<ChatViewModel>(factory = ChatViewModelFactory(room_id))
                                val navigationEventState = chatViewModel.navigationEvent.collectAsState(initial = null)
                                LaunchedEffect(navigationEventState.value) {
                                    val event = navigationEventState.value
                                    if (event is NavigationEvent.GOBackToStop) {
                                        navController.popBackStack()
                                        homeViewModel.stopLocationUpdates()
                                    }
                                }
                                ChatView(viewModel = chatViewModel)
                            }
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
    class ChatViewModelFactory(private val room_id: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
                return ChatViewModel(room_id) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

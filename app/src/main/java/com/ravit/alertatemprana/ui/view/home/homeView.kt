package com.ravit.alertatemprana.ui.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravit.alertatemprana.R

@Composable
fun HomeView(viewModel: HomeViewModel) {
    val location = viewModel.location
    val isTracking = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Alerta Temprana",
            modifier = Modifier
                .padding(vertical = 32.dp),
            color = Color.Gray,
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = "Comparte tu ubicación",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        )
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Image description",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .weight(1f),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "location \n${location?.latitude}, ${location?.longitude} ",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )
        Button(
            onClick = {
                if (isTracking.value) {
                    viewModel.stopLocationUpdates()
                } else {
                    viewModel.startLocationUpdates()
                }
                isTracking.value = !isTracking.value
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isTracking.value) "Detener Localización" else "Iniciar Localización")
        }
    }
}

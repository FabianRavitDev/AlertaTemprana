package com.ravit.alertatemprana.ui.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Mensaje") },
            text = { Text("¿Deseas enviar un alerta con tu ubicación?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.startLocationUpdates()
                        showDialog.value = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Generar Alerta",
                textAlign = TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color(0xFFA62520)
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Image description",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Al enviar la alerta compartirás tu ubicación",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = {
                    showDialog.value = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA62520))
            ) {
                Text("Enviar Alerta")
            }
        }
    }
}

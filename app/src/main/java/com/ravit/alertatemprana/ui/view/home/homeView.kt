package com.ravit.alertatemprana.ui.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ravit.alertatemprana.R

@Composable
fun HomeView(viewModel: HomeViewModel) {
    val count = viewModel.count

    val location = viewModel.location

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
            textAlign = TextAlign.Center
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
            text = "location ${count} \n${location?.latitude}, ${location?.longitude} ",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )
        Button(
            onClick = { viewModel.incrementCount() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.End)
        ) {
            Text("Increment")
        }
    }
}

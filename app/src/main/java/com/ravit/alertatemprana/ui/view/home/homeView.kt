package com.ravit.alertatemprana.ui.view.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravit.alertatemprana.R
import com.ravit.alertatemprana.ui.theme.GrayPrimary
import com.ravit.alertatemprana.ui.theme.GreenPrimary
import com.ravit.alertatemprana.ui.theme.RedPrimary
import com.ravit.alertatemprana.ui.theme.YellowPrimary

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeView(viewModel: HomeViewModel) {
    val showDialog by viewModel.showDialog.collectAsState()
    val error by viewModel.error.collectAsState()
    val MonserratFontFamily = FontFamily(
        Font(R.font.montserrat, FontWeight.Normal),
        Font(R.font.montserrat_bold, FontWeight.Bold),
        Font(R.font.montserrat_semi_bold, FontWeight.SemiBold),
        Font(R.font.montserrat_italic, FontWeight.Normal)
    )

    if (error) {
            AlertDialog(
                onDismissRequest = { viewModel.toggleError(false) },
                title = { Text(text = "Error",
                    style = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = MonserratFontFamily,
                        fontSize = 24.sp,
                        color = RedPrimary
                    )
                ) },
                text = { Text(viewModel.messageError.value) },
                confirmButton = {
                    Button(
                        onClick = { viewModel.toggleError(false) }
                    ) {
                        Text(text = "OK",
                            style = androidx.compose.ui.text.TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = MonserratFontFamily,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        )
                    }
                }
            )
        }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.toggleDialog(false) },
            title = { Text(text = "Mensaje" ,
                style = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontFamily = MonserratFontFamily,
                    fontSize = 24.sp,
                    color = GreenPrimary
                )
            ) },
            text = { Text(text = "¿Deseas enviar una alerta con tu ubicación?",
                style = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = MonserratFontFamily,
                    fontSize = 24.sp,
                    color = Color.Black
                )
            ) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.login()
                        viewModel.toggleDialog(false)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Enviar",
                        style = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = MonserratFontFamily,
                        fontSize = 14.sp,
                        color = GreenPrimary
                        )
                    )
                }
            },
            dismissButton = {
                Button(onClick = { viewModel.toggleDialog(false) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GrayPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Cancelar",
                        style = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = MonserratFontFamily,
                        fontSize = 14.sp,
                        color = Color.White
                        )
                    )
                }
            }
        )
    }

    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
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
                    text = "Sistema Integrado de Emergencias y Convivencia Ciudadana.",
                    textAlign = TextAlign.Center,
                    style = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = MonserratFontFamily,
                        fontSize = 24.sp,
                        color = GreenPrimary
                    )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logovertical),
                    contentDescription = "Image description",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentScale = ContentScale.Fit
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
                    style = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = MonserratFontFamily,
                        fontSize = 12.sp,
                        color = GrayPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = {
                        viewModel.toggleDialog(true)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary)
                ) {
                    Text(text = "Enviar Alerta",
                        style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = MonserratFontFamily,
                            fontSize = 14.sp,
                            color = GreenPrimary
                        )
                    )
                }
            }
            Column (modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Versión Beta 1.0",
                    style = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = MonserratFontFamily,
                        fontSize = 12.sp,
                        color = GrayPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        if (isLoading) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)))

            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp),
                color = YellowPrimary
            )
        }
    }
}

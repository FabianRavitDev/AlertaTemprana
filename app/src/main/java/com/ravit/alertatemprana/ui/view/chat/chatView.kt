package com.ravit.alertatemprana.ui.view.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravit.alertatemprana.R
import com.ravit.alertatemprana.network.WebSocket.WebSocketRoomChannel
import com.ravit.alertatemprana.ui.model.String.Messages
import com.ravit.alertatemprana.ui.theme.GrayPrimary
import com.ravit.alertatemprana.ui.theme.GreenPrimary
import com.ravit.alertatemprana.ui.theme.YellowPrimary

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatView(viewModel: ChatViewModel) {
    val textState by viewModel.textState.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val error by viewModel.isStopLocation.collectAsState()
    val nolocation by viewModel.noLocation.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val MonserratFontFamily = FontFamily(
        Font(R.font.montserrat, FontWeight.Normal),
        Font(R.font.montserrat_bold, FontWeight.Bold),
        Font(R.font.montserrat_semi_bold, FontWeight.SemiBold),
        Font(R.font.montserrat_italic, FontWeight.Normal)
    )

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = Messages.TITLE_ALERT, style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontFamily = MonserratFontFamily,
                fontSize = 24.sp,
                color = GreenPrimary
            )) },
            text = { Text(text = Messages.MENSAJE_END_ALERT,
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = MonserratFontFamily,
                    fontSize = 20.sp,
                    color = Color.Black
                )) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.goBack()
                        showDialog.value = false
                        WebSocketRoomChannel.disconnect()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Si", style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = MonserratFontFamily,
                        fontSize = 14.sp,
                        color = GreenPrimary
                    ))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false },colors = ButtonDefaults.buttonColors(
                    containerColor = GrayPrimary,
                    contentColor = Color.White
                )) {
                    Text(text = "No", style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = MonserratFontFamily,
                        fontSize = 14.sp,
                        color = Color.White
                    ))
                }
            }
        )
    }

    if (error) {
        AlertDialog(
            onDismissRequest = { viewModel.goBack() },
            title = { Text(text = Messages.TITLE_ALERT,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontFamily = MonserratFontFamily,
                                fontSize = 24.sp,
                                color = GreenPrimary
                            )
                        )
                    },
            text = { Text(text = Messages.MENSAJE_STOP_ALERT,
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = MonserratFontFamily,
                    fontSize = 20.sp,
                    color = Color.Black
                )) },
            confirmButton = {
                Button(onClick = { viewModel.goBack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowPrimary,
                        contentColor = Color.White
                    )) {
                    Text(text = "OK",
                        style = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = MonserratFontFamily,
                        fontSize = 14.sp,
                        color = GreenPrimary
                    ))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Row (modifier = Modifier.padding(vertical = 10.dp)) {
                            Text(
                                text = Messages.SENT_ALERT,
                                color = Color.White,
                                style = TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = MonserratFontFamily,
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = { showDialog.value = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = YellowPrimary
                            )
                        ) {
                            Text(
                                text = Messages.STOP_ALERT,
                                color = YellowPrimary,
                                style = androidx.compose.ui.text.TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = MonserratFontFamily,
                                    fontSize = 14.sp,
                                    color = YellowPrimary
                                )
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = GreenPrimary
                )
            )
        },
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ) {
            if (nolocation) {
                Snackbar(
                    modifier = Modifier.padding(8.dp).alpha(0.5f),
                    containerColor = YellowPrimary,
                    action = {
                        IconButton(onClick = { viewModel.closeNoLocation() }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar",
                                tint = Color.Black )
                        }
                    }
                ) {
                    Text(
                        text = Messages.NO_SHARED_LOCATION,
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = MonserratFontFamily,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    )
                }
            }
            LazyColumn(modifier = Modifier
                .weight(1f),
                reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .then(
                                if (message.user_id == viewModel.userID) {
                                    Modifier.padding(start = 64.dp)
                                } else {
                                    Modifier.padding(end = 64.dp)
                                }
                            ),
                        contentAlignment = if (message.user_id == viewModel.userID) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        message.body?.let {
                            Text(
                                text = it,
                                color = Color.Black,
                                style = TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = MonserratFontFamily,
                                    fontSize = 16.sp,
                                ),
                                modifier = Modifier
                                    .background(
                                        color = if (message.user_id == viewModel.userID) GreenPrimary.copy(
                                            alpha = 0.25f
                                        ) else Color.LightGray.copy(alpha = 0.25f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textState.text,
                    onValueChange = { newText -> viewModel.updateTextState(newText) },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, shape = RoundedCornerShape(50.dp)),
                    placeholder = { Text(text = "Mensaje",
                        style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = MonserratFontFamily,
                            fontSize = 16.sp,
                        )) },
                    shape = RoundedCornerShape(50.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = GreenPrimary.copy(alpha = 0.75f),
                        unfocusedBorderColor = GrayPrimary.copy(alpha = 0.75f)
                    )
                )
                IconButton(
                    onClick = {
                        if (textState.text.isNotEmpty()) {
                            viewModel.onMessageSend()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 8.dp)
                ) {
                    Icon(Icons.Filled.Send, "Send", tint = GreenPrimary)
                }
            }
        }
    }
}

package com.ravit.alertatemprana.ui.view.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravit.alertatemprana.ui.model.SenderType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatView(viewModel: ChatViewModel) {
    val textState by viewModel.textState.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    viewModel.fetchMessage()

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Alerta") },
            text = { Text("¿Deseas dejar de compartir tu ubicación?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.goBack()
                        showDialog.value = false
                    }
                ) {
                    Text("Si")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("No")
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

                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = "Alerta",
                                tint = Color(0xFFF3D014)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Alerta enviada",
                                color = Color.Black,
                                style = androidx.compose.ui.text.TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = { showDialog.value = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color(0xFFA62520)
                            )
                        ) {
                            Text(
                                text = "Detener Alerta",
                                color = Color(0xFFA62520)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .background(Color(0xFFADD8E6))
        ) {
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
                                if (message.sender == SenderType.SEND) {
                                    Modifier.padding(start = 64.dp)
                                } else {
                                    Modifier.padding(end = 64.dp)
                                }
                            ),
                        contentAlignment = if (message.sender == SenderType.SEND) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Text(
                            text = message.message,
                            color = Color.Black, //  Color.White
                            modifier = Modifier
                                .background(
                                    color = if (message.sender == SenderType.SEND) Color(0xFFDCF8C6) else Color.White, // Color.White
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
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
                    placeholder = { Text("Mensaje") },
                    shape = RoundedCornerShape(50.dp)
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
                    Icon(Icons.Filled.Send, "Send", tint = Color(0xFF296588))
                }
            }
        }
    }
}
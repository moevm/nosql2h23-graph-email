package com.example.mail_list.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MailListScreen(
    viewModel: MailListViewModel
) {
    var searchText by remember { mutableStateOf("") }

    val mailsListCards by viewModel.mailListCards.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mail List",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                backgroundColor = Color.White,
                contentColor = Color.Black,
                navigationIcon = {
                    TextButton(
                        onClick = {

                        }
                    ) {
                        Text(text = "Export", color = Color(0xFF5DB075))
                    }
                },
                actions = {
                    TextButton(
                        onClick = {

                        }
                    ) {
                        Text(text = "Logout", color = Color(0xFF5DB075))
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .height(70.dp),
                    label = { Text("Search", color = Color.Gray) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF5DB075),
                        unfocusedBorderColor = Color(0xFFE8E8E8),
                        cursorColor = MaterialTheme.colorScheme.primary,
                        backgroundColor = Color(0xFFF6F6F6)
                    ),
                    shape = MaterialTheme.shapes.extraLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    userScrollEnabled = true
                ) {
                    mailsListCards?.let {
                        items(it.size) { mail ->
                            MailCard(it[mail], {})
                        }
                    }
                }
            }
            FloatingActionButton(
                onClick = { /* Действие по нажатию кнопки */ },
                modifier = Modifier
                    .wrapContentSize(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(56.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
    /*
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            loadUrl("http://192.168.1.216:5000/api/graph/")
            settings.javaScriptEnabled = true
        }
    }, update = {
        it.loadUrl("http://192.168.1.216:5000/api/graph/")
    })*/
}
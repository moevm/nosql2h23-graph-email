package com.example.mail_list.presentation.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mail_list.presentation.R

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MailListScreen(
    viewModel: MailListViewModel,
    navigateToFilter: () -> Unit,
    navigateToLogin: () -> Unit,
    startDate: String?,
    endDate: String?,
    sender: String?,
    receiver: String?,
    subject: String?
) {
    var searchText by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    val mailsListCards by viewModel.mailListCards.collectAsState()

    LaunchedEffect(Unit) {
        if (startDate.isNullOrEmpty() && endDate.isNullOrEmpty() && sender.isNullOrEmpty() && receiver.isNullOrEmpty() && subject.isNullOrEmpty()) {
            Log.d("PLOP", "getMails")
            viewModel.getMails()
        } else {
            Log.d("PLOP", "$startDate $endDate $sender $receiver $subject")
            viewModel.getMailsWithFilter(
                startDate = startDate,
                endDate = endDate,
                sender = sender,
                receiver = receiver,
                subject = subject
            )
        }
    }

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
                            navigateToLogin()
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
                    label = { Text("Search by title", color = Color.Gray) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,

                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.getMailsWithFilter(
                                startDate = null,
                                endDate = null,
                                sender = null,
                                receiver = null,
                                subject = searchText
                            )
                            keyboardController?.hide()
                        }
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF5DB075),
                        unfocusedBorderColor = Color(0xFFE8E8E8),
                        cursorColor = MaterialTheme.colorScheme.primary,
                        backgroundColor = Color(0xFFF6F6F6)
                    ),
                    shape = MaterialTheme.shapes.extraLarge,
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
            Box(modifier = Modifier.fillMaxSize()) {
                FloatingActionButton(
                    onClick = { navigateToFilter() },
                    modifier = Modifier
                        .align(alignment = Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(47.dp),
                    shape = CircleShape,
                    containerColor = Color(0xFFD9D9D9),
                ) {
                    Icon(
                        painterResource(id = R.drawable.filter),
                        modifier = Modifier
                            .size(24.dp)
                            .align(alignment = Alignment.Center),
                        //imageVector = Icons.Rounded.Menu,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
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
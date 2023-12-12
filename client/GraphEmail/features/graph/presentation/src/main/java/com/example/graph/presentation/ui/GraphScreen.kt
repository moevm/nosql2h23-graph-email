package com.example.graph.presentation.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.viewinterop.AndroidView
import com.example.graph.presentation.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GraphScreen(
    viewModel: GraphViewModel,
    startDate: String?,
    endDate: String?,
    sender: String?,
    receiver: String?,
    subject: String?,
    navigateToLogin: () -> Unit,
    navigateToFilter: () -> Unit,
    navigateToMailList: (
        startDate: String, endDate: String, sender: String, receiver: String, subject: String
    ) -> Unit,
) {
    var searchText by remember { mutableStateOf("") }
    var isGraphShown by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val url = viewModel.graphEntity.collectAsState()
    LaunchedEffect(key1 = Unit) {
        isGraphShown = true
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Graph",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                style = MaterialTheme.typography.titleLarge,
            )
        }, backgroundColor = Color.White, contentColor = Color.Black, navigationIcon = {
            TextButton(onClick = {

            }) {
                Text(text = "Export", color = Color(0xFF5DB075))
            }
        }, actions = {
            TextButton(onClick = {
                navigateToLogin()
            }) {
                Text(text = "Logout", color = Color(0xFF5DB075))
            }
        })
    }, content = { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(70.dp),
                label = { Text("Search by subject", color = Color.Gray) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboardController?.hide()
                }),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF5DB075),
                    unfocusedBorderColor = Color(0xFFE8E8E8),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = Color(0xFFF6F6F6)
                ),
                shape = MaterialTheme.shapes.extraLarge,
            )

            if (isGraphShown) {
                ShowGraph(
                    url = viewModel.buildGraphUrl("http://192.168.1.216:5000/api/graph?",
                        startDate = startDate,
                        endDate = endDate,
                        sender = sender,
                        receiver = receiver,
                        subject = searchText.ifEmpty { subject })
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = { navigateToFilter() },
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .padding(horizontal = 16.dp, vertical = 68.dp)
                    .size(47.dp),
                shape = CircleShape,
                containerColor = Color(0xFFD9D9D9),
            ) {
                Icon(
                    painterResource(id = R.drawable.filter),
                    modifier = Modifier
                        .size(24.dp)
                        .align(alignment = Alignment.Center),
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    },
        bottomBar = {
            BottomNavigationBar(
                navigate = { screen ->
                    when (screen) {
                        BottomNavScreens.GRAPH -> {

                        }

                        BottomNavScreens.MAIL_LIST -> {
                            navigateToMailList(
                                startDate ?: "",
                                endDate ?: "",
                                sender ?: "",
                                receiver ?: "",
                                searchText.ifEmpty { subject ?: "" }
                            )
                        }

                        BottomNavScreens.SETTINGS -> {

                        }
                    }
                }
            )
        }
    )
}

@Composable
fun BottomNavigationBar(
    navigate: (screen: BottomNavScreens) -> Unit
) {
    val items = listOf(
        BottomNavigationItem(
            screen = BottomNavScreens.GRAPH,
            icon = Icons.Default.Share,
            selected = true
        ),
        BottomNavigationItem(
            screen = BottomNavScreens.MAIL_LIST,
            icon = Icons.Default.List,
            selected = false
        ),
        BottomNavigationItem(
            screen = BottomNavScreens.SETTINGS,
            icon = Icons.Default.Settings,
            selected = false
        ),
    )
    BottomNavigation(
        modifier = Modifier, backgroundColor = Color(0xFF5DB075)
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.screen.title) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Black,
                selected = item.selected,
                onClick = {
                    navigate(item.screen)
                },
                label = { Text(text = item.screen.title) },
                modifier = Modifier,
            )
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ShowGraph(
    url: String,
) {
    Log.d("PLOP", "URL = $url")
    AndroidView(modifier = Modifier.fillMaxWidth(), factory = {
        WebView(it).apply {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(url)
        }
    }, update = {
        it.loadUrl(url)
    })
}
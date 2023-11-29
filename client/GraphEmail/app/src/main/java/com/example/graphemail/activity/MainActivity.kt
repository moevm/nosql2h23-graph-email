package com.example.graphemail.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.graphemail.Navigation
import com.example.graphemail.ui.theme.GraphEmailTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GraphEmailTheme {
                // A surface container using the 'background' color from the theme
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .navigationBarsPadding()
                ){
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun GraphApp() {
    Navigation()
}

@Preview(showBackground = true)
@Composable
fun PreviewGraphApp() {
    GraphApp()
}
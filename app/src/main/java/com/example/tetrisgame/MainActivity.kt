package com.example.tetrisgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.tetrisgame.ui.theme.TetrisGameTheme
import com.example.tetrisgame.ui.MainMenu
import com.example.tetrisgame.ui.TetrisGame

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TetrisGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TetrisApp()
                }
            }
        }
    }
}

@Composable
fun TetrisApp() {
    var currentScreen by remember { mutableStateOf("menu") }

    when (currentScreen) {
        "menu" -> MainMenu(
            onStartGame = { currentScreen = "game" }
        )

        "game" -> TetrisGame(
            onBackToMenu = { currentScreen = "menu" }
        )
    }
}
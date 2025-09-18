package com.example.tetrisgame.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tetrisgame.audio.SoundManager
import com.example.tetrisgame.game.TetrisEngine
import com.example.tetrisgame.game.TetrisGameState
import kotlinx.coroutines.delay

@Composable
fun TetrisGame(onBackToMenu: () -> Unit) {
    var gameState by remember { mutableStateOf(TetrisGameState()) }
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }
    val engine = remember { TetrisEngine(soundManager) }

    LaunchedEffect(Unit) {
        gameState = engine.spawnNewPiece(gameState)
    }

    // Cleanup sound manager when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            soundManager.release()
        }
    }

    LaunchedEffect(gameState.isPaused, gameState.isGameOver) {
        while (!gameState.isPaused && !gameState.isGameOver && gameState.currentPiece != null) {
            delay(gameState.calculateDropSpeed())
            gameState = engine.movePieceDown(gameState)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                ScorePanel(
                    gameState = gameState,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                NextPiecePreview(
                    nextPiece = gameState.nextPiece,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TetrisBoard(
                gameState = gameState,
                modifier = Modifier.wrapContentSize()
            )

            Spacer(modifier = Modifier.height(16.dp))

            GameControls(
                onMoveLeft = {
                    if (!gameState.isPaused) {
                        gameState = engine.movePieceLeft(gameState)
                    }
                },
                onMoveRight = {
                    if (!gameState.isPaused) {
                        gameState = engine.movePieceRight(gameState)
                    }
                },
                onMoveDown = {
                    if (!gameState.isPaused) {
                        gameState = engine.movePieceDown(gameState)
                    }
                },
                onRotate = {
                    if (!gameState.isPaused) {
                        gameState = engine.rotatePiece(gameState)
                    }
                },
                onHardDrop = {
                    if (!gameState.isPaused) {
                        gameState = engine.hardDrop(gameState)
                    }
                },
                onPause = {
                    gameState = engine.togglePause(gameState)
                },
                onToggleSound = {
                    soundManager.toggleSound()
                },
                isPaused = gameState.isPaused,
                isSoundOn = soundManager.isSoundOn()
            )
        }

        GameOverDialog(
            gameState = gameState,
            onRestart = {
                gameState = engine.resetGame()
                gameState = engine.spawnNewPiece(gameState)
            },
            onBackToMenu = onBackToMenu
        )

        if (gameState.isPaused && !gameState.isGameOver) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.8f)
                    )
                ) {
                    Text(
                        text = "PAUSED",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
        }
    }
}



package com.example.tetrisgame.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TetrisGame(onBackToMenu: () -> Unit) {
    var blockX by remember { mutableIntStateOf(4) }
    var blockY by remember { mutableIntStateOf(0) }
    var isGameRunning by remember { mutableStateOf(true) }
    val dropSpeed = 1000L
    // Game logic
    LaunchedEffect(isGameRunning) {
        while (isGameRunning) {
            delay(dropSpeed)
            if (blockY < 19) {
                blockY += 1
            } else {
                blockY = 0
                blockX = 4
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Animated background
        AnimatedBackground(
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(
                modifier = Modifier
                    .size(300.dp, 600.dp)
            ) {
                drawGameBoard(blockX, blockY)
            }

            CanvasControls(
                onMoveLeft = {
                    if (blockX > 0) blockX -= 1
                },
                onMoveRight = {
                    if (blockX < 9) blockX += 1
                },
                onMoveDown = {
                    if (blockY < 19) blockY += 1
                },
                onPause = {
                    isGameRunning = !isGameRunning
                }
            )

            Button(
                onClick = onBackToMenu,
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Back to Menu")
            }
        }
    }
}

private fun DrawScope.drawGameBoard(blockX: Int, blockY: Int) {
    val cellSize = 30.dp.toPx()
    val boardWidth = cellSize * 10
    val boardHeight = cellSize * 20

    drawRect(
        color = Color(0xFF1A1A1A),
        topLeft = Offset.Zero,
        size = Size(boardWidth, boardHeight)
    )

    for (i in 0..10) {
        drawLine(
            color = Color.Gray,
            start = Offset(i * cellSize, 0f),
            end = Offset(i * cellSize, boardHeight),
            strokeWidth = 1.dp.toPx()
        )
    }

    for (i in 0..20) {
        drawLine(
            color = Color.Gray,
            start = Offset(0f, i * cellSize),
            end = Offset(boardWidth, i * cellSize),
            strokeWidth = 1.dp.toPx()
        )
    }

    drawRect(
        color = Color.Cyan,
        topLeft = Offset(blockX * cellSize, blockY * cellSize),
        size = Size(cellSize, cellSize)
    )
}

@Composable
private fun CanvasControls(
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onMoveDown: () -> Unit,
    onPause: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CanvasButton(
                onClick = onMoveLeft,
                color = Color.Blue
            )

            CanvasButton(
                onClick = onMoveDown,
                color = Color.Green
            )

            CanvasButton(
                onClick = onMoveRight,
                color = Color.Blue
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

      /*  CanvasButton(
            onClick = onPause,
            color = Color(0xFFFF9800)
        )*/
    }
}

@Composable
private fun CanvasButton(
    onClick: () -> Unit,
    color: Color
) {
    var isPressed by remember { mutableStateOf(false) }

    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier.size(60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) color.copy(alpha = 0.7f) else color
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawRect(
                    color = Color.White.copy(alpha = 0.1f),
                    size = size
                )
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}



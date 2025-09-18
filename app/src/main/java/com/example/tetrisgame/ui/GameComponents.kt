package com.example.tetrisgame.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tetrisgame.game.*

@Composable
fun TetrisBoard(
    gameState: TetrisGameState,
    modifier: Modifier = Modifier
) {
    val cellSize = 28.dp

    Card(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .border(2.dp, Color.Cyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Canvas(
            modifier = Modifier
                .size(cellSize * BOARD_WIDTH, cellSize * BOARD_HEIGHT)
                .padding(4.dp)
        ) {
            drawTetrisBoard(gameState, cellSize.toPx())
        }
    }
}

private fun DrawScope.drawTetrisBoard(gameState: TetrisGameState, cellSize: Float) {
    val boardWidth = cellSize * BOARD_WIDTH
    val boardHeight = cellSize * BOARD_HEIGHT

    drawRect(
        color = Color(0xFF0A0A0A),
        size = Size(boardWidth, boardHeight)
    )

    for (i in 0..BOARD_WIDTH) {
        drawLine(
            color = Color.Gray.copy(alpha = 0.3f),
            start = Offset(i * cellSize, 0f),
            end = Offset(i * cellSize, boardHeight),
            strokeWidth = 1.dp.toPx()
        )
    }

    for (i in 0..BOARD_HEIGHT) {
        drawLine(
            color = Color.Gray.copy(alpha = 0.3f),
            start = Offset(0f, i * cellSize),
            end = Offset(boardWidth, i * cellSize),
            strokeWidth = 1.dp.toPx()
        )
    }

    // Draw placed pieces
    for (row in gameState.board.cells.indices) {
        for (col in gameState.board.cells[row].indices) {
            val cell = gameState.board.cells[row][col]
            if (cell != null) {
                drawCell(
                    color = cell,
                    x = col * cellSize,
                    y = row * cellSize,
                    size = cellSize
                )
            }
        }
    }

    // Draw ghost piece (preview where piece will land)
    val ghostPiece = TetrisEngine().getGhostPiece(gameState)
    ghostPiece?.let { ghost ->
        val shape = ghost.getRotatedShape()
        for (row in shape.indices) {
            for (col in shape[row].indices) {
                if (shape[row][col]) {
                    val x = (ghost.x + col) * cellSize
                    val y = (ghost.y + row) * cellSize
                    drawCell(
                        color = ghost.tetromino.color.copy(alpha = 0.3f),
                        x = x,
                        y = y,
                        size = cellSize,
                        isGhost = true
                    )
                }
            }
        }
    }

    // Draw current piece
    gameState.currentPiece?.let { piece ->
        val shape = piece.getRotatedShape()
        for (row in shape.indices) {
            for (col in shape[row].indices) {
                if (shape[row][col]) {
                    val x = (piece.x + col) * cellSize
                    val y = (piece.y + row) * cellSize
                    drawCell(
                        color = piece.tetromino.color,
                        x = x,
                        y = y,
                        size = cellSize
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawCell(
    color: Color,
    x: Float,
    y: Float,
    size: Float,
    isGhost: Boolean = false
) {
    val margin = size * 0.05f
    val cellSize = size - margin * 2

    if (isGhost) {
        drawRect(
            color = Color.Transparent,
            topLeft = Offset(x + margin, y + margin),
            size = Size(cellSize, cellSize)
        )

        // Ghost border
        val borderWidth = 2.dp.toPx()
        drawRect(
            color = color,
            topLeft = Offset(x + margin, y + margin),
            size = Size(cellSize, borderWidth)
        )
        drawRect(
            color = color,
            topLeft = Offset(x + margin, y + size - margin - borderWidth),
            size = Size(cellSize, borderWidth)
        )
        drawRect(
            color = color,
            topLeft = Offset(x + margin, y + margin),
            size = Size(borderWidth, cellSize)
        )
        drawRect(
            color = color,
            topLeft = Offset(x + size - margin - borderWidth, y + margin),
            size = Size(borderWidth, cellSize)
        )
    } else {
        // Draw solid cell
        drawRect(
            color = color,
            topLeft = Offset(x + margin, y + margin),
            size = Size(cellSize, cellSize)
        )

        // Inner highlight for 3D effect
        val highlight = Color.White.copy(alpha = 0.3f)
        drawRect(
            color = highlight,
            topLeft = Offset(x + margin, y + margin),
            size = Size(cellSize, cellSize * 0.3f)
        )
        drawRect(
            color = highlight,
            topLeft = Offset(x + margin, y + margin),
            size = Size(cellSize * 0.3f, cellSize)
        )
    }
}

@Composable
fun NextPiecePreview(
    nextPiece: Tetromino?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "NEXT",
                color = Color.Cyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                nextPiece?.let { piece ->
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cellSize = 16.dp.toPx()
                        val shape = piece.shape
                        val startX = (size.width - shape[0].size * cellSize) / 2
                        val startY = (size.height - shape.size * cellSize) / 2

                        for (row in shape.indices) {
                            for (col in shape[row].indices) {
                                if (shape[row][col]) {
                                    val x = startX + col * cellSize
                                    val y = startY + row * cellSize
                                    drawCell(
                                        color = piece.color,
                                        x = x,
                                        y = y,
                                        size = cellSize
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScorePanel(
    gameState: TetrisGameState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SCORE",
                color = Color.Cyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = gameState.score.toString(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "LEVEL",
                        color = Color.Cyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = gameState.level.toString(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "LINES",
                        color = Color.Cyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = gameState.lines.toString(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun GameControls(
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onMoveDown: () -> Unit,
    onRotate: () -> Unit,
    onHardDrop: () -> Unit,
    onPause: () -> Unit,
    onToggleSound: () -> Unit,
    isPaused: Boolean,
    isSoundOn: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GameButton(
                onClick = onRotate,
                icon = Icons.Default.Refresh,
                color = Color.Magenta,
                label = "ROTATE"
            )

            GameButton(
                onClick = onHardDrop,
                icon = Icons.Default.KeyboardArrowDown,
                color = Color.Red,
                label = "DROP"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GameButton(
                onClick = onMoveLeft,
                icon = Icons.Default.KeyboardArrowLeft,
                color = Color.Blue,
                label = "LEFT"
            )

            GameButton(
                onClick = onMoveDown,
                icon = Icons.Default.KeyboardArrowDown,
                color = Color.Green,
                label = "DOWN"
            )

            GameButton(
                onClick = onMoveRight,
                icon = Icons.Default.KeyboardArrowRight,
                color = Color.Blue,
                label = "RIGHT"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GameButton(
                onClick = onPause,
                icon = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Clear,
                color = Color(0xFFFF9800),
                label = if (isPaused) "RESUME" else "PAUSE",
                modifier = Modifier.width(120.dp)
            )

            GameButton(
                onClick = onToggleSound,
                icon = if (isSoundOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                color = Color(0xFF9C27B0),
                label = if (isSoundOn) "SOUND" else "MUTE",
                modifier = Modifier.width(120.dp)
            )
        }
    }
}

@Composable
private fun GameButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier
            .height(56.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) color.copy(alpha = 0.7f) else color
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                color = Color.White,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
fun GameOverDialog(
    gameState: TetrisGameState,
    onRestart: () -> Unit,
    onBackToMenu: () -> Unit
) {
    if (gameState.isGameOver) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    text = "GAME OVER",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text("Final Score: ${gameState.score}")
                    Text("Level: ${gameState.level}")
                    Text("Lines: ${gameState.lines}")
                }
            },
            confirmButton = {
                Button(
                    onClick = onRestart,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text("PLAY AGAIN")
                }
            },
            dismissButton = {
                Button(
                    onClick = onBackToMenu,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("MENU")
                }
            }
        )
    }
}
package com.example.tetrisgame.game

import com.example.tetrisgame.audio.SoundManager
import kotlin.random.Random

class TetrisEngine(private val soundManager: SoundManager? = null) {

    fun spawnNewPiece(gameState: TetrisGameState): TetrisGameState {
        val currentPiece = gameState.nextPiece ?: getRandomTetromino()
        val nextPiece = getRandomTetromino()

        val newPiece = GamePiece(
            tetromino = currentPiece,
            x = BOARD_WIDTH / 2 - 1,
            y = 0
        )

        // Check if game is over
        val isGameOver = !gameState.board.isValidPosition(newPiece)

        // Play game over sound if game is over and it wasn't over before
        if (isGameOver && !gameState.isGameOver) {
            soundManager?.playGameOver()
        }

        return gameState.copy(
            currentPiece = if (isGameOver) null else newPiece,
            nextPiece = nextPiece,
            isGameOver = isGameOver
        )
    }

    fun movePieceLeft(gameState: TetrisGameState): TetrisGameState {
        val piece = gameState.currentPiece ?: return gameState
        val newPiece = piece.moveLeft()

        return if (gameState.board.isValidPosition(newPiece)) {
            gameState.copy(currentPiece = newPiece)
        } else {
            gameState
        }
    }

    fun movePieceRight(gameState: TetrisGameState): TetrisGameState {
        val piece = gameState.currentPiece ?: return gameState
        val newPiece = piece.moveRight()

        return if (gameState.board.isValidPosition(newPiece)) {
            gameState.copy(currentPiece = newPiece)
        } else {
            gameState
        }
    }

    fun movePieceDown(gameState: TetrisGameState): TetrisGameState {
        val piece = gameState.currentPiece ?: return gameState
        val newPiece = piece.moveDown()

        return if (gameState.board.isValidPosition(newPiece)) {
            gameState.copy(currentPiece = newPiece)
        } else {
            // Piece can't move down, place it and spawn new one
            placePieceAndContinue(gameState)
        }
    }

    fun rotatePiece(gameState: TetrisGameState): TetrisGameState {
        val piece = gameState.currentPiece ?: return gameState
        val rotatedPiece = piece.rotate()

        // Try basic rotation
        if (gameState.board.isValidPosition(rotatedPiece)) {
            return gameState.copy(currentPiece = rotatedPiece)
        }

        // Try wall kicks (SRS - Super Rotation System)
        val wallKicks = getWallKicks(piece.rotation % 4, (piece.rotation + 1) % 4)
        for (kick in wallKicks) {
            val kickedPiece = rotatedPiece.copy(
                x = rotatedPiece.x + kick.first,
                y = rotatedPiece.y + kick.second
            )
            if (gameState.board.isValidPosition(kickedPiece)) {
                return gameState.copy(currentPiece = kickedPiece)
            }
        }

        return gameState // Rotation not possible
    }

    fun hardDrop(gameState: TetrisGameState): TetrisGameState {
        var piece = gameState.currentPiece ?: return gameState
        var dropDistance = 0

        // Move piece down until it can't move anymore
        while (gameState.board.isValidPosition(piece.moveDown())) {
            piece = piece.moveDown()
            dropDistance++
        }

        val updatedGameState = gameState.copy(currentPiece = piece)
        val finalGameState = placePieceAndContinue(updatedGameState)

        // Add bonus score for hard drop
        val bonusScore = dropDistance * 2
        return finalGameState.copy(score = finalGameState.score + bonusScore)
    }

    private fun placePieceAndContinue(gameState: TetrisGameState): TetrisGameState {
        val piece = gameState.currentPiece ?: return gameState

        // Play block landing sound
        soundManager?.playBlockLand()

        // Place piece on board
        val newBoard = gameState.board.placePiece(piece)

        // Clear completed lines
        val (clearedBoard, linesCleared) = newBoard.clearLines()

        // Play line clear sound if lines were cleared
        if (linesCleared > 0) {
            soundManager?.playLineClear()
        }

        // Calculate score
        val lineScore = calculateLineScore(linesCleared, gameState.level)
        val newScore = gameState.score + lineScore
        val newLines = gameState.lines + linesCleared
        val newLevel = calculateLevel(newLines)

        // Create new state without current piece
        val intermediateState = gameState.copy(
            board = clearedBoard,
            currentPiece = null,
            score = newScore,
            lines = newLines,
            level = newLevel
        )

        // Spawn new piece
        return spawnNewPiece(intermediateState)
    }

    private fun getRandomTetromino(): Tetromino {
        val types = TetrominoType.values()
        return Tetromino.createTetromino(types[Random.nextInt(types.size)])
    }

    private fun calculateLineScore(linesCleared: Int, level: Int): Int {
        return when (linesCleared) {
            1 -> 40 * level      // Single
            2 -> 100 * level     // Double  
            3 -> 300 * level     // Triple
            4 -> 1200 * level    // Tetris
            else -> 0
        }
    }

    private fun calculateLevel(totalLines: Int): Int {
        return (totalLines / 10) + 1
    }

    private fun getWallKicks(fromRotation: Int, toRotation: Int): List<Pair<Int, Int>> {
        // Simplified wall kick system
        return listOf(
            Pair(-1, 0),  // Left
            Pair(1, 0),   // Right
            Pair(0, -1),  // Up
            Pair(-1, -1), // Left-Up
            Pair(1, -1)   // Right-Up
        )
    }

    fun togglePause(gameState: TetrisGameState): TetrisGameState {
        return gameState.copy(isPaused = !gameState.isPaused)
    }

    fun resetGame(): TetrisGameState {
        return TetrisGameState()
    }

    fun getGhostPiece(gameState: TetrisGameState): GamePiece? {
        val piece = gameState.currentPiece ?: return null
        var ghostPiece = piece

        // Move ghost piece down until it would collide
        while (gameState.board.isValidPosition(ghostPiece.moveDown())) {
            ghostPiece = ghostPiece.moveDown()
        }

        return ghostPiece
    }
}
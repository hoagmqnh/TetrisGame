package com.example.tetrisgame.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SoundManager(private val context: Context) {
    private var soundPool: SoundPool? = null
    private var isSoundEnabled by mutableStateOf(true)
    
    // Sound IDs
    private var blockLandSoundId: Int = 0
    private var lineClearSoundId: Int = 0
    private var gameOverSoundId: Int = 0
    
    init {
        initializeSounds()
    }
    
    private fun initializeSounds() {
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .build()
            
        // Load sound effects
        blockLandSoundId = soundPool?.load(context, R.raw.block_land, 1) ?: 0
        lineClearSoundId = soundPool?.load(context, R.raw.line_clear, 1) ?: 0
        gameOverSoundId = soundPool?.load(context, R.raw.game_over, 1) ?: 0
    }
    
    fun playBlockLand() {
        if (isSoundEnabled) {
            soundPool?.play(blockLandSoundId, 0.5f, 0.5f, 1, 0, 1.0f)
        }
    }
    
    fun playLineClear() {
        if (isSoundEnabled) {
            soundPool?.play(lineClearSoundId, 0.7f, 0.7f, 1, 0, 1.0f)
        }
    }
    
    fun playGameOver() {
        if (isSoundEnabled) {
            soundPool?.play(gameOverSoundId, 0.8f, 0.8f, 1, 0, 1.0f)
        }
    }
    
    fun toggleSound() {
        isSoundEnabled = !isSoundEnabled
    }
    
    fun isSoundOn(): Boolean = isSoundEnabled
    
    fun release() {
        soundPool?.release()
        soundPool = null
    }
}

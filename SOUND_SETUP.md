# Sound Setup for Tetris Game

## Overview
The Tetris game now includes sound effects for:
- **Block Landing**: Plays when a tetromino piece lands on the board
- **Line Clear**: Plays when one or more lines are cleared
- **Game Over**: Plays when the game ends

## Sound Files Required
To enable sound effects, you need to add the following audio files to `app/src/main/res/raw/`:

1. **block_land.wav** (or .ogg) - Short, low-pitched sound for block landing
2. **line_clear.wav** (or .ogg) - Satisfying sound for line clearing
3. **game_over.wav** (or .ogg) - Dramatic sound for game over

## Current Placeholder Files
The current implementation uses placeholder text files:
- `block_land.txt`
- `line_clear.txt` 
- `game_over.txt`

## How to Add Real Sound Files
1. Replace the `.txt` files with actual audio files (`.wav` or `.ogg` format)
2. Keep the same filenames: `block_land`, `line_clear`, `game_over`
3. Remove the `.txt` extension from the filenames
4. Build and run the app

## Sound Controls
- **Sound Toggle Button**: Located in the game controls (purple button with speaker icon)
- **Mute/Unmute**: Click the sound button to toggle audio on/off
- **Visual Indicator**: Button shows speaker icon when sound is on, muted icon when off

## Technical Details
- Uses Android's `SoundPool` for efficient sound playback
- Sounds are loaded once when the game starts
- Volume levels are optimized for each sound type
- Sound manager is properly cleaned up when the game ends

## Recommended Sound Characteristics
- **Block Land**: 0.1-0.3 seconds, low frequency (200-400 Hz)
- **Line Clear**: 0.2-0.5 seconds, medium frequency (400-800 Hz), satisfying tone
- **Game Over**: 0.5-2.0 seconds, dramatic, can be higher frequency (600-1200 Hz)

# Tetris Game Demo - Android Canvas

Demo đơn giản về việc sử dụng Canvas API trong Jetpack Compose để tạo game Tetris cơ bản.

## 🎯 Mục tiêu Demo

Triển khai các yêu cầu cơ bản:

- ✅ **Game loop**: Khối tự động rơi xuống mỗi giây
- ✅ **Di chuyển khối**: Điều khiển khối bằng các nút bấm
- ✅ **Canvas rendering**: Vẽ game board và khối bằng Canvas API
- ✅ **Canvas buttons**: Nút điều khiển được vẽ bằng Canvas

## 📱 Tính năng

### Menu Game

- Menu chính với giao diện đẹp
- Nút "Start Game" để vào demo

### Demo Game

- **Canvas Board**: Game board 10x20 được vẽ hoàn toàn bằng Canvas
- **Game Loop**: Khối màu xanh (cyan) tự động rơi xuống
- **Controls**:
  - ← → : Di chuyển trái/phải
  - ↓ : Di chuyển xuống nhanh
  - ⏸ : Tạm dừng/tiếp tục game
- **Canvas Buttons**: Tất cả nút điều khiển đều sử dụng Canvas để vẽ
- **Reset**: Khối tự reset về đầu khi chạm đáy

## 🏗️ Cấu trúc Code

```
app/src/main/java/com/example/tetrisgame/
├── MainActivity.kt          # Activity chính với navigation
├── ui/
│   ├── MainMenu.kt         # Menu game 
│   └── TetrisGame.kt       # Demo game với Canvas
└── ui/theme/               # Theme configuration
```

## 🎨 Canvas Implementation

### Game Board Rendering

```kotlin
// Vẽ grid 10x20 với Canvas
drawRect(color = Color(0xFF1A1A1A), ...) // Background
drawLine(...) // Grid lines  
drawRect(color = Color.Cyan, ...) // Block
```

### Canvas Controls

```kotlin
// Nút điều khiển được vẽ bằng Canvas
Canvas(modifier = Modifier.fillMaxSize()) {
    drawRect(color = Color.White.copy(alpha = 0.1f), size = size)
}
```

### Game Loop

```kotlin
LaunchedEffect(isGameRunning) {
    while (isGameRunning) {
        delay(1000) // Rơi mỗi giây
        blockY += 1 // Di chuyển xuống
    }
}
```

## 🚀 Chạy Demo

```bash
# Build APK
./gradlew assembleDebug

# Install và chạy
./gradlew installDebug
```

## 🎮 Hướng dẫn sử dụng

1. Mở app → nhấn "START GAME"
2. Xem khối xanh tự động rơi xuống
3. Dùng các nút điều khiển:
  - ← → để di chuyển ngang
  - ↓ để rơi nhanh hơn
  - ⏸ để pause/resume
4. Khối sẽ reset về đầu khi chạm đáy

## 💡 Điểm nổi bật

- **Pure Canvas**: Game board được vẽ 100% bằng Canvas API
- **Simple State**: Chỉ sử dụng `blockX`, `blockY` để demo
- **Canvas Buttons**: Nút điều khiển cũng sử dụng Canvas rendering
- **Game Loop**: Coroutines đơn giản cho tự động rơi
- **Interactive**: Có thể điều khiển khối real-time

## 🔧 Yêu cầu

- Android API 24+
- Kotlin 1.9+
- Jetpack Compose

---
*Demo này tập trung vào việc hiểu và áp dụng Canvas API trong Compose thay vì tạo game Tetris hoàn
chỉnh.*
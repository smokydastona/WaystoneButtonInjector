# WaystoneInjector Dev Config Guide

## Quick Start

1. **Enable Dev Mode**: Copy `waystoneinjector-dev.json.example` to your Minecraft config folder as `waystoneinjector-dev.json`
2. **Edit Live**: Modify the JSON file while the game is running
3. **Reopen Menu**: Close and reopen the waystone menu to see changes instantly!

## Dev Mode Features

### ✅ Live Configuration Reload
- Changes apply immediately when you reopen the waystone menu
- No need to restart Minecraft
- See your adjustments in real-time

### 📊 Debug Overlay
When `showDebugOverlay: true`:
- **Green box** = Scroll list position and size
- **Magenta box** = Portal position and size
- **Info panel** (top-left) = Current values for all elements
- **Mouse coordinates** = For precise positioning

### 🎛️ Configuration Options

#### Master Controls
```json
"enabled": true,              // Master toggle for dev mode
"showDebugOverlay": true      // Show debug bounding boxes and info
```

#### Scroll List Settings
```json
"scrollList": {
  "xOffset": 0,               // Horizontal offset from center (pixels)
  "yOffset": 0,               // Vertical offset (pixels)
  "width": 180,               // List width (pixels) - ADJUST THIS TO MAKE SMALLER
  "height": 150,              // List height (pixels)
  "topMargin": 80,            // Distance from top of screen
  "bottomMargin": 40,         // Distance from bottom of screen
  "itemHeight": 22,           // Height of each waystone entry
  "centered": true            // Auto-center horizontally
}
```

**To make the list smaller and centered:**
1. Reduce `width` (try 150-200)
2. Keep `centered: true`
3. Adjust `xOffset` to fine-tune horizontal position

#### Portal Animation Settings
```json
"portal": {
  "xOffset": 0,               // Horizontal offset from center
  "yOffset": -50,             // Vertical offset (negative = move up)
  "width": 128,               // Portal width (pixels)
  "height": 128,              // Portal height (pixels)
  "centered": true,           // Auto-center
  "animationSpeed": 100       // Milliseconds per frame (26 frames total)
}
```

#### Background Settings
```json
"background": {
  "renderDirtBackground": false,      // Vanilla dirt background
  "backgroundColor": -2147483648,     // ARGB hex color
  "renderMenuBackground": true,       // Custom menu background
  "menuBackgroundAlpha": 180          // 0-255 (0=transparent, 255=opaque)
}
```

#### Texture Overrides
Test different textures without editing code:
```json
"textures": {
  "portalAnimation": "waystoneinjector:textures/gui/animations/portal/mystic_%d.png",
  "listBackground": "waystoneinjector:textures/gui/backgrounds/list_panel.png",
  "menuBackground": "waystoneinjector:textures/gui/backgrounds/menu_background.png",
  "entryBackground": "waystoneinjector:textures/gui/backgrounds/list_entry_background.png",
  "portalFrame": "waystoneinjector:textures/gui/backgrounds/portal_frame.png"
}
```

Format: `"namespace:path/to/texture.png"`
For animations: Use `%d` as placeholder for frame number (1-26)

#### Render Order (Z-Index)
Control what renders on top:
```json
"renderOrder": {
  "background": 0,       // Lowest (back)
  "portal": 10,
  "scrollList": 20,
  "buttons": 30,
  "tooltips": 100        // Highest (front)
}
```

Higher numbers = rendered on top of lower numbers

## Example: Make Scroll List Smaller & Centered

```json
{
  "enabled": true,
  "showDebugOverlay": true,
  "scrollList": {
    "xOffset": 0,
    "yOffset": 0,
    "width": 160,           // ← Smaller width
    "height": 140,          // ← Smaller height
    "topMargin": 90,        // ← More space from top
    "bottomMargin": 50,
    "itemHeight": 20,       // ← Smaller entries
    "centered": true        // ← Keep centered
  },
  // ... rest of config
}
```

## Workflow

1. **Open waystone menu** → See current layout
2. **Edit `config/waystoneinjector-dev.json`** → Make changes
3. **Save file**
4. **Close and reopen waystone menu** → See changes instantly!
5. **Repeat** until perfect
6. **Copy final values** to code or leave as config
7. **Set `enabled: false`** to disable dev mode

## Tips

- **Use debug overlay first** to see exact current positions
- **Make small changes** (5-10 pixels at a time)
- **Keep the file open** in a text editor for quick edits
- **Invalid JSON** will print error to console and use last valid config
- **Missing file** creates default config automatically

## Disabling Dev Mode

When you're done:
```json
{
  "enabled": false,
  // ... rest stays the same
}
```

Or simply delete `config/waystoneinjector-dev.json`

## Color Format (ARGB)

Background colors use ARGB hex:
- `0xFF000000` = Solid black
- `0x80000000` = Semi-transparent black (50%)
- `0xAAFFFFFF` = Semi-transparent white
- `0x00000000` = Fully transparent

Format: `0xAARRGGBB`
- AA = Alpha (00=transparent, FF=opaque)
- RR = Red (00-FF)
- GG = Green (00-FF)
- BB = Blue (00-FF)

# Animated Portal Background

An animated purple portal swirl that displays behind all waystone GUI textures (except sharestones, which use color overlays instead).

## How It Works

When you open a waystone GUI:
- **Regular Waystones** (regular, mossy, blackstone, deepslate, endstone): Animated portal renders behind, then waystone texture on top
- **Sharestones**: Color overlay renders behind instead (no portal animation)

## Animation Details

- **Frames**: 16 frames in a vertical strip (4096x256 total)
- **Frame Size**: 256x256 pixels each
- **Animation Speed**: 2 ticks per frame (`frametime: 2` in mcmeta)
- **Interpolation**: Enabled for smooth transitions
- **Total Loop Time**: ~1.6 seconds (32 ticks)

## Visual Style

The portal features:
- Purple/magenta gradient (like nether portals)
- Radial gradient from center to edges
- Rotating swirl arcs
- Semi-transparent (allows waystone texture to show clearly)
- Colors: Blue Violet (#8A2BE2), Medium Orchid (#B452CD), Indigo (#4B0082)

## Files

- `portal_animation.png` - The 256x4096 animation strip (16 frames stacked vertically)
- `portal_animation.png.mcmeta` - Animation metadata (frametime and interpolation settings)

## Customization

### Change Animation Speed
Edit `portal_animation.png.mcmeta`:
```json
{
  "animation": {
    "frametime": 2,  // Change to 1 (faster) or 4 (slower)
    "interpolate": true
  }
}
```

### Change Portal Colors
Run `.\create_portal_animation.ps1` and modify the color values:
```powershell
# Center color (lighter)
$brush.CenterColor = [System.Drawing.Color]::FromArgb(180, 180, 82, 255)

# Edge colors (darker)
$edgeColors = @(
    [System.Drawing.Color]::FromArgb(100, 75, 0, 130),   # Indigo
    [System.Drawing.Color]::FromArgb(120, 138, 43, 226)  # Blue Violet
)
```

### Create Different Animation

You can replace `portal_animation.png` with any 256-width vertical strip texture. The height must be a multiple of 256 (each 256-pixel segment is one frame).

Example frame counts:
- 8 frames: 256x2048
- 16 frames: 256x4096 (current)
- 32 frames: 256x8192

**Remember**: Update `frametime` in the `.mcmeta` file if you change the frame count to maintain smooth animation speed.

## Regenerating

To regenerate the portal animation:
```powershell
.\create_portal_animation.ps1
```

This will recreate the 16-frame purple portal animation with the default settings.

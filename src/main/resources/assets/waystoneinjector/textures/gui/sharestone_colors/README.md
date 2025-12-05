# Sharestone Color Overlay Textures

This directory contains semi-transparent color overlay textures for each of the 16 sharestone variants in the Waystones mod.

## How It Works

When you open a sharestone GUI, the mod:
1. Detects which color sharestone you clicked (e.g., `blue_sharestone`, `red_sharestone`, etc.)
2. Renders the corresponding color overlay from this directory **first** (as a background layer)
3. Renders the main `sharestone.png` texture **on top**

This creates a layered effect where the color shows through behind the main sharestone texture.

## Sharestone Colors

All 16 Minecraft dye colors are supported:

| Color | File | RGB Values |
|-------|------|------------|
| Black | `black.png` | 25, 25, 25 |
| Blue | `blue.png` | 51, 76, 178 |
| Brown | `brown.png` | 102, 76, 51 |
| Cyan | `cyan.png` | 76, 127, 153 |
| Gray | `gray.png` | 76, 76, 76 |
| Green | `green.png` | 102, 127, 51 |
| Light Blue | `light_blue.png` | 102, 153, 216 |
| Light Gray | `light_gray.png` | 153, 153, 153 |
| Lime | `lime.png` | 127, 204, 25 |
| Magenta | `magenta.png` | 178, 76, 216 |
| Orange | `orange.png` | 216, 127, 51 |
| Pink | `pink.png` | 242, 127, 165 |
| Purple | `purple.png` | 127, 63, 178 (default) |
| Red | `red.png` | 153, 51, 51 |
| White | `white.png` | 255, 255, 255 |
| Yellow | `yellow.png` | 229, 229, 51 |

## Transparency

All color overlays use **50% alpha (128/255)** to appear semi-transparent. This allows the color to show through while still letting the main sharestone texture be visible on top.

## Customization

You can customize these colors by editing the PNG files:
- Keep the 256x256 resolution
- Use RGBA format (PNG with alpha channel)
- Adjust alpha channel for more/less transparency
- Change RGB values to customize the color

**Tip:** Use image editing software that supports alpha channels (GIMP, Photoshop, Paint.NET) to maintain transparency when editing.

## Regenerating Textures

If you need to regenerate all color overlays, run:
```powershell
.\create_sharestone_overlays.ps1
```

This will recreate all 16 color files with the default Minecraft dye colors at 50% transparency.

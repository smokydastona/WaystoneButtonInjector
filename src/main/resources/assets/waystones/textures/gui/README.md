# Waystones GUI Texture Overrides

This folder contains custom textures to override the default Waystones GUI.

## GUI Textures

### Main Menus
- **waystone.png** (256x128) - Main waystone selection screen background
- **warp_plate.png** (256x128) - Warp plate GUI background

### UI Elements
- **checkbox.png** (16x16) - Checkbox for global waystones
- **inventory_button.png** (16x16) - Inventory button icon

## How to Customize

1. Edit the PNG files in this directory with your custom textures
2. Maintain the same dimensions as the originals
3. Rebuild the mod with `./gradlew build`
4. The mod will override Waystones textures at runtime

## Original Texture Locations

These textures override:
```
assets/waystones/textures/gui/menu/waystone.png
assets/waystones/textures/gui/menu/warp_plate.png
assets/waystones/textures/gui/checkbox.png
assets/waystones/textures/gui/inventory_button.png
```

## Tips

- Use transparency (alpha channel) for non-rectangular UIs
- Keep text readable - test at 1x and 2x GUI scale
- Match Minecraft's style or go completely custom
- Use 256x128 for menu backgrounds (standard GUI size)

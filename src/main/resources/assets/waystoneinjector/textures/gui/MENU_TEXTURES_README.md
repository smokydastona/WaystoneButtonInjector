# Enhanced Waystone Menu Textures

This folder contains custom textures for the scrollable waystone menu. Edit these PNG files to customize the look of your menu!

## Texture Files

### `menu_background.png` (270x200 pixels)
- **Purpose**: Main container background for the entire waystone selection menu
- **Default**: Dark gray background with light gray outer border and darker inner border
- **Usage**: Replaces the standard Minecraft container GUI
- **Tips**: 
  - This is the main backdrop behind everything
  - Keep it semi-dark so text is readable
  - The portal, title, and list render on top of this

### `list_panel.png` (220x400 pixels)
- **Purpose**: Background panel behind the scrollable waystone list
- **Default**: Semi-transparent dark blue-gray with subtle border
- **Usage**: Currently NOT used (ObjectSelectionList has its own background)
- **Tips**: 
  - Can be used to add a custom panel behind the list entries
  - Make it semi-transparent (alpha channel) to see through to menu_background
  - 220 pixels wide matches the waystone button width
  - 400 pixels tall covers most list heights

### `portal_frame.png` (80x80 pixels)
- **Purpose**: Decorative frame around the mystical portal animation
- **Default**: Circular purple gradient ring design
- **Usage**: Renders behind the 64x64 portal animation at the top center
- **Tips**: 
  - The portal animation (64x64) renders centered within this frame
  - Leave the center area transparent or dark to not obscure the portal
  - Purple/mystical colors work well with the portal theme
  - Can be ornate border, runes, or simple ring

### `list_entry_background.png` (220x20 pixels)
- **Purpose**: Background for individual waystone entries in the list
- **Default**: Fully transparent (uses default button rendering)
- **Usage**: Currently NOT used - WaystoneButtons have their own rendering
- **Tips**: 
  - Could be enabled to add custom backgrounds per list entry
  - Would render behind each button
  - 220x20 matches standard WaystoneButton size

## How to Customize

1. **Open the PNG files** in any image editor (GIMP, Photoshop, Paint.NET, etc.)
2. **Edit the textures** to match your texture pack style
3. **Save as PNG** with transparency support (if using alpha channel)
4. **Reload the mod** in Minecraft to see changes

## Color Recommendations

- **Dark themes**: Use dark grays/blues with subtle borders for readability
- **Mystical theme**: Purple, blue, and teal colors match the portal aesthetic
- **Stone theme**: Grays and browns to match waystone blocks
- **Custom themes**: Match your resource pack's color palette

## Advanced Customization

To enable the unused textures (`list_panel.png` and `list_entry_background.png`), you'll need to modify:
- `EnhancedWaystoneSelectionScreen.java` - Add rendering calls
- `ScrollableWaystoneList.java` - Add background rendering to entries

The current implementation uses minimal custom textures to maintain compatibility with your existing overlay system.

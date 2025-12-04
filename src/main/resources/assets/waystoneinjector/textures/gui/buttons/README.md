# Button Background Textures

This directory contains themed button background textures that match each waystone type.

## Required Textures

Each waystone type needs 6 button background textures (3 for left side, 3 for right side):

### File Naming Convention
`<waystoneType>_<side>_<count>.png`

- **waystoneType**: regular, mossy, blackstone, deepslate, endstone, sharestone
- **side**: left, right
- **count**: 1, 2, 3 (number of buttons on that side)

### Texture Specifications
- **Dimensions**: 64 pixels wide × 96 pixels tall
- **Format**: PNG with transparency
- **Layout**: Vertical stack of 3 button slots (each 32 pixels tall)
  - Top slot (0-31): First button position
  - Middle slot (32-63): Second button position
  - Bottom slot (64-95): Third button position

### Required Files (36 total)

**Regular Waystone (brown/sandy texture)**:
- regular_left_1.png
- regular_left_2.png
- regular_left_3.png
- regular_right_1.png
- regular_right_2.png
- regular_right_3.png

**Mossy Waystone (green mossy texture)**:
- mossy_left_1.png
- mossy_left_2.png
- mossy_left_3.png
- mossy_right_1.png
- mossy_right_2.png
- mossy_right_3.png

**Blackstone Waystone (dark gray/black texture)**:
- blackstone_left_1.png
- blackstone_left_2.png
- blackstone_left_3.png
- blackstone_right_1.png
- blackstone_right_2.png
- blackstone_right_3.png

**Deepslate Waystone (dark slate texture)**:
- deepslate_left_1.png
- deepslate_left_2.png
- deepslate_left_3.png
- deepslate_right_1.png
- deepslate_right_2.png
- deepslate_right_3.png

**End Stone Waystone (pale yellow texture)**:
- endstone_left_1.png
- endstone_left_2.png
- endstone_left_3.png
- endstone_right_1.png
- endstone_right_2.png
- endstone_right_3.png

**Sharestone (purple/amethyst texture)**:
- sharestone_left_1.png
- sharestone_left_2.png
- sharestone_left_3.png
- sharestone_right_1.png
- sharestone_right_2.png
- sharestone_right_3.png

## Design Guidelines

1. **Match Main Waystone Texture**: Each button background should visually match the corresponding waystone type's main GUI texture (found in `../`)

2. **Button Borders**: Each 32-pixel slot should have subtle borders/frames that indicate clickable button areas

3. **Texture Continuity**: When multiple buttons are shown, the texture should flow naturally as a vertical panel

4. **States**: The texture shows the default/normal state - Minecraft will handle hover/pressed states automatically

5. **Transparency**: Edges should have appropriate alpha blending to smoothly integrate with the main waystone GUI

## Example Layout

For `regular_left_3.png` (3 buttons on left side):
```
┌────────────────────┐  ← 0px
│                    │
│   Button Slot 1    │  ← Button at index 0 renders here
│                    │
├────────────────────┤  ← 32px
│                    │
│   Button Slot 2    │  ← Button at index 1 renders here
│                    │
├────────────────────┤  ← 64px
│                    │
│   Button Slot 3    │  ← Button at index 2 renders here
│                    │
└────────────────────┘  ← 96px
```

## Creating the Textures

1. Open the corresponding main waystone texture (e.g., `waystone_regular.png`)
2. Extract a vertical strip from the side/edge of the waystone GUI
3. Resize/tile to create 64×96 panel
4. Add subtle button frame/border overlays for each of the 3 slots
5. Save as PNG with transparency
6. Repeat for all 36 combinations

## Fallback Behavior

If a texture is missing, Minecraft will show a missing texture (pink/black checkerboard pattern). The mod will still function, but buttons won't have themed backgrounds.

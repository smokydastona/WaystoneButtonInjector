# v3.0.174 - Mixin Button Injection Implementation

## Changes Made

### 1. **CustomWaystoneButton.java** (NEW)
- **Extends**: `WaystoneButton` from Waystones mod
- **Purpose**: Custom button widget that renders overlay textures BEHIND button content
- **Key Features**:
  - Overrides `renderWidget()` to inject overlay texture before super.renderWidget()
  - Detects waystone type via reflection (waystoneType, blockType fields)
  - Supports 9 waystone types: regular, mossy, blackstone, deepslate, endstone, sharestone, warp_scroll, warp_stone, portstone
  - Preserves all original WaystoneButton functionality (XP cost, distance, tooltips)

### 2. **MixinWaystoneSelectionScreen.java** (REWRITTEN)
- **Target**: `WaystoneSelectionScreenBase` (Waystones mod)
- **Injection**: `@Redirect` on `createWaystoneButton()` method
- **Purpose**: Intercepts WaystoneButton instantiation and substitutes CustomWaystoneButton
- **Result**: All waystone list buttons now use custom rendering

### 3. **IWaystoneSelectionScreenAccessor.java** (REMOVED - No longer needed)
- Accessor interface was created for reflection approach
- No longer needed with Mixin @Redirect approach

### 4. **Decompiled Waystones Source** (NEW)
- Downloaded CFR decompiler to `libs/cfr.jar`
- Decompiled entire Waystones mod JAR to `decompiled/` folder
- 140+ classes analyzed to understand internal structure
- Key findings:
  - `WaystoneSelectionScreenBase.createWaystoneButton()` creates buttons at line 177
  - Constructor signature: `new WaystoneButton(int x, int y, IWaystone waystone, int xpLevelCost, Button.OnPress pressable)`
  - Perfect injection point for Mixin @Redirect

## Technical Implementation

### Render Order Fix
**Problem**: Previous overlay approach rendered textures AFTER buttons due to Minecraft's GUI system rendering widgets after background phases.

**Solution**: Replace button widget itself with custom implementation that renders overlay as part of widget rendering:

```java
@Override
public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    // STEP 1: Render overlay texture FIRST (behind everything)
    guiGraphics.blit(overlayTexture, this.getX(), this.getY() - 8, 0, 0, 220, 36, 220, 36);
    
    // STEP 2: Render original button (renders on top of overlay)
    super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
}
```

### Mixin Injection Point
```java
@Redirect(
    method = "createWaystoneButton",
    at = @At(
        value = "NEW",
        target = "(IILnet/blay09/mods/waystones/api/IWaystone;ILnet/minecraft/client/gui/components/Button$OnPress;)Lnet/blay09/mods/waystones/client/gui/widget/WaystoneButton;"
    ),
    remap = false
)
private WaystoneButton redirectWaystoneButtonCreation(...) {
    return new CustomWaystoneButton(x, y, waystone, xpLevelCost, pressable);
}
```

### Type Detection
Uses reflection to inspect waystone NBT fields:
1. Check `waystoneType` field for type keywords
2. Fallback to `blockType` field
3. Default to "regular" if detection fails

## Validation Status

✅ **Source Code**: 0 compilation errors  
✅ **Mixin Target**: WaystoneSelectionScreenBase.createWaystoneButton()  
✅ **Button Inheritance**: CustomWaystoneButton extends WaystoneButton  
✅ **Overlay Detection**: 9 waystone types supported  
⏳ **GitHub Actions**: Pushed to remote, awaiting compilation  

## Expected Behavior

When opening a Waystone selection GUI in-game:
1. Mixin intercepts WaystoneButton creation
2. CustomWaystoneButton substituted instead
3. Overlay texture renders FIRST (behind button)
4. Button renders on top with all vanilla functionality
5. Distance, XP cost, and other overlays render correctly

## Next Steps

1. Wait for GitHub Actions compilation result
2. Test in-game to verify overlay rendering
3. Check console for debug message: `[WaystoneInjector] Injecting custom button for waystone: <name>`
4. Validate all 9 waystone types show correct overlays
5. If successful, remove old renderWaystoneListOverlays() code from ClientEvents.java

## Files Modified
- `src/main/java/com/example/waystoneinjector/gui/CustomWaystoneButton.java` (NEW)
- `src/main/java/com/example/waystoneinjector/mixin/MixinWaystoneSelectionScreen.java` (REWRITTEN)
- `libs/cfr.jar` (NEW - decompiler)
- `decompiled/` folder (NEW - 140+ decompiled Waystones classes)

## Commit
**Message**: Implement Mixin-based custom button injection - overlay textures now render behind buttons  
**Tag**: v3.0.174 (when ready to release)  
**Status**: Pushed to GitHub, pending compilation

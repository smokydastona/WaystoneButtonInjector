# Better Waystones Menu Feature Port - Technical Plan
**Project:** WaystoneButtonInjector (1.20.1)  
**Source:** Better Waystones Menu (1.18.2)  
**Created:** December 4, 2025  
**Status:** Planning Phase

---

## Executive Summary

Port 5 key features from Better Waystones Menu (1.18.2) to WaystoneButtonInjector (1.20.1):
1. ✅ **Scrollbar** for waystone list
2. ✅ **Search box** with right-click clear
3. ✅ **Sorting modes** (alphabetical, distance, recent, etc.)
4. ✅ **Drag-and-drop** waystone reordering
5. ✅ **CTRL+hover** detailed waystone information

**Estimated Scope:** 1,500-2,500 lines of new code across 8-12 new files  
**Development Time:** 10-20 hours (incremental approach recommended)  
**Complexity:** High - Complete GUI overhaul required

---

## Current Architecture Analysis

### Existing Implementation
**File:** `ClientEvents.java`
- **Approach:** Event-based button injection into existing WaystoneSelectionScreen
- **Method:** `ScreenEvent.Init.Post` listener adds custom buttons to screen edges
- **Limitations:** 
  - No access to waystone list data
  - Cannot modify existing waystone buttons
  - Limited to adding peripheral buttons only
  - No control over layout or rendering

### Why Complete Screen Replacement Required

The original Waystones mod's `WaystoneSelectionScreen` is **not extensible** enough for these features:
- Waystone list is **private/internal** - can't access for scrolling or sorting
- Search requires **filtering the waystone list** - need direct control
- Drag-and-drop needs **mouse event interception** - can't do from outside
- Custom rendering (CTRL tooltips) requires **screen-level control**

**Conclusion:** Must create custom screen that replaces WaystoneSelectionScreen entirely.

---

## Architecture Decision: Screen Replacement Strategy

### Option 1: Mixin Approach ❌ (Not Recommended)
- Use Mixins to modify WaystoneSelectionScreen internals
- **Pros:** Less code duplication
- **Cons:** 
  - Fragile - breaks with Waystones updates
  - Complex to maintain
  - Hard to debug
  - Waystones internals may be obfuscated

### Option 2: Complete Screen Replacement ✅ (Recommended)
- Intercept screen opening event and replace with custom screen
- **Pros:**
  - Full control over all features
  - No dependency on Waystones internals
  - Easier to maintain and debug
  - Can add any feature without limitations
- **Cons:**
  - More initial code to write
  - Need to replicate base Waystones functionality

**Decision:** Use complete screen replacement approach.

---

## Implementation Plan - Phase Breakdown

### PHASE 1: Foundation (2-3 hours)
**Goal:** Create custom screen that replicates basic Waystones functionality

#### 1.1 Create Custom Screen Class
**File:** `src/main/java/com/example/waystoneinjector/client/gui/EnhancedWaystoneSelectionScreen.java`

```java
public class EnhancedWaystoneSelectionScreen extends Screen {
    // Screen replacement for WaystoneSelectionScreen
    // Initial version: just display waystones (no enhancements yet)
}
```

**Features:**
- Extends `net.minecraft.client.gui.screens.Screen`
- Constructor accepts waystone data (needs research on Waystones API)
- Basic `init()`, `render()`, and `onClose()` methods
- Display title and waystone list (simple, no scroll yet)

**Estimated Lines:** ~200-300

#### 1.2 Screen Interception System
**Modify:** `ClientEvents.java`

**Current:**
```java
if (!className.equals("net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen")) {
    return;
}
// Add buttons to existing screen
```

**New:**
```java
if (className.equals("net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen")) {
    // Cancel the event and replace with our custom screen
    event.setCanceled(true);
    Minecraft.getInstance().setScreen(new EnhancedWaystoneSelectionScreen(...));
}
```

**Challenge:** Need to extract waystone data from original screen to pass to custom screen.

**Estimated Lines:** ~50-100 modifications

#### 1.3 Waystone Data Extraction
**Options:**
- **A:** Use reflection to access private fields in WaystoneSelectionScreen
- **B:** Hook into Waystones network packets to capture waystone list
- **C:** Use Waystones public API if available

**Research Required:** Determine which option is viable for 1.20.1 Waystones

**Estimated Lines:** ~100-200 (utility class)

**Deliverable:** Working custom screen that displays waystones (basic functionality only)

---

### PHASE 2: Scrollbar Implementation (2-3 hours)
**Goal:** Add scrollable waystone list container

#### 2.1 Create Scrollable Container Widget
**File:** `src/main/java/com/example/waystoneinjector/client/gui/widget/ScrollableWaystoneList.java`

**Reference:** Better Waystones Menu's `ScrollableContainerWidget.java`

**Features:**
- Extends `net.minecraft.client.gui.components.AbstractWidget`
- Vertical scrollbar rendering
- Mouse wheel scroll support
- Drag scrollbar support
- Viewport clipping (only show visible waystones)
- Auto-hide scrollbar when not needed

**Key Methods:**
```java
public class ScrollableWaystoneList extends AbstractWidget {
    private List<WaystoneButton> waystones;
    private double scrollOffset = 0.0;
    private int maxScroll = 0;
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick);
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta);
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY);
    
    private void updateScrollbar();
    private void renderScrollbar(GuiGraphics graphics);
}
```

**1.18.2 → 1.20.1 API Changes:**
- `PoseStack` → `GuiGraphics` (rendering parameter change)
- `blit()` → `graphics.blit()` method signature changes
- Widget positioning system updates

**Estimated Lines:** ~300-400

#### 2.2 Integrate Scrollable Container
**Modify:** `EnhancedWaystoneSelectionScreen.java`

Replace simple list rendering with scrollable container:
```java
@Override
protected void init() {
    super.init();
    
    // Create scrollable list
    this.waystoneList = new ScrollableWaystoneList(
        this.width / 2 - 100, 40,  // x, y
        200, this.height - 80,      // width, height
        this.waystones
    );
    
    this.addRenderableWidget(waystoneList);
}
```

**Estimated Lines:** ~50-100 modifications

**Deliverable:** Scrollable waystone list with mouse wheel and drag support

---

### PHASE 3: Search Box (1-2 hours)
**Goal:** Add search field with filtering and right-click clear

#### 3.1 Create Enhanced Text Field
**File:** `src/main/java/com/example/waystoneinjector/client/gui/widget/WaystoneSearchField.java`

**Reference:** Better Waystones Menu's `BetterTextFieldWidget.java`

**Features:**
- Extends `net.minecraft.client.gui.components.EditBox`
- Search icon rendering
- Right-click to clear functionality
- Real-time filtering callback
- Placeholder text when empty

**Key Methods:**
```java
public class WaystoneSearchField extends EditBox {
    private Consumer<String> onSearchChanged;
    
    public WaystoneSearchField(int x, int y, int width, int height) {
        super(Minecraft.getInstance().font, x, y, width, height, Component.literal("Search..."));
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1 && this.isMouseOver(mouseX, mouseY)) { // Right-click
            this.setValue("");
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public void insertText(String text) {
        super.insertText(text);
        if (onSearchChanged != null) {
            onSearchChanged.accept(this.getValue());
        }
    }
}
```

**Estimated Lines:** ~150-200

#### 3.2 Implement Filtering Logic
**Modify:** `EnhancedWaystoneSelectionScreen.java`

Add search field and filtering:
```java
private WaystoneSearchField searchField;
private List<Waystone> allWaystones;
private List<Waystone> filteredWaystones;

@Override
protected void init() {
    // Add search field at top
    this.searchField = new WaystoneSearchField(
        this.width / 2 - 100, 20,
        200, 16
    );
    this.searchField.setOnSearchChanged(this::filterWaystones);
    this.addRenderableWidget(searchField);
    
    // Pass filtered list to scrollable container
    this.waystoneList = new ScrollableWaystoneList(
        this.width / 2 - 100, 40,
        200, this.height - 80,
        this.filteredWaystones
    );
}

private void filterWaystones(String query) {
    if (query.isEmpty()) {
        filteredWaystones = new ArrayList<>(allWaystones);
    } else {
        filteredWaystones = allWaystones.stream()
            .filter(ws -> ws.getName().toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());
    }
    waystoneList.updateWaystones(filteredWaystones);
}
```

**Estimated Lines:** ~100-150 modifications

**Deliverable:** Working search box with real-time filtering and right-click clear

---

### PHASE 4: Sorting Modes (2-3 hours)
**Goal:** Multiple sort options for waystone list

#### 4.1 Create Sorting System
**File:** `src/main/java/com/example/waystoneinjector/client/gui/sorting/SortingMode.java`

**Features:**
```java
public enum SortingMode {
    ALPHABETICAL("Alphabetical", Comparator.comparing(Waystone::getName)),
    DISTANCE("Distance", Comparator.comparingDouble(ws -> ws.getDistanceToPlayer())),
    RECENT("Recent", Comparator.comparingLong(Waystone::getLastUsedTime).reversed()),
    DIMENSION("Dimension", Comparator.comparing(Waystone::getDimension));
    
    private final String displayName;
    private final Comparator<Waystone> comparator;
    
    SortingMode(String displayName, Comparator<Waystone> comparator) {
        this.displayName = displayName;
        this.comparator = comparator;
    }
    
    public List<Waystone> sort(List<Waystone> waystones) {
        return waystones.stream()
            .sorted(this.comparator)
            .collect(Collectors.toList());
    }
}
```

**Estimated Lines:** ~100-150

#### 4.2 Create Sort Mode Button
**File:** `src/main/java/com/example/waystoneinjector/client/gui/widget/SortModeButton.java`

**Features:**
- Cycle through sort modes on click
- Display current mode icon/text
- Textured button (optional - can use simple text button)

**Estimated Lines:** ~100-150

#### 4.3 Integrate Sorting
**Modify:** `EnhancedWaystoneSelectionScreen.java`

```java
private SortingMode currentSortMode = SortingMode.ALPHABETICAL;
private SortModeButton sortButton;

@Override
protected void init() {
    // Add sort button next to search
    this.sortButton = new SortModeButton(
        this.width / 2 + 105, 20,
        20, 20,
        this::onSortModeChanged
    );
    this.addRenderableWidget(sortButton);
}

private void onSortModeChanged(SortingMode newMode) {
    this.currentSortMode = newMode;
    this.refreshWaystoneList();
}

private void refreshWaystoneList() {
    // Filter first
    filterWaystones(searchField.getValue());
    
    // Then sort
    filteredWaystones = currentSortMode.sort(filteredWaystones);
    
    // Update display
    waystoneList.updateWaystones(filteredWaystones);
}
```

**Estimated Lines:** ~100-200 modifications

**Deliverable:** Working sort modes with cycle button

---

### PHASE 5: Drag-and-Drop Reordering (3-4 hours)
**Goal:** Allow players to manually reorder waystones

#### 5.1 Create Draggable Waystone Button
**File:** `src/main/java/com/example/waystoneinjector/client/gui/widget/DraggableWaystoneButton.java`

**Reference:** Better Waystones Menu's `BetterWaystoneButton.java`

**Features:**
```java
public class DraggableWaystoneButton extends Button {
    private boolean isDragging = false;
    private double dragStartY = 0;
    private double currentDragY = 0;
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) { // Left mouse button
            if (!isDragging) {
                isDragging = true;
                dragStartY = mouseY;
            }
            currentDragY = mouseY;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isDragging) {
            isDragging = false;
            // Calculate which waystone to swap with
            onDragEnd(calculateDropIndex());
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (isDragging) {
            // Render with transparency and offset
            graphics.pose().pushPose();
            graphics.pose().translate(0, currentDragY - dragStartY, 100); // Higher Z to render on top
            // ... render with alpha transparency
            graphics.pose().popPose();
        } else {
            super.render(graphics, mouseX, mouseY, partialTick);
        }
    }
}
```

**Estimated Lines:** ~200-300

#### 5.2 Implement Swap Logic
**Modify:** `ScrollableWaystoneList.java`

Add reordering support:
```java
private int draggedIndex = -1;
private int dropTargetIndex = -1;

public void onWaystoneDragStart(int index) {
    this.draggedIndex = index;
}

public void onWaystoneDragEnd(int dropIndex) {
    if (draggedIndex >= 0 && dropIndex >= 0 && draggedIndex != dropIndex) {
        // Swap waystones
        Waystone temp = waystones.get(draggedIndex);
        waystones.set(draggedIndex, waystones.get(dropIndex));
        waystones.set(dropIndex, temp);
        
        // Persist order (save to config or client data)
        saveWaystoneOrder();
    }
    draggedIndex = -1;
}

private void saveWaystoneOrder() {
    // TODO: Implement persistence
    // Option 1: Save to client-side config
    // Option 2: Use Minecraft's savedData system
    // Option 3: Send to server if server-side mod installed
}
```

**Estimated Lines:** ~150-200 modifications

**Challenge:** Need persistence system for custom waystone order

#### 5.3 Persistence System
**File:** `src/main/java/com/example/waystoneinjector/client/WaystoneOrderManager.java`

**Features:**
- Save custom waystone order to client-side file
- Load order on screen open
- JSON or NBT format

```java
public class WaystoneOrderManager {
    private static final Path ORDER_FILE = FMLPaths.CONFIGDIR.get().resolve("waystoneinjector-order.json");
    
    public static Map<String, Integer> loadWaystoneOrder() {
        // Load from file
    }
    
    public static void saveWaystoneOrder(List<Waystone> orderedWaystones) {
        // Save to file
    }
}
```

**Estimated Lines:** ~100-150

**Deliverable:** Drag-and-drop reordering with persistence

---

### PHASE 6: CTRL+Hover Tooltips (2-3 hours)
**Goal:** Show detailed waystone info when holding CTRL and hovering

#### 6.1 Create Tooltip Renderer
**File:** `src/main/java/com/example/waystoneinjector/client/gui/tooltip/WaystoneTooltipRenderer.java`

**Features:**
```java
public class WaystoneTooltipRenderer {
    public static void renderDetailedTooltip(GuiGraphics graphics, Waystone waystone, int x, int y) {
        if (!Screen.hasControlDown()) {
            return; // Only show when CTRL held
        }
        
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal("§b§l" + waystone.getName()));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.literal("§7Dimension: §f" + waystone.getDimension()));
        tooltip.add(Component.literal("§7Location: §f" + waystone.getX() + ", " + waystone.getY() + ", " + waystone.getZ()));
        tooltip.add(Component.literal("§7Distance: §f" + String.format("%.1f", waystone.getDistanceToPlayer()) + " blocks"));
        
        if (waystone.isGlobal()) {
            tooltip.add(Component.literal("§a✓ Global Waystone"));
        }
        
        if (waystone.getLastUsedTime() > 0) {
            long timeSince = System.currentTimeMillis() - waystone.getLastUsedTime();
            tooltip.add(Component.literal("§7Last used: §f" + formatTimeSince(timeSince)));
        }
        
        // Render tooltip box
        graphics.renderTooltip(Minecraft.getInstance().font, tooltip, x, y);
    }
    
    private static String formatTimeSince(long ms) {
        // Convert to human-readable format
    }
}
```

**Estimated Lines:** ~150-200

#### 6.2 Integrate Tooltip Rendering
**Modify:** `DraggableWaystoneButton.java`

```java
@Override
public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    super.render(graphics, mouseX, mouseY, partialTick);
    
    // Check if hovering and CTRL held
    if (this.isHovered() && Screen.hasControlDown()) {
        WaystoneTooltipRenderer.renderDetailedTooltip(
            graphics, 
            this.waystone, 
            mouseX, 
            mouseY
        );
    }
}
```

**Estimated Lines:** ~20-30 modifications

**Deliverable:** Detailed tooltips when CTRL+hovering waystone buttons

---

## File Structure Summary

### New Files to Create
```
src/main/java/com/example/waystoneinjector/client/
├── gui/
│   ├── EnhancedWaystoneSelectionScreen.java        [300-400 lines]
│   ├── sorting/
│   │   └── SortingMode.java                        [100-150 lines]
│   ├── tooltip/
│   │   └── WaystoneTooltipRenderer.java            [150-200 lines]
│   └── widget/
│       ├── ScrollableWaystoneList.java             [300-400 lines]
│       ├── WaystoneSearchField.java                [150-200 lines]
│       ├── SortModeButton.java                     [100-150 lines]
│       └── DraggableWaystoneButton.java            [200-300 lines]
└── WaystoneOrderManager.java                       [100-150 lines]
```

**Total New Code:** ~1,500-2,150 lines

### Files to Modify
```
src/main/java/com/example/waystoneinjector/client/
└── ClientEvents.java                                [~100-200 line modifications]
```

---

## API Migration Guide: 1.18.2 → 1.20.1

### Key Changes to Handle

#### 1. Rendering System
**1.18.2:**
```java
public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    RenderSystem.setShaderTexture(0, TEXTURE);
    blit(poseStack, x, y, 0, 0, width, height);
}
```

**1.20.1:**
```java
public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    graphics.blit(TEXTURE, x, y, 0, 0, width, height);
}
```

#### 2. Widget Creation
**1.18.2:**
```java
Button button = new Button(x, y, width, height, text, this::onPress);
```

**1.20.1:**
```java
Button button = Button.builder(text, this::onPress)
    .bounds(x, y, width, height)
    .build();
```

#### 3. Component/Text System
**1.18.2:**
```java
new TextComponent("Hello")
new TranslatableComponent("key")
```

**1.20.1:**
```java
Component.literal("Hello")
Component.translatable("key")
```

#### 4. Screen Methods
**1.18.2:**
```java
@Override
public void init() {
    super.init();
}
```

**1.20.1:**
```java
@Override
protected void init() {
    super.init();
}
```

---

## Testing Plan

### Phase-by-Phase Testing

#### Phase 1 Testing
- [ ] Custom screen opens when accessing waystone
- [ ] All waystones display correctly
- [ ] Can click waystone to teleport
- [ ] Screen closes properly
- [ ] No crashes or errors in logs

#### Phase 2 Testing
- [ ] Scrollbar appears when waystone list exceeds viewport
- [ ] Mouse wheel scrolling works
- [ ] Drag scrollbar with mouse works
- [ ] Viewport clipping works (no waystones render outside bounds)
- [ ] Scrollbar auto-hides when not needed

#### Phase 3 Testing
- [ ] Search box accepts text input
- [ ] Filtering works in real-time
- [ ] Right-click clears search
- [ ] Filtered list displays correctly in scrollable container
- [ ] Search preserves after scroll

#### Phase 4 Testing
- [ ] Sort button cycles through all modes
- [ ] Each sort mode orders correctly:
  - [ ] Alphabetical (A-Z)
  - [ ] Distance (nearest first)
  - [ ] Recent (most recent first)
  - [ ] Dimension (grouped by dimension)
- [ ] Sort persists with search filtering

#### Phase 5 Testing
- [ ] Can drag waystone buttons
- [ ] Visual feedback during drag (transparency/offset)
- [ ] Drop swaps waystones
- [ ] Custom order persists after closing screen
- [ ] Custom order loads correctly on reopen
- [ ] Drag-drop works with scrolling
- [ ] Drag-drop works with search filtering

#### Phase 6 Testing
- [ ] Tooltips appear when CTRL+hover
- [ ] Tooltips hide when CTRL released
- [ ] Tooltips show correct information:
  - [ ] Waystone name
  - [ ] Dimension
  - [ ] Coordinates
  - [ ] Distance
  - [ ] Global status
  - [ ] Last used time
- [ ] Tooltips don't interfere with clicking

### Integration Testing
- [ ] All features work together simultaneously
- [ ] No performance issues with large waystone lists (100+ waystones)
- [ ] No memory leaks
- [ ] Compatible with existing redirect buttons
- [ ] No conflicts with Waystones mod updates

---

## Risk Assessment & Mitigation

### High Risk Areas

#### 1. Waystones API Access
**Risk:** Can't access waystone data from WaystoneSelectionScreen  
**Mitigation:** 
- Research Waystones 1.20.1 API documentation
- Use reflection as fallback
- Hook network packets if needed
- Worst case: Manual data collection via player interaction

#### 2. API Breaking Changes
**Risk:** 1.20.1 GUI API incompatible with 1.18.2 patterns  
**Mitigation:**
- Study 1.20.1 Forge documentation thoroughly
- Test each widget independently before integration
- Keep Better Waystones Menu as reference only, not copy-paste

#### 3. Performance with Large Lists
**Risk:** 1000+ waystones cause lag  
**Mitigation:**
- Implement virtual scrolling (only render visible waystones)
- Lazy loading for tooltip data
- Caching for distance calculations
- Profile with 1000+ test waystones

#### 4. Persistence System
**Risk:** Custom order not saving or loading correctly  
**Mitigation:**
- Use proven serialization (JSON or NBT)
- Validate on save and load
- Handle corruption gracefully (reset to default order)
- Add debug logging

---

## Development Workflow

### Incremental Development Process

1. **Start Phase 1**
   - Create branch: `feature/enhanced-waystone-screen`
   - Implement foundation
   - Test thoroughly
   - Commit: "Phase 1: Basic custom screen replacement"

2. **Continue with Phase 2-6**
   - One phase at a time
   - Test each phase independently
   - Commit after each phase completion
   - Don't proceed to next phase until current is working

3. **Integration Phase**
   - Test all features together
   - Fix conflicts
   - Performance optimization
   - Commit: "Integration: All features working together"

4. **Polish Phase**
   - Code cleanup
   - Add comments
   - Update README
   - Create user documentation
   - Commit: "Polish: Documentation and cleanup"

5. **Release**
   - Tag version (e.g., `v4.0.0` - major version bump for big feature)
   - GitHub Actions builds automatically
   - Test release build
   - Create GitHub release with feature list

### Git Workflow
```bash
# Start new feature
git checkout -b feature/enhanced-waystone-screen
git add -A
git commit -m "Phase 1: Basic custom screen replacement"
git push origin feature/enhanced-waystone-screen

# After each phase
git add -A
git commit -m "Phase 2: Scrollbar implementation"
git push

# Final merge
git checkout main
git merge feature/enhanced-waystone-screen
git push
git tag v4.0.0
git push origin v4.0.0
```

---

## Continuation Checkpoints

### Resume from Any Phase

Each phase is independent enough to pause and resume:

**To Resume Phase 1:**
- Read "PHASE 1: Foundation" section
- Create `EnhancedWaystoneSelectionScreen.java`
- Modify `ClientEvents.java` for screen interception

**To Resume Phase 2:**
- Ensure Phase 1 is complete and working
- Read "PHASE 2: Scrollbar Implementation"
- Create `ScrollableWaystoneList.java`

**To Resume Phase 3:**
- Ensure Phases 1-2 complete
- Read "PHASE 3: Search Box"
- Create `WaystoneSearchField.java`

**To Resume Phase 4:**
- Ensure Phases 1-3 complete
- Read "PHASE 4: Sorting Modes"
- Create `SortingMode.java`

**To Resume Phase 5:**
- Ensure Phases 1-4 complete
- Read "PHASE 5: Drag-and-Drop"
- Create `DraggableWaystoneButton.java`

**To Resume Phase 6:**
- Ensure Phases 1-5 complete
- Read "PHASE 6: CTRL+Hover Tooltips"
- Create `WaystoneTooltipRenderer.java`

### Progress Tracking

Mark phases as complete:
- [ ] Phase 1: Foundation
- [ ] Phase 2: Scrollbar
- [ ] Phase 3: Search Box
- [ ] Phase 4: Sorting Modes
- [ ] Phase 5: Drag-and-Drop
- [ ] Phase 6: CTRL+Hover Tooltips
- [ ] Integration Testing
- [ ] Polish & Release

---

## Research Tasks (Before Starting)

### Must Research Before Phase 1:
1. **Waystones API:**
   - How to get waystone list from player context
   - Waystone data structure (name, location, dimension, etc.)
   - Teleport method signature
   - Network packet structure (if needed)

2. **1.20.1 Screen API:**
   - Screen lifecycle methods
   - Widget registration system
   - Event handling (mouse, keyboard)
   - Rendering order (background, widgets, tooltips)

### Research Resources:
- Waystones mod source: https://github.com/blay09/Waystones
- Forge 1.20.1 docs: https://docs.minecraftforge.net/
- Better Waystones Menu reference: https://github.com/Loxoz/BetterWaystonesMenu

---

## Success Criteria

### Minimum Viable Product (MVP)
- ✅ Custom screen opens instead of default
- ✅ All waystones display correctly
- ✅ Scrollbar for long lists
- ✅ Search box with filtering
- ✅ At least 2 sort modes working

### Full Feature Set
- ✅ All 5 features implemented
- ✅ All features work together
- ✅ No crashes or major bugs
- ✅ Performance acceptable with 100+ waystones
- ✅ Custom order persists correctly

### Polish Goals
- ✅ Clean, commented code
- ✅ User documentation
- ✅ Config options for enabling/disabling features
- ✅ Keyboard shortcuts (ESC to close, Tab to search, etc.)
- ✅ Accessibility features (screen reader support)

---

## Configuration Options (Future)

Add to `WaystoneConfig.java`:

```toml
[enhancedGui]
    # Enable enhanced waystone menu (disable to use original)
    enabled = true
    
    # Enable scrollbar
    enableScrollbar = true
    
    # Enable search box
    enableSearch = true
    
    # Enable sorting modes
    enableSorting = true
    
    # Enable drag-and-drop reordering
    enableDragDrop = true
    
    # Enable CTRL+hover tooltips
    enableDetailedTooltips = true
    
    # Default sort mode (alphabetical, distance, recent, dimension)
    defaultSortMode = "alphabetical"
    
    # Max waystones per page (before scrolling needed)
    maxVisibleWaystones = 10
```

---

## Notes & Observations

### Design Decisions Log

**Decision 1:** Complete screen replacement vs Mixin  
**Reasoning:** More maintainable, less fragile, full control  
**Date:** Dec 4, 2025

**Decision 2:** Phases approach  
**Reasoning:** Incremental development reduces risk, easier to test and debug  
**Date:** Dec 4, 2025

### Known Limitations

1. **Server-side mod not required** - All features client-side only
2. **Custom order is per-client** - Each player has their own waystone order
3. **Distance calculations** - May be approximate if waystone in different dimension
4. **Performance** - Very large waystone lists (1000+) may need optimization

### Future Enhancements (Post-MVP)

- Waystone favorites/pinning system
- Color coding by dimension
- Waystone grouping/categories
- Custom waystone icons
- Keyboard navigation (arrow keys, Enter to select)
- Multi-column layout for wide screens
- Compact/expanded view toggle
- Export/import waystone order
- Shareable waystone lists between players

---

## Contact & Support

**Project Repository:** https://github.com/smokydastona/WaystoneButtonInjector  
**Original Mod (1.18.2):** https://github.com/Loxoz/BetterWaystonesMenu  
**Waystones Mod:** https://github.com/blay09/Waystones

---

**Last Updated:** December 4, 2025  
**Plan Version:** 1.0  
**Status:** Ready to begin Phase 1

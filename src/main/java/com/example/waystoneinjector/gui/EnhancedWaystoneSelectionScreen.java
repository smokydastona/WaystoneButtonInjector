package com.example.waystoneinjector.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreenBase;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Enhanced waystone selection screen that extends the base Waystones screen.
 * Adds scrollable list functionality and custom overlays while maintaining API compatibility.
 * Dirt background is prevented via MixinScreen.
 */
public class EnhancedWaystoneSelectionScreen extends WaystoneSelectionScreenBase {
    
    // Performance constants (DashLoader-inspired optimization)
    private static final int ANIMATION_FRAME_COUNT = 26;
    private static final long ANIMATION_FRAME_TIME_MS = 100L; // 100ms per frame
    
    // Mystical portal animation textures (26 frames)
    private static final ResourceLocation[] MYSTICAL_PORTALS = new ResourceLocation[ANIMATION_FRAME_COUNT];
    
    static {
        // Pre-load all animation frame ResourceLocations (cached for performance)
        for (int i = 0; i < ANIMATION_FRAME_COUNT; i++) {
            MYSTICAL_PORTALS[i] = new ResourceLocation("waystoneinjector", "textures/gui/mystical/mystic_" + (i + 1) + ".png");
        }
    }
    
    private long animationStartTime;
    private int cachedAnimationFrame = 0; // Cached to reduce recalculation (performance optimization)
    private ScrollableWaystoneList scrollableList;
    private boolean useScrollableList = true; // Toggle between pagination and scrollable
    
    // Cached reflection objects (Fastload-inspired optimization - reuse instead of recreating)
    private static java.lang.reflect.Field waystoneFieldCache = null;
    private static java.lang.reflect.Method xpCostMethodCache = null;
    
    // Debug logging flag (FastAsyncWorldSave-inspired: prevent I/O blocking)
    private static final boolean DEBUG_LOGGING = false; // Set to true for development
    
    public EnhancedWaystoneSelectionScreen(WaystoneSelectionMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.animationStartTime = System.currentTimeMillis();
        if (DEBUG_LOGGING) {
            System.out.println("[WaystoneInjector] EnhancedWaystoneSelectionScreen initialized");
        }
    }
    
    @Override
    public void renderBackground(@Nonnull GuiGraphics guiGraphics) {
        // No background - fully transparent (prevents dirt texture)
        // World will be visible behind the GUI
    }
    
    @Override
    public void removed() {
        // CRITICAL: Prevent memory leak (MemoryLeakFix-inspired MC-101260)
        // Clear references to prevent screen from keeping world loaded
        if (scrollableList != null) {
            scrollableList.cleanup(); // Clean up button references
            this.removeWidget(scrollableList);
            scrollableList = null;
        }
        super.removed();
    }
    
    @Override
    public void init() {
        // Call super.init() first, then disable opacity
        super.init();
        // Disable the default screen opacity/blur
        this.clearWidgets(); // Clear any widgets added by super.init()
        
        // Get waystones list and xp cost from the menu via reflection (Fastload-inspired: cached reflection)
        try {
            var menuClass = this.menu.getClass();
            
            // Use cached reflection field or create and cache it (reduces overhead on reopening GUI)
            if (waystoneFieldCache == null) {
                waystoneFieldCache = menuClass.getDeclaredField("waystones");
                waystoneFieldCache.setAccessible(true);
            }
            
            @SuppressWarnings("unchecked")
            List<IWaystone> waystones = (List<IWaystone>) waystoneFieldCache.get(this.menu);
            
            // Early exit if no waystones (Fastload philosophy: skip unnecessary work)
            if (waystones.isEmpty()) {
                if (DEBUG_LOGGING) System.out.println("[WaystoneInjector] No waystones available");
                return;
            }
            
            // Get XP cost per waystone (default to 1 if can't find it)
            int xpCostPerWaystone = 1;
            try {
                if (xpCostMethodCache == null) {
                    xpCostMethodCache = menuClass.getDeclaredMethod("getCostForWaystone", IWaystone.class);
                    xpCostMethodCache.setAccessible(true);
                }
                xpCostPerWaystone = (int) xpCostMethodCache.invoke(this.menu, waystones.get(0));
            } catch (Exception ignored) {
                // Default to 1 if method not found
            }
            
            // Create scrollable list widget that fills most of the screen
            int listTop = this.topPos + 80; // Space for mystical portal
            int listBottom = this.height - 40; // Leave space at bottom
            int listHeight = listBottom - listTop;
            
            this.scrollableList = new ScrollableWaystoneList(
                this,
                this.minecraft,
                this.width,
                listHeight,
                listTop,
                listBottom,
                20, // Item height - standard button height
                waystones,
                xpCostPerWaystone
            );
            
            this.addRenderableWidget(scrollableList);
            
            if (DEBUG_LOGGING) {
                System.out.println("[WaystoneInjector] Created scrollable list: " + waystones.size() + " waystones");
            }
            
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to create scrollable list: " + e.getMessage());
            e.printStackTrace();
            // Fall back to default pagination
            useScrollableList = false;
            super.init();
        }
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (useScrollableList && scrollableList != null) {
            // ModernUI-inspired: Update smooth scroll before rendering
            scrollableList.renderWithSmoothScroll(guiGraphics, mouseX, mouseY, partialTicks);
            
            // Render mystical portal animation at top center
            renderMysticalPortal(guiGraphics);
            
            // Render title above portal
            int titleX = this.width / 2 - this.font.width(this.title) / 2;
            guiGraphics.drawString(this.font, this.title, titleX, this.topPos + 6, 0xFFFFFF, true);
            
            // Call super.render to handle widgets (including scrollable list)
            super.render(guiGraphics, mouseX, mouseY, partialTicks);
            
            // Render tooltips
            this.renderTooltip(guiGraphics, mouseX, mouseY);
            
        } else {
            // Fall back to default pagination rendering
            super.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Override to prevent base class from accessing null searchBox
        if (useScrollableList && scrollableList != null) {
            // Handle ESC key to close screen
            if (keyCode == 256) { // GLFW_KEY_ESCAPE
                this.onClose();
                return true;
            }
            return false;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Do NOT call super.renderBg — this prevents container backgrounds
        // Background is drawn in render() method instead
    }
    
    // Custom GUI textures for the enhanced menu
    private static final ResourceLocation PORTAL_FRAME = 
        new ResourceLocation("waystoneinjector", "textures/gui/portal_frame.png");
    
    /**
     * Renders the animated mystical portal texture with frame
     * Optimized with frame caching (DashLoader-inspired)
     */
    private void renderMysticalPortal(GuiGraphics guiGraphics) {
        // Calculate current animation frame (cached for performance)
        long elapsed = System.currentTimeMillis() - animationStartTime;
        cachedAnimationFrame = (int) ((elapsed / ANIMATION_FRAME_TIME_MS) % ANIMATION_FRAME_COUNT);
        
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        // Render portal at top center of screen
        int portalX = this.width / 2 - 32; // Center horizontally (64x64 portal)
        int portalY = this.topPos + 20;
        
        // Render portal frame behind the animation (80x80)
        int frameX = this.width / 2 - 40;
        int frameY = this.topPos + 12;
        guiGraphics.blit(PORTAL_FRAME, frameX, frameY, 0, 0, 80, 80, 80, 80);
        
        // Render animated portal on top (uses cached frame for performance)
        guiGraphics.blit(MYSTICAL_PORTALS[cachedAnimationFrame], portalX, portalY, 0, 0, 64, 64, 64, 64);
        RenderSystem.disableBlend();
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (useScrollableList && scrollableList != null) {
            return scrollableList.mouseScrolled(mouseX, mouseY, delta);
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (useScrollableList && scrollableList != null) {
            if (scrollableList.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (useScrollableList && scrollableList != null) {
            if (scrollableList.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    /**
     * Called when a waystone is selected from the scrollable list
     */
    public void onWaystoneSelected(IWaystone waystone) {
        super.onWaystoneSelected(waystone);
    }
    
    @Override
    protected boolean allowSorting() {
        return false; // Disable sorting in scrollable mode
    }
    
    @Override
    protected boolean allowDeletion() {
        return false; // Disable deletion in scrollable mode
    }
}

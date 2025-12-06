package com.example.waystoneinjector.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreenBase;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

/**
 * Enhanced waystone selection screen that extends the base Waystones screen.
 * Adds scrollable list functionality and custom overlays while maintaining API compatibility.
 */
public class EnhancedWaystoneSelectionScreen extends WaystoneSelectionScreenBase {
    
    // Mystical portal animation textures (26 frames)
    private static final ResourceLocation[] MYSTICAL_PORTALS = new ResourceLocation[26];
    
    static {
        for (int i = 0; i < 26; i++) {
            MYSTICAL_PORTALS[i] = new ResourceLocation("waystoneinjector", "textures/gui/mystical/mystic_" + (i + 1) + ".png");
        }
    }
    
    private long animationStartTime;
    private ScrollableWaystoneList scrollableList;
    private boolean useScrollableList = true; // Toggle between pagination and scrollable
    
    public EnhancedWaystoneSelectionScreen(WaystoneSelectionMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.animationStartTime = System.currentTimeMillis();
        System.out.println("========================================");
        System.out.println("[WaystoneInjector] ✓✓✓ EnhancedWaystoneSelectionScreen CONSTRUCTOR CALLED ✓✓✓");
        System.out.println("========================================");
    }
    
    @Override
    public void init() {
        System.out.println("[WaystoneInjector] ✓ EnhancedWaystoneSelectionScreen.init() called");
        // DON'T call super.init() - we're replacing the entire UI
        // Get waystones list and xp cost from the menu via reflection
        try {
            var menuClass = this.menu.getClass();
            var waystoneField = menuClass.getDeclaredField("waystones");
            waystoneField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<IWaystone> waystones = (List<IWaystone>) waystoneField.get(this.menu);
            
            // Get XP cost per waystone (default to 1 if can't find it)
            int xpCostPerWaystone = 1;
            try {
                var xpCostMethod = menuClass.getDeclaredMethod("getCostForWaystone", IWaystone.class);
                xpCostMethod.setAccessible(true);
                if (!waystones.isEmpty()) {
                    xpCostPerWaystone = (int) xpCostMethod.invoke(this.menu, waystones.get(0));
                }
            } catch (Exception ignored) {
                System.out.println("[WaystoneInjector] Using default XP cost: 1");
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
            
            System.out.println("[WaystoneInjector] Created scrollable list with " + waystones.size() + " waystones (XP cost: " + xpCostPerWaystone + ")");
            
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
        System.out.println("[WaystoneInjector] ✓ EnhancedWaystoneSelectionScreen.render() called - useScrollableList=" + useScrollableList);
        if (useScrollableList && scrollableList != null) {
            // Render background (dirt texture)
            this.renderBackground(guiGraphics);
            
            // Render the container GUI box
            this.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
            
            // Render the scrollable list (this has its own semi-transparent background)
            scrollableList.render(guiGraphics, mouseX, mouseY, partialTicks);
            
            // Render mystical portal animation at top center
            renderMysticalPortal(guiGraphics);
            
            // Render title above portal
            int titleX = this.width / 2 - this.font.width(this.title) / 2;
            guiGraphics.drawString(this.font, this.title, titleX, this.topPos + 6, 0xFFFFFF, true);
            
            // Render labels and tooltips
            this.renderLabels(guiGraphics, mouseX, mouseY);
            
        } else {
            // Fall back to default pagination rendering
            super.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Draw standard container GUI box
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        
        // Use the standard container GUI rendering
        // This draws the gray container box background
        guiGraphics.blit(GUI_CONTAINER_LOCATION, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    private static final net.minecraft.resources.ResourceLocation GUI_CONTAINER_LOCATION = 
        new net.minecraft.resources.ResourceLocation("textures/gui/container/generic_54.png");
    
    /**
     * Renders the animated mystical portal texture
     */
    private void renderMysticalPortal(GuiGraphics guiGraphics) {
        long elapsed = System.currentTimeMillis() - animationStartTime;
        int animationFrame = (int) ((elapsed / 100) % 26); // 100ms per frame, loop through 26 frames
        
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        // Render portal at top center of screen
        int portalX = this.width / 2 - 32; // Center horizontally
        int portalY = this.topPos + 20;
        
        guiGraphics.blit(MYSTICAL_PORTALS[animationFrame], portalX, portalY, 0, 0, 64, 64, 64, 64);
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
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Don't render the default background when using scrollable list
        if (!useScrollableList) {
            super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
        }
    }
}

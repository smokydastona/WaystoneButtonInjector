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
    }
    
    @Override
    public void init() {
        super.init();
        
        if (useScrollableList) {
            // Get waystones list from the menu via reflection
            try {
                var menuClass = this.menu.getClass();
                var waystoneField = menuClass.getDeclaredField("waystones");
                waystoneField.setAccessible(true);
                @SuppressWarnings("unchecked")
                List<IWaystone> waystones = (List<IWaystone>) waystoneField.get(this.menu);
                
                // Create scrollable list widget
                int listTop = this.topPos + 64;
                int listBottom = this.topPos + this.imageHeight - 25;
                int listHeight = listBottom - listTop;
                
                this.scrollableList = new ScrollableWaystoneList(
                    this,
                    this.minecraft,
                    this.width,
                    listHeight,
                    listTop,
                    listBottom,
                    40, // Item height (increased for better visibility)
                    waystones
                );
                
                this.addRenderableWidget(scrollableList);
                
            } catch (Exception e) {
                System.err.println("[WaystoneInjector] Failed to create scrollable list: " + e.getMessage());
                e.printStackTrace();
                useScrollableList = false; // Fall back to pagination
            }
        }
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (useScrollableList) {
            // Custom rendering with scrollable list
            this.renderBackground(guiGraphics);
            
            // Render mystical portal animation
            renderMysticalPortal(guiGraphics);
            
            // Render the scrollable list
            if (scrollableList != null) {
                scrollableList.render(guiGraphics, mouseX, mouseY, partialTicks);
            }
            
            // Render title
            guiGraphics.drawString(this.font, this.title, this.leftPos + 8, this.topPos + 6, 0x404040, false);
            
        } else {
            // Fall back to default pagination rendering
            super.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
    }
    
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
        int portalX = this.leftPos + (this.imageWidth - 64) / 2;
        int portalY = this.topPos + 10;
        
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
        return !useScrollableList && super.allowSorting();
    }
    
    @Override
    protected boolean allowDeletion() {
        return !useScrollableList && super.allowDeletion();
    }
}

package com.example.waystoneinjector.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.blay09.mods.waystones.network.message.SelectWaystoneMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

/**
 * Custom waystone selection screen with scrollable list instead of pagination.
 * Based on BetterWaystonesMenu design but for 1.20.1.
 */
public class GuiWaystoneSelectionScreen extends AbstractContainerScreen<WaystoneSelectionMenu> {
    
    private static final ResourceLocation MYSTICAL_PORTALS[] = new ResourceLocation[26];
    static {
        for (int i = 0; i < 26; i++) {
            MYSTICAL_PORTALS[i] = new ResourceLocation("waystoneinjector", "textures/gui/mystical/mystic_" + (i + 1) + ".png");
        }
    }
    
    private ScrollableWaystoneList waystoneList;
    private List<IWaystone> waystones;
    private int animationFrame = 0;
    private long lastFrameTime = 0;
    
    public GuiWaystoneSelectionScreen(WaystoneSelectionMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 270;
        this.imageHeight = 200;
        
        // Get waystones from menu
        try {
            var menuClass = menu.getClass().getSuperclass(); // WaystoneSelectionMenuBase
            var field = menuClass.getDeclaredField("waystones");
            field.setAccessible(true);
            this.waystones = (List<IWaystone>) field.get(menu);
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to get waystones from menu: " + e.getMessage());
            this.waystones = List.of();
        }
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Create scrollable waystone list
        int listTop = this.topPos + 40;
        int listBottom = this.topPos + this.imageHeight - 25;
        
        this.waystoneList = new ScrollableWaystoneList(
            this,
            this.minecraft,
            this.width,
            this.height,
            listTop,
            listBottom,
            40, // Item height (36 for texture + 4 padding)
            this.waystones
        );
        
        this.addWidget(this.waystoneList);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        
        // Render mystical portal animation
        renderMysticalPortal(guiGraphics);
        
        // Render scrollable list
        this.waystoneList.render(guiGraphics, mouseX, mouseY, partialTicks);
        
        // Render title
        guiGraphics.drawString(this.font, this.title, this.leftPos + 8, this.topPos + 6, 0x404040, false);
        
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
    
    private void renderMysticalPortal(GuiGraphics guiGraphics) {
        // Update animation frame every 100ms
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime > 100) {
            animationFrame = (animationFrame + 1) % 26;
            lastFrameTime = currentTime;
        }
        
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        // Render mystical portal at center of screen
        int portalX = this.leftPos + (this.imageWidth - 64) / 2;
        int portalY = this.topPos + 10;
        
        guiGraphics.blit(MYSTICAL_PORTALS[animationFrame], portalX, portalY, 0, 0, 64, 64, 64, 64);
        
        RenderSystem.disableBlend();
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Background is rendered by renderBackground()
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return this.waystoneList.mouseScrolled(mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.waystoneList.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.waystoneList.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
    
    public void onWaystoneSelected(IWaystone waystone) {
        Balm.getNetworking().sendToServer(new SelectWaystoneMessage(waystone.getWaystoneUid()));
        this.onClose();
    }
}

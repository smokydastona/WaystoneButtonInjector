package com.example.waystoneinjector.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Enhanced waystone selection screen with scrollable list and animated portal.
 * Features transparent background to show the world behind the GUI.
 */
@SuppressWarnings("null")
public class EnhancedWaystoneSelectionScreen extends AbstractContainerScreen<WaystoneSelectionMenu> {
    
    // Animation constants
    private static final int ANIMATION_FRAME_COUNT = 26;
    private static final long ANIMATION_FRAME_TIME_MS = 100L;
    
    // Lazy-loaded animation textures
    private static final ResourceLocation[] MYSTICAL_PORTALS = new ResourceLocation[ANIMATION_FRAME_COUNT];
    private static volatile boolean animationTexturesLoaded = false;
    
    static {
        // Background thread to load animation frames
        Thread animationLoader = new Thread(() -> {
            try {
                for (int i = 0; i < ANIMATION_FRAME_COUNT; i++) {
                    MYSTICAL_PORTALS[i] = new ResourceLocation("waystoneinjector", "textures/gui/animations/portal/mystic_" + (i + 1) + ".png");
                    if (i % 5 == 0 && i > 0) {
                        Thread.sleep(10);
                    }
                }
                animationTexturesLoaded = true;
            } catch (Exception e) {
                System.err.println("[WaystoneInjector] Failed to load animation textures: " + e.getMessage());
            }
        }, "WaystoneAnimationLoader");
        animationLoader.setDaemon(true);
        animationLoader.setPriority(Thread.MIN_PRIORITY);
        animationLoader.start();
    }
    
    private long animationStartTime;
    private int cachedAnimationFrame = 0;
    private WaystoneListWidget waystoneList;
    
    // Cached reflection objects
    private static Field waystoneFieldCache = null;
    private static Method selectWaystoneMethodCache = null;
    
    public EnhancedWaystoneSelectionScreen(WaystoneSelectionMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.animationStartTime = System.currentTimeMillis();
        this.imageWidth = 256;
        this.imageHeight = 256;
    }
    
    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // No background - fully transparent
    }
    
    @Override
    public void removed() {
        if (waystoneList != null) {
            this.removeWidget(waystoneList);
            waystoneList = null;
        }
        super.removed();
    }
    
    @Override
    protected void init() {
        super.init();
        
        try {
            // Get waystones from menu via reflection
            if (waystoneFieldCache == null) {
                waystoneFieldCache = this.menu.getClass().getDeclaredField("waystones");
                waystoneFieldCache.setAccessible(true);
            }
            
            @SuppressWarnings("unchecked")
            List<IWaystone> waystones = (List<IWaystone>) waystoneFieldCache.get(this.menu);
            
            if (waystones.isEmpty()) {
                return;
            }
            
            // Create scrollable waystone list
            int listTop = this.topPos + 80; // Space for portal animation
            int listBottom = this.height - 40;
            int listWidth = Math.min(300, this.width - 40);
            
            waystoneList = new WaystoneListWidget(
                this.minecraft,
                listWidth,
                listBottom - listTop,
                listTop,
                listBottom,
                22, // Item height
                waystones,
                this::selectWaystone
            );
            
            this.addRenderableWidget(waystoneList);
            
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to initialize waystone list: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Render animated portal at top center
        renderMysticalPortal(guiGraphics);
        
        // Render title above portal
        int titleX = this.width / 2 - this.font.width(this.title) / 2;
        guiGraphics.drawString(this.font, this.title, titleX, this.topPos + 6, 0xFFFFFF, true);
        
        // Render widgets (scrollable list)
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
    
    private void renderMysticalPortal(GuiGraphics guiGraphics) {
        if (!animationTexturesLoaded) {
            return;
        }
        
        // Calculate current frame
        long elapsedTime = System.currentTimeMillis() - animationStartTime;
        int frameIndex = (int) ((elapsedTime / ANIMATION_FRAME_TIME_MS) % ANIMATION_FRAME_COUNT);
        
        if (frameIndex != cachedAnimationFrame) {
            cachedAnimationFrame = frameIndex;
        }
        
        // Render portal centered at top
        int portalWidth = 128;
        int portalHeight = 128;
        int portalX = (this.width - portalWidth) / 2;
        int portalY = this.topPos + 20;
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        guiGraphics.blit(
            MYSTICAL_PORTALS[cachedAnimationFrame],
            portalX, portalY,
            0, 0,
            portalWidth, portalHeight,
            portalWidth, portalHeight
        );
        
        RenderSystem.disableBlend();
    }
    
    private void selectWaystone(IWaystone waystone) {
        try {
            if (selectWaystoneMethodCache == null) {
                selectWaystoneMethodCache = this.menu.getClass().getDeclaredMethod("selectWaystone", IWaystone.class);
                selectWaystoneMethodCache.setAccessible(true);
            }
            selectWaystoneMethodCache.invoke(this.menu, waystone);
            this.onClose();
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to select waystone: " + e.getMessage());
        }
    }
    
    /**
     * Scrollable list widget for displaying waystones
     */
    private static class WaystoneListWidget extends ObjectSelectionList<WaystoneListWidget.Entry> {
        
        public WaystoneListWidget(net.minecraft.client.Minecraft minecraft, int width, int height, 
                                   int y0, int y1, int itemHeight, List<IWaystone> waystones,
                                   java.util.function.Consumer<IWaystone> onSelect) {
            super(minecraft, width, height, y0, y1, itemHeight);
            
            // Add entries for each waystone
            for (IWaystone waystone : waystones) {
                this.addEntry(new Entry(waystone, onSelect, width - 8));
            }
        }
        
        @Override
        public int getRowWidth() {
            return this.width - 8;
        }
        
        @Override
        protected int getScrollbarPosition() {
            return this.x0 + this.width - 6;
        }
        
        @Override
        protected void renderBackground(@Nonnull GuiGraphics guiGraphics) {
            // Semi-transparent dark background
            guiGraphics.fill(this.x0, this.y0, 
                           this.x1, this.y1, 
                           0xAA000000);
        }
        
        /**
         * Individual waystone entry in the list
         */
        private static class Entry extends ObjectSelectionList.Entry<Entry> {
            private final IWaystone waystone;
            private final Button button;
            
            public Entry(IWaystone waystone, java.util.function.Consumer<IWaystone> onSelect, int width) {
                this.waystone = waystone;
                
                String displayName = waystone.getName();
                
                this.button = Button.builder(
                    Component.literal(displayName),
                    btn -> onSelect.accept(waystone)
                ).bounds(0, 0, width, 20).build();
            }
            
            @Override
            public void render(@Nonnull GuiGraphics guiGraphics, int index, int top, int left, 
                             int width, int height, int mouseX, int mouseY, 
                             boolean hovering, float partialTicks) {
                this.button.setX(left);
                this.button.setY(top);
                this.button.setWidth(width);
                this.button.render(guiGraphics, mouseX, mouseY, partialTicks);
            }
            
            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                return this.button.mouseClicked(mouseX, mouseY, button);
            }
            
            @Override
            public @Nonnull Component getNarration() {
                return this.button.getMessage();
            }
            
            public @Nonnull List<? extends net.minecraft.client.gui.components.events.GuiEventListener> children() {
                return java.util.Collections.singletonList(this.button);
            }
            
            public @Nonnull List<? extends net.minecraft.client.gui.narration.NarratableEntry> narratables() {
                return java.util.Collections.singletonList(this.button);
            }
        }
    }
}

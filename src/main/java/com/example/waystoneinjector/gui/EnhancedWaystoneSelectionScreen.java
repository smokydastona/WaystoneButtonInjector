package com.example.waystoneinjector.gui;

import com.example.waystoneinjector.config.DevConfig;
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
        
        // Load/reload dev config
        DevConfig.checkReload();
        
        System.out.println("[WaystoneInjector] Dev mode enabled: " + DevConfig.isEnabled());
        
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
            
            // Get settings from dev config or use defaults
            DevConfig.ScrollListSettings listSettings = DevConfig.getScrollList();
            
            int listWidth = DevConfig.isEnabled() ? listSettings.width : 250;
            int listTop = DevConfig.isEnabled() ? listSettings.topMargin : (this.topPos + 60);
            int listBottom = DevConfig.isEnabled() ? (this.height - listSettings.bottomMargin) : (this.topPos + 210);
            int itemHeight = DevConfig.isEnabled() ? listSettings.itemHeight : 22;
            
            System.out.println("[WaystoneInjector] List dimensions: " + listWidth + "x" + (listBottom - listTop) + " at y=" + listTop);
            System.out.println("[WaystoneInjector] Screen dimensions: " + this.width + "x" + this.height);
            
            waystoneList = new WaystoneListWidget(
                this.minecraft,
                listWidth,
                listBottom - listTop,
                listTop,
                listBottom,
                itemHeight,
                waystones,
                this::selectWaystone,
                listSettings
            );
            
            this.addRenderableWidget(waystoneList);
            
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to initialize waystone list: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        DevConfig.checkReload();
        
        // Render elements in order based on render order settings
        if (DevConfig.isEnabled()) {
            renderInDevMode(guiGraphics, mouseX, mouseY, partialTicks);
        } else {
            renderInNormalMode(guiGraphics, mouseX, mouseY, partialTicks);
        }
    }
    
    private void renderInNormalMode(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Render animated portal at top center
        renderMysticalPortal(guiGraphics);
        
        // Render title above portal
        int titleX = this.width / 2 - this.font.width(this.title) / 2;
        guiGraphics.drawString(this.font, this.title, titleX, this.topPos + 6, 0xFFFFFF, true);
        
        // Render widgets (scrollable list)
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
    
    private void renderInDevMode(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Render portal
        int[] portalBounds = renderMysticalPortal(guiGraphics);
        
        // Render title
        int titleX = this.width / 2 - this.font.width(this.title) / 2;
        guiGraphics.drawString(this.font, this.title, titleX, this.topPos + 6, 0xFFFFFF, true);
        
        // Render widgets
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        
        // Render debug overlay on top
        if (waystoneList != null) {
            DebugOverlay.render(guiGraphics, this.width, this.height,
                waystoneList.getX0(), waystoneList.getY0(), 
                waystoneList.getListWidth(), waystoneList.getListHeight(),
                portalBounds[0], portalBounds[1], portalBounds[2], portalBounds[3]);
        }
    }
    
    private int[] renderMysticalPortal(GuiGraphics guiGraphics) {
        if (!animationTexturesLoaded) {
            return new int[]{0, 0, 0, 0};
        }
        
        // Get animation speed from dev config
        long frameTimeMs = DevConfig.isEnabled() ? 
            DevConfig.getPortal().animationSpeed : 100L;
        
        // Calculate current frame
        long elapsedTime = System.currentTimeMillis() - animationStartTime;
        int frameIndex = (int) ((elapsedTime / frameTimeMs) % ANIMATION_FRAME_COUNT);
        
        if (frameIndex != cachedAnimationFrame) {
            cachedAnimationFrame = frameIndex;
        }
        
        // Get portal settings
        DevConfig.PortalSettings portalSettings = DevConfig.getPortal();
        int portalWidth = DevConfig.isEnabled() ? portalSettings.width : 128;
        int portalHeight = DevConfig.isEnabled() ? portalSettings.height : 128;
        
        int portalX, portalY;
        if (DevConfig.isEnabled() && portalSettings.centered) {
            portalX = (this.width - portalWidth) / 2 + portalSettings.xOffset;
            portalY = (this.height - portalHeight) / 2 + portalSettings.yOffset;
        } else if (DevConfig.isEnabled()) {
            portalX = portalSettings.xOffset;
            portalY = portalSettings.yOffset;
        } else {
            portalX = (this.width - portalWidth) / 2;
            portalY = this.topPos + 20;
        }
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        ResourceLocation texture = DevConfig.isEnabled() ? 
            DevConfig.getPortalTexture(cachedAnimationFrame) : 
            MYSTICAL_PORTALS[cachedAnimationFrame];
        
        guiGraphics.blit(
            texture,
            portalX, portalY,
            0, 0,
            portalWidth, portalHeight,
            portalWidth, portalHeight
        );
        
        RenderSystem.disableBlend();
        
        return new int[]{portalX, portalY, portalWidth, portalHeight};
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
        private int xPos;
        
        public WaystoneListWidget(net.minecraft.client.Minecraft minecraft, int width, int height, 
                                   int y0, int y1, int itemHeight, List<IWaystone> waystones,
                                   java.util.function.Consumer<IWaystone> onSelect,
                                   DevConfig.ScrollListSettings settings) {
            super(minecraft, width, height, y0, y1, itemHeight);
            
            // Center horizontally if enabled
            if (DevConfig.isEnabled() && settings.centered) {
                this.xPos = (minecraft.getWindow().getGuiScaledWidth() - width) / 2 + settings.xOffset;
            } else if (DevConfig.isEnabled()) {
                this.xPos = settings.xOffset;
            } else {
                this.xPos = (minecraft.getWindow().getGuiScaledWidth() - width) / 2;
            }
            
            // Add entries for each waystone
            for (IWaystone waystone : waystones) {
                this.addEntry(new Entry(waystone, onSelect, width - 8));
            }
        }
        
        public int getX0() { return this.xPos; }
        public int getY0() { return this.y0; }
        public int getListWidth() { return this.width; }
        public int getListHeight() { return this.height; }
        
        @Override
        public int getRowLeft() {
            return this.xPos + 4;
        }
        
        @Override
        public int getRowWidth() {
            return this.width - 8;
        }
        
        @Override
        protected int getScrollbarPosition() {
            return this.xPos + this.width - 6;
        }
        
        @Override
        protected void renderBackground(@Nonnull GuiGraphics guiGraphics) {
            // Semi-transparent dark background
            guiGraphics.fill(this.xPos, this.y0, 
                           this.xPos + this.width, this.y1, 
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

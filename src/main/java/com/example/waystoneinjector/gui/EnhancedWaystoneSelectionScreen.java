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
    private static Field fromWaystoneFieldCache = null;
    private static Method selectWaystoneMethodCache = null;
    
    public EnhancedWaystoneSelectionScreen(WaystoneSelectionMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.animationStartTime = System.currentTimeMillis();
        this.imageWidth = 256;
        this.imageHeight = 256;
        
        // Detect waystone variant from the clicked block
        detectWaystoneVariant();
    }
    
    /**
     * Detect which waystone variant was clicked and set the appropriate profile
     */
    private void detectWaystoneVariant() {
        try {
            // Get the fromWaystone from the menu
            if (fromWaystoneFieldCache == null) {
                fromWaystoneFieldCache = this.menu.getClass().getDeclaredField("fromWaystone");
                fromWaystoneFieldCache.setAccessible(true);
            }
            
            Object fromWaystone = fromWaystoneFieldCache.get(this.menu);
            if (fromWaystone == null || this.minecraft == null || this.minecraft.level == null) {
                DevConfig.setCurrentVariant("regular");  // Default
                return;
            }
            
            // Get the block position
            Method getPosMethod = fromWaystone.getClass().getMethod("getPos");
            Object posObj = getPosMethod.invoke(fromWaystone);
            if (!(posObj instanceof net.minecraft.core.BlockPos)) {
                DevConfig.setCurrentVariant("regular");
                return;
            }
            
            net.minecraft.core.BlockPos pos = (net.minecraft.core.BlockPos) posObj;
            net.minecraft.world.level.block.state.BlockState state = this.minecraft.level.getBlockState(pos);
            net.minecraft.world.level.block.Block block = state.getBlock();
            
            // Get the block's registry name
            net.minecraft.resources.ResourceLocation blockId = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(block);
            
            if (blockId == null) {
                DevConfig.setCurrentVariant("regular");
                return;
            }
            
            // Map block ID to variant name
            String path = blockId.getPath();
            String variant = switch (path) {
                case "waystone" -> "regular";
                case "mossy_waystone" -> "mossy";
                case "sandy_waystone" -> "sandy";
                case "blackstone_waystone" -> "blackstone";
                case "deepslate_waystone" -> "deepslate";
                case "end_stone_waystone" -> "endstone";
                default -> "regular";
            };
            
            DevConfig.setCurrentVariant(variant);
            System.out.println("[WaystoneInjector] Detected waystone variant: " + variant + " (block: " + blockId + ")");
            
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to detect waystone variant: " + e.getMessage());
            e.printStackTrace();
            DevConfig.setCurrentVariant("regular");  // Fallback
        }
    }
    
    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Get background settings from dev config
        DevConfig.BackgroundSettings bgSettings = DevConfig.getBackground();
        
        int bgWidth = DevConfig.isEnabled() && DevConfig.getScrollList().centered ? 
            DevConfig.getScrollList().width : 250;
        int bgHeight = DevConfig.isEnabled() ? 
            DevConfig.getScrollList().height : 150;
        int bgX = (this.width - bgWidth) / 2;
        int bgY = (this.height - bgHeight) / 2;
        
        if (DevConfig.isEnabled()) {
            bgX += DevConfig.getScrollList().xOffset;
            bgY += DevConfig.getScrollList().yOffset;
        }
        
        // Render background based on config
        if (DevConfig.isEnabled() && bgSettings.renderMenuBackground) {
            int alpha = bgSettings.menuBackgroundAlpha;
            int color = (alpha << 24) | (bgSettings.backgroundColor & 0x00FFFFFF);
            guiGraphics.fill(bgX, bgY, bgX + bgWidth, bgY + bgHeight, color);
        } else if (!DevConfig.isEnabled()) {
            // Default: semi-transparent dark background
            guiGraphics.fill(bgX, bgY, bgX + bgWidth, bgY + bgHeight, 0xAA000000);
        }
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
            
            int listWidth = DevConfig.isEnabled() ? listSettings.width : Math.min(300, this.width - 40);
            int listTop = DevConfig.isEnabled() ? (listSettings.topMargin + listSettings.yOffset) : (this.topPos + 80);
            int listBottom = DevConfig.isEnabled() ? (this.height - listSettings.bottomMargin + listSettings.yOffset) : (this.height - 40);
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
        // Render portal (behind everything)
        int[] portalBounds = renderMysticalPortal(guiGraphics);
        
        // Render title
        DevConfig.TextSettings textSettings = DevConfig.getText();
        if (textSettings.showTitle) {
            int titleX = (this.width / 2 - this.font.width(this.title) / 2) + textSettings.titleX;
            int titleY = this.topPos + textSettings.titleY;
            guiGraphics.drawString(this.font, this.title, titleX, titleY, textSettings.titleColor, textSettings.titleShadow);
        }
        
        // Render widgets (scrollable list)
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        
        // Render waystone variant textures ON TOP of list to cover edges
        renderWaystoneVariants(guiGraphics);
        
        // Render debug overlay on top
        if (waystoneList != null) {
            DebugOverlay.render(guiGraphics, this.width, this.height,
                waystoneList.getX0(), waystoneList.getY0(), 
                waystoneList.getListWidth(), waystoneList.getListHeight(),
                portalBounds[0], portalBounds[1], portalBounds[2], portalBounds[3]);
        }
    }
    
    private void renderWaystoneVariants(GuiGraphics guiGraphics) {
        if (!DevConfig.isEnabled()) return;
        
        DevConfig.WaystoneVariantSettings variant = DevConfig.getWaystoneVariant();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        // Render waystone variant (centered)
        if (variant.showVariantTexture) {
            ResourceLocation texture = DevConfig.getWaystoneVariantTexture();
            if (texture != null) {
                int x = centerX - (variant.variantWidth / 2) + variant.variantX;
                int y = centerY - (variant.variantHeight / 2) + variant.variantY;
                guiGraphics.blit(texture, x, y, 0, 0, variant.variantWidth, variant.variantHeight, variant.variantWidth, variant.variantHeight);
            }
        }
        
        // Render sharestone (centered)
        if (variant.showSharestoneTexture) {
            ResourceLocation texture = DevConfig.getSharestoneTexture();
            if (texture != null) {
                int x = centerX - (variant.sharestoneWidth / 2) + variant.sharestoneX;
                int y = centerY - (variant.sharestoneHeight / 2) + variant.sharestoneY;
                guiGraphics.blit(texture, x, y, 0, 0, variant.sharestoneWidth, variant.sharestoneHeight, variant.sharestoneWidth, variant.sharestoneHeight);
            }
        }
        
        // Render items
        int itemY = centerY + 100;
        int itemSpacing = 40;
        int currentX = centerX - (itemSpacing * 3);
        
        if (variant.showWarpScroll) {
            guiGraphics.blit(DevConfig.getItemTexture("warp_scroll"), currentX, itemY, 0, 0, 32, 32, 32, 32);
            currentX += itemSpacing;
        }
        if (variant.showBoundScroll) {
            guiGraphics.blit(DevConfig.getItemTexture("bound_scroll"), currentX, itemY, 0, 0, 32, 32, 32, 32);
            currentX += itemSpacing;
        }
        if (variant.showReturnScroll) {
            guiGraphics.blit(DevConfig.getItemTexture("return_scroll"), currentX, itemY, 0, 0, 32, 32, 32, 32);
            currentX += itemSpacing;
        }
        if (variant.showWarpStone) {
            guiGraphics.blit(DevConfig.getItemTexture("warp_stone"), currentX, itemY, 0, 0, 32, 32, 32, 32);
            currentX += itemSpacing;
        }
        if (variant.showPortstone) {
            guiGraphics.blit(DevConfig.getItemTexture("portstone"), currentX, itemY, 0, 0, 32, 32, 32, 32);
            currentX += itemSpacing;
        }
        if (variant.showWarpPlate) {
            guiGraphics.blit(DevConfig.getItemTexture("warp_plate"), currentX, itemY, 0, 0, 32, 32, 32, 32);
        }
        
        RenderSystem.disableBlend();
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
        private int listWidth;
        
        public WaystoneListWidget(net.minecraft.client.Minecraft minecraft, int width, int height, 
                                   int y0, int y1, int itemHeight, List<IWaystone> waystones,
                                   java.util.function.Consumer<IWaystone> onSelect,
                                   DevConfig.ScrollListSettings settings) {
            super(minecraft, width, height, y0, y1, itemHeight);
            
            this.listWidth = width;
            
            // Center horizontally if enabled
            if (DevConfig.isEnabled() && settings.centered) {
                this.xPos = (minecraft.getWindow().getGuiScaledWidth() - width) / 2 + settings.xOffset;
            } else if (DevConfig.isEnabled()) {
                this.xPos = settings.xOffset;
            } else {
                this.xPos = (minecraft.getWindow().getGuiScaledWidth() - width) / 2;
            }
            
            // CRITICAL: Update the list's actual X boundaries
            this.x0 = this.xPos;
            this.x1 = this.xPos + width;
            
            // Add entries for each waystone
            for (IWaystone waystone : waystones) {
                this.addEntry(new Entry(waystone, onSelect, width - 8));
            }
        }
        
        public int getX0() { return this.x0; }
        public int getY0() { return this.y0; }
        public int getListWidth() { return this.listWidth; }
        public int getListHeight() { return this.height; }
        
        @Override
        public int getRowLeft() {
            return this.x0 + 4;
        }
        
        @Override
        public int getRowWidth() {
            return this.width - 8;
        }
        
        @Override
        protected int getScrollbarPosition() {
            return this.x1 - 6;
        }
        
        @Override
        protected void renderBackground(@Nonnull GuiGraphics guiGraphics) {
            // No background here - handled by main screen
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

package com.example.waystoneinjector.gui;

import com.example.waystoneinjector.config.DevConfig;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Scrollable waystone selection screen that matches the original Waystones menu layout.
 * No custom textures - uses vanilla/Waystones rendering with a scrollable list.
 */
public class ScrollableWaystoneSelectionScreen extends AbstractContainerScreen<WaystoneSelectionMenu> {
    
    private List<IWaystone> waystones;
    private List<IWaystone> filteredWaystones;
    private WaystoneScrollList waystoneList;
    private EditBox searchBox;
    private String searchText = "";
    
    // Cached reflection objects
    private static Field waystoneFieldCache = null;
    private static Field fromWaystoneFieldCache = null;
    private static Method selectWaystoneMethodCache = null;
    
    // Layout constants matching original
    private static final int HEADER_HEIGHT = 64;
    private static final int FOOTER_HEIGHT = 25;
    private static final int ENTRY_HEIGHT = 22;
    private static final int SEARCH_BOX_HEIGHT = 24;
    
    public ScrollableWaystoneSelectionScreen(WaystoneSelectionMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        
        try {
            // Get waystones from menu via reflection
            if (waystoneFieldCache == null) {
                waystoneFieldCache = this.menu.getClass().getDeclaredField("waystones");
                waystoneFieldCache.setAccessible(true);
            }
            
            @SuppressWarnings("unchecked")
            List<IWaystone> waystoneList = (List<IWaystone>) waystoneFieldCache.get(this.menu);
            this.waystones = waystoneList;
            this.filteredWaystones = waystoneList.stream().collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to get waystones from menu: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Match original sizing
        this.imageWidth = 270;
        this.imageHeight = 200;
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Calculate dynamic height based on screen size (matching original)
        int maxContentHeight = (int) (this.height * 0.6f);
        int maxButtonsVisible = (maxContentHeight - HEADER_HEIGHT - FOOTER_HEIGHT) / ENTRY_HEIGHT;
        int buttonsVisible = Math.max(4, Math.min(maxButtonsVisible, this.waystones.size()));
        int contentHeight = HEADER_HEIGHT + buttonsVisible * ENTRY_HEIGHT + FOOTER_HEIGHT;
        
        this.imageWidth = this.width;
        this.imageHeight = contentHeight;
        
        // Recalculate positions after size change
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        
        // Search box at top (matching original position)
        this.searchBox = new EditBox(
            this.font,
            this.width / 2 - 99,
            this.topPos + HEADER_HEIGHT - SEARCH_BOX_HEIGHT,
            198,
            20,
            Component.empty()
        );
        this.searchBox.setResponder(text -> {
            this.searchText = text;
            updateFilteredList();
        });
        this.addRenderableWidget(this.searchBox);
        
        // Create scrollable list
        int listTop = this.topPos + HEADER_HEIGHT;
        int listBottom = this.topPos + HEADER_HEIGHT + buttonsVisible * ENTRY_HEIGHT;
        
        this.waystoneList = new WaystoneScrollList(
            this.minecraft,
            200,
            listBottom - listTop,
            listTop,
            listBottom,
            ENTRY_HEIGHT,
            this.filteredWaystones,
            this::selectWaystone
        );
        
        this.addRenderableWidget(this.waystoneList);
    }
    
    private void updateFilteredList() {
        this.filteredWaystones = this.waystones.stream()
            .filter(waystone -> waystone.getName().toLowerCase().contains(this.searchText.toLowerCase()))
            .collect(Collectors.toList());
        
        // Recreate list with filtered waystones
        if (this.waystoneList != null) {
            this.removeWidget(this.waystoneList);
            
            int listTop = this.topPos + HEADER_HEIGHT;
            int maxContentHeight = (int) (this.height * 0.6f);
            int maxButtonsVisible = (maxContentHeight - HEADER_HEIGHT - FOOTER_HEIGHT) / ENTRY_HEIGHT;
            int buttonsVisible = Math.max(4, Math.min(maxButtonsVisible, this.waystones.size()));
            int listBottom = this.topPos + HEADER_HEIGHT + buttonsVisible * ENTRY_HEIGHT;
            
            this.waystoneList = new WaystoneScrollList(
                this.minecraft,
                200,
                listBottom - listTop,
                listTop,
                listBottom,
                ENTRY_HEIGHT,
                this.filteredWaystones,
                this::selectWaystone
            );
            
            this.addRenderableWidget(this.waystoneList);
        }
    }
    
    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // No custom background - let parent handle it
    }
    
    @Override
    protected void renderLabels(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Render title centered
        try {
            if (fromWaystoneFieldCache == null) {
                fromWaystoneFieldCache = this.menu.getClass().getDeclaredField("fromWaystone");
                fromWaystoneFieldCache.setAccessible(true);
            }
            
            Object fromWaystone = fromWaystoneFieldCache.get(this.menu);
            int titleY = fromWaystone != null ? 20 : 0;
            
            guiGraphics.drawCenteredString(this.font, this.title, this.imageWidth / 2, titleY, 0xFFFFFF);
            
            // Draw "Current Location" header if fromWaystone exists (matching original)
            if (fromWaystone != null) {
                // This would need more reflection to get waystone name, etc.
                // For now, simplified version
            }
            
        } catch (Exception e) {
            // Fallback
            guiGraphics.drawCenteredString(this.font, this.title, this.imageWidth / 2, 0, 0xFFFFFF);
        }
        
        // Show "No waystones activated" message if empty
        if (this.waystones.isEmpty()) {
            guiGraphics.drawCenteredString(
                this.font,
                Component.literal("§cNo waystones activated"),
                this.imageWidth / 2,
                this.imageHeight / 2 - 20,
                0xFFFFFF
            );
        }
    }
    
    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Render default background (dirt pattern)
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        
        // Render everything else
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
    
    private void selectWaystone(IWaystone waystone) {
        try {
            if (selectWaystoneMethodCache == null) {
                selectWaystoneMethodCache = this.menu.getClass().getDeclaredMethod("selectWaystone", IWaystone.class);
                selectWaystoneMethodCache.setAccessible(true);
            }
            
            System.out.println("[WaystoneInjector] Selecting waystone: " + waystone.getName());
            selectWaystoneMethodCache.invoke(this.menu, waystone);
            this.onClose();
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to select waystone: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Scrollable list widget matching original waystone button layout
     */
    private static class WaystoneScrollList extends ObjectSelectionList<WaystoneScrollList.Entry> {
        
        public WaystoneScrollList(net.minecraft.client.Minecraft minecraft, int width, int height,
                                   int y0, int y1, int itemHeight, List<IWaystone> waystones,
                                   java.util.function.Consumer<IWaystone> onSelect) {
            super(minecraft, width, height, y0, y1, itemHeight);
            
            // Center the list
            int centerX = minecraft.getWindow().getGuiScaledWidth() / 2;
            this.x0 = centerX - width / 2;
            this.x1 = centerX + width / 2;
            
            // Add entries for each waystone
            for (IWaystone waystone : waystones) {
                this.addEntry(new Entry(waystone, onSelect, width - 8));
            }
        }
        
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
            // No custom background
        }
        
        /**
         * Individual waystone entry matching original button style
         */
        private static class Entry extends ObjectSelectionList.Entry<Entry> {
            private final IWaystone waystone;
            private final Button button;
            
            public Entry(IWaystone waystone, java.util.function.Consumer<IWaystone> onSelect, int width) {
                this.waystone = waystone;
                
                String displayName = waystone.getName();
                
                this.button = Button.builder(
                    Component.literal(displayName),
                    btn -> {
                        System.out.println("[WaystoneInjector] Button clicked: " + displayName);
                        onSelect.accept(waystone);
                    }
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

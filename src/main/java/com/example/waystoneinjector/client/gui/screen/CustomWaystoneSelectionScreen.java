package com.example.waystoneinjector.client.gui.screen;

import com.example.waystoneinjector.client.WaystoneTypeRegistry;
import com.example.waystoneinjector.client.gui.widget.ThemedButton;
import com.example.waystoneinjector.config.WaystoneConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.waystones.api.Waystone;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CustomWaystoneSelectionScreen extends AbstractContainerScreen<WaystoneSelectionMenu> {
    
    private static final AtomicReference<String> currentWaystoneType = new AtomicReference<>("regular");
    
    private static final ResourceLocation TEXTURE_REGULAR = new ResourceLocation("waystoneinjector", "textures/gui/waystone_regular.png");
    private static final ResourceLocation TEXTURE_MOSSY = new ResourceLocation("waystoneinjector", "textures/gui/waystone_mossy.png");
    private static final ResourceLocation TEXTURE_BLACKSTONE = new ResourceLocation("waystoneinjector", "textures/gui/waystone_blackstone.png");
    private static final ResourceLocation TEXTURE_DEEPSLATE = new ResourceLocation("waystoneinjector", "textures/gui/waystone_deepslate.png");
    private static final ResourceLocation TEXTURE_ENDSTONE = new ResourceLocation("waystoneinjector", "textures/gui/waystone_endstone.png");
    private static final ResourceLocation TEXTURE_SHARESTONE = new ResourceLocation("waystoneinjector", "textures/gui/sharestone.png");
    
    private EditBox searchBox;
    private ScrollableWaystoneList waystoneList;
    private List<Waystone> allWaystones;
    private String searchText = "";
    
    public CustomWaystoneSelectionScreen(WaystoneSelectionMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 256;
        this.imageHeight = 256;
        
        // Detect waystone type from menu
        detectWaystoneType(menu);
        
        // Get all waystones from menu
        this.allWaystones = new ArrayList<>();
        try {
            // Access waystones from menu using reflection or API
            // This is a placeholder - actual implementation depends on Waystones API
            System.out.println("[CustomWaystoneScreen] Initializing with type: " + currentWaystoneType.get());
        } catch (Exception e) {
            System.err.println("[CustomWaystoneScreen] Error accessing waystones: " + e.getMessage());
        }
    }
    
    public static void setWaystoneType(String type) {
        currentWaystoneType.set(type);
        System.out.println("[CustomWaystoneScreen] Waystone type set to: " + type);
    }
    
    private void detectWaystoneType(WaystoneSelectionMenu menu) {
        try {
            Waystone waystoneFrom = menu.getWaystoneFrom();
            if (waystoneFrom != null) {
                String waystoneTypeStr = waystoneFrom.getWaystoneType().toString();
                String detectedType = parseWaystoneType(waystoneTypeStr);
                currentWaystoneType.set(detectedType);
                
                // Register in persistent registry
                String waystoneName = waystoneFrom.getName().getString();
                WaystoneTypeRegistry.registerWaystone(waystoneName, detectedType);
                
                System.out.println("[CustomWaystoneScreen] Detected type: " + detectedType + " from waystone: " + waystoneName);
            }
        } catch (Exception e) {
            System.err.println("[CustomWaystoneScreen] Error detecting waystone type: " + e.getMessage());
            currentWaystoneType.set("regular");
        }
    }
    
    private String parseWaystoneType(String waystoneTypeStr) {
        String lowerType = waystoneTypeStr.toLowerCase();
        
        if (lowerType.contains("sharestone")) return "sharestone";
        if (lowerType.contains("mossy")) return "mossy";
        if (lowerType.contains("blackstone")) return "blackstone";
        if (lowerType.contains("deepslate")) return "deepslate";
        if (lowerType.contains("end_stone") || lowerType.contains("endstone")) return "endstone";
        
        return "regular";
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Create search box
        int searchBoxWidth = 160;
        int searchBoxX = (this.width - searchBoxWidth) / 2;
        int searchBoxY = this.topPos + 20;
        
        this.searchBox = new EditBox(this.font, searchBoxX, searchBoxY, searchBoxWidth, 20, Component.literal("Search"));
        this.searchBox.setResponder(this::onSearchChanged);
        this.addRenderableWidget(this.searchBox);
        
        // Create scrollable waystone list (no pagination buttons!)
        int listX = this.leftPos + 20;
        int listY = this.topPos + 50;
        int listWidth = 216;
        int listHeight = 160;
        
        this.waystoneList = new ScrollableWaystoneList(listX, listY, listWidth, listHeight, this.allWaystones, this::onWaystoneSelected);
        this.addRenderableWidget(this.waystoneList);
        
        // Add custom server transfer buttons
        addCustomButtons();
    }
    
    private void onSearchChanged(String text) {
        this.searchText = text.toLowerCase();
        if (this.waystoneList != null) {
            this.waystoneList.filter(this.searchText);
        }
    }
    
    private void onWaystoneSelected(Waystone waystone) {
        // Send packet to server to teleport to waystone
        // Implementation depends on Waystones networking API
        System.out.println("[CustomWaystoneScreen] Selected waystone: " + waystone.getName().getString());
        this.onClose();
    }
    
    private void addCustomButtons() {
        // Build list of enabled button configs
        List<ButtonConfig> buttonConfigs = new ArrayList<>();
        
        if (WaystoneConfig.BUTTON1_ENABLED.get()) {
            buttonConfigs.add(new ButtonConfig(
                WaystoneConfig.BUTTON1_LABEL.get(),
                WaystoneConfig.BUTTON1_COMMAND.get(),
                WaystoneConfig.BUTTON1_WIDTH.get(),
                WaystoneConfig.BUTTON1_HEIGHT.get(),
                WaystoneConfig.BUTTON1_X_OFFSET.get(),
                WaystoneConfig.BUTTON1_Y_OFFSET.get(),
                WaystoneConfig.BUTTON1_TEXT_COLOR.get(),
                WaystoneConfig.BUTTON1_SIDE.get()
            ));
        }
        if (WaystoneConfig.BUTTON2_ENABLED.get()) {
            buttonConfigs.add(new ButtonConfig(
                WaystoneConfig.BUTTON2_LABEL.get(),
                WaystoneConfig.BUTTON2_COMMAND.get(),
                WaystoneConfig.BUTTON2_WIDTH.get(),
                WaystoneConfig.BUTTON2_HEIGHT.get(),
                WaystoneConfig.BUTTON2_X_OFFSET.get(),
                WaystoneConfig.BUTTON2_Y_OFFSET.get(),
                WaystoneConfig.BUTTON2_TEXT_COLOR.get(),
                WaystoneConfig.BUTTON2_SIDE.get()
            ));
        }
        if (WaystoneConfig.BUTTON3_ENABLED.get()) {
            buttonConfigs.add(new ButtonConfig(
                WaystoneConfig.BUTTON3_LABEL.get(),
                WaystoneConfig.BUTTON3_COMMAND.get(),
                WaystoneConfig.BUTTON3_WIDTH.get(),
                WaystoneConfig.BUTTON3_HEIGHT.get(),
                WaystoneConfig.BUTTON3_X_OFFSET.get(),
                WaystoneConfig.BUTTON3_Y_OFFSET.get(),
                WaystoneConfig.BUTTON3_TEXT_COLOR.get(),
                WaystoneConfig.BUTTON3_SIDE.get()
            ));
        }
        if (WaystoneConfig.BUTTON4_ENABLED.get()) {
            buttonConfigs.add(new ButtonConfig(
                WaystoneConfig.BUTTON4_LABEL.get(),
                WaystoneConfig.BUTTON4_COMMAND.get(),
                WaystoneConfig.BUTTON4_WIDTH.get(),
                WaystoneConfig.BUTTON4_HEIGHT.get(),
                WaystoneConfig.BUTTON4_X_OFFSET.get(),
                WaystoneConfig.BUTTON4_Y_OFFSET.get(),
                WaystoneConfig.BUTTON4_TEXT_COLOR.get(),
                WaystoneConfig.BUTTON4_SIDE.get()
            ));
        }
        if (WaystoneConfig.BUTTON5_ENABLED.get()) {
            buttonConfigs.add(new ButtonConfig(
                WaystoneConfig.BUTTON5_LABEL.get(),
                WaystoneConfig.BUTTON5_COMMAND.get(),
                WaystoneConfig.BUTTON5_WIDTH.get(),
                WaystoneConfig.BUTTON5_HEIGHT.get(),
                WaystoneConfig.BUTTON5_X_OFFSET.get(),
                WaystoneConfig.BUTTON5_Y_OFFSET.get(),
                WaystoneConfig.BUTTON5_TEXT_COLOR.get(),
                WaystoneConfig.BUTTON5_SIDE.get()
            ));
        }
        if (WaystoneConfig.BUTTON6_ENABLED.get()) {
            buttonConfigs.add(new ButtonConfig(
                WaystoneConfig.BUTTON6_LABEL.get(),
                WaystoneConfig.BUTTON6_COMMAND.get(),
                WaystoneConfig.BUTTON6_WIDTH.get(),
                WaystoneConfig.BUTTON6_HEIGHT.get(),
                WaystoneConfig.BUTTON6_X_OFFSET.get(),
                WaystoneConfig.BUTTON6_Y_OFFSET.get(),
                WaystoneConfig.BUTTON6_TEXT_COLOR.get(),
                WaystoneConfig.BUTTON6_SIDE.get()
            ));
        }
        
        if (buttonConfigs.isEmpty()) return;
        
        // Separate buttons by side
        List<ButtonConfig> leftButtons = new ArrayList<>();
        List<ButtonConfig> rightButtons = new ArrayList<>();
        
        for (ButtonConfig config : buttonConfigs) {
            if ("left".equalsIgnoreCase(config.side)) {
                leftButtons.add(config);
            } else if ("right".equalsIgnoreCase(config.side)) {
                rightButtons.add(config);
            }
        }
        
        List<ButtonConfig> autoButtons = new ArrayList<>();
        for (ButtonConfig config : buttonConfigs) {
            if ("auto".equalsIgnoreCase(config.side)) {
                autoButtons.add(config);
            }
        }
        
        for (ButtonConfig config : autoButtons) {
            if (leftButtons.size() <= rightButtons.size()) {
                leftButtons.add(config);
            } else {
                rightButtons.add(config);
            }
        }
        
        int centerY = this.height / 2;
        int verticalSpacing = 5;
        int sideMargin = 10;
        
        // Add left side buttons
        for (int i = 0; i < leftButtons.size(); i++) {
            ButtonConfig config = leftButtons.get(i);
            int totalHeight = leftButtons.size() * config.height + (leftButtons.size() - 1) * verticalSpacing;
            int startY = centerY - totalHeight / 2;
            int y = startY + (i * (config.height + verticalSpacing)) + config.yOffset;
            int x = sideMargin + config.xOffset;
            
            final String command = config.command;
            Component labelComponent = Component.literal(config.label).withStyle(style -> style.withColor(config.textColor));
            
            ThemedButton button = new ThemedButton(
                x, y, config.width, config.height,
                labelComponent,
                btn -> handleServerTransfer(command),
                currentWaystoneType::get,
                "left",
                i,
                leftButtons.size()
            );
            
            this.addRenderableWidget(button);
        }
        
        // Add right side buttons
        for (int i = 0; i < rightButtons.size(); i++) {
            ButtonConfig config = rightButtons.get(i);
            int totalHeight = rightButtons.size() * config.height + (rightButtons.size() - 1) * verticalSpacing;
            int startY = centerY - totalHeight / 2;
            int y = startY + (i * (config.height + verticalSpacing)) + config.yOffset;
            int x = this.width - config.width - sideMargin + config.xOffset;
            
            final String command = config.command;
            Component labelComponent = Component.literal(config.label).withStyle(style -> style.withColor(config.textColor));
            
            ThemedButton button = new ThemedButton(
                x, y, config.width, config.height,
                labelComponent,
                btn -> handleServerTransfer(command),
                currentWaystoneType::get,
                "right",
                i,
                rightButtons.size()
            );
            
            this.addRenderableWidget(button);
        }
    }
    
    private void handleServerTransfer(String command) {
        System.out.println("[CustomWaystoneScreen] Server transfer: " + command);
        // Implementation would use ClientEvents.connectToServer()
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Render background first
        renderBackground(graphics);
        
        // Render widgets
        super.render(graphics, mouseX, mouseY, partialTick);
        
        // Render title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, this.topPos + 6, 0x404040);
    }
    
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        // Render custom background texture based on waystone type
        ResourceLocation texture = getTextureForType(currentWaystoneType.get());
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }
    
    private ResourceLocation getTextureForType(String type) {
        return switch (type) {
            case "sharestone" -> TEXTURE_SHARESTONE;
            case "mossy" -> TEXTURE_MOSSY;
            case "blackstone" -> TEXTURE_BLACKSTONE;
            case "deepslate" -> TEXTURE_DEEPSLATE;
            case "endstone" -> TEXTURE_ENDSTONE;
            default -> TEXTURE_REGULAR;
        };
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.waystoneList != null && this.waystoneList.mouseScrolled(mouseX, mouseY, delta)) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
    
    // Scrollable list widget (no pagination!)
    private static class ScrollableWaystoneList extends AbstractWidget {
        private final List<Waystone> allWaystones;
        private final List<Waystone> filteredWaystones;
        private final WaystoneSelectCallback callback;
        private double scrollOffset = 0;
        private static final int ENTRY_HEIGHT = 36;
        
        public ScrollableWaystoneList(int x, int y, int width, int height, List<Waystone> waystones, WaystoneSelectCallback callback) {
            super(x, y, width, height, Component.empty());
            this.allWaystones = waystones;
            this.filteredWaystones = new ArrayList<>(waystones);
            this.callback = callback;
        }
        
        public void filter(String searchText) {
            this.filteredWaystones.clear();
            if (searchText.isEmpty()) {
                this.filteredWaystones.addAll(this.allWaystones);
            } else {
                for (Waystone waystone : this.allWaystones) {
                    if (waystone.getName().getString().toLowerCase().contains(searchText)) {
                        this.filteredWaystones.add(waystone);
                    }
                }
            }
            this.scrollOffset = 0;
        }
        
        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
            if (this.isMouseOver(mouseX, mouseY)) {
                double maxScroll = Math.max(0, (this.filteredWaystones.size() * ENTRY_HEIGHT) - this.height);
                this.scrollOffset = Math.max(0, Math.min(maxScroll, this.scrollOffset - delta * 20));
                return true;
            }
            return false;
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0 && this.isMouseOver(mouseX, mouseY)) {
                int relativeY = (int) (mouseY - this.getY() + this.scrollOffset);
                int index = relativeY / ENTRY_HEIGHT;
                
                if (index >= 0 && index < this.filteredWaystones.size()) {
                    Waystone waystone = this.filteredWaystones.get(index);
                    this.callback.onSelected(waystone);
                    return true;
                }
            }
            return false;
        }
        
        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            // Enable scissor for clipping
            graphics.enableScissor(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height);
            
            int y = this.getY() - (int) this.scrollOffset;
            
            for (Waystone waystone : this.filteredWaystones) {
                if (y + ENTRY_HEIGHT > this.getY() && y < this.getY() + this.height) {
                    // Render waystone entry
                    boolean hovered = mouseX >= this.getX() && mouseX < this.getX() + this.width &&
                                    mouseY >= y && mouseY < y + ENTRY_HEIGHT;
                    
                    int bgColor = hovered ? 0x80FFFFFF : 0x40000000;
                    graphics.fill(this.getX(), y, this.getX() + this.width, y + ENTRY_HEIGHT, bgColor);
                    
                    // Draw waystone name
                    graphics.drawString(net.minecraft.client.Minecraft.getInstance().font,
                        waystone.getName(),
                        this.getX() + 5,
                        y + (ENTRY_HEIGHT - 8) / 2,
                        0xFFFFFF);
                }
                
                y += ENTRY_HEIGHT;
            }
            
            graphics.disableScissor();
        }
        
        @Override
        protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput output) {
            // Narration for accessibility
        }
    }
    
    @FunctionalInterface
    private interface WaystoneSelectCallback {
        void onSelected(Waystone waystone);
    }
    
    private static class ButtonConfig {
        String label, command, side;
        int width, height, xOffset, yOffset, textColor;
        
        ButtonConfig(String label, String command, int width, int height, int xOffset, int yOffset, String textColorHex, String side) {
            this.label = label;
            this.command = command;
            this.width = width;
            this.height = height;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.side = side;
            
            try {
                String colorStr = textColorHex.trim();
                if (colorStr.startsWith("0x") || colorStr.startsWith("0X")) {
                    this.textColor = Integer.parseInt(colorStr.substring(2), 16);
                } else {
                    this.textColor = Integer.parseInt(colorStr, 16);
                }
            } catch (NumberFormatException e) {
                this.textColor = 0xFFFFFF;
            }
        }
    }
}

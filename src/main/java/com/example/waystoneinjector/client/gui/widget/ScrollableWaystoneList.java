package com.example.waystoneinjector.client.gui.widget;

import com.example.waystoneinjector.client.gui.WaystoneData;
import com.example.waystoneinjector.client.gui.WaystoneOrderManager;
import com.example.waystoneinjector.client.gui.tooltip.WaystoneTooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Scrollable container widget for displaying waystone list
 * Phase 5: Drag-and-drop reordering support
 * Phase 6: CTRL+hover tooltip support
 */
@SuppressWarnings("null")
public class ScrollableWaystoneList extends AbstractWidget {
    
    private static final int SCROLLBAR_WIDTH = 6;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 2;
    private static final int SCROLLBAR_COLOR = 0xFF8B8B8B;
    private static final int SCROLLBAR_BG_COLOR = 0xFF303030;
    
    private List<WaystoneData> waystones;
    private final List<Button> waystoneButtons = new ArrayList<>();
    private final Consumer<WaystoneData> onWaystoneSelected;
    
    private double scrollOffset = 0.0;
    private int maxScroll = 0;
    private boolean isDraggingScrollbar = false;
    private double dragStartY = 0;
    private double scrollStartOffset = 0;
    
    // Drag-and-drop state (Phase 5)
    private int draggedIndex = -1;
    private double draggedButtonY = 0;
    private int dropTargetIndex = -1;
    
    // Tooltip state (Phase 6)
    private int hoveredIndex = -1;
    
    public ScrollableWaystoneList(int x, int y, int width, int height, List<WaystoneData> waystones, Consumer<WaystoneData> onWaystoneSelected) {
        super(x, y, width, height, Component.literal("Waystone List"));
        this.waystones = waystones;
        this.onWaystoneSelected = onWaystoneSelected;
        this.updateButtons();
    }
    
    public void updateWaystones(List<WaystoneData> newWaystones) {
        this.waystones = newWaystones;
        this.scrollOffset = 0; // Reset scroll when list changes
        this.updateButtons();
    }
    
    private void updateButtons() {
        this.waystoneButtons.clear();
        
        for (int i = 0; i < waystones.size(); i++) {
            final WaystoneData waystone = waystones.get(i);
            
            Button button = Button.builder(
                Component.literal(waystone.getName() + " - " + waystone.getDimensionName()),
                btn -> onWaystoneSelected.accept(waystone)
            ).bounds(0, 0, this.width - SCROLLBAR_WIDTH - 4, BUTTON_HEIGHT).build();
            
            waystoneButtons.add(button);
        }
        
        updateScrollbar();
    }
    
    private void updateScrollbar() {
        int totalContentHeight = waystones.size() * (BUTTON_HEIGHT + BUTTON_SPACING);
        maxScroll = Math.max(0, totalContentHeight - this.height);
        
        // Clamp scroll offset
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
    }
    
    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Draw background
        graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xCC000000);
        
        // Track hovered waystone for tooltips
        hoveredIndex = -1;
        
        // Enable scissor (viewport clipping)
        enableScissor(graphics, this.getX(), this.getY(), this.width - SCROLLBAR_WIDTH - 2, this.height);
        
        // Render visible waystone buttons
        int currentY = this.getY() - (int) scrollOffset;
        
        for (int i = 0; i < waystoneButtons.size(); i++) {
            if (i == draggedIndex) {
                continue; // Skip dragged button (render it last on top)
            }
            
            Button button = waystoneButtons.get(i);
            int buttonY = currentY + i * (BUTTON_HEIGHT + BUTTON_SPACING);
            
            // Show drop indicator
            if (dropTargetIndex == i && draggedIndex >= 0) {
                graphics.fill(
                    this.getX() + 2,
                    buttonY - 1,
                    this.getX() + this.width - SCROLLBAR_WIDTH - 2,
                    buttonY + 1,
                    0xFF00FF00
                );
            }
            
            // Only render if visible in viewport
            if (buttonY + BUTTON_HEIGHT >= this.getY() && buttonY < this.getY() + this.height) {
                button.setX(this.getX() + 2);
                button.setY(buttonY);
                button.render(graphics, mouseX, mouseY, partialTick);
                
                // Track if hovering this button
                if (button.isMouseOver(mouseX, mouseY)) {
                    hoveredIndex = i;
                }
            }
        }
        
        // Render dragged button on top with transparency
        if (draggedIndex >= 0 && draggedIndex < waystoneButtons.size()) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 100); // Higher Z-index
            
            Button draggedButton = waystoneButtons.get(draggedIndex);
            draggedButton.setX(this.getX() + 2);
            draggedButton.setY((int) draggedButtonY);
            
            // Render with slight transparency
            draggedButton.setAlpha(0.7f);
            draggedButton.render(graphics, mouseX, mouseY, partialTick);
            draggedButton.setAlpha(1.0f);
            
            graphics.pose().popPose();
        }
        
        // Disable scissor
        disableScissor(graphics);
        
        // Render scrollbar if needed
        if (maxScroll > 0) {
            renderScrollbar(graphics, mouseX, mouseY);
        }
        
        // Render tooltip if hovering and CTRL held (Phase 6)
        if (hoveredIndex >= 0 && hoveredIndex < waystones.size() && Screen.hasControlDown()) {
            WaystoneData waystone = waystones.get(hoveredIndex);
            Minecraft mc = Minecraft.getInstance();
            
            if (mc.player != null) {
                double playerX = mc.player.getX();
                double playerZ = mc.player.getZ();
                ResourceKey<Level> playerDim = mc.player.level().dimension();
                
                WaystoneTooltipRenderer.renderDetailedTooltip(
                    graphics, waystone, mouseX, mouseY, playerX, playerZ, playerDim
                );
            }
        }
    }
    
    private void renderScrollbar(GuiGraphics graphics, int mouseX, int mouseY) {
        int scrollbarX = this.getX() + this.width - SCROLLBAR_WIDTH - 1;
        int scrollbarY = this.getY();
        int scrollbarHeight = this.height;
        
        // Draw scrollbar background
        graphics.fill(scrollbarX, scrollbarY, scrollbarX + SCROLLBAR_WIDTH, scrollbarY + scrollbarHeight, SCROLLBAR_BG_COLOR);
        
        // Calculate scrollbar thumb size and position
        double contentRatio = (double) this.height / (this.height + maxScroll);
        int thumbHeight = Math.max(20, (int) (scrollbarHeight * contentRatio));
        
        double scrollRatio = maxScroll > 0 ? scrollOffset / maxScroll : 0;
        int thumbY = scrollbarY + (int) ((scrollbarHeight - thumbHeight) * scrollRatio);
        
        // Draw scrollbar thumb
        int thumbColor = isDraggingScrollbar ? 0xFFAAAAAA : SCROLLBAR_COLOR;
        graphics.fill(scrollbarX, thumbY, scrollbarX + SCROLLBAR_WIDTH, thumbY + thumbHeight, thumbColor);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isMouseOver(mouseX, mouseY)) {
            return false;
        }
        
        // Check if clicking on scrollbar
        if (maxScroll > 0) {
            int scrollbarX = this.getX() + this.width - SCROLLBAR_WIDTH - 1;
            if (mouseX >= scrollbarX && mouseX <= scrollbarX + SCROLLBAR_WIDTH) {
                isDraggingScrollbar = true;
                dragStartY = mouseY;
                scrollStartOffset = scrollOffset;
                return true;
            }
        }
        
        // Check if clicking on a waystone button
        int currentY = this.getY() - (int) scrollOffset;
        
        for (int i = 0; i < waystoneButtons.size(); i++) {
            Button btn = waystoneButtons.get(i);
            int buttonY = currentY + i * (BUTTON_HEIGHT + BUTTON_SPACING);
            
            // Only check visible buttons
            if (buttonY + BUTTON_HEIGHT >= this.getY() && buttonY < this.getY() + this.height) {
                btn.setX(this.getX() + 2);
                btn.setY(buttonY);
                
                if (btn.isMouseOver(mouseX, mouseY)) {
                    // Left click + SHIFT = start drag (Phase 5)
                    if (button == 0 && Screen.hasShiftDown()) {
                        draggedIndex = i;
                        draggedButtonY = buttonY;
                        dragStartY = mouseY;
                        System.out.println("[WaystoneInjector] Started dragging waystone: " + waystones.get(i).getName());
                        return true;
                    }
                    
                    // Normal click = select waystone
                    if (btn.mouseClicked(mouseX, mouseY, button)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isDraggingScrollbar) {
            isDraggingScrollbar = false;
            return true;
        }
        
        // Handle drag-and-drop release (Phase 5)
        if (draggedIndex >= 0) {
            if (dropTargetIndex >= 0 && dropTargetIndex != draggedIndex) {
                // Swap waystones
                WaystoneData draggedWaystone = waystones.remove(draggedIndex);
                waystones.add(dropTargetIndex, draggedWaystone);
                
                System.out.println("[WaystoneInjector] Reordered: " + draggedWaystone.getName() + 
                                   " from index " + draggedIndex + " to " + dropTargetIndex);
                
                // Save new order
                WaystoneOrderManager.saveWaystoneOrder(waystones);
                
                // Update buttons
                updateButtons();
            }
            
            draggedIndex = -1;
            dropTargetIndex = -1;
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDraggingScrollbar) {
            double deltaY = mouseY - dragStartY;
            
            // Calculate scroll movement
            double contentRatio = (double) this.height / (this.height + maxScroll);
            int thumbHeight = Math.max(20, (int) (this.height * contentRatio));
            double scrollableHeight = this.height - thumbHeight;
            
            if (scrollableHeight > 0) {
                double scrollDelta = (deltaY / scrollableHeight) * maxScroll;
                scrollOffset = scrollStartOffset + scrollDelta;
                scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
            }
            
            return true;
        }
        
        // Handle waystone drag (Phase 5)
        if (draggedIndex >= 0) {
            double deltaY = mouseY - dragStartY;
            int currentY = this.getY() - (int) scrollOffset;
            draggedButtonY = currentY + draggedIndex * (BUTTON_HEIGHT + BUTTON_SPACING) + deltaY;
            
            // Calculate drop target index
            int relativeY = (int) (draggedButtonY - currentY + (BUTTON_HEIGHT + BUTTON_SPACING) / 2);
            dropTargetIndex = relativeY / (BUTTON_HEIGHT + BUTTON_SPACING);
            dropTargetIndex = Math.max(0, Math.min(dropTargetIndex, waystones.size() - 1));
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!this.isMouseOver(mouseX, mouseY)) {
            return false;
        }
        
        if (maxScroll > 0) {
            // Scroll speed: 3 buttons per scroll
            double scrollSpeed = (BUTTON_HEIGHT + BUTTON_SPACING) * 3;
            scrollOffset -= delta * scrollSpeed;
            scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
            return true;
        }
        
        return false;
    }
    
    private void enableScissor(GuiGraphics graphics, int x, int y, int width, int height) {
        // Get Minecraft window scale
        Minecraft mc = Minecraft.getInstance();
        double scale = mc.getWindow().getGuiScale();
        
        // Calculate scissor box in screen coordinates
        int scaledX = (int) (x * scale);
        int scaledY = (int) (mc.getWindow().getGuiScaledHeight() - (y + height)) * (int) scale;
        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);
        
        // Enable scissor test
        graphics.enableScissor(scaledX, scaledY, scaledX + scaledWidth, scaledY + scaledHeight);
    }
    
    private void disableScissor(GuiGraphics graphics) {
        graphics.disableScissor();
    }
    
    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(net.minecraft.client.gui.narration.NarratedElementType.TITLE, 
                   Component.literal("Waystone list with " + waystones.size() + " waystones"));
    }
}

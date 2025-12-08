package com.example.waystoneinjector.gui;

import com.example.waystoneinjector.config.DevConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

/**
 * Debug overlay for GUI development
 */
public class DebugOverlay {
    
    @SuppressWarnings("null")
    public static void render(GuiGraphics guiGraphics, int screenWidth, int screenHeight, 
                             int listX, int listY, int listWidth, int listHeight,
                             int portalX, int portalY, int portalWidth, int portalHeight) {
        if (!DevConfig.showDebugOverlay()) return;
        
        Font font = Minecraft.getInstance().font;
        List<String> lines = new ArrayList<>();
        
        // Title
        lines.add("§e§lWaystoneInjector Dev Mode");
        lines.add("§7Edit: config/waystoneinjector-dev.json");
        lines.add("");
        
        // Scroll list info
        lines.add("§b[Scroll List]");
        lines.add(String.format("  Position: %d, %d", listX, listY));
        lines.add(String.format("  Size: %d x %d", listWidth, listHeight));
        lines.add(String.format("  Centered: %b", DevConfig.getScrollList().centered));
        lines.add("");
        
        // Portal info
        lines.add("§d[Portal]");
        lines.add(String.format("  Position: %d, %d", portalX, portalY));
        lines.add(String.format("  Size: %d x %d", portalWidth, portalHeight));
        lines.add(String.format("  Speed: %dms/frame", DevConfig.getPortal().animationSpeed));
        lines.add("");
        
        // Screen info
        lines.add("§a[Screen]");
        lines.add(String.format("  Size: %d x %d", screenWidth, screenHeight));
        lines.add(String.format("  Mouse: %d, %d", 
            (int)Minecraft.getInstance().mouseHandler.xpos(), 
            (int)Minecraft.getInstance().mouseHandler.ypos()));
        
        // Render background panel
        int panelWidth = 250;
        int panelHeight = lines.size() * 10 + 10;
        guiGraphics.fill(5, 5, 5 + panelWidth, 5 + panelHeight, 0xCC000000);
        
        // Render text
        int y = 10;
        for (String line : lines) {
            guiGraphics.drawString(font, line, 10, y, 0xFFFFFF);
            y += 10;
        }
        
        // Render bounding boxes
        renderBoundingBox(guiGraphics, listX, listY, listWidth, listHeight, 0xFF00FF00); // Green for list
        renderBoundingBox(guiGraphics, portalX, portalY, portalWidth, portalHeight, 0xFFFF00FF); // Magenta for portal
    }
    
    private static void renderBoundingBox(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        // Top
        guiGraphics.fill(x, y, x + width, y + 1, color);
        // Bottom
        guiGraphics.fill(x, y + height - 1, x + width, y + height, color);
        // Left
        guiGraphics.fill(x, y, x + 1, y + height, color);
        // Right
        guiGraphics.fill(x + width - 1, y, x + width, y + height, color);
        
        // Corner markers (larger for visibility)
        guiGraphics.fill(x, y, x + 5, y + 5, color);
        guiGraphics.fill(x + width - 5, y, x + width, y + 5, color);
        guiGraphics.fill(x, y + height - 5, x + 5, y + height, color);
        guiGraphics.fill(x + width - 5, y + height - 5, x + width, y + height, color);
    }
}

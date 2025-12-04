package com.example.waystoneinjector.client.gui.tooltip;

import com.example.waystoneinjector.client.gui.WaystoneData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders detailed waystone information tooltips
 * Phase 6: CTRL+hover tooltip display
 */
@SuppressWarnings("null")
public class WaystoneTooltipRenderer {
    
    /**
     * Render detailed tooltip when CTRL is held
     */
    public static void renderDetailedTooltip(GuiGraphics graphics, WaystoneData waystone, int mouseX, int mouseY, double playerX, double playerZ, net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> playerDimension) {
        // Only show when CTRL held
        if (!Screen.hasControlDown()) {
            return;
        }
        
        List<Component> tooltip = new ArrayList<>();
        
        // Title with formatting
        tooltip.add(Component.literal("§b§l" + waystone.getName()));
        tooltip.add(Component.literal("")); // Blank line
        
        // Dimension
        tooltip.add(Component.literal("§7Dimension: §f" + waystone.getDimensionName()));
        
        // Coordinates
        tooltip.add(Component.literal("§7Location: §f" + waystone.getX() + ", " + waystone.getY() + ", " + waystone.getZ()));
        
        // Distance (if in same dimension)
        double distance = waystone.getDistanceToPlayer(playerX, playerZ, playerDimension);
        if (distance != Double.MAX_VALUE) {
            tooltip.add(Component.literal("§7Distance: §f" + String.format("%.1f", distance) + " blocks"));
        } else {
            tooltip.add(Component.literal("§7Distance: §c(Different dimension)"));
        }
        
        // Global status
        if (waystone.isGlobal()) {
            tooltip.add(Component.literal("")); // Blank line
            tooltip.add(Component.literal("§a✓ Global Waystone"));
        }
        
        // Render the tooltip
        graphics.renderComponentTooltip(Minecraft.getInstance().font, tooltip, mouseX, mouseY);
    }
}

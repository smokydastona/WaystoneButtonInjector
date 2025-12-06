package com.example.waystoneinjector.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.client.gui.widget.WaystoneButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom waystone button that renders type-specific overlay textures behind the button content.
 * Extends WaystoneButton to inject overlay rendering while preserving original functionality.
 */
public class CustomWaystoneButton extends WaystoneButton {
    
    // Overlay textures (220x36) for each waystone type
    private static final ResourceLocation OVERLAY_REGULAR = new ResourceLocation("waystoneinjector", "textures/gui/overlays/regular.png");
    private static final ResourceLocation OVERLAY_MOSSY = new ResourceLocation("waystoneinjector", "textures/gui/overlays/mossy.png");
    private static final ResourceLocation OVERLAY_BLACKSTONE = new ResourceLocation("waystoneinjector", "textures/gui/overlays/blackstone.png");
    private static final ResourceLocation OVERLAY_DEEPSLATE = new ResourceLocation("waystoneinjector", "textures/gui/overlays/deepslate.png");
    private static final ResourceLocation OVERLAY_ENDSTONE = new ResourceLocation("waystoneinjector", "textures/gui/overlays/endstone.png");
    private static final ResourceLocation OVERLAY_SHARESTONE = new ResourceLocation("waystoneinjector", "textures/gui/overlays/sharestone.png");
    private static final ResourceLocation OVERLAY_WARP_SCROLL = new ResourceLocation("waystoneinjector", "textures/gui/overlays/warp_scroll.png");
    private static final ResourceLocation OVERLAY_WARP_STONE = new ResourceLocation("waystoneinjector", "textures/gui/overlays/warp_stone.png");
    private static final ResourceLocation OVERLAY_PORTSTONE = new ResourceLocation("waystoneinjector", "textures/gui/overlays/portstone.png");
    
    private final ResourceLocation overlayTexture;

    public CustomWaystoneButton(int x, int y, IWaystone waystone, int xpLevelCost, Button.OnPress pressable) {
        super(x, y, waystone, xpLevelCost, pressable);
        this.overlayTexture = getOverlayForWaystone(waystone);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // STEP 1: Render original button background and base
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
        
        // STEP 2: Render overlay texture ON TOP of button background
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        guiGraphics.blit(overlayTexture, this.getX(), this.getY() - 8, 0, 0, 220, 36, 220, 36);
    }
    
    /**
     * Detects waystone type from NBT data and returns corresponding overlay texture
     */
    private ResourceLocation getOverlayForWaystone(IWaystone waystone) {
        String type = detectWaystoneType(waystone);
        
        return switch (type) {
            case "mossy" -> OVERLAY_MOSSY;
            case "blackstone" -> OVERLAY_BLACKSTONE;
            case "deepslate" -> OVERLAY_DEEPSLATE;
            case "endstone", "end_stone" -> OVERLAY_ENDSTONE;
            case "sharestone" -> OVERLAY_SHARESTONE;
            case "warp_scroll", "scroll" -> OVERLAY_WARP_SCROLL;
            case "warp_stone", "bound_scroll" -> OVERLAY_WARP_STONE;
            case "portstone" -> OVERLAY_PORTSTONE;
            default -> OVERLAY_REGULAR;
        };
    }
    
    /**
     * Inspects waystone NBT to determine block type (mossy, blackstone, etc.)
     */
    private String detectWaystoneType(IWaystone waystone) {
        try {
            // Try to access NBT data through reflection
            var waystoneClass = waystone.getClass();
            
            // Check for waystoneType field (common in waystone implementations)
            try {
                var typeField = waystoneClass.getDeclaredField("waystoneType");
                typeField.setAccessible(true);
                Object typeValue = typeField.get(waystone);
                if (typeValue != null) {
                    String typeStr = typeValue.toString().toLowerCase();
                    if (typeStr.contains("mossy")) return "mossy";
                    if (typeStr.contains("blackstone")) return "blackstone";
                    if (typeStr.contains("deepslate")) return "deepslate";
                    if (typeStr.contains("end")) return "endstone";
                    if (typeStr.contains("sharestone")) return "sharestone";
                    if (typeStr.contains("scroll")) return "warp_scroll";
                    if (typeStr.contains("stone") && typeStr.contains("warp")) return "warp_stone";
                    if (typeStr.contains("portstone")) return "portstone";
                }
            } catch (NoSuchFieldException ignored) {}
            
            // Check for blockType field
            try {
                var blockField = waystoneClass.getDeclaredField("blockType");
                blockField.setAccessible(true);
                Object blockValue = blockField.get(waystone);
                if (blockValue != null) {
                    String blockStr = blockValue.toString().toLowerCase();
                    if (blockStr.contains("mossy")) return "mossy";
                    if (blockStr.contains("blackstone")) return "blackstone";
                    if (blockStr.contains("deepslate")) return "deepslate";
                    if (blockStr.contains("end")) return "endstone";
                }
            } catch (NoSuchFieldException ignored) {}
            
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Failed to detect waystone type: " + e.getMessage());
        }
        
        return "regular"; // Default fallback
    }
}

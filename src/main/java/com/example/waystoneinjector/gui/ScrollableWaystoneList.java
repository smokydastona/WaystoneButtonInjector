package com.example.waystoneinjector.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.waystones.api.IWaystone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * Scrollable list widget for waystone selection.
 * Replaces the paginated button system with a smooth scrolling list.
 */
public class ScrollableWaystoneList extends ObjectSelectionList<ScrollableWaystoneList.WaystoneEntry> {
    
    private final EnhancedWaystoneSelectionScreen parent;
    
    public ScrollableWaystoneList(EnhancedWaystoneSelectionScreen parent, Minecraft mc, int width, int height, int top, int bottom, int itemHeight, List<IWaystone> waystones) {
        super(mc, width, height, top, bottom, itemHeight);
        this.parent = parent;
        
        // Add all waystones to the list
        for (IWaystone waystone : waystones) {
            this.addEntry(new WaystoneEntry(waystone));
        }
    }
    
    @Override
    public int getRowWidth() {
        return 220;
    }
    
    @Override
    protected int getScrollbarPosition() {
        return this.width / 2 + 124;
    }
    
    public class WaystoneEntry extends ObjectSelectionList.Entry<WaystoneEntry> {
        
        private final IWaystone waystone;
        private final ResourceLocation overlayTexture;
        
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
        
        public WaystoneEntry(IWaystone waystone) {
            this.waystone = waystone;
            this.overlayTexture = getOverlayForWaystone(waystone);
        }
        
        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
            // Render overlay texture as background
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            guiGraphics.blit(overlayTexture, left, top, 0, 0, 220, 36, 220, 36);
            RenderSystem.disableBlend();
            
            // Render waystone name
            String name = waystone.getName().isEmpty() ? "Unnamed Waystone" : waystone.getName();
            Component nameComponent = Component.literal(name);
            if (waystone.isGlobal()) {
                nameComponent = Component.literal(name).withStyle(net.minecraft.ChatFormatting.YELLOW);
            }
            
            guiGraphics.drawString(minecraft.font, nameComponent, left + 5, top + 14, 0xFFFFFF);
            
            // Render distance if in same dimension
            if (waystone.getDimension() == minecraft.player.level().dimension()) {
                int distance = (int) minecraft.player.position().distanceTo(waystone.getPos().getCenter());
                String distanceStr;
                if (distance < 10000) {
                    distanceStr = distance + "m";
                } else {
                    distanceStr = String.format("%.1f", (float) distance / 1000.0f)
                            .replace(",0", "")
                            .replace(".0", "") + "km";
                }
                int xOffset = 220 - minecraft.font.width(distanceStr) - 5;
                guiGraphics.drawString(minecraft.font, distanceStr, left + xOffset, top + 14, 0xAAAAAA);
            }
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                parent.onWaystoneSelected(waystone);
                return true;
            }
            return false;
        }
        
        @Override
        public Component getNarration() {
            return Component.literal(waystone.getName());
        }
        
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
        
        private String detectWaystoneType(IWaystone waystone) {
            try {
                var waystoneClass = waystone.getClass();
                
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
            
            return "regular";
        }
    }
}

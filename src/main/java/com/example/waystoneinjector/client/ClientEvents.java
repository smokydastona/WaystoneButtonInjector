package com.example.waystoneinjector.client;

import com.example.waystoneinjector.client.gui.widget.ThemedButton;
import com.example.waystoneinjector.config.WaystoneConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("null")
public class ClientEvents {

    static {
        System.out.println("[WaystoneInjector] ClientEvents class loaded!");
    }
    
    private static final AtomicReference<EditBox> currentSearchBox = new AtomicReference<>(null);
    private static final AtomicReference<Screen> currentWaystoneScreen = new AtomicReference<>(null);
    private static final AtomicReference<Object> currentWaystoneList = new AtomicReference<>(null);
    private static final AtomicReference<String> currentWaystoneType = new AtomicReference<>("regular");
    
    // Custom GUI texture locations
    private static final ResourceLocation TEXTURE_REGULAR = new ResourceLocation("waystoneinjector", "textures/gui/waystone_regular.png");
    private static final ResourceLocation TEXTURE_MOSSY = new ResourceLocation("waystoneinjector", "textures/gui/waystone_mossy.png");
    private static final ResourceLocation TEXTURE_BLACKSTONE = new ResourceLocation("waystoneinjector", "textures/gui/waystone_blackstone.png");
    private static final ResourceLocation TEXTURE_DEEPSLATE = new ResourceLocation("waystoneinjector", "textures/gui/waystone_deepslate.png");
    private static final ResourceLocation TEXTURE_ENDSTONE = new ResourceLocation("waystoneinjector", "textures/gui/waystone_endstone.png");
    private static final ResourceLocation TEXTURE_SHARESTONE = new ResourceLocation("waystoneinjector", "textures/gui/sharestone.png");

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getLevel().isClientSide()) return;
        
        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        
        // Get block's registry key
        ResourceLocation blockId = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(block);
        if (blockId == null) return;
        
        // Check if it's a waystone block
        if (!blockId.getNamespace().equals("waystones")) return;
        
        String path = blockId.getPath();
        System.out.println("[WaystoneInjector] Right-clicked waystone block: " + path);
        
        // Detect type from block registry path
        String detectedType = "regular";
        if (path.contains("sharestone")) {
            detectedType = "sharestone";
        } else if (path.contains("mossy")) {
            detectedType = "mossy";
        } else if (path.contains("blackstone")) {
            detectedType = "blackstone";
        } else if (path.contains("deepslate")) {
            detectedType = "deepslate";
        } else if (path.contains("end_stone") || path.contains("endstone")) {
            detectedType = "endstone";
        }
        
        currentWaystoneType.set(detectedType);
        System.out.println("[WaystoneInjector] Pre-set waystone type to: " + detectedType);
    }

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        try {
            System.out.println("[WaystoneInjector] *** onScreenInit EVENT FIRED ***");
            Screen screen = event.getScreen();
            if (screen == null) {
                System.out.println("[WaystoneInjector] Screen is null, returning");
                return;
            }

            // Detect Waystones selection screen specifically
            String className = screen.getClass().getName();
            System.out.println("[WaystoneInjector] Screen detected: " + className);
        
            if (!className.equals("net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen")) {
                System.out.println("[WaystoneInjector] Not a waystone screen, ignoring");
                return;
            }

            System.out.println("[WaystoneInjector] ✓✓✓ WAYSTONE SCREEN DETECTED! Adding enhancements... ✓✓✓");
            
            // Don't reset if we already detected type from right-click
            String preClickType = currentWaystoneType.get();
            System.out.println("[WaystoneInjector] Pre-click type was: " + preClickType);
            
            // Try to detect waystone type from the screen (as backup if right-click didn't work)
            detectWaystoneType(screen);
            System.out.println("[WaystoneInjector] ✓ Final detected type: " + currentWaystoneType.get());
            
            // Store screen reference
            currentWaystoneScreen.set(screen);
            
            // Add custom server transfer buttons
            System.out.println("[WaystoneInjector] ✓ Adding custom buttons...");
            addCustomButtons(screen, event);
            
            // Add search box enhancement
            addSearchBoxEnhancement(screen);
            
            // Find and store waystone list for keyboard navigation
            findWaystoneList(screen);
            
            // Hide pagination buttons (Next/Previous)
            hidePaginationButtons(screen, event);
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] ERROR in onScreenInit: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void hidePaginationButtons(Screen screen, ScreenEvent.Init.Post event) {
        try {
            System.out.println("[WaystoneInjector] Attempting to enable continuous scrolling...");
            
            // Find the waystone list widget using reflection
            Object waystoneList = null;
            for (Field field : screen.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(screen);
                
                if (value != null && value.getClass().getSimpleName().contains("WaystoneList")) {
                    waystoneList = value;
                    System.out.println("[WaystoneInjector] Found waystone list: " + value.getClass().getName());
                    break;
                }
            }
            
            if (waystoneList == null) {
                System.err.println("[WaystoneInjector] Could not find waystone list!");
                return;
            }
            
            // Dump all fields to see what we have
            System.out.println("[WaystoneInjector] Waystone list fields:");
            for (Field field : waystoneList.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                System.out.println("  - " + field.getName() + " (" + field.getType().getSimpleName() + ")");
            }
            
            // Try to find and modify pagination-related fields
            boolean modified = false;
            for (Field field : waystoneList.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                
                // Look for page-related fields
                if (fieldName.contains("page") || fieldName.contains("Page")) {
                    if (field.getType() == int.class || field.getType() == Integer.class) {
                        try {
                            int currentValue = field.getInt(waystoneList);
                            System.out.println("[WaystoneInjector] Found field: " + fieldName + " = " + currentValue);
                            
                            // If it's itemsPerPage or similar, set it very high
                            if (fieldName.toLowerCase().contains("per") || fieldName.toLowerCase().contains("size")) {
                                field.setInt(waystoneList, 999);
                                System.out.println("[WaystoneInjector] ✓ Set " + fieldName + " to 999");
                                modified = true;
                            }
                        } catch (Exception e) {
                            System.err.println("[WaystoneInjector] Error modifying field " + fieldName + ": " + e.getMessage());
                        }
                    }
                }
            }
            
            // Now hide the pagination buttons
            List<?> renderables = event.getListenersList();
            int hiddenCount = 0;
            
            for (Object widget : renderables) {
                if (widget instanceof net.minecraft.client.gui.components.Button) {
                    net.minecraft.client.gui.components.Button button = (net.minecraft.client.gui.components.Button) widget;
                    Component message = button.getMessage();
                    String text = message.getString().toLowerCase();
                    
                    // Check if it's a pagination button
                    if (text.contains("next") || text.contains("previous") || text.contains("page")) {
                        button.visible = false;
                        button.active = false;
                        hiddenCount++;
                        System.out.println("[WaystoneInjector] ✓ Hidden pagination button: " + text);
                    }
                }
            }
            
            System.out.println("[WaystoneInjector] ✓ Hidden " + hiddenCount + " pagination buttons");
            System.out.println("[WaystoneInjector] ✓ Modified pagination: " + modified);
            
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Error enabling continuous scrolling: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void detectWaystoneType(Screen screen) {
        try {
            // Check if we already have a type from right-click event
            String currentType = currentWaystoneType.get();
            if (currentType != null && !currentType.equals("regular")) {
                System.out.println("[WaystoneInjector] Using type from right-click event: " + currentType);
                return; // Already detected, don't override
            }
            
            System.out.println("[WaystoneInjector] No right-click type detected, attempting to detect from screen...");
            
            // Try to access the menu to get waystone data
            Field menuField = findField(screen.getClass(), "menu", "container");
            if (menuField != null) {
                menuField.setAccessible(true);
                Object menu = menuField.get(screen);
                System.out.println("[WaystoneInjector] Found menu: " + (menu != null ? menu.getClass().getName() : "null"));
                
                if (menu != null) {
                    // Try to call getWaystoneFrom() to get the current waystone
                    Method getWaystoneFromMethod = findMethod(menu.getClass(), "getWaystoneFrom");
                    if (getWaystoneFromMethod != null) {
                        getWaystoneFromMethod.setAccessible(true);
                        Object waystoneFrom = getWaystoneFromMethod.invoke(menu);
                        System.out.println("[WaystoneInjector] Found waystoneFrom: " + (waystoneFrom != null ? waystoneFrom.getClass().getName() : "null"));
                        
                        if (waystoneFrom != null) {
                            // Get waystone name
                            String waystoneName = null;
                            Method getNameMethod = findMethod(waystoneFrom.getClass(), "getName");
                            if (getNameMethod != null) {
                                getNameMethod.setAccessible(true);
                                Object nameComponent = getNameMethod.invoke(waystoneFrom);
                                if (nameComponent != null) {
                                    waystoneName = nameComponent.toString();
                                    System.out.println("[WaystoneInjector] Waystone name: " + waystoneName);
                                }
                            }
                            
                            // Get waystone type
                            String detectedType = null;
                            Method getWaystoneTypeMethod = findMethod(waystoneFrom.getClass(), "getWaystoneType");
                            if (getWaystoneTypeMethod != null) {
                                getWaystoneTypeMethod.setAccessible(true);
                                Object waystoneTypeObj = getWaystoneTypeMethod.invoke(waystoneFrom);
                                if (waystoneTypeObj != null) {
                                    String waystoneTypeStr = waystoneTypeObj.toString();
                                    System.out.println("[WaystoneInjector] WaystoneType: " + waystoneTypeStr);
                                    
                                    // Parse the ResourceLocation to get type
                                    String lowerType = waystoneTypeStr.toLowerCase();
                                    
                                    if (lowerType.contains("sharestone")) {
                                        detectedType = "sharestone";
                                        System.out.println("[WaystoneInjector] ✓ Detected SHARESTONE");
                                    } else if (lowerType.contains("mossy")) {
                                        detectedType = "mossy";
                                        System.out.println("[WaystoneInjector] ✓ Detected MOSSY WAYSTONE");
                                    } else if (lowerType.contains("blackstone")) {
                                        detectedType = "blackstone";
                                        System.out.println("[WaystoneInjector] ✓ Detected BLACKSTONE WAYSTONE");
                                    } else if (lowerType.contains("deepslate")) {
                                        detectedType = "deepslate";
                                        System.out.println("[WaystoneInjector] ✓ Detected DEEPSLATE WAYSTONE");
                                    } else if (lowerType.contains("end_stone") || lowerType.contains("endstone")) {
                                        detectedType = "endstone";
                                        System.out.println("[WaystoneInjector] ✓ Detected END STONE WAYSTONE");
                                    } else {
                                        detectedType = "regular";
                                        System.out.println("[WaystoneInjector] ✓ Detected REGULAR WAYSTONE");
                                    }
                                    
                                    currentWaystoneType.set(detectedType);
                                    
                                    // Register this waystone in the persistent registry
                                    if (waystoneName != null && detectedType != null) {
                                        WaystoneTypeRegistry.registerWaystone(waystoneName, detectedType);
                                    }
                                } else {
                                    System.out.println("[WaystoneInjector] ⚠ getWaystoneType() returned null");
                                    currentWaystoneType.set("regular");
                                }
                            } else {
                                System.out.println("[WaystoneInjector] ⚠ Could not find getWaystoneType() method");
                                currentWaystoneType.set("regular");
                            }
                        } else {
                            System.out.println("[WaystoneInjector] ⚠ getWaystoneFrom() returned null - waystone not placed in world?");
                            currentWaystoneType.set("regular");
                        }
                    } else {
                        System.out.println("[WaystoneInjector] ⚠ Could not find getWaystoneFrom() method");
                        currentWaystoneType.set("regular");
                    }
                }
            } else {
                System.out.println("[WaystoneInjector] ⚠ Could not find menu field in screen");
                currentWaystoneType.set("regular");
            }
        } catch (Exception e) {
            System.err.println("[WaystoneInjector] Error detecting waystone type: " + e.getMessage());
            e.printStackTrace();
            currentWaystoneType.set("regular");
        }
    }
    
    private static void addSearchBoxEnhancement(Screen screen) {
        try {
            // Find the existing search box in the waystone screen
            Field searchBoxField = findField(screen.getClass(), "searchBox");
            if (searchBoxField != null) {
                searchBoxField.setAccessible(true);
                Object searchBox = searchBoxField.get(screen);
                
                if (searchBox instanceof EditBox) {
                    EditBox box = (EditBox) searchBox;
                    
                    // Store reference for event handlers
                    currentSearchBox.set(box);
                    
                    System.out.println("[WaystoneInjector] Search box enhancements active:");
                    System.out.println("[WaystoneInjector] - Right-click to clear");
                    System.out.println("[WaystoneInjector] - ESC to clear and unfocus");
                    System.out.println("[WaystoneInjector] - Number keys 1-9 for quick waystone selection");
                }
            }
        } catch (Exception e) {
            System.out.println("[WaystoneInjector] Could not enhance search box: " + e.getMessage());
        }
    }
    
    @SubscribeEvent
    public static void onMouseClicked(ScreenEvent.MouseButtonPressed.Pre event) {
        EditBox searchBox = currentSearchBox.get();
        if (searchBox == null) return;
        
        // Right-click to clear
        if (event.getButton() == 1) { // Right mouse button
            double mouseX = event.getMouseX();
            double mouseY = event.getMouseY();
            
            // Check if click is within search box bounds
            if (mouseX >= searchBox.getX() && mouseX <= searchBox.getX() + searchBox.getWidth() &&
                mouseY >= searchBox.getY() && mouseY <= searchBox.getY() + searchBox.getHeight()) {
                
                if (!searchBox.getValue().isEmpty()) {
                    searchBox.setValue("");
                    System.out.println("[WaystoneInjector] Search box cleared via right-click");
                    event.setCanceled(true);
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        Screen screen = currentWaystoneScreen.get();
        EditBox searchBox = currentSearchBox.get();
        
        // Handle search box ESC key
        if (searchBox != null && event.getKeyCode() == GLFW.GLFW_KEY_ESCAPE && searchBox.isFocused()) {
            if (!searchBox.getValue().isEmpty()) {
                searchBox.setValue("");
                searchBox.setFocused(false);
                System.out.println("[WaystoneInjector] Search box cleared via ESC");
                event.setCanceled(true);
                return;
            }
        }
        
        // Handle keyboard navigation for waystone list
        if (screen == null) return;
        
        // Number keys 1-9 for quick selection (only if search box not focused)
        if (searchBox == null || !searchBox.isFocused()) {
            int keyCode = event.getKeyCode();
            
            // Keys 1-9 (GLFW key codes)
            if (keyCode >= GLFW.GLFW_KEY_1 && keyCode <= GLFW.GLFW_KEY_9) {
                int slotIndex = keyCode - GLFW.GLFW_KEY_1; // 0-8 for keys 1-9
                if (selectWaystoneAtIndex(screen, slotIndex)) {
                    System.out.println("[WaystoneInjector] Quick-selected waystone " + (slotIndex + 1) + " via keyboard");
                    event.setCanceled(true);
                }
            }
        }
    }
    
    private static boolean selectWaystoneAtIndex(Screen screen, int index) {
        try {
            Object waystoneList = currentWaystoneList.get();
            if (waystoneList == null) return false;
            
            // Get the children/entries from the list
            Field childrenField = findField(waystoneList.getClass(), "children", "entries");
            if (childrenField != null) {
                childrenField.setAccessible(true);
                Object children = childrenField.get(waystoneList);
                
                if (children instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Object> entries = (List<Object>) children;
                    
                    if (index >= 0 && index < entries.size()) {
                        Object entry = entries.get(index);
                        
                        // Try to trigger the click on this entry
                        Method clickMethod = findMethod(entry.getClass(), "mouseClicked", "onClick");
                        if (clickMethod != null) {
                            clickMethod.setAccessible(true);
                            clickMethod.invoke(entry, 0.0, 0.0, 0);
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[WaystoneInjector] Could not select waystone at index " + index + ": " + e.getMessage());
        }
        return false;
    }
    
    private static Method findMethod(Class<?> clazz, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                // Try to find method with common signatures
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getName().equals(methodName)) {
                        return method;
                    }
                }
            } catch (Exception e) {
                // Continue
            }
        }
        return null;
    }
    
    private static void findWaystoneList(Screen screen) {
        try {
            // Try to find the waystone list widget for keyboard navigation
            Field listField = findField(screen.getClass(), "waystoneList", "list");
            if (listField != null) {
                listField.setAccessible(true);
                Object list = listField.get(screen);
                currentWaystoneList.set(list);
                System.out.println("[WaystoneInjector] Found waystone list widget for keyboard navigation");
            }
        } catch (Exception e) {
            System.out.println("[WaystoneInjector] Could not find waystone list: " + e.getMessage());
        }
    }
    
    @SubscribeEvent
    public static void onRenderBackground(ScreenEvent.BackgroundRendered event) {
        Screen screen = currentWaystoneScreen.get();
        if (screen == null || event.getScreen() != screen) return;
        
        // Get the appropriate texture based on waystone type
        String waystoneType = currentWaystoneType.get();
        ResourceLocation texture = getTextureForType(waystoneType);
        System.out.println("[WaystoneInjector] Rendering background - Type: " + waystoneType + ", Texture: " + texture);
        
        GuiGraphics graphics = event.getGuiGraphics();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        // Render custom background texture
        int screenWidth = screen.width;
        int screenHeight = screen.height;
        
        // Center the 256x256 texture on screen
        int x = (screenWidth - 256) / 2;
        int y = (screenHeight - 256) / 2;
        
        graphics.blit(texture, x, y, 0, 0, 256, 256, 256, 256);
    }
    
    @SubscribeEvent
    public static void onRenderWaystoneList(ScreenEvent.Render.Post event) {
        Screen screen = currentWaystoneScreen.get();
        if (screen == null || event.getScreen() != screen) return;
        
        Object waystoneList = currentWaystoneList.get();
        if (waystoneList == null) return;
        
        GuiGraphics graphics = event.getGuiGraphics();
        
        // Get current waystone type color for custom buttons
        int buttonColor = WaystoneTypeRegistry.getColorForType(currentWaystoneType.get());
        
        try {
            // Get the waystone entries from the list
            Field childrenField = findField(waystoneList.getClass(), "children", "entries");
            if (childrenField != null) {
                childrenField.setAccessible(true);
                Object children = childrenField.get(waystoneList);
                
                if (children instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Object> entries = (List<Object>) children;
                    
                    // Try to get list position
                    Field yField = findField(waystoneList.getClass(), "y0", "top");
                    Field xField = findField(waystoneList.getClass(), "x0", "left");
                    
                    if (yField != null && xField != null) {
                        yField.setAccessible(true);
                        xField.setAccessible(true);
                        
                        int listY = yField.getInt(waystoneList);
                        int listX = xField.getInt(waystoneList);
                        
                        // Render colored overlay for each waystone entry
                        for (int i = 0; i < entries.size(); i++) {
                            Object entry = entries.get(i);
                            
                            // Try to get waystone name from entry
                            Field waystoneField = findField(entry.getClass(), "waystone", "data");
                            if (waystoneField != null) {
                                waystoneField.setAccessible(true);
                                Object waystone = waystoneField.get(entry);
                                
                                if (waystone != null) {
                                    Field nameField = findField(waystone.getClass(), "name", "waystoneData");
                                    if (nameField != null) {
                                        nameField.setAccessible(true);
                                        Object name = nameField.get(waystone);
                                        
                                        if (name != null) {
                                            String waystoneName = name.toString();
                                            String type = WaystoneTypeRegistry.getWaystoneType(waystoneName);
                                            
                                            if (!type.equals("unknown")) {
                                                int color = WaystoneTypeRegistry.getColorForType(type);
                                                int entryY = listY + (i * 36); // Approximate entry height
                                                
                                                // Draw semi-transparent colored background
                                                int alpha = 40; // Low opacity
                                                int colorWithAlpha = (alpha << 24) | (color & 0xFFFFFF);
                                                graphics.fill(listX, entryY, listX + 220, entryY + 36, colorWithAlpha);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // Render colored overlays on custom server transfer buttons
            renderCustomButtonOverlays(graphics, screen, buttonColor);
            
        } catch (Exception e) {
            // Silently fail - rendering is optional
        }
    }
    
    private static void renderCustomButtonOverlays(GuiGraphics graphics, Screen screen, int color) {
        try {
            // Get config values to know which buttons exist
            List<String> labels = WaystoneConfig.getEnabledLabels();
            int numButtons = Math.min(labels.size(), 6);
            if (numButtons == 0) return;
            
            int leftButtons = (numButtons + 1) / 2;
            int rightButtons = numButtons / 2;
            
            int buttonWidth = 60;
            int buttonHeight = 30;
            int verticalSpacing = 5;
            int sideMargin = 10;
            int centerY = screen.height / 2;
            
            // Semi-transparent color overlay
            int alpha = 60; // Slightly higher opacity for buttons
            int colorWithAlpha = (alpha << 24) | (color & 0xFFFFFF);
            
            // Render left side button overlays
            for (int i = 0; i < leftButtons; i++) {
                int y = centerY - ((leftButtons * buttonHeight + (leftButtons - 1) * verticalSpacing) / 2) + (i * (buttonHeight + verticalSpacing));
                graphics.fill(sideMargin, y, sideMargin + buttonWidth, y + buttonHeight, colorWithAlpha);
            }
            
            // Render right side button overlays
            for (int i = 0; i < rightButtons; i++) {
                int y = centerY - ((rightButtons * buttonHeight + (rightButtons - 1) * verticalSpacing) / 2) + (i * (buttonHeight + verticalSpacing));
                int x = screen.width - buttonWidth - sideMargin;
                graphics.fill(x, y, x + buttonWidth, y + buttonHeight, colorWithAlpha);
            }
        } catch (Exception e) {
            // Silently fail
        }
    }
    
    private static ResourceLocation getTextureForType(String type) {
        return switch (type) {
            case "sharestone" -> TEXTURE_SHARESTONE;
            case "mossy" -> TEXTURE_MOSSY;
            case "blackstone" -> TEXTURE_BLACKSTONE;
            case "deepslate" -> TEXTURE_DEEPSLATE;
            case "endstone" -> TEXTURE_ENDSTONE;
            default -> TEXTURE_REGULAR;
        };
    }
    
    private static Field findField(Class<?> clazz, String... fieldNames) {
        for (String fieldName : fieldNames) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (NoSuchFieldException e) {
                // Try superclass
                Class<?> superClass = clazz.getSuperclass();
                if (superClass != null) {
                    try {
                        return superClass.getDeclaredField(fieldName);
                    } catch (NoSuchFieldException e2) {
                        // Continue
                    }
                }
            }
        }
        return null;
    }
    
    // Helper class to store button configuration
    private static class ButtonConfig {
        String label;
        String command;
        int width;
        int height;
        int xOffset;
        int yOffset;
        int textColor;
        String side; // "auto", "left", or "right"
        
        ButtonConfig(String label, String command, int width, int height, int xOffset, int yOffset, String textColorHex, String side) {
            this.label = label;
            this.command = command;
            this.width = width;
            this.height = height;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.side = side;
            
            // Parse hex color
            try {
                String colorStr = textColorHex.trim();
                if (colorStr.startsWith("0x") || colorStr.startsWith("0X")) {
                    this.textColor = Integer.parseInt(colorStr.substring(2), 16);
                } else {
                    this.textColor = Integer.parseInt(colorStr, 16);
                }
            } catch (NumberFormatException e) {
                this.textColor = 0xFFFFFF; // Default to white
            }
        }
    }
    
    private static void addCustomButtons(Screen screen, ScreenEvent.Init.Post event) {
        try {
            System.out.println("[WaystoneInjector] >>> Starting addCustomButtons <<<");
        
        // Build list of enabled button configs
        java.util.List<ButtonConfig> buttonConfigs = new java.util.ArrayList<>();
        
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
        
        System.out.println("[WaystoneInjector] ✓ Enabled buttons: " + buttonConfigs.size());
        
        if (buttonConfigs.isEmpty()) {
            System.out.println("[WaystoneInjector] ✗ No enabled buttons found!");
            return;
        }
        
        // Separate buttons by side
        java.util.List<ButtonConfig> leftButtons = new java.util.ArrayList<>();
        java.util.List<ButtonConfig> rightButtons = new java.util.ArrayList<>();
        
        // First pass: assign explicit left/right
        for (ButtonConfig config : buttonConfigs) {
            if ("left".equalsIgnoreCase(config.side)) {
                leftButtons.add(config);
            } else if ("right".equalsIgnoreCase(config.side)) {
                rightButtons.add(config);
            }
        }
        
        // Second pass: distribute "auto" buttons evenly
        java.util.List<ButtonConfig> autoButtons = new java.util.ArrayList<>();
        for (ButtonConfig config : buttonConfigs) {
            if ("auto".equalsIgnoreCase(config.side)) {
                autoButtons.add(config);
            }
        }
        
        // Distribute auto buttons to balance left/right
        for (ButtonConfig config : autoButtons) {
            if (leftButtons.size() <= rightButtons.size()) {
                leftButtons.add(config);
            } else {
                rightButtons.add(config);
            }
        }
        
        System.out.println("[WaystoneInjector] ✓ Button distribution - Left: " + leftButtons.size() + ", Right: " + rightButtons.size());
        
        int centerY = screen.height / 2;
        int verticalSpacing = 5;
        int sideMargin = 10;
        
        // Add left side buttons
        for (int i = 0; i < leftButtons.size(); i++) {
            ButtonConfig config = leftButtons.get(i);
            
            // Calculate Y position (centered vertically)
            int totalHeight = leftButtons.size() * config.height + (leftButtons.size() - 1) * verticalSpacing;
            int startY = centerY - totalHeight / 2;
            int y = startY + (i * (config.height + verticalSpacing)) + config.yOffset;
            int x = sideMargin + config.xOffset;
            
            final String command = config.command;
            Component labelComponent = Component.literal(config.label).withStyle(style -> style.withColor(config.textColor));
            
            System.out.println("[WaystoneInjector] ✓ Creating LEFT button " + (i+1) + ": '" + config.label + "' at (" + x + "," + y + ") type=" + currentWaystoneType.get());
            
            // Create themed button with waystone type background (using supplier for dynamic updates)
            ThemedButton button = new ThemedButton(
                x, y, config.width, config.height,
                labelComponent,
                btn -> handleServerTransfer(command),
                currentWaystoneType::get,  // Pass supplier that always gets current type
                "left",
                i,
                leftButtons.size()
            );
            
            // Add button using the screen's method
            try {
                event.addListener(button);
                System.out.println("[WaystoneInjector] ✓ Added LEFT button " + (i+1));
            } catch (Exception e) {
                System.err.println("[WaystoneInjector] ✗ Failed to add LEFT button: " + e.getMessage());
            }
        }
        
        // Add right side buttons
        for (int i = 0; i < rightButtons.size(); i++) {
            ButtonConfig config = rightButtons.get(i);
            
            // Calculate Y position (centered vertically)
            int totalHeight = rightButtons.size() * config.height + (rightButtons.size() - 1) * verticalSpacing;
            int startY = centerY - totalHeight / 2;
            int y = startY + (i * (config.height + verticalSpacing)) + config.yOffset;
            int x = screen.width - config.width - sideMargin + config.xOffset;
            
            final String command = config.command;
            Component labelComponent = Component.literal(config.label).withStyle(style -> style.withColor(config.textColor));
            
            System.out.println("[WaystoneInjector] ✓ Creating RIGHT button " + (i+1) + ": '" + config.label + "' at (" + x + "," + y + ") type=" + currentWaystoneType.get());
            
            // Create themed button with waystone type background (using supplier for dynamic updates)
            ThemedButton button = new ThemedButton(
                x, y, config.width, config.height,
                labelComponent,
                btn -> handleServerTransfer(command),
                currentWaystoneType::get,  // Pass supplier that always gets current type
                "right",
                i,
                rightButtons.size()
            );
            
            // Add button using the screen's method
            try {
                event.addListener(button);
                System.out.println("[WaystoneInjector] ✓ Added RIGHT button " + (i+1));
            } catch (Exception e) {
                System.err.println("[WaystoneInjector] ✗ Failed to add RIGHT button: " + e.getMessage());
            }
        }
        
        System.out.println("[WaystoneInjector] ✓✓✓ Successfully added " + (leftButtons.size() + rightButtons.size()) + " buttons! ✓✓✓");
        
        } catch (Exception e) {
            System.out.println("[WaystoneInjector] Could not add custom buttons: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void handleServerTransfer(String command) {
        System.out.println("[WaystoneInjector] Handling server transfer: " + command);
        String serverAddress = parseRedirectAddress(command);
        if (serverAddress != null && !serverAddress.isEmpty()) {
            connectToServer(serverAddress);
        } else {
            System.err.println("[WaystoneInjector] Could not parse server address from: " + command);
        }
    }
    
    public static String parseRedirectAddress(String command) {
        String[] parts = command.trim().split("\\s+");
        if (parts.length >= 2 && parts[0].equalsIgnoreCase("redirect")) {
            if (parts[1].equals("@s") && parts.length >= 3) {
                return parts[2];
            } else {
                return parts[1];
            }
        }
        return null;
    }

    /**
     * Connect to a server by IP address
     * Used by DeathSleepEvents for death/sleep redirects
     */
    public static void connectToServer(String serverAddress) {
        Minecraft mc = Minecraft.getInstance();
        System.out.println("[WaystoneInjector] Connecting to server: " + serverAddress);
        
        // Disconnect from current server first
        if (mc.level != null) {
            mc.level.disconnect();
        }
        
        // Parse server address
        ServerAddress address = ServerAddress.parseString(serverAddress);
        
        // Create ServerData object (1.20.1 API)
        ServerData serverData = new ServerData(serverAddress, serverAddress, false);
        
        // Connect to the server (1.20.1 API)
        net.minecraft.client.gui.screens.ConnectScreen.startConnecting(
            mc.screen,
            mc,
            address,
            serverData,
            false
        );
    }
}

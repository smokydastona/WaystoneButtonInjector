package com.example.waystoneinjector.client;

import com.example.waystoneinjector.config.WaystoneConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "waystoneinjector", bus = Mod.EventBusSubscriber.Bus.FORGE)
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
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (screen == null) return;

        // Detect Waystones selection screen specifically
        String className = screen.getClass().getName();
        System.out.println("[WaystoneInjector] Screen detected: " + className);
        
        if (!className.equals("net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreen")) {
            return;
        }

        System.out.println("[WaystoneInjector] Waystone screen detected! Adding enhancements...");
        
        // Detect waystone type from the screen
        detectWaystoneType(screen);
        
        // Store screen reference
        currentWaystoneScreen.set(screen);
        
        // Add custom server transfer buttons
        addCustomButtons(event, screen);
        
        // Add search box enhancement
        addSearchBoxEnhancement(event, screen);
        
        // Find and store waystone list for keyboard navigation
        findWaystoneList(screen);
    }
    
    private static void detectWaystoneType(Screen screen) {
        try {
            // Try to access the menu to get waystone data
            Field menuField = findField(screen.getClass(), "menu", "container");
            if (menuField != null) {
                menuField.setAccessible(true);
                Object menu = menuField.get(screen);
                
                if (menu != null) {
                    // Try to get the waystone data from the menu
                    Field waystoneField = findField(menu.getClass(), "waystone", "waystoneData", "blockEntity");
                    if (waystoneField != null) {
                        waystoneField.setAccessible(true);
                        Object waystone = waystoneField.get(menu);
                        
                        if (waystone != null) {
                            // Get waystone name for registry
                            String waystoneName = null;
                            Field nameField = findField(waystone.getClass(), "name", "waystoneData");
                            if (nameField != null) {
                                nameField.setAccessible(true);
                                Object name = nameField.get(waystone);
                                if (name != null) {
                                    waystoneName = name.toString();
                                }
                            }
                            
                            // Try to get the block/type information
                            Field blockField = findField(waystone.getClass(), "block", "blockState", "type");
                            if (blockField != null) {
                                blockField.setAccessible(true);
                                Object block = blockField.get(waystone);
                                
                                if (block != null) {
                                    String blockName = block.toString();
                                    System.out.println("[WaystoneInjector] Waystone type detected: " + blockName);
                                    
                                    String detectedType;
                                    // Detect waystone type
                                    if (blockName.contains("sharestone")) {
                                        detectedType = "sharestone";
                                        System.out.println("[WaystoneInjector] This is a SHARESTONE - Using purple texture");
                                    } else if (blockName.contains("mossy")) {
                                        detectedType = "mossy";
                                        System.out.println("[WaystoneInjector] This is a MOSSY WAYSTONE - Using green texture");
                                    } else if (blockName.contains("blackstone")) {
                                        detectedType = "blackstone";
                                        System.out.println("[WaystoneInjector] This is a BLACKSTONE WAYSTONE - Using dark texture");
                                    } else if (blockName.contains("deepslate")) {
                                        detectedType = "deepslate";
                                        System.out.println("[WaystoneInjector] This is a DEEPSLATE WAYSTONE - Using gray texture");
                                    } else if (blockName.contains("end_stone")) {
                                        detectedType = "endstone";
                                        System.out.println("[WaystoneInjector] This is an END STONE WAYSTONE - Using yellow texture");
                                    } else {
                                        detectedType = "regular";
                                        System.out.println("[WaystoneInjector] This is a REGULAR WAYSTONE - Using brown texture");
                                    }
                                    
                                    currentWaystoneType.set(detectedType);
                                    
                                    // Register this waystone in the persistent registry
                                    if (waystoneName != null) {
                                        WaystoneTypeRegistry.registerWaystone(waystoneName, detectedType);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[WaystoneInjector] Could not detect waystone type: " + e.getMessage());
        }
    }
    
    private static void addSearchBoxEnhancement(ScreenEvent.Init.Post event, Screen screen) {
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
        ResourceLocation texture = getTextureForType(currentWaystoneType.get());
        
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
    
    private static void addCustomButtons(ScreenEvent.Init.Post event, Screen screen) {
        
        // Get config values
        List<String> labels = WaystoneConfig.getEnabledLabels();
        List<String> commands = WaystoneConfig.getEnabledCommands();

        System.out.println("[WaystoneInjector] Enabled buttons: " + labels.size());

        int numButtons = Math.min(labels.size(), 6); // Max 6 buttons
        if (numButtons == 0) {
            System.out.println("[WaystoneInjector] No enabled buttons found!");
            return;
        }

        // Calculate button dimensions based on number of buttons
        int leftButtons = (numButtons + 1) / 2;
        int rightButtons = numButtons / 2;

        int buttonWidth = 60;
        int buttonHeight = 30;
        int verticalSpacing = 5;
        int sideMargin = 10;

        int centerY = screen.height / 2;

        // Add left side buttons
        for (int i = 0; i < leftButtons; i++) {
            final int buttonIndex = i;
            String label = labels.get(buttonIndex);
            final String command = commands.get(buttonIndex);

            int y = centerY - ((leftButtons * buttonHeight + (leftButtons - 1) * verticalSpacing) / 2) + (i * (buttonHeight + verticalSpacing));

            Button button = Button.builder(
                Component.literal(label),
                btn -> handleServerTransfer(command)
            ).bounds(sideMargin, y, buttonWidth, buttonHeight).build();

            event.addListener(button);
        }

        // Add right side buttons
        for (int i = 0; i < rightButtons; i++) {
            final int buttonIndex = leftButtons + i;
            String label = labels.get(buttonIndex);
            final String command = commands.get(buttonIndex);

            int y = centerY - ((rightButtons * buttonHeight + (rightButtons - 1) * verticalSpacing) / 2) + (i * (buttonHeight + verticalSpacing));
            int x = screen.width - buttonWidth - sideMargin;

            Button button = Button.builder(
                Component.literal(label),
                btn -> handleServerTransfer(command)
            ).bounds(x, y, buttonWidth, buttonHeight).build();

            event.addListener(button);
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
    
    private static String parseRedirectAddress(String command) {
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

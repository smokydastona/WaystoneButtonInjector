package com.example.waystoneinjector.client;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
        
        // Store screen reference
        currentWaystoneScreen.set(screen);
        
        // Add custom server transfer buttons
        addCustomButtons(event, screen);
        
        // Add search box enhancement
        addSearchBoxEnhancement(event, screen);
        
        // Find and store waystone list for tooltip rendering
        findWaystoneList(screen);
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
                    
                    System.out.println("[WaystoneInjector] Found search box at x=" + box.getX() + ", y=" + box.getY());
                    System.out.println("[WaystoneInjector] Search box enhancements active:");
                    System.out.println("[WaystoneInjector] - Right-click to clear");
                    System.out.println("[WaystoneInjector] - ESC to clear and unfocus");
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
        EditBox searchBox = currentSearchBox.get();
        if (searchBox == null) return;
        
        // ESC key to clear and unfocus
        if (event.getKeyCode() == GLFW.GLFW_KEY_ESCAPE && searchBox.isFocused()) {
            if (!searchBox.getValue().isEmpty()) {
                searchBox.setValue("");
                searchBox.setFocused(false);
                System.out.println("[WaystoneInjector] Search box cleared via ESC");
                event.setCanceled(true);
            }
        }
    }
    
    private static void findWaystoneList(Screen screen) {
        try {
            // Try to find the waystone list widget for tooltip rendering
            Field listField = findField(screen.getClass(), "waystoneList", "list");
            if (listField != null) {
                listField.setAccessible(true);
                Object list = listField.get(screen);
                currentWaystoneList.set(list);
                System.out.println("[WaystoneInjector] Found waystone list widget for enhanced tooltips");
            }
        } catch (Exception e) {
            System.out.println("[WaystoneInjector] Could not find waystone list: " + e.getMessage());
        }
    }
    
    @SubscribeEvent
    public static void onRenderScreen(ScreenEvent.Render.Post event) {
        Screen screen = currentWaystoneScreen.get();
        if (screen == null || event.getScreen() != screen) return;
        
        // Check if CTRL is held
        if (!Screen.hasControlDown()) return;
        
        GuiGraphics graphics = event.getGuiGraphics();
        int mouseX = (int) event.getMouseX();
        int mouseY = (int) event.getMouseY();
        
        // Try to get waystone information under cursor
        try {
            Object waystoneList = currentWaystoneList.get();
            if (waystoneList == null) return;
            
            // Get the hovered waystone using reflection
            Field hoveredField = findField(waystoneList.getClass(), "hovered", "hoveredSlot");
            if (hoveredField != null) {
                hoveredField.setAccessible(true);
                Object hovered = hoveredField.get(waystoneList);
                
                if (hovered != null) {
                    // Extract waystone information
                    List<Component> tooltip = getWaystoneTooltip(hovered);
                    if (!tooltip.isEmpty()) {
                        graphics.renderComponentTooltip(Minecraft.getInstance().font, tooltip, mouseX, mouseY);
                    }
                }
            }
        } catch (Exception e) {
            // Silently fail - tooltip is optional
        }
    }
    
    private static List<Component> getWaystoneTooltip(Object waystone) {
        List<Component> tooltip = new ArrayList<>();
        
        try {
            // Try to extract waystone name
            Field nameField = findField(waystone.getClass(), "name", "waystoneData");
            if (nameField != null) {
                nameField.setAccessible(true);
                Object name = nameField.get(waystone);
                if (name != null) {
                    tooltip.add(Component.literal("§6Waystone Details"));
                    tooltip.add(Component.literal("§7Name: §f" + name.toString()));
                }
            }
            
            // Try to extract global flag
            Field globalField = findField(waystone.getClass(), "isGlobal", "global");
            if (globalField != null) {
                globalField.setAccessible(true);
                Object isGlobal = globalField.get(waystone);
                if (isGlobal instanceof Boolean && (Boolean) isGlobal) {
                    tooltip.add(Component.literal("§9Global Waystone"));
                }
            }
            
            // Try to extract dimension
            Field dimensionField = findField(waystone.getClass(), "dimension", "level");
            if (dimensionField != null) {
                dimensionField.setAccessible(true);
                Object dimension = dimensionField.get(waystone);
                if (dimension != null) {
                    tooltip.add(Component.literal("§7Dimension: §f" + dimension.toString()));
                }
            }
            
        } catch (Exception e) {
            // Return empty tooltip on error
            tooltip.clear();
        }
        
        return tooltip;
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

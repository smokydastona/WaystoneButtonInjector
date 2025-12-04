package com.example.waystoneinjector.client;

import com.example.waystoneinjector.config.WaystoneConfig;
import net.minecraft.client.Minecraft;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "waystoneinjector", bus = Mod.EventBusSubscriber.Bus.FORGE)
@SuppressWarnings("null")
public class ClientEvents {

    static {
        System.out.println("[WaystoneInjector] ClientEvents class loaded!");
    }

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
        
        // Add custom server transfer buttons
        addCustomButtons(event, screen);
        
        // Add search box enhancement
        addSearchBoxEnhancement(event, screen);
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
                    
                    // Enhance the search box with right-click to clear
                    System.out.println("[WaystoneInjector] Found search box, adding right-click clear feature");
                    
                    // The search box already exists, we just log that we found it
                    // Right-click clear will be added via screen event in next phase
                    System.out.println("[WaystoneInjector] Search box position: x=" + box.getX() + ", y=" + box.getY());
                }
            }
        } catch (Exception e) {
            System.out.println("[WaystoneInjector] Could not enhance search box: " + e.getMessage());
        }
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

package com.example.waystoneinjector.client.gui.screen;

import com.example.waystoneinjector.client.gui.widget.ThemedButton;
import com.example.waystoneinjector.config.WaystoneConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CustomWaystoneScreen extends Screen {
    
    private static final ResourceLocation BACKGROUND = new ResourceLocation("waystones", "textures/gui/waystone_selection.png");
    private final Screen originalScreen;
    private final Object originalMenu;
    private EditBox searchBox;
    private ScrollableWaystoneList waystoneList;
    private final Supplier<String> waystoneTypeSupplier;
    
    private List<WaystoneEntry> allWaystones = new ArrayList<>();
    private List<WaystoneEntry> filteredWaystones = new ArrayList<>();
    
    public CustomWaystoneScreen(Screen originalScreen, Supplier<String> waystoneTypeSupplier) {
        super(Component.literal("Waystones"));
        this.originalScreen = originalScreen;
        this.waystoneTypeSupplier = waystoneTypeSupplier;
        this.originalMenu = extractMenu(originalScreen);
        this.allWaystones = extractWaystones(originalScreen);
        this.filteredWaystones = new ArrayList<>(allWaystones);
    }
    
    private Object extractMenu(Screen screen) {
        try {
            for (Field field : screen.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(screen);
                if (value != null && value.getClass().getSimpleName().contains("Menu")) {
                    System.out.println("[CustomWaystoneScreen] Found menu: " + value.getClass().getName());
                    return value;
                }
            }
        } catch (Exception e) {
            System.err.println("[CustomWaystoneScreen] Error extracting menu: " + e.getMessage());
        }
        return null;
    }
    
    private List<WaystoneEntry> extractWaystones(Screen screen) {
        List<WaystoneEntry> waystones = new ArrayList<>();
        try {
            // Find the waystone list widget
            for (Field field : screen.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(screen);
                
                if (value != null && value.getClass().getSimpleName().contains("WaystoneList")) {
                    System.out.println("[CustomWaystoneScreen] Found waystone list");
                    
                    // Try to get children/entries
                    for (Field listField : value.getClass().getSuperclass().getDeclaredFields()) {
                        listField.setAccessible(true);
                        if (listField.getName().equals("children")) {
                            Object children = listField.get(value);
                            if (children instanceof List) {
                                List<?> entries = (List<?>) children;
                                System.out.println("[CustomWaystoneScreen] Found " + entries.size() + " waystone entries");
                                
                                for (Object entry : entries) {
                                    WaystoneEntry waystoneEntry = extractWaystoneFromEntry(entry);
                                    if (waystoneEntry != null) {
                                        waystones.add(waystoneEntry);
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("[CustomWaystoneScreen] Error extracting waystones: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("[CustomWaystoneScreen] Extracted " + waystones.size() + " waystones");
        return waystones;
    }
    
    private WaystoneEntry extractWaystoneFromEntry(Object entry) {
        try {
            // Get the waystone object from the entry
            for (Field field : entry.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(entry);
                
                if (value != null && value.getClass().getSimpleName().equals("Waystone")) {
                    String name = getWaystoneName(value);
                    Object waystoneId = getWaystoneId(value);
                    return new WaystoneEntry(name, waystoneId, value, entry);
                }
            }
        } catch (Exception e) {
            System.err.println("[CustomWaystoneScreen] Error extracting waystone from entry: " + e.getMessage());
        }
        return null;
    }
    
    private String getWaystoneName(Object waystone) {
        try {
            Method method = waystone.getClass().getMethod("getWaystoneName");
            Object name = method.invoke(waystone);
            return name != null ? name.toString() : "Unknown";
        } catch (Exception e) {
            try {
                // Try as a field
                Field field = waystone.getClass().getDeclaredField("name");
                field.setAccessible(true);
                Object name = field.get(waystone);
                return name != null ? name.toString() : "Unknown";
            } catch (Exception e2) {
                return "Unknown";
            }
        }
    }
    
    private Object getWaystoneId(Object waystone) {
        try {
            Method method = waystone.getClass().getMethod("getWaystoneUid");
            return method.invoke(waystone);
        } catch (Exception e) {
            try {
                Field field = waystone.getClass().getDeclaredField("waystoneUid");
                field.setAccessible(true);
                return field.get(waystone);
            } catch (Exception e2) {
                return null;
            }
        }
    }
    
    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int listWidth = 300;
        int listHeight = this.height - 120;
        
        // Create search box
        this.searchBox = new EditBox(this.font, centerX - 150, 40, 200, 20, Component.literal("Search"));
        this.searchBox.setResponder(this::onSearchChanged);
        this.addRenderableWidget(this.searchBox);
        
        // Create scrollable waystone list
        this.waystoneList = new ScrollableWaystoneList(
            this.minecraft, 
            listWidth, 
            listHeight, 
            70, 
            this.height - 40,
            25,
            this
        );
        this.waystoneList.updateEntries(filteredWaystones);
        this.addRenderableWidget(this.waystoneList);
        
        // Add custom themed buttons at the bottom
        int buttonY = this.height - 30;
        int buttonSpacing = 75;
        int startX = centerX - (buttonSpacing * 2);
        
        // Home button
        this.addRenderableWidget(new ThemedButton(
            startX, buttonY, 70, 20,
            Component.literal("Home"),
            waystoneTypeSupplier,
            (button) -> onWaystoneAction("home")
        ));
        
        // Random button
        this.addRenderableWidget(new ThemedButton(
            startX + buttonSpacing, buttonY, 70, 20,
            Component.literal("Random"),
            waystoneTypeSupplier,
            (button) -> onWaystoneAction("random")
        ));
        
        // Invite button
        this.addRenderableWidget(new ThemedButton(
            startX + buttonSpacing * 2, buttonY, 70, 20,
            Component.literal("Invite"),
            waystoneTypeSupplier,
            (button) -> onWaystoneAction("invite")
        ));
        
        // Settings button
        this.addRenderableWidget(new ThemedButton(
            startX + buttonSpacing * 3, buttonY, 70, 20,
            Component.literal("Settings"),
            waystoneTypeSupplier,
            (button) -> onWaystoneAction("settings")
        ));
        
        // Close button
        this.addRenderableWidget(Button.builder(Component.literal("Close"), (button) -> {
            this.minecraft.setScreen(null);
        }).bounds(centerX + 160, buttonY, 60, 20).build());
    }
    
    private void onSearchChanged(String searchText) {
        filteredWaystones.clear();
        
        if (searchText.isEmpty()) {
            filteredWaystones.addAll(allWaystones);
        } else {
            String lowerSearch = searchText.toLowerCase();
            for (WaystoneEntry waystone : allWaystones) {
                if (waystone.name.toLowerCase().contains(lowerSearch)) {
                    filteredWaystones.add(waystone);
                }
            }
        }
        
        if (waystoneList != null) {
            waystoneList.updateEntries(filteredWaystones);
        }
    }
    
    private void onWaystoneAction(String action) {
        System.out.println("[CustomWaystoneScreen] Action: " + action);
        
        try {
            // Call the corresponding method on the original screen/menu via reflection
            if (originalMenu != null) {
                Method method = findMethod(originalMenu.getClass(), action);
                if (method != null) {
                    method.setAccessible(true);
                    method.invoke(originalMenu);
                    this.minecraft.setScreen(null);
                }
            }
        } catch (Exception e) {
            System.err.println("[CustomWaystoneScreen] Error invoking action: " + e.getMessage());
        }
    }
    
    private void onWaystoneSelected(WaystoneEntry waystone) {
        System.out.println("[CustomWaystoneScreen] Selected waystone: " + waystone.name);
        
        try {
            // Trigger teleport via reflection on the original entry
            if (waystone.originalEntry != null) {
                Method method = findMethod(waystone.originalEntry.getClass(), "onPress", "onClick", "teleport");
                if (method != null) {
                    method.setAccessible(true);
                    if (method.getParameterCount() == 0) {
                        method.invoke(waystone.originalEntry);
                    } else {
                        method.invoke(waystone.originalEntry, 0.0, 0.0);
                    }
                    this.minecraft.setScreen(null);
                }
            }
        } catch (Exception e) {
            System.err.println("[CustomWaystoneScreen] Error teleporting: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Method findMethod(Class<?> clazz, String... names) {
        for (String name : names) {
            try {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getName().toLowerCase().contains(name.toLowerCase())) {
                        return method;
                    }
                }
            } catch (Exception e) {
                // Continue
            }
        }
        return null;
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        
        // Render title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        
        // Render waystone count
        String count = filteredWaystones.size() + " / " + allWaystones.size() + " waystones";
        graphics.drawString(this.font, count, this.width / 2 + 60, 45, 0xAAAAAA);
        
        super.render(graphics, mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    // Inner class for waystone entries
    private static class WaystoneEntry {
        final String name;
        final Object id;
        final Object waystone;
        final Object originalEntry;
        
        WaystoneEntry(String name, Object id, Object waystone, Object originalEntry) {
            this.name = name;
            this.id = id;
            this.waystone = waystone;
            this.originalEntry = originalEntry;
        }
    }
    
    // Scrollable list widget
    private class ScrollableWaystoneList extends net.minecraft.client.gui.components.AbstractSelectionList<ScrollableWaystoneList.Entry> {
        private final CustomWaystoneScreen screen;
        
        public ScrollableWaystoneList(Minecraft minecraft, int width, int height, int y0, int y1, int itemHeight, CustomWaystoneScreen screen) {
            super(minecraft, width, height, y0, y1, itemHeight);
            this.screen = screen;
            this.setRenderBackground(false);
            this.setRenderTopAndBottom(false);
        }
        
        public void updateEntries(List<WaystoneEntry> waystones) {
            this.clearEntries();
            for (WaystoneEntry waystone : waystones) {
                this.addEntry(new Entry(waystone));
            }
        }
        
        @Override
        public int getRowWidth() {
            return this.width - 20;
        }
        
        @Override
        protected int getScrollbarPosition() {
            return this.width / 2 + this.width / 2 - 6;
        }
        
        class Entry extends net.minecraft.client.gui.components.AbstractSelectionList.Entry<Entry> {
            private final WaystoneEntry waystone;
            
            Entry(WaystoneEntry waystone) {
                this.waystone = waystone;
            }
            
            @Override
            public void render(GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                // Draw background when hovered
                if (isMouseOver) {
                    graphics.fill(left, top, left + width, top + height, 0x80FFFFFF);
                }
                
                // Draw waystone name
                graphics.drawString(screen.font, waystone.name, left + 5, top + 5, 0xFFFFFF);
            }
            
            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                screen.onWaystoneSelected(waystone);
                return true;
            }
            
            @Override
            public Component getNarration() {
                return Component.literal(waystone.name);
            }
        }
    }
}

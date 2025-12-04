package com.example.waystoneinjector.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

public class WaystoneConfig {
    public static final ForgeConfigSpec SPEC;
    
    // Individual button configs
    public static final ForgeConfigSpec.BooleanValue BUTTON1_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON1_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON1_COMMAND;
    public static final ForgeConfigSpec.IntValue BUTTON1_WIDTH;
    public static final ForgeConfigSpec.IntValue BUTTON1_HEIGHT;
    public static final ForgeConfigSpec.IntValue BUTTON1_X_OFFSET;
    public static final ForgeConfigSpec.IntValue BUTTON1_Y_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON1_TEXT_COLOR;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON1_SIDE;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON1_DEATH_REDIRECT;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON1_SLEEP_REDIRECT;
    public static final ForgeConfigSpec.IntValue BUTTON1_SLEEP_CHANCE;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON2_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_COMMAND;
    public static final ForgeConfigSpec.IntValue BUTTON2_WIDTH;
    public static final ForgeConfigSpec.IntValue BUTTON2_HEIGHT;
    public static final ForgeConfigSpec.IntValue BUTTON2_X_OFFSET;
    public static final ForgeConfigSpec.IntValue BUTTON2_Y_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_TEXT_COLOR;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_SIDE;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_DEATH_REDIRECT;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_SLEEP_REDIRECT;
    public static final ForgeConfigSpec.IntValue BUTTON2_SLEEP_CHANCE;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON3_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_COMMAND;
    public static final ForgeConfigSpec.IntValue BUTTON3_WIDTH;
    public static final ForgeConfigSpec.IntValue BUTTON3_HEIGHT;
    public static final ForgeConfigSpec.IntValue BUTTON3_X_OFFSET;
    public static final ForgeConfigSpec.IntValue BUTTON3_Y_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_TEXT_COLOR;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_SIDE;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_DEATH_REDIRECT;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_SLEEP_REDIRECT;
    public static final ForgeConfigSpec.IntValue BUTTON3_SLEEP_CHANCE;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON4_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_COMMAND;
    public static final ForgeConfigSpec.IntValue BUTTON4_WIDTH;
    public static final ForgeConfigSpec.IntValue BUTTON4_HEIGHT;
    public static final ForgeConfigSpec.IntValue BUTTON4_X_OFFSET;
    public static final ForgeConfigSpec.IntValue BUTTON4_Y_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_TEXT_COLOR;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_SIDE;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_DEATH_REDIRECT;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_SLEEP_REDIRECT;
    public static final ForgeConfigSpec.IntValue BUTTON4_SLEEP_CHANCE;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON5_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_COMMAND;
    public static final ForgeConfigSpec.IntValue BUTTON5_WIDTH;
    public static final ForgeConfigSpec.IntValue BUTTON5_HEIGHT;
    public static final ForgeConfigSpec.IntValue BUTTON5_X_OFFSET;
    public static final ForgeConfigSpec.IntValue BUTTON5_Y_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_TEXT_COLOR;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_SIDE;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_DEATH_REDIRECT;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_SLEEP_REDIRECT;
    public static final ForgeConfigSpec.IntValue BUTTON5_SLEEP_CHANCE;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON6_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_COMMAND;
    public static final ForgeConfigSpec.IntValue BUTTON6_WIDTH;
    public static final ForgeConfigSpec.IntValue BUTTON6_HEIGHT;
    public static final ForgeConfigSpec.IntValue BUTTON6_X_OFFSET;
    public static final ForgeConfigSpec.IntValue BUTTON6_Y_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_TEXT_COLOR;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_SIDE;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_DEATH_REDIRECT;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_SLEEP_REDIRECT;
    public static final ForgeConfigSpec.IntValue BUTTON6_SLEEP_CHANCE;
    
    // Feverdream integration settings (built-in client-side death/sleep detection)
    public static final ForgeConfigSpec.ConfigValue<java.util.List<? extends String>> FEVERDREAM_REDIRECTS;
    public static final ForgeConfigSpec.IntValue FEVERDREAM_DEATH_COUNT;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("═══════════════════════════════════════════════════════════════════════════════",
                        "  Waystone Button Injector - Client Configuration",
                        "═══════════════════════════════════════════════════════════════════════════════",
                        "",
                        "QUICK START GUIDE:",
                        "  1. Enable a button: Set 'enabled = true'",
                        "  2. Set button label: Use plain text or color codes (&a = green, &c = red, etc.)",
                        "  3. Set command: Use 'redirect @s server.ip:port' OR any server command",
                        "  4. Customize appearance (optional): width, height, position, color",
                        "",
                        "═══════════════════════════════════════════════════════════════════════════════",
                        "  COMPLETE BUTTON EXAMPLE",
                        "═══════════════════════════════════════════════════════════════════════════════",
                        "",
                        "[button1]",
                        "    # Turn this button ON",
                        "    enabled = true",
                        "",
                        "    # Button text (supports Minecraft color codes with &)",
                        "    label = \"&aHub Server\"",
                        "",
                        "    # What happens when you click the button:",
                        "    # - For server transfers: \"redirect @s server.address.com:25565\"",
                        "    # - For commands: \"spawn\", \"warp lobby\", \"waystone teleport MyWaystone\"",
                        "    command = \"redirect @s hub.example.com:25565\"",
                        "",
                        "    # BUTTON APPEARANCE (all optional - leave as default if unsure)",
                        "    width = 80          # Button width in pixels (default: 60, range: 20-200)",
                        "    height = 35         # Button height in pixels (default: 30, range: 15-100)",
                        "    textColor = \"0x00FF00\"  # Hex color for text (0xFFFFFF=white, 0xFF0000=red, 0x00FF00=green)",
                        "",
                        "    # BUTTON POSITION (all optional - defaults work for most setups)",
                        "    side = \"left\"       # Where to place: \"auto\", \"left\", or \"right\"",
                        "    xOffset = 0         # Move left/right from default position (-500 to 500)",
                        "    yOffset = -10       # Move up/down from default position (-500 to 500)",
                        "",
                        "    # AUTOMATIC EVENTS - These trigger when you're ON hub.example.com:25565",
                        "    # (The mod detects which server you're connected to automatically)",
                        "",
                        "    # When you DIE while playing on hub.example.com, run this command:",
                        "    deathRedirect = \"spawn\"",
                        "",
                        "    # When you SLEEP while playing on hub.example.com, run this command:",
                        "    sleepRedirect = \"warp dream\"",
                        "",
                        "    # Sleep redirect chance (0-100%, only applies if sleepRedirect is set)",
                        "    sleepChance = 100",
                        "",
                        "═══════════════════════════════════════════════════════════════════════════════",
                        "  COMMON COLOR CODES FOR LABELS",
                        "═══════════════════════════════════════════════════════════════════════════════",
                        "  &0 = Black    &1 = Dark Blue   &2 = Dark Green   &3 = Dark Aqua",
                        "  &4 = Dark Red &5 = Dark Purple &6 = Gold         &7 = Gray",
                        "  &8 = Dark Gray &9 = Blue       &a = Green        &b = Aqua",
                        "  &c = Red      &d = Light Purple &e = Yellow      &f = White",
                        "  &l = Bold     &o = Italic      &n = Underline    &r = Reset",
                        "",
                        "═══════════════════════════════════════════════════════════════════════════════",
                        "  COMMON TEXT COLORS (HEX)",
                        "═══════════════════════════════════════════════════════════════════════════════",
                        "  0xFFFFFF = White     0xFF0000 = Red        0x00FF00 = Green",
                        "  0x0000FF = Blue      0xFFFF00 = Yellow     0xFF00FF = Magenta",
                        "  0x00FFFF = Cyan      0xFFA500 = Orange     0x800080 = Purple",
                        "  0xFFD700 = Gold      0xC0C0C0 = Silver     0x808080 = Gray",
                        "",
                        "═══════════════════════════════════════════════════════════════════════════════");
        
        // ═══════════════════════════════════════════════════════════════════════════════
        // Button 1 - Configure your first custom button here!
        // ═══════════════════════════════════════════════════════════════════════════════
        builder.push("button1");
        
        builder.comment("",
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
                        "  BUTTON 1 CONFIGURATION",
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        BUTTON1_ENABLED = builder
                .comment("",
                        "Enable this button? Set to 'true' to activate",
                        "")
                .define("enabled", false);
        
        BUTTON1_LABEL = builder
                .comment("Button label (supports & color codes: &aGreen, &cRed, &bBlue, etc.)")
                .define("label", "Button 1");
        
        BUTTON1_COMMAND = builder
                .comment("Command when clicked:",
                        "  - Server transfer: 'redirect @s server.ip:25565'",
                        "  - Any command: 'spawn', 'warp lobby', 'waystone teleport Home'")
                .define("command", "");
        
        builder.comment("",
                        "────────────────────────────────────────────────────────────────────────────────",
                        "  Appearance Settings (Optional - leave as default if unsure)",
                        "────────────────────────────────────────────────────────────────────────────────");
        
        BUTTON1_WIDTH = builder
                .comment("Button width in pixels (20-200)")
                .defineInRange("width", 60, 20, 200);
        
        BUTTON1_HEIGHT = builder
                .comment("Button height in pixels (15-100)")
                .defineInRange("height", 30, 15, 100);
        
        BUTTON1_TEXT_COLOR = builder
                .comment("Text color in hex (0xFFFFFF=white, 0xFF0000=red, 0x00FF00=green)")
                .define("textColor", "0xFFFFFF");
        
        builder.comment("",
                        "────────────────────────────────────────────────────────────────────────────────",
                        "  Position Settings (Optional)",
                        "────────────────────────────────────────────────────────────────────────────────");
        
        BUTTON1_SIDE = builder
                .comment("Placement: 'auto' (balanced), 'left' (force left side), 'right' (force right side)")
                .define("side", "auto");
        
        BUTTON1_X_OFFSET = builder
                .comment("Horizontal adjustment in pixels (negative = left, positive = right)")
                .defineInRange("xOffset", 0, -500, 500);
        
        BUTTON1_Y_OFFSET = builder
                .comment("Vertical adjustment in pixels (negative = up, positive = down)")
                .defineInRange("yOffset", 0, -500, 500);
        
        builder.comment("",
                        "────────────────────────────────────────────────────────────────────────────────",
                        "  Automatic Events - Triggered when you're ON this button's server",
                        "  (The mod auto-detects which server matches this button's 'command')",
                        "────────────────────────────────────────────────────────────────────────────────");
        
        BUTTON1_DEATH_REDIRECT = builder
                .comment("When you DIE while connected to THIS button's server, run this command",
                        "Examples: 'spawn', 'warp hub', 'waystone teleport Home', 'redirect @s another.server.com'")
                .define("deathRedirect", "");
        
        BUTTON1_SLEEP_REDIRECT = builder
                .comment("When you SLEEP while connected to THIS button's server, run this command")
                .define("sleepRedirect", "");
        
        BUTTON1_SLEEP_CHANCE = builder
                .comment("Sleep redirect chance: 100=always, 50=half the time, 0=never")
                .defineInRange("sleepChance", 100, 0, 100);
        
        builder.pop();
        
        // ═══════════════════════════════════════════════════════════════════════════════
        // Button 2 - Same options as Button 1
        // ═══════════════════════════════════════════════════════════════════════════════
        builder.push("button2");
        builder.comment("",
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
                        "  BUTTON 2 CONFIGURATION",
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        BUTTON2_ENABLED = builder.define("enabled", false);
        BUTTON2_LABEL = builder.define("label", "Button 2");
        BUTTON2_COMMAND = builder.define("command", "");
        BUTTON2_WIDTH = builder.defineInRange("width", 60, 20, 200);
        BUTTON2_HEIGHT = builder.defineInRange("height", 30, 15, 100);
        BUTTON2_TEXT_COLOR = builder.define("textColor", "0xFFFFFF");
        BUTTON2_SIDE = builder.define("side", "auto");
        BUTTON2_X_OFFSET = builder.defineInRange("xOffset", 0, -500, 500);
        BUTTON2_Y_OFFSET = builder.defineInRange("yOffset", 0, -500, 500);
        BUTTON2_DEATH_REDIRECT = builder.define("deathRedirect", "");
        BUTTON2_SLEEP_REDIRECT = builder.define("sleepRedirect", "");
        BUTTON2_SLEEP_CHANCE = builder.defineInRange("sleepChance", 100, 0, 100);
        builder.pop();
        
        // ═══════════════════════════════════════════════════════════════════════════════
        // Button 3
        // ═══════════════════════════════════════════════════════════════════════════════
        builder.push("button3");
        BUTTON3_ENABLED = builder.define("enabled", false);
        BUTTON3_LABEL = builder.define("label", "Button 3");
        BUTTON3_COMMAND = builder.define("command", "");
        BUTTON3_WIDTH = builder.defineInRange("width", 60, 20, 200);
        BUTTON3_HEIGHT = builder.defineInRange("height", 30, 15, 100);
        BUTTON3_TEXT_COLOR = builder.define("textColor", "0xFFFFFF");
        BUTTON3_SIDE = builder.define("side", "auto");
        BUTTON3_X_OFFSET = builder.defineInRange("xOffset", 0, -500, 500);
        BUTTON3_Y_OFFSET = builder.defineInRange("yOffset", 0, -500, 500);
        BUTTON3_DEATH_REDIRECT = builder.define("deathRedirect", "");
        BUTTON3_SLEEP_REDIRECT = builder.define("sleepRedirect", "");
        BUTTON3_SLEEP_CHANCE = builder.defineInRange("sleepChance", 100, 0, 100);
        builder.pop();
        
        // ═══════════════════════════════════════════════════════════════════════════════
        // Button 4
        // ═══════════════════════════════════════════════════════════════════════════════
        builder.push("button4");
        BUTTON4_ENABLED = builder.define("enabled", false);
        BUTTON4_LABEL = builder.define("label", "Button 4");
        BUTTON4_COMMAND = builder.define("command", "");
        BUTTON4_WIDTH = builder.defineInRange("width", 60, 20, 200);
        BUTTON4_HEIGHT = builder.defineInRange("height", 30, 15, 100);
        BUTTON4_TEXT_COLOR = builder.define("textColor", "0xFFFFFF");
        BUTTON4_SIDE = builder.define("side", "auto");
        BUTTON4_X_OFFSET = builder.defineInRange("xOffset", 0, -500, 500);
        BUTTON4_Y_OFFSET = builder.defineInRange("yOffset", 0, -500, 500);
        BUTTON4_DEATH_REDIRECT = builder.define("deathRedirect", "");
        BUTTON4_SLEEP_REDIRECT = builder.define("sleepRedirect", "");
        BUTTON4_SLEEP_CHANCE = builder.defineInRange("sleepChance", 100, 0, 100);
        builder.pop();
        
        // ═══════════════════════════════════════════════════════════════════════════════
        // Button 5
        // ═══════════════════════════════════════════════════════════════════════════════
        builder.push("button5");
        BUTTON5_ENABLED = builder.define("enabled", false);
        BUTTON5_LABEL = builder.define("label", "Button 5");
        BUTTON5_COMMAND = builder.define("command", "");
        BUTTON5_WIDTH = builder.defineInRange("width", 60, 20, 200);
        BUTTON5_HEIGHT = builder.defineInRange("height", 30, 15, 100);
        BUTTON5_TEXT_COLOR = builder.define("textColor", "0xFFFFFF");
        BUTTON5_SIDE = builder.define("side", "auto");
        BUTTON5_X_OFFSET = builder.defineInRange("xOffset", 0, -500, 500);
        BUTTON5_Y_OFFSET = builder.defineInRange("yOffset", 0, -500, 500);
        BUTTON5_DEATH_REDIRECT = builder.define("deathRedirect", "");
        BUTTON5_SLEEP_REDIRECT = builder.define("sleepRedirect", "");
        BUTTON5_SLEEP_CHANCE = builder.defineInRange("sleepChance", 100, 0, 100);
        builder.pop();
        
        // ═══════════════════════════════════════════════════════════════════════════════
        // Button 6
        // ═══════════════════════════════════════════════════════════════════════════════
        builder.push("button6");
        BUTTON6_ENABLED = builder.define("enabled", false);
        BUTTON6_LABEL = builder.define("label", "Button 6");
        BUTTON6_COMMAND = builder.define("command", "");
        BUTTON6_WIDTH = builder.defineInRange("width", 60, 20, 200);
        BUTTON6_HEIGHT = builder.defineInRange("height", 30, 15, 100);
        BUTTON6_TEXT_COLOR = builder.define("textColor", "0xFFFFFF");
        BUTTON6_SIDE = builder.define("side", "auto");
        BUTTON6_X_OFFSET = builder.defineInRange("xOffset", 0, -500, 500);
        BUTTON6_Y_OFFSET = builder.defineInRange("yOffset", 0, -500, 500);
        BUTTON6_DEATH_REDIRECT = builder.define("deathRedirect", "");
        BUTTON6_SLEEP_REDIRECT = builder.define("sleepRedirect", "");
        BUTTON6_SLEEP_CHANCE = builder.defineInRange("sleepChance", 100, 0, 100);
        builder.pop();
        
        // Feverdream Integration Settings (Built-in Death/Sleep Detection)
        builder.push("feverdream");
        builder.comment("Client-side death and sleep detection - no server-side mod required!",
                        "Each server can have its own death and sleep redirect settings");
        FEVERDREAM_REDIRECTS = builder
                .comment("Redirect mappings - each line configures one server's behavior",
                        "Format: 'type:currentServer->destinationServer'",
                        "Types:",
                        "  'death:' - Redirects when you die on this server",
                        "  'sleep:' - Redirects when you wake up from sleep on this server",
                        "Examples:",
                        "  'death:survival.example.com->hub.example.com'  (die on survival, go to hub)",
                        "  'sleep:192.168.1.100:25565->192.168.1.101:25565'  (sleep on server 1, wake on server 2)",
                        "  'death:localhost:25565->localhost:25566'  (die on port 25565, respawn on 25566)",
                        "Each server can have both death and sleep redirects - just add separate entries")
                .defineList("redirects", java.util.Arrays.asList(), 
                           obj -> obj instanceof String && ((String)obj).contains("->") && 
                                  (((String)obj).startsWith("death:") || ((String)obj).startsWith("sleep:")));
        FEVERDREAM_DEATH_COUNT = builder
                .comment("Number of deaths before auto-redirect activates (1-10)",
                        "Set to 1 to redirect on first death",
                        "Set to 2 to redirect on second death, etc.",
                        "Set to 10 to require 10 deaths before redirecting",
                        "Only applies if deathRedirectEnabled is true")
                .defineInRange("deathCount", 1, 1, 10);
        builder.pop();

        SPEC = builder.build();
    }
    
    // Helper method to get enabled buttons as lists (for compatibility)
    public static List<String> getEnabledLabels() {
        List<String> labels = new ArrayList<>();
        if (BUTTON1_ENABLED.get()) labels.add(BUTTON1_LABEL.get());
        if (BUTTON2_ENABLED.get()) labels.add(BUTTON2_LABEL.get());
        if (BUTTON3_ENABLED.get()) labels.add(BUTTON3_LABEL.get());
        if (BUTTON4_ENABLED.get()) labels.add(BUTTON4_LABEL.get());
        if (BUTTON5_ENABLED.get()) labels.add(BUTTON5_LABEL.get());
        if (BUTTON6_ENABLED.get()) labels.add(BUTTON6_LABEL.get());
        return labels;
    }
    
    public static List<String> getEnabledCommands() {
        List<String> commands = new ArrayList<>();
        if (BUTTON1_ENABLED.get()) commands.add(BUTTON1_COMMAND.get());
        if (BUTTON2_ENABLED.get()) commands.add(BUTTON2_COMMAND.get());
        if (BUTTON3_ENABLED.get()) commands.add(BUTTON3_COMMAND.get());
        if (BUTTON4_ENABLED.get()) commands.add(BUTTON4_COMMAND.get());
        if (BUTTON5_ENABLED.get()) commands.add(BUTTON5_COMMAND.get());
        if (BUTTON6_ENABLED.get()) commands.add(BUTTON6_COMMAND.get());
        return commands;
    }
    
    // Feverdream config getters
    public static String getDeathRedirectServer(String currentServer) {
        // Check each button's command to see if it matches the current server
        // If it does, return that button's death redirect setting
        if (BUTTON1_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON1_COMMAND.get())) {
            String redirect = BUTTON1_DEATH_REDIRECT.get();
            return redirect.isEmpty() ? null : redirect;
        }
        if (BUTTON2_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON2_COMMAND.get())) {
            String redirect = BUTTON2_DEATH_REDIRECT.get();
            return redirect.isEmpty() ? null : redirect;
        }
        if (BUTTON3_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON3_COMMAND.get())) {
            String redirect = BUTTON3_DEATH_REDIRECT.get();
            return redirect.isEmpty() ? null : redirect;
        }
        if (BUTTON4_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON4_COMMAND.get())) {
            String redirect = BUTTON4_DEATH_REDIRECT.get();
            return redirect.isEmpty() ? null : redirect;
        }
        if (BUTTON5_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON5_COMMAND.get())) {
            String redirect = BUTTON5_DEATH_REDIRECT.get();
            return redirect.isEmpty() ? null : redirect;
        }
        if (BUTTON6_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON6_COMMAND.get())) {
            String redirect = BUTTON6_DEATH_REDIRECT.get();
            return redirect.isEmpty() ? null : redirect;
        }
        
        // Fall back to old feverdream.redirects format for backwards compatibility
        for (String mapping : FEVERDREAM_REDIRECTS.get()) {
            if (!mapping.startsWith("death:")) continue;
            
            String cleanMapping = mapping.substring(6);
            String[] parts = cleanMapping.split("->");
            if (parts.length == 2) {
                String source = parts[0].trim();
                String destination = parts[1].trim();
                if (currentServer.toLowerCase().contains(source.toLowerCase()) ||
                    source.toLowerCase().contains(currentServer.toLowerCase())) {
                    return destination;
                }
            }
        }
        return null;
    }
    
    public static int getFeverdreamDeathCount() {
        return FEVERDREAM_DEATH_COUNT.get();
    }
    
    public static String getSleepRedirectServer(String currentServer) {
        // Check each button's command to see if it matches the current server
        // If it does, return that button's sleep redirect setting
        if (BUTTON1_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON1_COMMAND.get())) {
            String redirect = BUTTON1_SLEEP_REDIRECT.get();
            return redirect.isEmpty() ? null : redirect;
        }
        if (BUTTON2_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON2_COMMAND.get())) {
            String redirect = BUTTON2_SLEEP_REDIRECT.get();
            return redirect.isEmpty() ? null : redirect;
        }
        if (BUTTON3_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON3_COMMAND.get())) {
            String redirect = BUTTON3_SLEEP_REDIRECT.get();
            return redirect.isEmpty() ? null : redirect;
        }
        if (BUTTON4_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON4_COMMAND.get())) {
            String redirect = BUTTON4_SLEEP_REDIRECT.get();
            return redirect.isEmpty() ? null : redirect;
        }
        if (BUTTON5_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON5_COMMAND.get())) {
            String redirect = BUTTON5_SLEEP_REDIRECT.get();
            return redirect.isEmpty() ? null : redirect;
        }
        if (BUTTON6_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON6_COMMAND.get())) {
            String redirect = BUTTON6_SLEEP_REDIRECT.get();
            return redirect.isEmpty() ? null : redirect;
        }
        
        // Fall back to old feverdream.redirects format for backwards compatibility
        for (String mapping : FEVERDREAM_REDIRECTS.get()) {
            if (!mapping.startsWith("sleep:")) continue;
            
            // Remove "sleep:" prefix
            String cleanMapping = mapping.substring(6);
            String[] parts = cleanMapping.split("->");
            if (parts.length == 2) {
                String source = parts[0].trim();
                String destination = parts[1].trim();
                // Match current server (case-insensitive, partial match)
                if (currentServer.toLowerCase().contains(source.toLowerCase()) ||
                    source.toLowerCase().contains(currentServer.toLowerCase())) {
                    return destination;
                }
            }
        }
        return null; // No mapping found
    }
    
    // Get sleep chance percentage for current server
    public static int getSleepChance(String currentServer) {
        if (BUTTON1_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON1_COMMAND.get())) {
            return BUTTON1_SLEEP_CHANCE.get();
        }
        if (BUTTON2_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON2_COMMAND.get())) {
            return BUTTON2_SLEEP_CHANCE.get();
        }
        if (BUTTON3_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON3_COMMAND.get())) {
            return BUTTON3_SLEEP_CHANCE.get();
        }
        if (BUTTON4_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON4_COMMAND.get())) {
            return BUTTON4_SLEEP_CHANCE.get();
        }
        if (BUTTON5_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON5_COMMAND.get())) {
            return BUTTON5_SLEEP_CHANCE.get();
        }
        if (BUTTON6_ENABLED.get() && serverMatchesCommand(currentServer, BUTTON6_COMMAND.get())) {
            return BUTTON6_SLEEP_CHANCE.get();
        }
        return 100; // Default 100% if no button matches
    }
    
    // Helper method to check if current server matches a button's command
    private static boolean serverMatchesCommand(String currentServer, String command) {
        // Parse "redirect server.address.com" or "redirect @s server.address.com"
        String[] parts = command.trim().split("\\s+");
        if (parts.length >= 2 && parts[0].equalsIgnoreCase("redirect")) {
            String serverAddress;
            if (parts[1].equals("@s") && parts.length >= 3) {
                serverAddress = parts[2];
            } else {
                serverAddress = parts[1];
            }
            // Match current server (case-insensitive, partial match)
            return currentServer.toLowerCase().contains(serverAddress.toLowerCase()) ||
                   serverAddress.toLowerCase().contains(currentServer.toLowerCase());
        }
        return false;
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SPEC, "waystoneinjector-client.toml");
    }
}

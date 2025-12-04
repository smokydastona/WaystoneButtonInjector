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
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON1_DEATH_REDIRECT;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON1_SLEEP_REDIRECT;
    public static final ForgeConfigSpec.IntValue BUTTON1_SLEEP_CHANCE;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON2_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_COMMAND;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_DEATH_REDIRECT;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_SLEEP_REDIRECT;
    public static final ForgeConfigSpec.IntValue BUTTON2_SLEEP_CHANCE;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON3_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_COMMAND;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_DEATH_REDIRECT;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_SLEEP_REDIRECT;
    public static final ForgeConfigSpec.IntValue BUTTON3_SLEEP_CHANCE;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON4_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_COMMAND;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_DEATH_REDIRECT;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_SLEEP_REDIRECT;
    public static final ForgeConfigSpec.IntValue BUTTON4_SLEEP_CHANCE;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON5_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_COMMAND;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_DEATH_REDIRECT;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_SLEEP_REDIRECT;
    public static final ForgeConfigSpec.IntValue BUTTON5_SLEEP_CHANCE;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON6_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_COMMAND;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_DEATH_REDIRECT;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_SLEEP_REDIRECT;
    public static final ForgeConfigSpec.IntValue BUTTON6_SLEEP_CHANCE;
    
    // Feverdream integration settings (built-in client-side death/sleep detection)
    public static final ForgeConfigSpec.ConfigValue<java.util.List<? extends String>> FEVERDREAM_REDIRECTS;
    public static final ForgeConfigSpec.IntValue FEVERDREAM_DEATH_COUNT;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Waystone Button Injector Configuration");
        
        // Button 1
        builder.push("button1");
        BUTTON1_ENABLED = builder
                .comment("Enable Button 1")
                .define("enabled", false);
        BUTTON1_LABEL = builder
                .comment("Button label (use & for color codes, e.g., &aGreen Text)")
                .define("label", "Button 1");
        BUTTON1_COMMAND = builder
                .comment("Command to execute (without leading /)")
                .define("command", "");
        BUTTON1_DEATH_REDIRECT = builder
                .comment("Server to redirect to when you die (leave empty to disable death redirect for this button's server)")
                .define("deathRedirect", "");
        BUTTON1_SLEEP_REDIRECT = builder
                .comment("Server to redirect to when you sleep (leave empty to disable sleep redirect for this button's server)")
                .define("sleepRedirect", "");
        BUTTON1_SLEEP_CHANCE = builder
                .comment("Chance (0-100%) that sleep redirect will trigger. 100 = always, 50 = 50% chance, 0 = never")
                .defineInRange("sleepChance", 100, 0, 100);
        builder.pop();
        
        // Button 2
        builder.push("button2");
        BUTTON2_ENABLED = builder
                .comment("Enable Button 2")
                .define("enabled", false);
        BUTTON2_LABEL = builder
                .comment("Button label (use & for color codes)")
                .define("label", "Button 2");
        BUTTON2_COMMAND = builder
                .comment("Command to execute (without leading /)")
                .define("command", "");
        BUTTON2_DEATH_REDIRECT = builder
                .comment("Server to redirect to when you die (leave empty to disable death redirect for this button's server)")
                .define("deathRedirect", "");
        BUTTON2_SLEEP_REDIRECT = builder
                .comment("Server to redirect to when you sleep (leave empty to disable sleep redirect for this button's server)")
                .define("sleepRedirect", "");
        BUTTON2_SLEEP_CHANCE = builder
                .comment("Chance (0-100%) that sleep redirect will trigger. 100 = always, 50 = 50% chance, 0 = never")
                .defineInRange("sleepChance", 100, 0, 100);
        builder.pop();
        
        // Button 3
        builder.push("button3");
        BUTTON3_ENABLED = builder
                .comment("Enable Button 3")
                .define("enabled", false);
        BUTTON3_LABEL = builder
                .comment("Button label (use & for color codes)")
                .define("label", "Button 3");
        BUTTON3_COMMAND = builder
                .comment("Command to execute (without leading /)")
                .define("command", "");
        BUTTON3_DEATH_REDIRECT = builder
                .comment("Server to redirect to when you die (leave empty to disable death redirect for this button's server)")
                .define("deathRedirect", "");
        BUTTON3_SLEEP_REDIRECT = builder
                .comment("Server to redirect to when you sleep (leave empty to disable sleep redirect for this button's server)")
                .define("sleepRedirect", "");
        BUTTON3_SLEEP_CHANCE = builder
                .comment("Chance (0-100%) that sleep redirect will trigger. 100 = always, 50 = 50% chance, 0 = never")
                .defineInRange("sleepChance", 100, 0, 100);
        builder.pop();
        
        // Button 4
        builder.push("button4");
        BUTTON4_ENABLED = builder
                .comment("Enable Button 4")
                .define("enabled", false);
        BUTTON4_LABEL = builder
                .comment("Button label (use & for color codes)")
                .define("label", "Button 4");
        BUTTON4_COMMAND = builder
                .comment("Command to execute (without leading /)")
                .define("command", "");
        BUTTON4_DEATH_REDIRECT = builder
                .comment("Server to redirect to when you die (leave empty to disable death redirect for this button's server)")
                .define("deathRedirect", "");
        BUTTON4_SLEEP_REDIRECT = builder
                .comment("Server to redirect to when you sleep (leave empty to disable sleep redirect for this button's server)")
                .define("sleepRedirect", "");
        BUTTON4_SLEEP_CHANCE = builder
                .comment("Chance (0-100%) that sleep redirect will trigger. 100 = always, 50 = 50% chance, 0 = never")
                .defineInRange("sleepChance", 100, 0, 100);
        builder.pop();
        
        // Button 5
        builder.push("button5");
        BUTTON5_ENABLED = builder
                .comment("Enable Button 5")
                .define("enabled", false);
        BUTTON5_LABEL = builder
                .comment("Button label (use & for color codes)")
                .define("label", "Button 5");
        BUTTON5_COMMAND = builder
                .comment("Command to execute (without leading /)")
                .define("command", "");
        BUTTON5_DEATH_REDIRECT = builder
                .comment("Server to redirect to when you die (leave empty to disable death redirect for this button's server)")
                .define("deathRedirect", "");
        BUTTON5_SLEEP_REDIRECT = builder
                .comment("Server to redirect to when you sleep (leave empty to disable sleep redirect for this button's server)")
                .define("sleepRedirect", "");
        BUTTON5_SLEEP_CHANCE = builder
                .comment("Chance (0-100%) that sleep redirect will trigger. 100 = always, 50 = 50% chance, 0 = never")
                .defineInRange("sleepChance", 100, 0, 100);
        builder.pop();
        
        // Button 6
        builder.push("button6");
        BUTTON6_ENABLED = builder
                .comment("Enable Button 6")
                .define("enabled", false);
        BUTTON6_LABEL = builder
                .comment("Button label (use & for color codes)")
                .define("label", "Button 6");
        BUTTON6_COMMAND = builder
                .comment("Command to execute (without leading /)")
                .define("command", "");
        BUTTON6_DEATH_REDIRECT = builder
                .comment("Server to redirect to when you die (leave empty to disable death redirect for this button's server)")
                .define("deathRedirect", "");
        BUTTON6_SLEEP_REDIRECT = builder
                .comment("Server to redirect to when you sleep (leave empty to disable sleep redirect for this button's server)")
                .define("sleepRedirect", "");
        BUTTON6_SLEEP_CHANCE = builder
                .comment("Chance (0-100%) that sleep redirect will trigger. 100 = always, 50 = 50% chance, 0 = never")
                .defineInRange("sleepChance", 100, 0, 100);
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

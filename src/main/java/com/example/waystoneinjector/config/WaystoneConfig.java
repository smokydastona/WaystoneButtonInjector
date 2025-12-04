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
    
    public static final ForgeConfigSpec.BooleanValue BUTTON2_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON2_COMMAND;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON3_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON3_COMMAND;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON4_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON4_COMMAND;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON5_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON5_COMMAND;
    
    public static final ForgeConfigSpec.BooleanValue BUTTON6_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_LABEL;
    public static final ForgeConfigSpec.ConfigValue<String> BUTTON6_COMMAND;
    
    // Feverdream integration settings
    public static final ForgeConfigSpec.BooleanValue FEVERDREAM_DEATH_REDIRECT_ENABLED;
    public static final ForgeConfigSpec.IntValue FEVERDREAM_DEATH_COUNT;
    public static final ForgeConfigSpec.BooleanValue FEVERDREAM_SLEEP_TP_ENABLED;

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
        builder.pop();
        
        // Feverdream Integration Settings
        builder.push("feverdream");
        FEVERDREAM_DEATH_REDIRECT_ENABLED = builder
                .comment("Enable death-based redirects",
                        "When enabled, death packets from Feverdream will trigger server redirects",
                        "When disabled, all death packets are ignored")
                .define("deathRedirectEnabled", true);
        FEVERDREAM_DEATH_COUNT = builder
                .comment("Number of deaths before auto-redirect activates (1-10)",
                        "Set to 1 to redirect on first death",
                        "Set to 2 to redirect on second death, etc.",
                        "Set to 10 to require 10 deaths before redirecting",
                        "Only applies if deathRedirectEnabled is true")
                .defineInRange("deathCount", 1, 1, 10);
        FEVERDREAM_SLEEP_TP_ENABLED = builder
                .comment("Enable sleep-based teleportation",
                        "When enabled, sleeping in a bed can trigger server redirects",
                        "When disabled, only death-based redirects will work")
                .define("sleepTpEnabled", true);
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
    public static boolean isFeverdreamDeathRedirectEnabled() {
        return FEVERDREAM_DEATH_REDIRECT_ENABLED.get();
    }
    
    public static int getFeverdreamDeathCount() {
        return FEVERDREAM_DEATH_COUNT.get();
    }
    
    public static boolean isFeverdreamSleepTpEnabled() {
        return FEVERDREAM_SLEEP_TP_ENABLED.get();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SPEC, "waystoneinjector-client.toml");
    }
}

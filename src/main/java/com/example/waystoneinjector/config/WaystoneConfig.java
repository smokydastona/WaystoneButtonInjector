package com.example.waystoneinjector.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Arrays;
import java.util.List;

public class WaystoneConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BUTTON_LABELS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BUTTON_COMMANDS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Waystone Button Injector Configuration").push("buttons");

        BUTTON_LABELS = builder
                .comment("Button labels (use & for color codes, e.g., &aChaos Town)")
                .defineList("labels", 
                    Arrays.asList(),
                    obj -> obj instanceof String);

        BUTTON_COMMANDS = builder
                .comment("Commands to execute when buttons are clicked (without leading /)")
                .defineList("commands",
                    Arrays.asList(),
                    obj -> obj instanceof String);

        builder.pop();
        SPEC = builder.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SPEC, "waystoneinjector-client.toml");
    }
}

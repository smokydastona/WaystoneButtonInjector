/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.Block
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.api;

import java.util.HashMap;
import java.util.Map;
import net.blay09.mods.waystones.api.WaystoneStyle;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class WaystoneStyles {
    private static final Map<ResourceLocation, WaystoneStyle> styles = new HashMap<ResourceLocation, WaystoneStyle>();
    private static final Map<Block, WaystoneStyle> stylesByBlock = new HashMap<Block, WaystoneStyle>();
    public static WaystoneStyle DEFAULT = WaystoneStyles.register(new WaystoneStyle(new ResourceLocation("waystones", "waystone")));
    public static WaystoneStyle MOSSY = WaystoneStyles.register(new WaystoneStyle(new ResourceLocation("waystones", "mossy_waystone")));
    public static WaystoneStyle SANDY = WaystoneStyles.register(new WaystoneStyle(new ResourceLocation("waystones", "sandy_waystone")));
    public static WaystoneStyle BLACKSTONE = WaystoneStyles.register(new WaystoneStyle(new ResourceLocation("waystones", "blackstone_waystone")).withRuneColor(-6737101));
    public static WaystoneStyle DEEPSLATE = WaystoneStyles.register(new WaystoneStyle(new ResourceLocation("waystones", "deepslate_waystone")));
    public static WaystoneStyle END_STONE = WaystoneStyles.register(new WaystoneStyle(new ResourceLocation("waystones", "end_stone_waystone")).withRuneColor(-9305857));

    public static WaystoneStyle register(WaystoneStyle style) {
        styles.put(style.getBlockRegistryName(), style);
        return style;
    }

    @Nullable
    public static WaystoneStyle getStyle(Block block) {
        return stylesByBlock.computeIfAbsent(block, key -> WaystoneStyles.getStyle(BuiltInRegistries.f_256975_.m_7981_((Object)block)));
    }

    @Nullable
    public static WaystoneStyle getStyle(ResourceLocation name) {
        return styles.get(name);
    }
}

